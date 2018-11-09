package com.webex.dap.hive.serde_.example2_xml.processor;

import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Created by harry on 8/30/18.
 */
public class NodeArray extends ArrayList<Node> implements SerDeArray {
    public NodeArray withName(String name) {
        return this;
    }
}
