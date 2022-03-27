package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmDocBusinessType;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IKmDocBusinessTypeService extends IService<KmDocBusinessType> {
//    KmDocBusinessType getByDocIdAndBusinessType(  String docId,  String businessType);

    List<String> getBusinessTypes(String docId);
}
