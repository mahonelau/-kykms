package org.jeecg.modules.KM.service;

import org.jeecg.common.system.vo.SysCategoryModel;
import org.jeecg.modules.KM.entity.KmSearchRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.IOException;
import java.util.List;

public interface IKmSearchRecordService extends IService<KmSearchRecord> {

    void logSearch(String keyword,String title,String content,String topicCode,String ip);

    List<String> hotKeywordReport() throws IOException;
    List<String> hotTopicReport() ;
    List<SysCategoryModel> retriveHotTopic() throws IOException;
}
