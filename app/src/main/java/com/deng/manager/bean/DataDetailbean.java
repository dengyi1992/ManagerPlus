package com.deng.manager.bean;

/**
 * Created by deng on 16-5-12.
 */
public class DataDetailbean {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataDetailbean(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
