package com.webex.dap.hive.serde_.example2_xml.processor;

import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harry on 8/30/18.
 */
public class JavaXmlProcessor implements XmlProcessor {
    private static TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
    protected static DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = null;
    private DocumentBuilder builder = null;

    private static XPathFactory XPATH_FACTORY = null;

    static {
        DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
        DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);
        DOCUMENT_BUILDER_FACTORY.setIgnoringComments(true);
        // Theoretically we could use setIgnoringElementContentWhitespace(true)
        // but that would require a validating parser and the schema which we do not always have.
        // As a workaround we'll use custom solution to trim the whitespace from the text nodes
        // and drop them if all the text is just whitespace.
        // See also http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6545684
        XPATH_FACTORY = XPathFactory.newInstance();
    }

    private List<JavaXmlQuery> queries = new ArrayList<JavaXmlQuery>();
    private Map<String, XmlMapEntry> mapSpecification = null;

    public void initialize(XmlProcessorContext xmlProcessorContext){
        try{
            this.builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            XPath xpath = XPATH_FACTORY.newXPath();
            for (XmlQuery xmlQuery : xmlProcessorContext.getXmlQueries()){
//                this.queries.add(new JavaXmlQuery(xmlQuery));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Map<String,NodeArray> parse(String value){
        Map<String,NodeArray> result = null;
        Document document = null;
        try{
            result = new HashMap<String,NodeArray>();
            document = this.builder.parse(new InputSource(new StringReader(value)));
            for (JavaXmlQuery query : this.queries){
                XPathExpression expression = query.getExpression();
                String name = query.getName();

                NodeArray nodeArray = new NodeArray().withName(name);
                if (expression != null){
                    NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

                    for (int nodeIndex = 0;nodeIndex < nodeList.getLength();++nodeIndex){
                        Node node = nodeList.item(nodeIndex);
                        if (node.getNodeType() == Node.TEXT_NODE){
                            Node text = trimTextNode(node);

                            if (text != null){
                                nodeArray.add(node);
                            }
                        }else{
                            trimWhitespace(node);
                            nodeArray.add(node);
                        }
                    }
                }

                result.put(name,nodeArray);
            }
        }finally {
            return result;
        }
    }

    @Override
    public List getList(Object data) {
        return null;
    }

    @Override
    public Object getPrimitiveObjectValue(Object primitive, PrimitiveObjectInspector.PrimitiveCategory primitiveCategory) {
        return null;
    }

    private Node trimTextNode(Node node){
        String trimmedValue = node.getNodeValue().trim();
        if (trimmedValue.length() == 0){
            return null;
        }else{
            node.setNodeValue(trimmedValue);
            return node;
        }
    }

    private void trimWhitespace(Node node){
        List<Node> doomedChildren = new ArrayList<Node>();
        NodeList children = node.getChildNodes();
        for (int childIndex = 0; childIndex < children.getLength(); ++childIndex) {
            Node child = children.item(childIndex);
            short nodeType = child.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                trimWhitespace(child);
            } else if (nodeType == Node.TEXT_NODE) {
                String trimmedValue = child.getNodeValue().trim();
                if (trimmedValue.length() == 0) {
                    doomedChildren.add(child);
                } else {
                    child.setNodeValue(trimmedValue);
                }
            } else if (nodeType == Node.COMMENT_NODE) {
                node.removeChild(child);
            }
        }
        for (Node doomed : doomedChildren) {
            node.removeChild(doomed);
        }
    }
}
