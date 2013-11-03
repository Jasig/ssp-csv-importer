package au.com.bytecode.csv.bean


import java.beans.IntrospectionException
import java.beans.PropertyDescriptor
import java.beans.PropertyEditor
import java.beans.PropertyEditorManager
import java.lang.reflect.InvocationTargetException
import org.jasig.ssp.util.importers.csv.editors.Editor
import groovy.util.logging.Log4j
import static org.jasig.ssp.util.importers.csv.EmailService.notifyParsingError
import org.apache.commons.lang.StringUtils

import au.com.bytecode.csv.CSVReader
/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Log4j
class CsvToBean {
    private Map<Class<?>, PropertyEditor> editorMap = null;

    private static final String ERROR_PREFIX = "Parsing Error: ";
	private String columnParsingErrors = ""

    public List parse(MappingStrategy mapper, Reader reader) {
        return parse(mapper, new CSVReader(reader));
    }

    public List parse(MappingStrategy mapper, CSVReader csv, Map<String,Editor> editors) {
        Integer count = 0;
        try {
            String[] line;
            List list = new ArrayList();

            while (null != (line = csv.readNext())) {
                count++;
                Object obj = processLine(mapper, editors, line);
                if(obj == null)
                    continue
                list.add(obj);
            }
			if(StringUtils.isNotBlank(columnParsingErrors)){
				notifyParsingError(columnParsingErrors)
			}
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV! Error occured on line: " + count + e.getMessage() + "\n Column Errors:" + columnParsingErrors, e);
        }
		
    }

    protected Object processLine(MappingStrategy mapper, Map<String,Editor> editors, String[] line) throws IllegalAccessException, InvocationTargetException, InstantiationException, IntrospectionException {
        Object bean = mapper.createBean();
        for (int col = 0; col < line.length; col++) {
            PropertyDescriptor prop = mapper.findDescriptor(col);
            if (null != prop) {
				
                String value = checkForTrim(line[col], prop);
				try{
                Object obj = convertValue(value, prop, editors == null ? null : editors.get(prop.name));
				if(obj != null)
                	prop.getWriteMethod().invoke(bean, obj);
				}catch(Exception exp){
					String errorMessage = "Error parsing CSV! Error occured on column: " + col + " : value " + value + " property: " + prop.name + " error:" + exp.getMessage() + " processing continues.\n ";
					log.error errorMessage
					this.columnParsingErrors += errorMessage
					
				}
            }
        }
        return bean;
    }

    private String checkForTrim(String s, PropertyDescriptor prop) {
        return trimmableProperty(prop) ? s.trim() : s;
    }

    private boolean trimmableProperty(PropertyDescriptor prop) {
        return !prop.getPropertyType().getName().contains("String");
    }

    protected Object convertValue(String value, PropertyDescriptor prop, Editor editor) throws InstantiationException, IllegalAccessException {
        PropertyEditor editorProp = getPropertyEditor(prop);
        Object obj = value;
        if(null != editor){
          obj = editor.fromText(value)
        }else if (null != editorProp) {
           editorProp.setAsText(value)
           obj = editorProp.getValue()
        }else{
            obj = obj
        }
        return obj;
    }

    private PropertyEditor getPropertyEditorValue(Class<?> cls) {
        if (editorMap == null) {
            editorMap = new HashMap<Class<?>, PropertyEditor>();
        }

        PropertyEditor editor = editorMap.get(cls);

        if (editor == null) {
            editor = PropertyEditorManager.findEditor(cls);
            addEditorToMap(cls, editor);
        }

        return editor;
    }

    private void addEditorToMap(Class<?> cls, PropertyEditor editor) {
        if (editor != null) {
            editorMap.put(cls, editor);
        }
    }


    /*
     * Attempt to find custom property editor on descriptor first, else try the propery editor manager.
     */
    protected PropertyEditor getPropertyEditor(PropertyDescriptor desc) throws InstantiationException, IllegalAccessException {
        Class<?> cls = desc.getPropertyEditorClass();
        if (null != cls) return (PropertyEditor) cls.newInstance();
        return getPropertyEditorValue(desc.getPropertyType());
    }
}
