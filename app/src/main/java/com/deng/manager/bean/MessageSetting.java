package com.deng.manager.bean;

/**
 * Created by deng on 16-5-11.
 */
public class MessageSetting {
    private String name;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageSetting(String name, String id, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
        this.id=id;

    }

    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
