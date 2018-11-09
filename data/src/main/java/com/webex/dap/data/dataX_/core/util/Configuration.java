package com.webex.dap.data.dataX_.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Configuration {
    private Object root = null;

    public String getString(final String path) {
        Object string = this.get(path);

        if (null == string) {
            return null;
        }

        return String.valueOf(string);
    }

    /*

     */
    public Object get(final String path) {
        this.checkPath(path);
        try {
            return this.findObject(path);
        } catch (Exception e) {
            return null;
        }
    }

    private Object findObject(String path) {
        boolean isRootQuery = StringUtils.isBlank(path);
        if (isRootQuery) {
            return this.root;
        }

        Object target = this.root;

        for (final String each : split2List(path)) {

        }

        return null;
    }

    private List<String> split2List(String path) {
        return Arrays.asList(StringUtils.split(split(path), "."));
    }

    private String split(String path) {
        return null;
    }

    private void checkPath(String path) {

    }
}
