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

import static au.com.bytecode.csv.CSVReader.*
import static au.com.bytecode.csv.CSVParser.*
import static CsvImporterMeta.*
/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
class CsvImporterDefaults {
	private static final rootPath = System.getProperty("user.dir") + "/data/"
	public static def ARG_DEFAULT_VALUES = [(ARG_KEYS.DB_URL_FLAG) :"jdbc:postgresql://localhost:5432/ssp",
    	(ARG_KEYS.DB_USERNAME_FLAG):"sspadmin",
	    (ARG_KEYS.DB_PASSWORD_FLAG):"sspadmin",
		(ARG_KEYS.DB_DRIVER_FLAG):"org.postgresql.Driver",
	    (ARG_KEYS.VALIDATE_FLAG):true,
	    (ARG_KEYS.INPUT_DIR_FLAG):rootPath + "input/",
	    (ARG_KEYS.PROCESSING_DIR_FLAG):rootPath + "process/",
	    (ARG_KEYS.ARCHIVE_DIR_FLAG):rootPath + "archive/",
	    (ARG_KEYS.MAX_FAIL_FLAG):100,
	    (ARG_KEYS.LAG_TIME_MIN_FLAG):10,  //time required for file to be undisturbed before processing in minutes
	    (ARG_KEYS.COLUMN_NAME_FORMAT_FLAG):"underscore",
		(ARG_KEYS.TABLES_DEFINITION_CLASS_FLAG):'org.jasig.ssp.util.importers.csv.tables.CsvTableDefinition']  //underscore/camelcase
	
	public static def ARG_CSV_DEFAULT_VALUES = [
		(ARG_KEYS.CSV_SEPARATOR_FLAG):DEFAULT_SEPARATOR,
	    (ARG_KEYS.CSV_INITIAL_READ_SIZE_FLAG):INITIAL_READ_SIZE,
	    (ARG_KEYS.CSV_QUOTE_CHARACTER_FLAG):DEFAULT_QUOTE_CHARACTER,
	    (ARG_KEYS.CSV_ESCAPE_CHARACTER_FLAG):DEFAULT_ESCAPE_CHARACTER,
	    (ARG_KEYS.CSV_STRICT_QUOTES_FLAG):DEFAULT_STRICT_QUOTES,
		(ARG_KEYS.CSV_SKIP_LINES_FLAG): DEFAULT_SKIP_LINES,
		(ARG_KEYS.CSV_IGNORE_LEADING_WHITESPACE_FLAG):DEFAULT_IGNORE_LEADING_WHITESPACE,
	    (ARG_KEYS.CSV_NULL_CHARACTER_FLAG):NULL_CHARACTER]
	
	public static def ARG_EMAIL_VALUES = [
    (ARG_KEYS.EMAIL_ACTIVE):true,
	(ARG_KEYS.EMAIL_FROM_FLAG):"\"SSP DATA IMPORTER\"<sysadmin@localhost>" ,
 	(ARG_KEYS.EMAIL_TO_FLAG):"\"SSP DATA IMPORTER\"<sysadmin@localhost>",
	(ARG_KEYS.EMAIL_HOST_FLAG):"localhost",
	(ARG_KEYS.EMAIL_PORT_FLAG):25,
	(ARG_KEYS.EMAIL_PROTOCOL_FLAG):"smtp",
	(ARG_KEYS.EMAIL_SSL_REQUIRED_FLAG):true,
	(ARG_KEYS.EMAIL_USERNAME_FLAG):"sysadmin",
	(ARG_KEYS.EMAIL_PASSWORD_FLAG):"ssp"
	]

}
