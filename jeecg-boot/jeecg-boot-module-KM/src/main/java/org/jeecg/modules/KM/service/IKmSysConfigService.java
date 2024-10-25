package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmSysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IKmSysConfigService extends IService<KmSysConfig> {

    String getSysConfigValue(String sysConfigCode);
    List<KmSysConfig> querySiteInfo();

    Map<String,String> queryAllConfig();
}
