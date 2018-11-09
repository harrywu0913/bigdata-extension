package com.webex.dap.data.zk.election.example1;

import java.io.Serializable;

/**
 * Created by harry on 7/25/18.
 */
public class RunningData implements Serializable {

    private static final long serialVersionUID = 4260577459043203630L;

    private Long cid;
    private String name;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}