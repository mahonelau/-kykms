package org.jeecg.modules.KM.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc检索对象", description="km_doc")
public class KmSearchResultVO  implements Serializable {

    private Integer favourite;

    private String id;
    private String fileId;
    private String previewFileId;
    private Integer downloadFlag;
    private Integer publicRemark;
    private Long fileSize;
    private String content;
    private String title;
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "org_code")
    private String orgCode;
    private String createBy;
    private String fileType;
    @Dict(dicCode = "km_dict_category")
    private String category;
    private Date createTime;
    @Dict(dicCode = "km_dict_business")
    private String businessTypes;
    private String keywords;
    @Dict(dicCode="code",dictTable = "sys_category",dicText = "name")
    private String topicCodes;
    private BigInteger downloads;
    private BigInteger views;
    private String remark;
}
