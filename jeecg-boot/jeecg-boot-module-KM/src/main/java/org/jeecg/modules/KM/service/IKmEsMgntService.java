package org.jeecg.modules.KM.service;

import org.jeecg.common.api.vo.Result;
import java.io.IOException;

public interface IKmEsMgntService  {

    Result<?> initTemplate() throws IOException;

    Result<?> initKmDocTemplate() throws IOException;

    Result<?> initKmDocVisitTemplate() throws IOException ;

    Result<?> initKmSearchRecordTemplate() throws IOException ;
}
