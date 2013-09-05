package au.com.bytecode.csv.bean

import java.beans.IntrospectionException
import java.beans.PropertyDescriptor
import au.com.bytecode.csv.CSVReader

/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
interface MappingStrategy {

    /**
     * Implementation will have to return a property descriptor from a bean based on the current column.
     * @param col the column to find the description for
     * @throws java.beans.IntrospectionException
     * @return the related PropertyDescriptor
     */
    public abstract PropertyDescriptor findDescriptor(int col) throws IntrospectionException

    public PropertyDescriptor findDescriptorFromColumnName(String columnName) throws IntrospectionException

    public abstract String getColumnName(int col)



    public abstract int getNumberColumns()

    public abstract Object createBean() throws InstantiationException, IllegalAccessException

    /**
     * Implementation of this method can grab the header line before parsing begins to use to map columns
     * to bean properties.
     * @param reader the CSVReader to use for header parsing
     * @throws java.io.IOException if parsing fails
     */
    public void captureHeader(CSVReader reader) throws IOException
}
