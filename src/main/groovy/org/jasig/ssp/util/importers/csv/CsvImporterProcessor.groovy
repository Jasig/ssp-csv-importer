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

import groovy.time.TimeCategory
import groovy.util.logging.Log4j

import static CsvImporterMeta.*
import static org.jasig.ssp.util.importers.csv.CsvTableDefinition.TABLES
import org.apache.commons.lang.StringUtils
@Log4j
class CsvImporterProcessor {
    public static process() {

        Date now = new Date()
        Date lastRun
        use(TimeCategory) {
            lastRun = now - ARGS[ARG_KEYS.LAG_TIME_MIN_FLAG].minutes
        }

		File inputDir = new File(ARGS[ARG_KEYS.INPUT_DIR_FLAG])
        File processingDir = new File(ARGS[ARG_KEYS.PROCESSING_DIR_FLAG])
        File archiveDir = null
        if(StringUtils.isNotBlank(ARGS[ARG_KEYS.ARCHIVE_DIR_FLAG]))
            archiveDir = new File(ARGS[ARG_KEYS.ARCHIVE_DIR_FLAG])

		log.info 'Path To Input Folder: ' + ARGS[ARG_KEYS.INPUT_DIR_FLAG]
		log.info 'Path To Processing Folder: ' + ARGS[ARG_KEYS.PROCESSING_DIR_FLAG]
		log.info 'Path To Archive Folder: ' + ARGS[ARG_KEYS.ARCHIVE_DIR_FLAG]
		Boolean filesReady = true
        for(TableMetaData table:TABLES)   {
            File file = new File(inputDir, table.fileName)
			Date fileModifiedDate =  new Date(file.lastModified())
            if(file.isFile() && !fileModifiedDate.before(lastRun)){
	            filesReady = false
            }
        }
		if(!filesReady)
			System.exit(0)
		String filesToBeProcessed = ""
		for(TableMetaData table:TABLES)   {
            File file = new File(inputDir, table.fileName)
			file.renameTo(new File(processingDir, file.getName()));
            filesToBeProcessed += file.getName() + " \n"
        }
        EmailService.notifyProcessingStarted(filesToBeProcessed)
        String filesProcessed = ""
        for(TableMetaData table:TABLES)   {
            File file = new File(processingDir, table.fileName)
			String processed_prefix = ""
            if(file.isFile())    {
                CsvController controller = new CsvController()
				try{
                    controller.process(file, table.beanClass,
                        table.tableName,
                        table.naturalKeys,
                        table.editors,
                        ARGS[ARG_KEYS.VALIDATE_FLAG],
                        ARGS[ARG_KEYS.MAX_FAIL_FLAG])
				} catch(Exception exp){
                    EmailService.notifyException(exp)
					processed_prefix = "PROCESSING_FAILED_"
				}finally{
					
				}
                Date fileModifiedDate =  new Date(file.lastModified())
                if(archiveDir != null)
                    file.renameTo(new File(archiveDir, processed_prefix + fileModifiedDate.format('EEE_MMM_dd_hh_mm_ss_a_yyyy_') + file.getName()   ))
                else
                    file.delete();
                filesProcessed += file.getName() + " \n"
                if(StringUtils.isNotBlank(processed_prefix))
                    log.error "FILE PROCESSED: " + file.getName()
                else
                    log.info "FILE PROCESSED: " + file.getName()
            } else {
                log.error "FILE NOT PROCESSED: " + file.getName()
            }
        }
        EmailService.notifyProcessingCompleted(filesProcessed)
    }
}
