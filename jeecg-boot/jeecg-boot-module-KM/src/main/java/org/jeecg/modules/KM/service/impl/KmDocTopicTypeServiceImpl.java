package org.jeecg.modules.KM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.KM.entity.KmDocTopicType;
import org.jeecg.modules.KM.mapper.KmDocTopicTypeMapper;
import org.jeecg.modules.KM.service.IKmDocTopicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;


@Service
public class KmDocTopicTypeServiceImpl extends ServiceImpl<KmDocTopicTypeMapper, KmDocTopicType> implements IKmDocTopicTypeService {

    @Autowired
    KmDocTopicTypeMapper kmDocTopicTypeMapper;

    @Override
    public KmDocTopicType getByDocIdAndTopicId(  String docId,   String topicId){
//        return kmDocTopicTypeMapper.getByDocIdAndTopicId(docId,topicId);
        LambdaQueryWrapper<KmDocTopicType> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(KmDocTopicType::getDocId,docId);
        queryWrapper.eq(KmDocTopicType::getTopicId,topicId);
        KmDocTopicType tmpKmDocTopicType = super.getOne(queryWrapper);
        return tmpKmDocTopicType;
    }

    @Override
    public List<String> getDocTopicCodes(String docId){
        List<String> docTopicCodeList = kmDocTopicTypeMapper.getDocTopicCodes(docId);
        if(!docTopicCodeList.isEmpty()){
            return  docTopicCodeList;
        }
        else {
            return null;
        }
    }
}
