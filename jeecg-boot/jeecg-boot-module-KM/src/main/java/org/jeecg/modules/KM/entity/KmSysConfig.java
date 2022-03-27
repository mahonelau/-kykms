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
@TableName("km_sys_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_sys_config对象", description="km_sys_config")
public class KmSysConfig implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**itemCode*/
	@Excel(name = "itemCode", width = 15)
    @ApiModelProperty(value = "itemCode")
    private java.lang.String itemCode;
	/**itemValue*/
	@Excel(name = "itemValue", width = 15)
    @ApiModelProperty(value = "itemValue")
    private java.lang.String itemValue;
	/**itemName*/
	@Excel(name = "itemName", width = 15)
    @ApiModelProperty(value = "itemName")
    private java.lang.String itemName;
}
