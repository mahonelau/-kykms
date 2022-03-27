package org.jeecg.modules.KM.common.enums;

public enum DocStatusEnum {
    Draft(0,"草稿"),
    WaitAudit(1,"待审核"),
    Passed(2,"审核通过"),
    Reject(3,"审核驳回"),
    Delete(9,"删除");

    DocStatusEnum(Integer code, String name) {
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
