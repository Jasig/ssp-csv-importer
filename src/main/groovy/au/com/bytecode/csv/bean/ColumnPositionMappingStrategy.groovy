package au.com.bytecode.csv.bean

import au.com.bytecode.csv.CSVReader
/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
class ColumnPositionMappingStrategy extends HeaderColumnNameMappingStrategy{

    ColumnPositionMappingStrategy(Class type){
        super(type)
    }

    private String[] columnMapping = Arrays.newInstance()

    public void captureHeader(CSVReader reader) throws IOException {
        //do nothing, first line is not header
    }

    public String getColumnName(int col) {
        return (null != columnMapping && col < columnMapping.length) ? columnMapping[col] : null ;
    }
    public String[] getColumnMapping() {
        return columnMapping != null ? columnMapping.clone() : null;
    }
    public void setColumnMapping(String[] columnMapping) {
        this.columnMapping = columnMapping != null ? columnMapping.clone() : null;
    }

    public int getNumberColumns(){
        return columnMapping.length
    }
}
