package org.jeecg.modules.KM.service.impl;

import org.jeecg.modules.KM.entity.KmDocVersion;
import org.jeecg.modules.KM.mapper.KmDocVersionMapper;
import org.jeecg.modules.KM.service.IKmDocVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;


@Service
public class KmDocVersionServiceImpl extends ServiceImpl<KmDocVersionMapper, KmDocVersion> implements IKmDocVersionService {

    @Autowired
    private KmDocVersionMapper kmDocVersionMapper;

    @Override
    public List<String> getVersions(String docId){
        return  kmDocVersionMapper.getversions(docId);
    }
}
