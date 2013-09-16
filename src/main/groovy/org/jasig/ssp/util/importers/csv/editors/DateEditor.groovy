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

package org.jasig.ssp.util.importers.csv.editors

import org.apache.commons.lang.StringUtils 
import java.text.SimpleDateFormat
import groovy.util.logging.Log4j

@Log4j
class DateEditor implements Editor{

    List<SimpleDateFormat>  formats = new ArrayList<SimpleDateFormat>();

    DateEditor(SimpleDateFormat format){
        this.formats.add(format);
    }

    DateEditor(String formatAsString){
        this.formats.add(new SimpleDateFormat(formatAsString));
    }

	DateEditor(ArrayList<String> formatAsStrings){
		for(String formatAsString : formatAsStrings)
        	this.formats.add(new SimpleDateFormat(formatAsString));
    }


    def fromText(String value) {
		String parsingErrors = ""
		if(value != null && StringUtils.isNotBlank(value)){
			for(SimpleDateFormat format : formats){
				try{
					Date date =  format.parse(value)
					return date
				}catch(Exception e){
					parsingErrors += e.getMessage()
				}
			}
		}
		if(StringUtils.isNotBlank(parsingErrors)){
			String allowedFormats = ""
			for(SimpleDateFormat format: formats){
				allowedFormats += format.toPattern() + " : ";
			}
        	log.error parsingErrors	 + "allowed formats: " + allowedFormats
			throw new Exception(parsingErrors	 + "allowed formats: " + allowedFormats)
		}

		return null
    }
}
