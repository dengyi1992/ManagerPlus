package com.deng.manager.bean;

import java.util.List;

/**
 * Created by deng on 16-4-4.
 */
public class Task {

    /**
     * msg : success
     * content : [{"id":8,"ctime":"2016-03-17T01:30:52.000Z","interfaceurl":"http://localhost:3000/toutiao_common_c?tablename=_keji&toutiaonum=m5886334912&pagenum=1","interfacetag":"科技","cycle":5,"ifopen":0,"type":"??"},{"id":9,"ctime":"2016-03-17T02:04:33.000Z","interfaceurl":"http://localhost:3000/crawler","interfacetag":"试试","cycle":1,"ifopen":0,"type":"16363"},{"id":11,"ctime":"2016-03-17T02:04:33.000Z","interfaceurl":"http://localhost:3000/budejie_c","interfacetag":"试试","cycle":1,"ifopen":0,"type":"16363"},{"id":25,"ctime":"2016-03-17T02:04:33.000Z","interfaceurl":"http://localhost:3000/neihan_c","interfacetag":"试试","cycle":1,"ifopen":0,"type":"16363"}]
     */

    private String msg;
    /**
     * id : 8
     * ctime : 2016-03-17T01:30:52.000Z
     * interfaceurl : http://localhost:3000/toutiao_common_c?tablename=_keji&toutiaonum=m5886334912&pagenum=1
     * interfacetag : 科技
     * cycle : 5
     * ifopen : 0
     * type : ??
     */

    private List<ContentBean> content;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        private int id;
        private String ctime;
        private String interfaceurl;
        private String interfacetag;
        private int cycle;
        private int ifopen;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getInterfaceurl() {
            return interfaceurl;
        }

        public void setInterfaceurl(String interfaceurl) {
            this.interfaceurl = interfaceurl;
        }

        public String getInterfacetag() {
            return interfacetag;
        }

        public void setInterfacetag(String interfacetag) {
            this.interfacetag = interfacetag;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public int getIfopen() {
            return ifopen;
        }

        public void setIfopen(int ifopen) {
            this.ifopen = ifopen;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
