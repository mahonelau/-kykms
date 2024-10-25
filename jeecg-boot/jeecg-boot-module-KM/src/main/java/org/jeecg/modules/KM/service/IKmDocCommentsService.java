package org.jeecg.modules.KM.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.KM.VO.KmDocCommentsVO;
import org.jeecg.modules.KM.entity.KmDocComments;

import java.util.List;


public interface IKmDocCommentsService extends IService<KmDocComments> {

    List<String> getComments(String docId);

    Page<KmDocCommentsVO> queryPageList(Page<KmDocCommentsVO> page, KmDocComments kmDocComments);

    boolean save(KmDocComments kmDocComments);
}
