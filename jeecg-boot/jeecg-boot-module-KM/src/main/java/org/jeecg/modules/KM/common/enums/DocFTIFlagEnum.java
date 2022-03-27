package org.jeecg.modules.KM.common.enums;

public enum DocFTIFlagEnum {
    Fail(-1),
    WaitProcess(0),
    Processed(1),
    NonFTI(2);

    DocFTIFlagEnum(Integer code ) {
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
