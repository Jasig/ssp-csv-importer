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

import groovy.util.logging.Log4j
import org.apache.log4j.PropertyConfigurator
import org.apache.commons.lang.StringUtils
import static CsvImporterMeta.*
import static CsvImporterDefaults.*

@Log4j
class CsvImporterMain {
    public static void main(String[] args) {
        String env = System.getenv("env")
        if(env == null)
            env = getEnv(args)

        def config = new ConfigSlurper(env).parse(new File('log4j.groovy').toURI().toURL())
        PropertyConfigurator.configure(config.toProperties())

        ARGS = handleOptions(args, new ConfigSlurper(env).parse(new File('config.groovy').toURI().toURL()).toProperties())
		CsvImporterProcessor.process()
    }

    /** Handles the options passed from the command line, prints warnings for
     *  missing elements or exits the program if required options are missing.
     *
     * @param args the args passed from the command line
     * @return nothing
     */
    private static def handleOptions(args, Properties config) {
        if (args.length > 0 && (
        	args[0].contains(cliOption(ARG_KEYS.HELP_FLAG)) ||
                args[0].contains(cliOption(ARG_KEYS.USAGE_FLAG)))) {
            usage()
            System.exit(0)
        }
        def processedArgs = [:]
        /*** Pulls default values ***/
		ARG_KEYS.each{ key -> 
			if(ARG_DEFAULT_VALUES[key.value] != null)
               	processedArgs[key.value] = ARG_DEFAULT_VALUES[key.value]
			else if (ARG_CSV_DEFAULT_VALUES[key.value] != null)
                processedArgs[key.value] = ARG_CSV_DEFAULT_VALUES[key.value]
            else if (ARG_EMAIL_VALUES[key.value] != null)
                processedArgs[key.value] = ARG_EMAIL_VALUES[key.value]

            /*** Overrides with config values ***/
             if(config.containsKey(key.key))
                 processedArgs[key.value] = config.getProperty(key.key)

            log.info   key.value + ":" + processedArgs[key.value]
		}

        /*** Overrides with command line values ***/
        args.each { it ->

			ARG_KEYS.each{ key -> 
            	if (it.startsWith(cliOption(key.value))) {
	                if (hasValue(it, key.value)) {
	                    processedArgs[key.value] = valueOf(it, key.value)
	                } 
	            }
                log.info key.value + " :" + processedArgs[key.value]
			}
        }

        if (!(processedArgs[ARG_KEYS.DB_URL_FLAG])) {
            sayErr "\nError: Database URL not set, please set this parameter using the ${cliOption(ARG_KEYS.DB_URL_FLAG)} option"
            sayErr "(e.g.: ${cliOption(ARG_KEYS.DB_URL_FLAG)}=jdbc:postgresql://localhost:5432/ssp)"
            usage()
            System.exit(1)
        }

        processedArgs
    }

    private static def hasValue(cliArg, cliOpt) {
        cliArg ==~ "${cliOption(cliOpt)}=.*"
    }

    private static def valueOf(cliArg, cliOpt) {
        (cliArg =~ "${cliOption(cliOpt)}=(.*)")[0][1]
    }

    private static def sayErr(msg) {
        System.err.println(msg)
    }

    private static def usage() {
        sayErr(USAGE)
    }

    private static def getEnv(args){
        String env = null
        args.each { String it ->

            if (it.startsWith("-Denv")) {
                String[] values = it.split("=")
                if(values.length == 2)
                    env = values[1].trim()
                return
            }
        }
        return env
    }

}
