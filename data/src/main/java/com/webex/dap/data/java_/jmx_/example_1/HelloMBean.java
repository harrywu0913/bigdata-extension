package com.webex.dap.data.java_.jmx_.example_1;

/**
 * Created by harry on 9/19/18.
 */
public interface HelloMBean {
    public String getName();

    public void setName(String name);

    public String getAge();

    public void setAge(String age);

    public void helloWorld();

    public void helloWorld(String str);

    public void getTelephone();
}
