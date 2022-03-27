package org.jeecg.modules.KM.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDocFavourite;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IKmDocFavouriteService extends IService<KmDocFavourite> {

    Page<KmDocVO> queryPageList(Page<KmDocVO> page, String userId,  KmDocParamVO kmDocParamVO,String orderBy);

    Result<?> addFavouriteDoc(String docId);

    Result<?> delFavouriteDoc(String docId);


}
