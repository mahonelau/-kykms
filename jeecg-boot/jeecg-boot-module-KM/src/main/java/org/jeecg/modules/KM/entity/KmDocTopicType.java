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
@TableName("km_doc_topic_type")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc_topic_type对象", description="km_doc_topic_type")
public class KmDocTopicType implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**id*/
	@Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String docId;
	/**topicId*/
	@Excel(name = "topicId", width = 15)
    @ApiModelProperty(value = "topicId")
    private String topicId;
}
