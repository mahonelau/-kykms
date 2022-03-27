package org.jeecg.modules.KM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDocFavourite;
import org.jeecg.modules.KM.mapper.KmDocFavouriteMapper;
import org.jeecg.modules.KM.service.IKmDocFavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class KmDocFavouriteServiceImpl extends ServiceImpl<KmDocFavouriteMapper, KmDocFavourite> implements IKmDocFavouriteService {

    @Autowired
    private  KmDocFavouriteMapper kmDocFavouriteMapper;
    @Override
    public Page<KmDocVO> queryPageList(Page<KmDocVO> page, String userId,  KmDocParamVO kmDocParamVO,String orderBy){
        String dbType = CommonUtils.getDatabaseType();
        return kmDocFavouriteMapper.getPageList(page,userId,kmDocParamVO,dbType,orderBy);

    }

    /*
    增加收藏文档
     */
    @Override
    public Result<?> addFavouriteDoc(String docId){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser != null){
            LambdaQueryWrapper<KmDocFavourite> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(KmDocFavourite::getUserId,sysUser.getId());
            queryWrapper.eq(KmDocFavourite::getDocId,docId);
            if(this.count(queryWrapper) >=1)
                return Result.error("已经收藏的文档无需再次收藏");

            KmDocFavourite kmDocFavourite = new KmDocFavourite();
            kmDocFavourite.setUserId(sysUser.getId());
            kmDocFavourite.setDocId(docId);
            kmDocFavourite.setAddTime(DateUtils.getDate());
            if(super.save(kmDocFavourite))
                return Result.OK();
            else
                return Result.error("保存数据失败");
        }
        else
            return Result.error("获取登录信息异常");
    }

    /*
    删除收藏文档
     */
    @Override
    public Result<?> delFavouriteDoc(String docId){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser != null){
            LambdaQueryWrapper<KmDocFavourite> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(KmDocFavourite::getUserId,sysUser.getId());
            queryWrapper.eq(KmDocFavourite::getDocId,docId);
            if(this.count(queryWrapper) !=1)
                return Result.error("数据异常");
            if(kmDocFavouriteMapper.delete(queryWrapper)>0)
                return Result.OK();
            else
                return Result.error("删除数据失败");
        }
        else
            return Result.error("获取登录信息异常");
    }

}
