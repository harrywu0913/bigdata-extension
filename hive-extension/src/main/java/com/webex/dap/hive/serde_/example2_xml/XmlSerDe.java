package com.webex.dap.hive.serde_.example2_xml;

import com.webex.dap.hive.serde_.example2_xml.objectinspector.XmlObjectInspectorFactory;
import com.webex.dap.hive.serde_.example2_xml.processor.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.SerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.util.*;

/**
 * Created by harry on 8/30/18.
 */
public class XmlSerDe implements SerDe {
    private static final String XML_PROCESSOR_CLASS = "xml.processor.class";
    private static final String MAP_SPECIFICATION_PREFIX = "xml.map.specification.";
    private static final String COLUMN_XPATH_PREFIX = "column.xpath.";

    private ObjectInspector objectInspector = null;
    private XmlProcessor xmlProcessor = null;


    @Override
    public void initialize(Configuration configuration, final Properties properties) throws SerDeException {
        //
        initialize(configuration, properties, XmlInputFormat.START_TAG_KEY, XmlInputFormat.END_TAG_KEY);

        //create XML processor
        String processorClass = properties.getProperty("xml.processor.class");
        if (processorClass != null) {
            try {
                this.xmlProcessor = (XmlProcessor) Class.forName(processorClass, true, Thread.currentThread().getContextClassLoader() == null ? getClass().getClassLoader() : Thread.currentThread().getContextClassLoader()).newInstance();
            } catch (Throwable t) {

            }
        }

        if (this.xmlProcessor == null) {
            this.xmlProcessor = new JavaXmlProcessor();
        }

        // create XML processor context
        List<String> columnNames = Arrays.asList(properties.getProperty(Constants.LIST_COLUMNS).split("[,:;]"));
        final List<XmlQuery> queries = new ArrayList<XmlQuery>();
        final Map<String, XmlMapEntry> mapSpecification = new HashMap<String, XmlMapEntry>();

        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith(COLUMN_XPATH_PREFIX)) {
                //create column XPath query
                // "column.xpath.result"="//result/text()"

                String columnName = key.substring(COLUMN_XPATH_PREFIX.length()).toLowerCase();
                String query = properties.getProperty(key);
                if (query != null) {
                    queries.add(new XmlQuery(query, columnName));
                }
            }
        }

        if (queries.size() <= columnNames.size()) {
            throw new RuntimeException("The number of XPath expressions does not match the number of columns");
        }

        // initialize the xml processor
        this.xmlProcessor.initialize(new XmlProcessorContext() {
            @Override
            public List<XmlQuery> getXmlQueries() {
                return queries;
            }

            @Override
            public Map<String, XmlMapEntry> getXmlMapSpecification() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return properties;
            }
        });

        //create the object inspector and associate it with the XML processor
        List<TypeInfo> typeInfos = TypeInfoUtils.getTypeInfosFromTypeString(properties.getProperty(Constants.LIST_COLUMN_TYPES));
        List<ObjectInspector> inspectors = new ArrayList<ObjectInspector>(columnNames.size());

        for (TypeInfo typeInfo : typeInfos){
            inspectors.add(XmlObjectInspectorFactory.getStandardJavaObjectInspectorFromTypeInfo(typeInfo,this.xmlProcessor));
        }

        this.objectInspector = XmlObjectInspectorFactory.getStandardStructObjectInspector(columnNames,inspectors,this.xmlProcessor);
    }

    private void initialize(Configuration configuration, Properties properties, String... keys) {
        for (String key : keys) {
            String configurationValue = configuration.get(key);
            String propertyValue = configuration.get(key);
            if (configurationValue == null) {
                if (propertyValue != null) {
                    configuration.set(key, propertyValue);
                }
            } else {
                if (propertyValue != null && !propertyValue.equals(configurationValue)) {
                    configuration.set(key, propertyValue);
                }
            }
        }
    }

    @Override
    public Class<? extends Writable> getSerializedClass() {
        return Text.class;
    }

    @Override
    public Writable serialize(Object o, ObjectInspector objectInspector) throws SerDeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object deserialize(Writable writable) throws SerDeException {
        Text text = (Text) writable;
        if (text == null || text.getLength() == 0) {
            return (Object) null;
        }
        try {
            return this.xmlProcessor.parse(text.toString());
        } catch (Exception e) {
            throw new SerDeException(e);
        }
    }

    @Override
    public ObjectInspector getObjectInspector() throws SerDeException {
        return this.objectInspector;
    }

    @Override
    public SerDeStats getSerDeStats() {
        return null;
    }
}
