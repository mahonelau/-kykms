package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmDocVisitRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.IOException;
import java.util.List;

public interface IKmDocVisitRecordService extends IService<KmDocVisitRecord> {

    void logVisit(String docId,String ip,Integer visitType);
    void logVisit(String docId,String ip,Integer visitType,String keyword);
    void logVisit(String docId,String ip,Integer visitType,String keyword,String userId);

    List<String> recentlyVisitedDocs(String createBy) throws IOException;
}
