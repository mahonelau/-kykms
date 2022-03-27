package org.jeecg.modules.KM.service.impl;

import org.jeecg.modules.KM.entity.KmDocBusinessType;
import org.jeecg.modules.KM.mapper.KmDocBusinessTypeMapper;
import org.jeecg.modules.KM.service.IKmDocBusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

@Service
public class KmDocBusinessTypeServiceImpl extends ServiceImpl<KmDocBusinessTypeMapper, KmDocBusinessType> implements IKmDocBusinessTypeService {
    @Autowired
    private KmDocBusinessTypeMapper kmDocBusinessTypeMapper;


    @Override
    public List<String> getBusinessTypes(String docId){
        return  kmDocBusinessTypeMapper.getBusinessTypes(docId);
    }
}
