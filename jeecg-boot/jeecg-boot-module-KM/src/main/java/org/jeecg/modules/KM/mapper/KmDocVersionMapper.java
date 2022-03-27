package org.jeecg.modules.KM.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.KM.entity.KmDocVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface KmDocVersionMapper extends BaseMapper<KmDocVersion> {
    @Select("select version from km_doc_version where doc_id=#{id}")
    List<String> getversions(@Param("id") String docId);


}
