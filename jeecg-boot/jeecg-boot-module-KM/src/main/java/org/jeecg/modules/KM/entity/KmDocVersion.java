package org.jeecg.modules.KM.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("km_doc_version")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc_version对象", description="km_doc_version")
public class KmDocVersion implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**docid*/
    @Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private java.lang.String docId;
    /**file_id*/
    @Excel(name = "file_id", width = 15)
    @ApiModelProperty(value = "file_id")
    private java.lang.String fileId;
    /**version*/
    @Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private java.lang.Integer version;
    /**create_time*/
    @Excel(name = "create_time", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "id")
    private Date createTime;
    /**create_by*/
    @Excel(name = "create_by", width = 15)
    @ApiModelProperty(value = "id")
    private java.lang.String createBy;
    /**comment*/
    @Excel(name = "comment", width = 15)
    @ApiModelProperty(value = "comment")
    private java.lang.String comment;


}
