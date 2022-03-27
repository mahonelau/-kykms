package org.jeecg.modules.KM.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc统计结果", description="km_doc")
public class KmDocStatisticsVO implements Serializable {

    private String itemText;
    private Integer amount;
    private String fileSize;
}
