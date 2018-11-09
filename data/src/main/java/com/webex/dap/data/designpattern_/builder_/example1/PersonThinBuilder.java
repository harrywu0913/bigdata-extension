package com.webex.dap.data.designpattern_.builder_.example1;

/**
 * Created by harry on 9/18/18.
 */
public class PersonThinBuilder extends PersonBuilder {
    @Override
    public void buildHead() {
        System.out.println("PersonThinBuilder.buildHead");
    }

    @Override
    public void buildBody() {
        System.out.println("PersonThinBuilder.buildBody");
    }

    @Override
    public void buildArmLeft() {
        System.out.println("PersonThinBuilder.buildArmLeft");
    }

    @Override
    public void buildArmRight() {
        System.out.println("PersonThinBuilder.buildArmRight");
    }

    @Override
    public void buildLegLeft() {
        System.out.println("PersonThinBuilder.buildLegLeft");
    }

    @Override
    public void buildLegRight() {
        System.out.println("PersonThinBuilder.buildLegRight");
    }
}
