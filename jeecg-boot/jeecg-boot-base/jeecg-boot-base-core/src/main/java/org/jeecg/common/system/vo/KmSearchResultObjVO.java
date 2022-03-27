package org.jeecg.common.system.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

@Data
public class KmSearchResultObjVO<T> implements Serializable {
    private boolean success;
    private List<String>  paramPath;
    private IPage<T> kmSearchResultVOPage;
    private String message;
}
