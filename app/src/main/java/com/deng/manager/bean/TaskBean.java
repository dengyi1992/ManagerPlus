package com.deng.manager.bean;

/**
 * Created by deng on 16-3-12.
 */
public class TaskBean {
    public int Cycle;
    public String interfaceUrl;
    public String interfaceTag;
    public String type;
    public boolean isRepeat;

    public int getCycle() {
        return Cycle;
    }

    public void setCycle(int cycle) {
        Cycle = cycle;
    }

    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    public String getInterfaceTag() {
        return interfaceTag;
    }

    public void setInterfaceTag(String interfaceTag) {
        this.interfaceTag = interfaceTag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }
}
