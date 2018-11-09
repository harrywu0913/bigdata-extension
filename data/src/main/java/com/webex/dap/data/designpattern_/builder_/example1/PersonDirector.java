package com.webex.dap.data.designpattern_.builder_.example1;

/**
 * Created by harry on 9/18/18.
 */
public class PersonDirector {
    private PersonBuilder pb;

    public PersonDirector(PersonBuilder pb){
        this.pb = pb;
    }

    public void createPersion(){
        this.pb.buildHead();
        this.pb.buildBody();
        this.pb.buildArmLeft();
        this.pb.buildArmRight();
        this.pb.buildLegLeft();
        this.pb.buildLegRight();
    }
}
