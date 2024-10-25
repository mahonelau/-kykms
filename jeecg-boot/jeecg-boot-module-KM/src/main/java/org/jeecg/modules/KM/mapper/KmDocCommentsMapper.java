package org.jeecg.modules.KM.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.KM.VO.KmDocCommentsVO;
import org.jeecg.modules.KM.entity.KmDocComments;

import java.util.List;

public interface KmDocCommentsMapper extends BaseMapper<KmDocComments> {
    @Select("select * from km_doc_comments where doc_id=#{docId} order by create_time desc")
    List<String> getComments(@Param("docId") String docId);
    @Select("select kdc.*,su.avatar from km_doc_comments kdc left join sys_user su on kdc.create_by=su.username where doc_id=#{kmDocComments.docId} order by create_time desc")
    Page<KmDocCommentsVO>  getPageList(Page<KmDocCommentsVO> page, KmDocComments kmDocComments);

}
