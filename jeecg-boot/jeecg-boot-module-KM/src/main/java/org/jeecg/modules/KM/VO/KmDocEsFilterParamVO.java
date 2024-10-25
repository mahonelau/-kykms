package org.jeecg.modules.KM.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class KmDocEsFilterParamVO implements Serializable {
    //高级检索标识 0-普通 1-高级
    private Boolean advSearchFlag;
    @ApiModelProperty(value = "是否精确检索标识：0-否,1-是")
    private Boolean phraseMatchSearchFlag;

    private String title;

    private List<String> keywords;

    private String content;

    private List<String> category;

    private List<String> businessTypes;

	private List<String> topicCodes;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTimeStart")
    private Date createTimeStart;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTimeEnd")
    private Date createTimeEnd;

}
