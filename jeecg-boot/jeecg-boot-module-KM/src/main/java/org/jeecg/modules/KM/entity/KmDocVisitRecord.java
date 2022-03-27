package org.jeecg.modules.KM.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("km_doc_visit_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc_visit_record对象", description="km_doc_visit_record")
public class KmDocVisitRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**id*/
	@Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String docId;
	/**1：上传 2：预览 3：下载 4：删除*/
	@Excel(name = "1：上传 2：预览 3：下载 4：删除", width = 15)
    @ApiModelProperty(value = "1：上传 2：预览 3：下载 4：删除")
    private Integer visitType;
	private String keywords;
	private String keywordsMax;
	/**sourceIp*/
	@Excel(name = "sourceIp", width = 15)
    @ApiModelProperty(value = "sourceIp")
    private String sourceIp;
	/**createBy*/
    @ApiModelProperty(value = "createBy")
    private String createBy;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
}
