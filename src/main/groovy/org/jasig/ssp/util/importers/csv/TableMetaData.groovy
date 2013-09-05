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

import org.jasig.ssp.util.importers.csv.editors.Editor

class TableMetaData {

    String tableName
    String fileName
    Class  beanClass
    List<String> naturalKeys
    File csvFile = null
    Map<String, Editor>   editors = new HashMap<String,Editor>()

    TableMetaData(String tableName, String fileName, Class beanClass, List<String> naturalKeys, Map<String, Object> editors)   {
        this.tableName = tableName
        this.fileName = fileName
        this.beanClass = beanClass
        this.naturalKeys = naturalKeys
        this.editors = editors
    }
}
