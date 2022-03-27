package org.jeecg.modules.KM.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc检索对象", description="km_doc")
public class KmSearchResultVO  implements Serializable {

    private Integer favourite;

    private String id;
    private String fileId;
    private String previewFileId;
    private String fileNo;
    private Integer downloadFlag;
    private Integer publicFlag;
    private Long size;
    private String title;
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String orgCode;
    private String fileType;
    private String category;
    @Dict(dicCode = "km_dict_source")
    private String source;
    private String  pubTimeTxt;
    @Dict(dicCode = "km_dict_versions")
    private String versions;
    @Dict(dicCode = "km_dict_business")
    private String businessType;
    private String keywords;
    private String topicCodes;
    private BigInteger downloads;
    private BigInteger views;
    private String effectTime;
    private String remark;
}
