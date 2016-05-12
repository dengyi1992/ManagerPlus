package com.deng.manager.bean;

import java.util.List;

/**
 * Created by deng on 16-5-12.
 */
public class LogBean {


    /**
     * _id : 573441c8ebb1fc4540294e87
     * operator : dengyi
     * operatortype : enAdminUser
     * operate : dengyi给邓燚1授权了
     * time : 2016-05-12T08:41:44.578Z
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
        private String operator;
        private String operatortype;
        private String operate;
        private String time;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getOperatortype() {
            return operatortype;
        }

        public void setOperatortype(String operatortype) {
            this.operatortype = operatortype;
        }

        public String getOperate() {
            return operate;
        }

        public void setOperate(String operate) {
            this.operate = operate;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
