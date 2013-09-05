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

import au.com.bytecode.csv.CSVReader


import au.com.bytecode.csv.bean.HeaderColumnNameMappingStrategy
import au.com.bytecode.csv.bean.MappingStrategy
import groovy.util.logging.Log4j
import org.jasig.ssp.util.importers.csv.editors.Editor
import static CsvImporterMeta.*

/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class CsvController {
    private String dirPath = "./"
    private static final String ERROR_PREFIX = "Error Database: ";
    CsvController(){
    }

    def process(File file, Class beanClass, String tableName, List<String> naturalKeys, Map<String, Editor > editors, Boolean validate, Integer maximumAllowedErrors){

        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy(beanClass)
		CSVReader reader = new CSVReader(new FileReader(file),
                (char)ARGS[ARG_KEYS.CSV_SEPARATOR_FLAG],
                (char)ARGS[ARG_KEYS.CSV_QUOTE_CHARACTER_FLAG],
                (char)ARGS[ARG_KEYS.CSV_ESCAPE_CHARACTER_FLAG],
                (int)ARGS[ARG_KEYS.CSV_SKIP_LINES_FLAG],
                (boolean)ARGS[ARG_KEYS.CSV_STRICT_QUOTES_FLAG],
                (boolean)ARGS[ARG_KEYS.CSV_IGNORE_LEADING_WHITESPACE_FLAG])
								
        CsvToBeanConverter converter = new CsvToBeanConverter(reader, (MappingStrategy)strategy, editors, maximumAllowedErrors)
        List beanList = converter.convert(validate)
	
		
        BeanDao dao = new BeanDao(tableName,  strategy, naturalKeys)
        if(naturalKeys != null){
            for(Object bean:beanList){
                Integer count = dao.inTable(bean)
                if(count == 1)
                    dao.update(bean)
                else if (count == 0)
                    dao.insert(bean)
                else{
                    log.error(ERROR_PREFIX, "bean not unique", bean.dump());
                    EmailService.notifyBeanNotUnique(ERROR_PREFIX, "bean not unique", bean.dump())
				}
            }
        }else{
            dao.clearTable()
            for(Object bean:beanList){
                    dao.insert(bean)
            }

        }
    }
}
