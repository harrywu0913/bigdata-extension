package com.webex.dap.hive.serde_.example2_xml.processor;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by harry on 8/30/18.
 */
public interface XmlProcessorContext {
    public List<XmlQuery> getXmlQueries();
    public Map<String, XmlMapEntry> getXmlMapSpecification();
    public Properties getProperties();

}
