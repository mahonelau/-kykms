package org.jeecg.modules.KM.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.KM.VO.KmDocCommentsVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDoc;
import org.jeecg.modules.KM.entity.KmDocComments;
import org.jeecg.modules.KM.mapper.KmDocCommentsMapper;
import org.jeecg.modules.KM.service.IKmDocCommentsService;
import org.jeecg.modules.KM.service.IKmDocService;
import org.jeecg.modules.KM.service.IKmDocVersionService;
import org.jeecg.modules.KM.service.IKmFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


@Service
public class KmDocCommentsServiceImpl extends ServiceImpl<KmDocCommentsMapper, KmDocComments> implements IKmDocCommentsService {

    @Autowired
    private KmDocCommentsMapper kmDocCommentsMapper;

    @Autowired
    private IKmDocService kmDocService;

    @Override
    public List<String> getComments(String docId){
        return  kmDocCommentsMapper.getComments(docId);
    }

    @Override
    public Page<KmDocCommentsVO> queryPageList(Page<KmDocCommentsVO> page, KmDocComments kmDocComments){
        Page<KmDocCommentsVO> pageList = kmDocCommentsMapper.getPageList(page, kmDocComments);

        return pageList;
    }

    @Override
    public boolean save(KmDocComments kmDocComments){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null)
            return false;
        String userId = sysUser.getUsername();

        kmDocComments.setCreateBy(userId);
        kmDocComments.setCreateTime(DateTime.now());

        kmDocCommentsMapper.insert(kmDocComments);
        KmDoc kmdoc = kmDocService.getById(kmDocComments.getDocId());
        if (kmdoc != null) {
            kmdoc.setComments(kmdoc.getComments() == null? BigInteger.valueOf(1) : kmdoc.getComments().add(  BigInteger.valueOf(1)) );
        }
        kmDocService.updateById(kmdoc);
        return true;
    }
}
