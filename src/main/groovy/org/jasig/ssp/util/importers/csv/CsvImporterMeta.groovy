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
import static org.jasig.ssp.util.importers.csv.CsvImporterDefaults.*
class CsvImporterMeta {
	
	public static def AUTHOR = 'data.migration.tool'
	
	public static def ARG_KEYS = [
		"DB_URL_FLAG":'db-url',
	    "DB_USERNAME_FLAG":'db-username',
	    "DB_PASSWORD_FLAG":'db-password',
		"DB_DRIVER_FLAG":'db-driver',
		"VALIDATE_FLAG":'validate',
		"MAX_FAIL_FLAG":'max-fail',
		"LAG_TIME_MIN_FLAG":'lag-time-min',
		"INPUT_DIR_FLAG":'input-dir',
		"PROCESSING_DIR_FLAG":'processing-dir',
		"ARCHIVE_DIR_FLAG":'archive-dir',
	    "USAGE_FLAG":'usage',
	    "HELP_FLAG":'help',
	    "COLUMN_NAME_FORMAT_FLAG":'column-name',
	    "CSV_SEPARATOR_FLAG":'csv-sperator',
	    "CSV_INITIAL_READ_SIZE_FLAG":'csv-read-size',
	    "CSV_QUOTE_CHARACTER_FLAG":'csv-quote',
	    "CSV_ESCAPE_CHARACTER_FLAG":'csv-escape',
	    "CSV_STRICT_QUOTES_FLAG":'csv-strict-quote',
		"CSV_SKIP_LINES_FLAG":'csv-default-skip-lines',
	    "CSV_IGNORE_LEADING_WHITESPACE_FLAG":'ignore-white-space',
	    "CSV_NULL_CHARACTER_FLAG":'csv-null-char',
        "EMAIL_ACTIVE":"email-active",
		"EMAIL_FROM_FLAG":'email-from',
		"EMAIL_TO_FLAG":'email-to',
		"EMAIL_HOST_FLAG":'email-host',
		"EMAIL_PORT_FLAG":'email-port',
		"EMAIL_USERNAME_FLAG":'email-username',
		"EMAIL_PASSWORD_FLAG":'email-password',
		"EMAIL_PROTOCOL_FLAG":'email-protocol',
		"EMAIL_SSL_REQUIRED_FLAG":'email-ssl']

    public static def cliOption(name) {
        "--${name}"
    }

	public static def ARGS = [:]
	
	public static final def DEFAULT_OUTPUT_ENCODING = "UTF-8"
	
	public static final NON_UTF_CHARS = [
		"\u2018":"'",	// ë
		"\u2019":"'",	// í
		"\u201c":"\"",	// ì
		"\u201d":"\"",  // î
		"\u2013":"-"	// ñ non UTF8 dash
		]
		
    public static final USAGE = """
----------------------------------
- Using this program
----------------------------------

	This program inserts data from csv files into a database. Csv file has some lattitude in parsing including
    whitespace, sperator, quote, escape as well as ignore lines.  Csv files to be processed are placed in
    a folder. Periodically, the program should be run to determine if the files in the input folder have not
    been modified for a period of time (lag-time-min). Files are transfered to a process folder and processed.
    If an archive folder is supplied files are moved to the archive folder and labeled with with modified date
    and NOT PROCESSED if processing failed or did not occur.

    Emails can be sent to notify of processing start, processing complete and any validation of other failures.

    A config.groovy file is supplied that allows setting all parameters by env.

    The expectation is that the program will be used as a cron job running periodically at an appropriate time.

----------------------------------
- Running the program
----------------------------------

    The app can be executed in at least three different ways:

        1) Gradle run command. This is the easiest way to run the app if you
           are tweaking the source code:

             %> gradle -q run -PcliArgs="<opts>"

           Note that <opts> parsing is naive so spaces in argument values will
           not work as expected.

        2) Exploded Gradle-assembled application

             %> gradle installApp
             %> ./build/install/ssp-data-migration/bin/ssp-data-migration-csv <opts>

           Opts splitting is handled by your shell in this case, so whitespace
           in argument values should work as expected.

           Use this execution mechanism if you've received a zip or tar of the
           application.

        3) Without Gradle. Don't know why you'd want to do this as it is
           significantly more verbose and you'll need to do more legwork to
           make your JDBC driver visible:

            %>  groovy -cp \"src/main/groovy/:src/main/resources:/path/to/your/jdbc/driver\" \\
                src/main/groovy/net/unicon/util/migration/csv/Main.groovy \\
                <opts>

    Output is a direct insert into the identified database.

    Options all have POSIX "long opt" format, i.e. --option-name=option-value.
    The "=" is optional and can be replaced with whitespace. E.g.:

    %> gradle -q run -PcliArgs="${cliOption(ARG_KEYS.DB_URL_FLAG)}=jdbc:postgresql://localhost:5432/ssp"

----------------------------------
- Options
----------------------------------
    ${cliOption(ARG_KEYS.USAGE_FLAG)}, ${cliOption(ARG_KEYS.HELP_FLAG)}    Prints this usage message and performs no processing

    - setting run environment -

    use either -Denv=environment example: -Denv=prod or set system environment variable env to environment

    - Database Settings -

    ${cliOption(ARG_KEYS.DB_URL_FLAG)}          [Optional] The full URL to the source database (default:${ARG_DEFAULT_VALUES[ARG_KEYS.DB_URL_FLAG]})
    ${cliOption(ARG_KEYS.DB_USERNAME_FLAG)}     [Optional] The username for the source database  (default:${ARG_DEFAULT_VALUES[ARG_KEYS.DB_USERNAME_FLAG]})
    ${cliOption(ARG_KEYS.DB_PASSWORD_FLAG)}     [Optional] The password for the source database. (default:${ARG_DEFAULT_VALUES[ARG_KEYS.DB_PASSWORD_FLAG]})
    ${cliOption(ARG_KEYS.DB_DRIVER_FLAG)}       [Optional] The driver to be used example: (default:${ARG_DEFAULT_VALUES[ARG_KEYS.DB_DRIVER_FLAG]}) or net.sourceforge.jtds.jdbc.Driver

    - Location of input directories and validation -

    ${cliOption(ARG_KEYS.INPUT_DIR_FLAG)}       [Optional] full path to csv file location (default location: ${ARG_DEFAULT_VALUES[ARG_KEYS.INPUT_DIR_FLAG]}})
    ${cliOption(ARG_KEYS.PROCESSING_DIR_FLAG)}  [Optional] full path to csv location where file transferred for processing (default location: ${ARG_DEFAULT_VALUES[ARG_KEYS.PROCESSING_DIR_FLAG]})
    ${cliOption(ARG_KEYS.ARCHIVE_DIR_FLAG)}     [Optional] full path to archive dir, set blank to not store archived files  (default location: ${ARG_DEFAULT_VALUES[ARG_KEYS.ARCHIVE_DIR_FLAG]})
    ${cliOption(ARG_KEYS.LAG_TIME_MIN_FLAG)}    [Optional] set minutes file unmodified before beginning processing    (default: ${ARG_DEFAULT_VALUES[ARG_KEYS.LAG_TIME_MIN_FLAG]})
    ${cliOption(ARG_KEYS.MAX_FAIL_FLAG)}        [Optional] maximum invalid rows before a file is rejected (default: ${ARG_DEFAULT_VALUES[ARG_KEYS.MAX_FAIL_FLAG]})
    ${cliOption(ARG_KEYS.VALIDATE_FLAG)}        [Optional] set true to validate rows before sending to database (default: ${ARG_DEFAULT_VALUES[ARG_KEYS.VALIDATE_FLAG]})

    - CSV Parsing settings -

    ${cliOption(ARG_KEYS.COLUMN_NAME_FORMAT_FLAG)}              [Optional] underscore or camelcase (default:${ARG_DEFAULT_VALUES[ARG_KEYS.COLUMN_NAME_FORMAT_FLAG]})
    ${cliOption(ARG_KEYS.CSV_SEPARATOR_FLAG)}             [Optional] csv seperator (default: ${ARG_CSV_DEFAULT_VALUES[ARG_KEYS.CSV_SEPARATOR_FLAG]})
    ${cliOption(ARG_KEYS.CSV_QUOTE_CHARACTER_FLAG)}                [Optional] csv quote (default: ${ARG_CSV_DEFAULT_VALUES[ARG_KEYS.CSV_QUOTE_CHARACTER_FLAG]})
    ${cliOption(ARG_KEYS.CSV_ESCAPE_CHARACTER_FLAG)}               [Optional] csv excape (default: ${ARG_CSV_DEFAULT_VALUES[ARG_KEYS.CSV_ESCAPE_CHARACTER_FLAG]})
    ${cliOption(ARG_KEYS.CSV_IGNORE_LEADING_WHITESPACE_FLAG)}       [Optional] csv ignore leading white space (default: ${ARG_CSV_DEFAULT_VALUES[ARG_KEYS.CSV_IGNORE_LEADING_WHITESPACE_FLAG]})
    ${cliOption(ARG_KEYS.CSV_SKIP_LINES_FLAG)}   [Optional] number of lines to file to skip before reading (default: ${ARG_CSV_DEFAULT_VALUES[ARG_KEYS.CSV_SKIP_LINES_FLAG]})

    - Email settings -

     ${cliOption(ARG_KEYS.EMAIL_ACTIVE)}       [Optional] Activate email (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_ACTIVE]})
     ${cliOption(ARG_KEYS.EMAIL_FROM_FLAG)}         [Optional] address as string.  Address must follow RFC822 syntax. (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_FROM_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_TO_FLAG)}           [Optional] Comma delimited list of addresses. Addresses must follow RFC822 syntax. (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_TO_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_HOST_FLAG)}         [Optional] host name (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_HOST_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_PORT_FLAG)}         [Optional] port  (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_PORT_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_PROTOCOL_FLAG)}     [Optional] protocol to be used (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_PROTOCOL_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_SSL_REQUIRED_FLAG)}          [Optional] whether SSL/Authentication is to be used (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_SSL_REQUIRED_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_USERNAME_FLAG)}     [Optional] username (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_USERNAME_FLAG]})
     ${cliOption(ARG_KEYS.EMAIL_PASSWORD_FLAG)}     [Optional] password (default: ${ARG_EMAIL_VALUES[ARG_KEYS.EMAIL_PASSWORD_FLAG]})

    NOTE:  All values can be set through config.groovy found at top level of distribution.  Values can be pinned to environment.

"""

    public static final def UNIX_TIMESTAMP = /[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}.[0-9]{1,2}/

}
