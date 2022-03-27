package org.jeecg.modules.KM.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.KM.entity.KmDocBusinessType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface KmDocBusinessTypeMapper extends BaseMapper<KmDocBusinessType> {

    @Select("select business_type from km_doc_business_type where doc_id=#{id}")
    List<String> getBusinessTypes(@Param("id") String docId);

}
