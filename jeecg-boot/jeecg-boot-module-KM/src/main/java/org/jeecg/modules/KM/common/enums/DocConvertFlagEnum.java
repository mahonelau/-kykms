package org.jeecg.modules.KM.common.enums;

public enum DocConvertFlagEnum {

    Fail(-1),
    WaitConvert(0),
    Converted(1),
    NonConvert(2);

    DocConvertFlagEnum(Integer code ) {
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
