package org.jeecg.modules.KM.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.VO.KmDocStatisticsVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDoc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface KmDocMapper extends BaseMapper<KmDoc> {

    Page<KmDocVO> getPageList(Page<KmDocVO> page,
                              @Param("userId") String userId,
                              @Param("kmDocParamVO") KmDocParamVO kmDocParamVO,
                              @Param("permissionSql")String permissionSql,
                              @Param("dbType")String dbType,
                              @Param("orderBy") String orderBy);

    KmDoc getKmDocByFileId(@Param("fileId") String fileId);

    Page<KmDoc> queryTopicPageList(Page<KmDoc> page,@Param("topicId") String topicId);

    Page<KmDocStatisticsVO> queryKmDocStatistics(Page<KmDocStatisticsVO> page,@Param("statisticsType") Integer statisticsType,@Param("dbType")String dbType);
}
