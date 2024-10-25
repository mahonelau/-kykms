package org.jeecg.modules.KM.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc全局计数", description="km_doc")
public class KmDocSummaryVO implements Serializable {
    private Integer kmAmount;
    private Long fileSizeLong;
    private Long viewsLong;
    private Long downloadsLong;
    private Long commentsLong;
    private Long favouritesLong;
    private String fileSizeString;
}
