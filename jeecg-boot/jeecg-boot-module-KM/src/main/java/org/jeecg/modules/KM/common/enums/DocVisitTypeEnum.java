package org.jeecg.modules.KM.common.enums;

public enum DocVisitTypeEnum {


    Upload(0),
    AuditPass(1),
    AuditReject(2),
    ChangePreview(3),
    Download(4),
    View(5),
    Edit(6),
    Delete(7);

    DocVisitTypeEnum(Integer code) {
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
