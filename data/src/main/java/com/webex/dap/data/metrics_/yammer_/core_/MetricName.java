package com.webex.dap.data.metrics_.yammer_.core_;

import javax.management.ObjectName;

/**
 * Created by harry on 8/7/18.
 */
public class MetricName implements Comparable<MetricName> {
    private final String group;
    private final String type;
    private final String name;
    private final String scope;
    private final String mBeanName;

    public MetricName(Class<?> klass, String name) {
        this(klass, name, null);
    }

    public MetricName(String group, String type, String name) {
        this(group, type, name, null);
    }

    public MetricName(Class<?> klass, String name, String scope) {
        this(klass.getPackage() == null ? "" : klass.getPackage().getName(), klass.getSimpleName().replaceAll("\\$$", ""), name, scope);
    }

    public MetricName(String group, String type, String name, String scope) {
        this(group, type, name, scope, createMBeanName(group, type, name, scope));
    }

    public MetricName(String group, String type, String name, String scope, String mBeanName) {
        if (group == null || type == null) {
            throw new IllegalArgumentException("Both group and type need to be specified");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name needs to be specified");
        }
        this.group = group;
        this.type = type;
        this.name = name;
        this.scope = scope;
        this.mBeanName = mBeanName;
    }

    public String getGroup() {
        return group;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getScope() {
        return scope;
    }

    public String getmBeanName() {
        return mBeanName;
    }

    private static String createMBeanName(String group, String type, String name, String scope) {
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(ObjectName.quote(group)).append(":type=").append(ObjectName.quote(type));

        if (scope != null) {
            nameBuilder.append(",scope=").append(ObjectName.quote(scope));
        }

        if (name.length() > 0) {
            nameBuilder.append(",name=").append(ObjectName.quote(name));
        }

        return nameBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricName that = (MetricName) o;
        return mBeanName.equals(that.mBeanName);
    }

    @Override
    public int hashCode() {
        return mBeanName.hashCode();
    }

    public String toString() {
        return mBeanName;
    }

    @Override
    public int compareTo(MetricName o) {
        return mBeanName.compareTo(o.mBeanName);
    }

    public boolean hasScope() {
        return scope != null;
    }
}
