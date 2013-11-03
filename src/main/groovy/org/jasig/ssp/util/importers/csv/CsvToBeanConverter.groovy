/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.ssp.util.importers.csv

import au.com.bytecode.csv.bean.MappingStrategy
import au.com.bytecode.csv.bean.CsvToBean
import au.com.bytecode.csv.CSVReader
import org.apache.commons.lang.StringUtils
import org.jasig.ssp.util.importers.csv.editors.Editor

import javax.validation.ConstraintViolation
import groovy.util.logging.Log4j
import javax.validation.ValidatorFactory
import javax.validation.Validation
import javax.validation.Validator
import org.jasig.ssp.util.importers.csv.EmailService

import static org.jasig.ssp.util.importers.csv.EmailService.notifyValidationError

@Log4j
class CsvToBeanConverter {


    static final def VALIDATION_ERROR = "VALIDATION ERROR: "

    CSVReader reader
    Map<String, Editor> editors
    Validator beanValidator;
    Integer beansInViolation  = 0
    String beanSetValidationErrors  = ""
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
    Integer maximumValidationErrorPerFile = 100
	private MappingStrategy strategy = null
	  

    CsvToBeanConverter(CSVReader reader, MappingStrategy strategy,Map<String, Editor> editors, Integer maximumValidationErrorPerFile){
        this.strategy = strategy
        strategy.captureHeader(reader)
        this.reader = reader;
        this.editors = editors
        this.maximumValidationErrorPerFile = maximumValidationErrorPerFile
    }

    def convert(Boolean validate){
        CsvToBean csvToBean = new CsvToBean();
        List beans =  csvToBean.parse(strategy, reader, editors);
        List validatedBeans = new ArrayList<Object>()
        Integer beanLineNumber = 0;
		
        if(validate) {
            beanValidator = factory.getValidator() as Validator
            for(bean in beans)   {
                bean = this.validate(bean, ++beanLineNumber)
                if(bean != null){
                    validatedBeans.add(bean)
                }
				if(beansInViolation > maximumValidationErrorPerFile )  {
					String msg = VALIDATION_ERROR + "Maximum number of validation errors exceeded for "  + beans.get(0).getClass().getName()
					log.error msg
					beanSetValidationErrors = msg + "\n Validation Errors follow: \n" + beanSetValidationErrors
					EmailService.notifyExcessiveValidationErrors(beanSetValidationErrors)
					return new ArrayList<Object>()
				}
            }
            if(StringUtils.isNotBlank(beanSetValidationErrors))
                notifyValidationError(beanSetValidationErrors)
            return  validatedBeans
        }
        return  beans
    }

    def validate(Object bean, Integer beanLineNumber){

        Set constraintViolations = beanValidator.validate(bean );
        if(constraintViolations.size() > 0){
            beansInViolation++
            for(ConstraintViolation violation:constraintViolations){
				String msg = violation.getMessage() + " for object type " + bean.getClass().getName()   + "." + violation.propertyPath  + " bean: " + beanLineNumber
                log.error msg
                beanSetValidationErrors += msg + "\n"
            }
            return null;
        }
        return bean
    }
}
