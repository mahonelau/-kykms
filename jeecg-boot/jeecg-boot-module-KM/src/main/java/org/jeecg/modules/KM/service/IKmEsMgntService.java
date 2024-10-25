package org.jeecg.modules.KM.service;

import org.jeecg.common.api.vo.Result;
import java.io.IOException;

public interface IKmEsMgntService  {

    Result<?> initTemplateAndSyncDocs() throws IOException;

    //为初始化索引
    void initEXIndex();

    Result<?> syncReleasedDocToES() throws IOException;

    Result<?> initKmDocTemplate() throws IOException;

    Result<?> initKmDocVisitTemplate() throws IOException ;

    Result<?> initKmSearchRecordTemplate() throws IOException ;
}
