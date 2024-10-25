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
@TableName("km_sys_db_backup")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_sys_db_backup对象", description="数据库备份记录")
public class KmSysDbBackup implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**fileName*/
	@Excel(name = "fileName", width = 15)
    @ApiModelProperty(value = "fileName")
    private String fileName;
	/**filePath*/
	@Excel(name = "filePath", width = 15)
    @ApiModelProperty(value = "filePath")
    private String filePath;
    /**size*/
    @Excel(name = "size", width = 15)
    @ApiModelProperty(value = "size")
    private Long size;
    /**createTime*/
    @Excel(name = "createTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
}
