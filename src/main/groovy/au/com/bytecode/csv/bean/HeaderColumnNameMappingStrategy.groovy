package au.com.bytecode.csv.bean

import java.beans.BeanInfo
import java.beans.IntrospectionException
import java.beans.Introspector
import java.beans.PropertyDescriptor
import org.apache.commons.lang.StringUtils
import au.com.bytecode.csv.CSVReader
import  static org.jasig.ssp.util.importers.csv.CsvImporterMeta.*


/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
class HeaderColumnNameMappingStrategy implements MappingStrategy {
    protected String[] header;
    protected Map<String, PropertyDescriptor> descriptorMap = null;
    protected Class type;

    HeaderColumnNameMappingStrategy(Class type){
        this.type = type
    }

    public void captureHeader(CSVReader reader) throws IOException {
        header = removeBlankColumns(reader.readNext())
    }
	
	private String[] removeBlankColumns(String[] header){
		List<String> names = new ArrayList<String>()
		for(String name: header){
			if(StringUtils.isNotBlank(name))
				names.add(name)
		}
		return names.toArray(new String[names.size()])
	}
	
	

    public PropertyDescriptor findDescriptor(int col) throws IntrospectionException {
        return findDescriptorFromColumnName(getColumnName(col));
    }

    public PropertyDescriptor findDescriptorFromColumnName(String columnName) throws IntrospectionException {
		columnName = convetToCamelCase(columnName);
        return (null != columnName && columnName.trim().length() > 0) ? findDescriptor(columnName) : null;
    }
	
	private convetToCamelCase(String columnName){
		if(ARGS[ARG_KEYS.COLUMN_NAME_FORMAT_FLAG] == "underscore")
		{
			if(StringUtils.isBlank(columnName))
				return null;
			//name.trim().replaceAll(/(\B[A-Z])/,'_$1').toLowerCase()
			String[] columnNames =  columnName.split("_")
			columnName = columnNames[0].toLowerCase()
			for(Integer i = 1; i < columnNames.length; i++) {
				columnName +=  columnNames[i].capitalize()
			}
		}
		return columnName;
	}

    public String getColumnName(int col) {
        return (null != header && col < header.length) ? header[col] : null;
    }

    public int getNumberColumns(){
        return header.length
    }

    protected PropertyDescriptor findDescriptor(String name) throws IntrospectionException {
        if (null == descriptorMap) descriptorMap = loadDescriptorMap(type); //lazy load descriptors
        return descriptorMap.get(name.toUpperCase().trim());
    }

    protected boolean matches(String name, PropertyDescriptor desc) {
        return desc.getName().equals(name.trim());
    }

    protected Map<String, PropertyDescriptor> loadDescriptorMap(Class cls) throws IntrospectionException {
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();

        PropertyDescriptor[] descriptors;
        descriptors = loadDescriptors(type);
        for (PropertyDescriptor descriptor : descriptors) {
            map.put(getDescriptorKey(descriptor.getName()), descriptor);
        }

        return map;
    }

    private String getDescriptorKey(String name){

        return name.toUpperCase().trim();
    }

    private PropertyDescriptor[] loadDescriptors(Class cls) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(cls);
        return beanInfo.getPropertyDescriptors();
    }

    public Object createBean() throws InstantiationException, IllegalAccessException {
        return type.newInstance();
    }

}
