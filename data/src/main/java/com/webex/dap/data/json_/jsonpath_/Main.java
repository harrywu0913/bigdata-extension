package com.webex.dap.data.json_.jsonpath_;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.JsonContext;

/**
 * Created by harry on 7/9/18.
 */
public class Main {
    public static void main(String[] args) {

        JsonContext context = new JsonContext(Configuration.builder().jsonProvider(Configuration.defaultConfiguration().jsonProvider()).options(Option.SUPPRESS_EXCEPTIONS).build());

        DocumentContext dc = context.parse("{'a':'b'}").read(JsonPath.compile("$.message"));
    }
}
