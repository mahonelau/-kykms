package org.jeecg.modules.KM.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("km_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_file对象", description="km_file")
public class KmFile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**sha256*/
	@Excel(name = "sha256", width = 15)
    @ApiModelProperty(value = "sha256")
    private String sha256;
	/**physicalPath*/
	@Excel(name = "physicalPath", width = 15)
    @ApiModelProperty(value = "physicalPath")
    private String physicalPath;
	/**originalName*/
	@Excel(name = "originalName", width = 15)
    @ApiModelProperty(value = "originalName")
    private String originalName;
}
