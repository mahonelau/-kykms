package org.jeecg.modules.KM.VO;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.KM.entity.KmDoc;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class KmDocParamVO extends KmDoc implements Serializable {

    //多状态查询
    private List<Integer> statusList;

    private List<String> businessTypeList;
    private List<String> versionList;
    private List<String> sourceList;


    private List<String> addBusinessTypeList;
    private List<String> removeBusinessTypeList;

    private List<String> addVersionList;
    private List<String> removeVersionList;

    private List<String> addTopicIdList;
    private List<String> removeTopicIdList;

    private String pubTimeStart;

    private String pubTimeEnd;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTimeStart")
    private Date createTimeStart;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTimeEnd")
    private Date createTimeEnd;

}
