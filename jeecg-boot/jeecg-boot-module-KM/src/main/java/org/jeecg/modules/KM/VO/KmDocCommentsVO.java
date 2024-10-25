package org.jeecg.modules.KM.VO;


import lombok.Data;
import org.jeecg.modules.KM.entity.KmDocComments;

import java.io.Serializable;

@Data
public class KmDocCommentsVO extends KmDocComments implements Serializable {
    private String avatar;

}
