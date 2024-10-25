package org.jeecg.common.system.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="检索返回对象", description="检索返回对象")
public class KmSearchResultObjVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private List<String>  paramPath;
    private IPage<T> kmSearchResultVOPage;
    private String message;

    public KmSearchResultObjVO(){}
}
