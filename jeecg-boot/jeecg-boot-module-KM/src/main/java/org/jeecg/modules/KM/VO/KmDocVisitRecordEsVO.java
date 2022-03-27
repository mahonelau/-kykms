package org.jeecg.modules.KM.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

@Data
public class KmDocVisitRecordEsVO implements Serializable {
    private String docId;
	/**1：上传 2：预览 3：下载 4：删除*/
    private Integer visitType;
	private String keywords;
	private String[] keywordsMax;
    private String sourceIp;
    private String createBy;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
}
