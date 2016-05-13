package com.deng.manager.bean;

import java.util.List;

/**
 * Created by deng on 16-5-13.
 */
public class clientUserBean {

    /**
     * _id : 57354a0e0918d6b545d04c12
     * devId : asdsadaskjdlasdasdkk
     * type : 1qweqweqe
     * time : 2016-05-13T03:29:18.683Z
     */

    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public static class SuccessBean {
        private String _id;
        private String devId;
        private String type;
        private String time;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
