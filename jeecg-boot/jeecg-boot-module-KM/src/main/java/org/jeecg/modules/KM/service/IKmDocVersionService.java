package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmDocVersion;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;


public interface IKmDocVersionService extends IService<KmDocVersion> {
//    KmDocVersion getByDocIdAndVersion(  String docId,   String version);

    List<String> getVersions(String docId);
}
