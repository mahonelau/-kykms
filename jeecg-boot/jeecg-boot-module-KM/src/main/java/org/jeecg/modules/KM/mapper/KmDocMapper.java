package org.jeecg.modules.KM.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.VO.KmDocStatisticsVO;
import org.jeecg.modules.KM.VO.KmDocSummaryVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDoc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

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
    List<KmDocStatisticsVO> queryKmDocStatistics( @Param("statisticsType") Integer statisticsType, @Param("dbType")String dbType);
    KmDocSummaryVO queryKmDocSummary(@Param("dbType")String dbType);

    @Select("SELECT count(0) FROM km_doc_topic_type kdtp join km_doc kd on kd.id = kdtp.doc_id where  kd.status!=9 and topic_id=#{topidId} ")
    Integer checkTopicOfDoc(@Param("topidId") String topidId);
    @Select("SELECT count(0) FROM km_doc_business_type kdbt join km_doc kd on kd.id = kdbt.doc_id where  kd.status!=9 and kdbt.business_type=#{businessType} ")
    Integer checkBusinessTypeOfDoc(@Param("businessType") String businessType);

}
