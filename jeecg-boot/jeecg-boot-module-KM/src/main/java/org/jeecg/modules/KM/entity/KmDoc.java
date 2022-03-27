package org.jeecg.modules.KM.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("km_doc")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc对象", description="km_doc")
public class KmDoc implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**fileId*/
	@Excel(name = "fileId", width = 15)
    @ApiModelProperty(value = "fileId")
    private String fileId;
	/**previewFileId*/
	@Excel(name = "previewFileId", width = 15)
    @ApiModelProperty(value = "previewFileId")
    private String previewFileId;
	/**originalPreviewFileId*/
	@Excel(name = "originalPreviewFileId", width = 15)
    @ApiModelProperty(value = "originalPreviewFileId")
    private String originalPreviewFileId;
    private String indexId;
    /**orgCode*/
    @Excel(name = "orgCode", width = 15)
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    @ApiModelProperty(value = "orgCode")
    private String orgCode;
    /**depId*/
    @Excel(name = "depId", width = 15)
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    @ApiModelProperty(value = "depId")
    private String depId;
	/**fileSize*/
	@Excel(name = "fileSize", width = 15)
    @ApiModelProperty(value = "fileSize")
    private Long fileSize;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
	/**title*/
	@Excel(name = "title", width = 15)
    @ApiModelProperty(value = "title")
    private String title;
    /**fileNo*/
    @Excel(name = "fileNo", width = 15)
    @ApiModelProperty(value = "fileNo")
    private String fileNo;
    /**serialNumber*/
    @Excel(name = "serialNumber", width = 15)
    @ApiModelProperty(value = "serialNumber")
    private String serialNumber;
	/**fileType*/
	@Excel(name = "fileType", width = 15)
    @ApiModelProperty(value = "fileType")
    private String fileType;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;
	/**category*/
	@Excel(name = "category", width = 15)
    @ApiModelProperty(value = "category")
    @Dict(dicCode = "km_dict_category")
    private String category;
	/**source*/
	@Excel(name = "source", width = 15)
    @ApiModelProperty(value = "source")
    @Dict(dicCode = "km_dict_source")
    private String source;
    /**pubTimeTxt*/
    @Excel(name = "pubTimeTxt", width = 15)
    @ApiModelProperty(value = "pubTimeTxt")
    private String pubTimeTxt;
    /**pubTime*/
    @Excel(name = "pubTime", width = 15)
    @ApiModelProperty(value = "pubTime")
    @Dict(dicCode = "km_dict_pubTimeYear")
    private String pubTime;
	/**keywords*/
	@Excel(name = "keywords", width = 15)
    @ApiModelProperty(value = "keywords")
    private String keywords;
	/**ftiFlag*/
	@Excel(name = "ftiFlag", width = 15)
    @ApiModelProperty(value = "ftiFlag")
    @Dict(dicCode = "dict_fti_flag")
    private Integer ftiFlag;
    /**convertFlag*/
    @Excel(name = "convertFlag", width = 15)
    @ApiModelProperty(value = "convertFlag")
    private Integer convertFlag;
    /**releaseFlag*/
    @Excel(name = "releaseFlag", width = 15)
    @ApiModelProperty(value = "releaseFlag")
    private Integer releaseFlag;
    /**downloadFlag*/
    @Excel(name = "downloadFlag", width = 15)
    @ApiModelProperty(value = "downloadFlag")
    @Dict(dicCode = "dict_downloadFlag")
    private Integer downloadFlag;
    /**publicFlag*/
    @Excel(name = "publicFlag", width = 15)
    @ApiModelProperty(value = "publicFlag")
    @Dict(dicCode = "dict_publicFlag")
    private Integer publicFlag;
	/**downloads*/
	@Excel(name = "downloads", width = 15)
    @ApiModelProperty(value = "downloads")
    private BigInteger downloads;
	/**views*/
	@Excel(name = "views", width = 15)
    @ApiModelProperty(value = "views")
    private BigInteger views;
	/**lastUpdateBy*/
	@Excel(name = "lastUpdateBy", width = 15)
    @ApiModelProperty(value = "lastUpdateBy")
    private String lastUpdateBy;
	/**lastUpdateTime*/
	@Excel(name = "lastUpdateTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "lastUpdateTime")
    private Date lastUpdateTime;
	/**createBy*/
    @ApiModelProperty(value = "createBy")
    private String createBy;
	/**createTime*/
    @Excel(name = "createTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
    /**processMsg*/
    @Excel(name = "processMsg", width = 15)
    @ApiModelProperty(value = "processMsg")
    private String processMsg;
    /**effectTime*/
    @Excel(name = "effectTime", width = 15)
    @ApiModelProperty(value = "effectTime")
    private String effectTime;
    /**publicRemark*/
    @Dict(dicCode = "km_dict_public_remark")
    @Excel(name = "publicRemark", width = 15)
    @ApiModelProperty(value = "publicRemark")
    private String publicRemark;
    /**remark*/
    @Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private String remark;
}
