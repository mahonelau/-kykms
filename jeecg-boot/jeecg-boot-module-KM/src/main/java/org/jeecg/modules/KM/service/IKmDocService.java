package org.jeecg.modules.KM.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.KmSearchResultObjVO;
import org.jeecg.modules.KM.VO.*;
import org.jeecg.modules.KM.entity.KmDoc;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.KM.entity.KmFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;


public interface IKmDocService extends IService<KmDoc> {

    KmSearchResultObjVO searchESKmDoc(Page<KmSearchResultVO> page, KmDocEsParamVO kmDocEsParamVO, HttpServletRequest req) throws IOException;
    KmSearchResultObjVO checkDuplicateESKmDoc(Page<KmSearchResultVO> page, KmDocEsParamVO kmDocEsParamVO, HttpServletRequest req) throws IOException;

    Page<KmDocVO> queryPageList(Page<KmDocVO> page, KmDocParamVO kmDocParamVO,String orderBy);

    //首页最新发布文档列表
    Page<KmDocVO> queryPublicPageList(Page<KmDocVO> page, KmDocParamVO kmDocParamVO,String orderBy);

    void convertDocSync(KmDoc doc);

    void indexDocSync(KmDoc doc);

    void indexDocSyncBatch(List<String> idList);

    KmDoc saveDoc(KmFile kmFile);

    KmDoc getDocByFileId(String fileId);

    void downloadKmDoc(String docId, HttpServletResponse response, HttpServletRequest req) throws IOException;

    void viewKmDoc( String docId, HttpServletResponse response,HttpServletRequest req);

    Result<?> editDraft(KmDocParamVO kmDocParamVO);

    Result<?> editAndRelease(KmDocParamVO kmDocParamVO,HttpServletRequest req);
    Result<?> auditDoc(String id,HttpServletRequest req);

    Result<?> editAuditPassed(KmDocParamVO kmdocTarget);

    Result<?> editReleaseFlag(KmDoc kmdocTarget);

    Page<KmDoc> queryTopicPageList(Page<KmDoc> page,String topicId);

    Result<?> addDocToTopic( String topicId, List<String> docIds);

    Result<?> removeDocFromTopic( String topicId, String docId);

    Page<KmDocStatisticsVO> queryKmDocStatistics(Page<KmDocStatisticsVO> page,Integer statisticsType);

    Result<?> deleteDoc(String docId,HttpServletRequest req);

    KmDocEsVO getEsDocByDocId(String indexId);

    void initESIndex();
}
