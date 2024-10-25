package org.jeecg.modules.KM.common.enums;

public enum DocPublicRemark {
    OwnOrg(1,"本部门及下属"),
    Public(0,"所有部门");

    DocPublicRemark(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
