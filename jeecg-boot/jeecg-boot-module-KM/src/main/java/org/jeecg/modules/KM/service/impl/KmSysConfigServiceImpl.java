package org.jeecg.modules.KM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.KM.entity.KmSysConfig;
import org.jeecg.modules.KM.mapper.KmSysConfigMapper;
import org.jeecg.modules.KM.service.IKmSysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
//    @Cacheable(cacheNames = "kmSysConfig",key = "#sysConfigCode")
    public List<KmSysConfig> querySiteInfo(){
        LambdaQueryWrapper<KmSysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(KmSysConfig::getItemCode ,"Site");
        List<KmSysConfig> kmSysConfigList = this.list(queryWrapper);
//        KmSysConfig kmSysConfig = (KmSysConfig) list;
//        String retValue = "";
//        if(kmSysConfig != null )
//            retValue =  kmSysConfig.getItemValue();

        return kmSysConfigList;
    }

    @Override
    public Map<String,String> queryAllConfig(){
        List<KmSysConfig> sysConfigs = this.list();
        Map<String, String> res = new HashMap<String, String>();
        for (KmSysConfig config : sysConfigs) {
            String configCode = config.getItemCode();
            String configValue = config.getItemValue();
            res.put(configCode,configValue);
        }
        log.debug("-------登录KM参数-----" + res.toString());
        return res;
    }
}
