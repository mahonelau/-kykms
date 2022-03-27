package org.jeecg.modules.KM.common.enums;

public enum DocReleaseFlagEnum {
    Off(0),
    Released(1);

    DocReleaseFlagEnum(Integer code ) {
        this.code = code;
    }

    private Integer code;
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
