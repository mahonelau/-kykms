package org.jeecg.modules.KM.VO;


import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.KM.entity.KmDoc;
import java.io.Serializable;

@Data
public class KmDocVO extends KmDoc implements Serializable {
    private Integer favourite;
    @Dict(dicCode = "km_dict_business")
    private String businessTypes;
    @Dict(dicCode = "km_dict_versions")
    private String versions;
    @Dict(dicCode="id",dictTable = "sys_category",dicText = "name")
    private String topicIds;

}
