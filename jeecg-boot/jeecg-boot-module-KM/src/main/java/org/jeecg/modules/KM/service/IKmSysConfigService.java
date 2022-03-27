package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmSysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IKmSysConfigService extends IService<KmSysConfig> {

    String getSysConfigValue(String sysConfigCode);

}
