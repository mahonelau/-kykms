package org.jeecg.modules.KM.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("km_doc_comments")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc_comments对象", description="km_doc_comments对象")
public class KmDocComments implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**docid*/
    @Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String docId;
    /**create_time*/
    @Excel(name = "create_time", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "id")
    private Date createTime;
    /**create_by*/
    @Excel(name = "create_by", width = 15)
    @ApiModelProperty(value = "id")
    private String createBy;
    /**comment*/
    @Excel(name = "comment", width = 15)
    @ApiModelProperty(value = "comment")
    private String comment;


}
