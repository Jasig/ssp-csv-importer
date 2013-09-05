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

import java.beans.PropertyDescriptor
import groovy.util.logging.Log4j
import static org.apache.tools.ant.util.StringUtils.removeSuffix

@Log4j
class BeanDao {
    MappingStrategy strategy
    String tableName
    List<String> naturalKeys

    public BeanDao(String tableName, MappingStrategy strategy,  List<String> naturalKeys) {
        this.strategy = strategy;
        this.tableName = tableName;
        this.naturalKeys = naturalKeys
    }
    def Integer inTable(Object bean){
        String query = "select * from "
        query += tableName + " where "
        for(String naturalKey:naturalKeys){
            query += naturalKey + " = " + getPropertyValueFromColumnName(bean, naturalKey) + " AND "
        }
        query = removeSuffix(query, " AND ")
        log.debug(query) ;
        return (Integer)DataSource.sql.rows(query).size()
    }

    def update(bean){
        String query = "update " + tableName + " set "
        for (int i = 0; i < strategy.getNumberColumns(); i++) {
            String columnName = strategy.getColumnName(i)
            String value = getPropertyValue(bean, i);
            query +=  columnName + " = " + value + " , "
        }
        query = removeSuffix(query, " , ")
        query +=  ' where '
        for(String naturalKey:naturalKeys){
            query += naturalKey + " = " + getPropertyValueFromColumnName(bean, naturalKey) + " AND "
        }
        query = removeSuffix(query, " AND ")
        log.debug(query) ;
        DataSource.sql(query)
    }

    def insert(bean){
        String query = "insert into " + tableName + " ( "
        for (int i = 0; i < strategy.getNumberColumns(); i++) {
            String columnName = strategy.getColumnName(i)
            query += columnName + " , "
        }
        query = removeSuffix(query," , ")
        query +=  " ) values ("
        for (int i = 0; i < strategy.getNumberColumns(); i++) {
            query += getPropertyValue(bean, i) + " , "
        }
        query = removeSuffix(query, " , ")
        query +=  " )"

        log.debug(query) ;
        DataSource.sql(query)
    }

    def clearTable()  {
        String query = "DELETE FROM " + tableName
        DataSource.sql(query)
    }

    def private getPropertyValue(Object bean, int i){
        PropertyDescriptor prop = strategy.findDescriptor(i)
        String value = null
        if (null != prop) {
            value =  prop.getReadMethod().invoke(bean).toString();
        }
        if(value == null)
            return null;
        return "'" + value  + "'"
    }

    def private getPropertyValueFromColumnName(Object bean, String columnName) {
        PropertyDescriptor prop = strategy.findDescriptorFromColumnName(columnName)
        String value = null
        if (null != prop) {
            value =  prop.getReadMethod().invoke(bean).toString();
        }
        if(value == null)
            return null;
        return "'" + value + "'"
    }
}
