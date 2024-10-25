package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmDocTopicType;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IKmDocTopicTypeService extends IService<KmDocTopicType> {
    KmDocTopicType getByDocIdAndTopicId(  String docId,   String topicId);

    boolean removeDocFromAllTopics(  String docId );

    List<String> getDocTopicCodes(String docId);
}
