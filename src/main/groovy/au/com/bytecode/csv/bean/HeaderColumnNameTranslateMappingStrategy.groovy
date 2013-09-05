package au.com.bytecode.csv.bean

/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
class HeaderColumnNameTranslateMappingStrategy extends HeaderColumnNameMappingStrategy {
    private Map<String, String> columnMapping = new HashMap<String, String>();
    public String getColumnName(int col) {
        return col < header.length ? columnMapping.get(header[col].toUpperCase()) : null;
    }
    public Map<String, String> getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(Map<String, String> columnMapping) {
        for (String key : columnMapping.keySet()) {
            this.columnMapping.put(key.toUpperCase(), columnMapping.get(key));
        }
    }

    public int getNumberColumns(){
        return this.columnMapping.size()
    }
}
