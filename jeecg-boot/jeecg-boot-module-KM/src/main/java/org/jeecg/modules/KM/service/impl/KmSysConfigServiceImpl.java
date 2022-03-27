package org.jeecg.modules.KM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.KM.entity.KmSysConfig;
import org.jeecg.modules.KM.mapper.KmSysConfigMapper;
import org.jeecg.modules.KM.service.IKmSysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class KmSysConfigServiceImpl extends ServiceImpl<KmSysConfigMapper, KmSysConfig> implements IKmSysConfigService {

    @Autowired
    KmSysConfigMapper kmSysConfigMapper;

    @Override
    @Cacheable(cacheNames = "kmSysConfig",key = "#sysConfigCode")
    public String getSysConfigValue( String sysConfigCode){
        LambdaQueryWrapper<KmSysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KmSysConfig::getItemCode ,sysConfigCode);
        KmSysConfig kmSysConfig = this.getOne(queryWrapper);
        String retValue = "";
        if(kmSysConfig != null )
            retValue =  kmSysConfig.getItemValue();

        return retValue;
    }
}
