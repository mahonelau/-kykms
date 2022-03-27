package org.jeecg.modules.KM.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.KM.entity.KmDocTopicType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface KmDocTopicTypeMapper extends BaseMapper<KmDocTopicType> {
    List<String> getDocTopicCodes(@Param("docId") String docId);

}
