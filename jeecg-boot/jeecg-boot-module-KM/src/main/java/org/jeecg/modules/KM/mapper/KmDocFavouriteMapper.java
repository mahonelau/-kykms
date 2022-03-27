package org.jeecg.modules.KM.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDocFavourite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface KmDocFavouriteMapper extends BaseMapper<KmDocFavourite> {

    Page<KmDocVO> getPageList(Page<KmDocVO> page,
                              @Param("userId") String userId,
                              @Param("kmDocParamVO") KmDocParamVO kmDocParamVO,
                              @Param("dbType")String dbType,
                              @Param("orderBy") String orderBy);

}
