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
import java.lang.reflect.Method

import static CsvImporterMeta.*
import org.apache.commons.lang.StringUtils

@Log4j
class CsvImporterProcessor {
    public static process() {

        Date now = new Date()
        Date lastRun
        use(TimeCategory) {
			Integer lagTime = Integer.parseInt(ARGS[ARG_KEYS.LAG_TIME_MIN_FLAG].toString())
            lastRun = now - lagTime.minutes
        }

		File inputDir = createDirectories(ARGS[ARG_KEYS.INPUT_DIR_FLAG])

        File processingDir = createDirectories(ARGS[ARG_KEYS.PROCESSING_DIR_FLAG])
        File archiveDir = null
        if(StringUtils.isNotBlank(ARGS[ARG_KEYS.ARCHIVE_DIR_FLAG])) {
            archiveDir = createDirectories(ARGS[ARG_KEYS.ARCHIVE_DIR_FLAG])
        }
		log.info 'Path To Input Folder: ' + ARGS[ARG_KEYS.INPUT_DIR_FLAG]
		log.info 'Path To Processing Folder: ' + ARGS[ARG_KEYS.PROCESSING_DIR_FLAG]
		log.info 'Path To Archive Folder: ' + ARGS[ARG_KEYS.ARCHIVE_DIR_FLAG]
		Boolean filesReady = true
        Integer filesToProcess = 0;
		
		List<TableMetaData> tables = getTables()
		
        for(TableMetaData table:tables)   {
            File file = new File(inputDir, table.fileName)
			Date fileModifiedDate =  new Date(file.lastModified())
            if(file.isFile() && file.exists() && !fileModifiedDate.before(lastRun)){
	            filesReady = false
            }
            if(file.isFile() && file.exists())
                filesToProcess++
        }
        if(!filesReady) {
			log.info "Files not ready to process. Number of files found:" + filesToProcess.toString()
            System.exit(0)
        }

		for(TableMetaData table:tables)   {
            File file = new File(inputDir, table.fileName)
            if(file.isFile() && file.exists()) {
			    file.renameTo(new File(processingDir, file.getName()));
            }
        }
        filesToProcess = 0
        String filesToBeProcessed = ""
        for(TableMetaData table:tables)   {
            File file = new File(processingDir, table.fileName)
            if(file.isFile() && file.exists()) {
                filesToBeProcessed += file.getName() + " \n"
                filesToProcess++
            }
        }

        if(StringUtils.isNotBlank(filesToBeProcessed))
            EmailService.notifyProcessingStarted(filesToBeProcessed)
        else {
			log.info "No Files processed: " + filesToProcess + " files found. ß"
            System.exit(0)
        }
        String filesProcessed = ""
        for(TableMetaData table:tables)   {
			log.error table.fileName
            File file = new File(processingDir, table.fileName)
			String processed_prefix = ""
            if(file.isFile() && file.exists())    {
                CsvController controller = new CsvController()
				try{
                    controller.process(file, table.beanClass,
                        table.tableName,
                        table.naturalKeys,
                        table.editors,
                        ARGS[ARG_KEYS.VALIDATE_FLAG],
                        ARGS[ARG_KEYS.MAX_FAIL_FLAG])
				} catch(Exception exp){
					log.error "Processing failed for table: " + table.tableName + " \n" + exp.getMessage() + " \n" + exp.getStackTrace()
                    EmailService.notifyException("processing failed for table" + table.tableName, exp)
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
                log.error "Error File Not Processed because File: " + table.fileName + "is a file:" + file.isFile() + " exists " + file.exists()
            }
        }
        EmailService.notifyProcessingCompleted(filesProcessed)
    }

    private static File createDirectories(directoryPath) {
        File dir = new File(directoryPath)
        if (!dir.exists()) {
            if(!dir.mkdirs())
                System.exit(1)
        }
        return dir
    }

	private static List<TableMetaData> getTables(){
		Class<?> cls = Class.forName(ARGS[ARG_KEYS.TABLES_DEFINITION_CLASS_FLAG]);
		Method constructTableMethod = cls.getDeclaredMethod("constructTableDefinitions")
		return constructTableMethod.invoke(null);
	}
}
