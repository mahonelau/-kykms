package org.jeecg.modules.KM.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.KmSearchResultObjVO;
import org.jeecg.modules.KM.VO.*;
import org.jeecg.modules.KM.entity.KmDoc;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.KM.entity.KmFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;


public interface IKmDocService extends IService<KmDoc> {

    Result<?> updateOfficeFile(String docId, File file,String userName);

    Result<?> saveDocToEs(KmDocEsVO kmDocEsVO, String indexId);

    Result<?> deleteDocFromEs(String indexId);

    KmSearchResultObjVO searchESKmDoc(Page<KmSearchResultVO> page, KmDocEsParamVO kmDocEsParamVO, HttpServletRequest req) throws IOException;
    KmSearchResultObjVO checkDuplicateESKmDoc(Page<KmSearchResultVO> page, KmDocEsParamVO kmDocEsParamVO, HttpServletRequest req) throws IOException;

    Page<KmDocVO> queryPageList(Page<KmDocVO> page, KmDocParamVO kmDocParamVO,String orderBy);

    //首页最新发布文档列表
    Page<KmDocVO> queryPublicPageList(Page<KmDocVO> page, KmDocParamVO kmDocParamVO,String orderBy);

    Result<?> importExternalFile(File externalFile, KmDocParamVO kmDocParamVO);

    @Transactional
    KmDoc saveDoc(KmFile kmFile, KmDocParamVO kmDocParamVO);

    void convertDocSync(KmDoc doc);

    void indexDocSync(KmDoc doc);

    void indexDocSyncBatch(List<String> idList);

//    KmDoc saveDoc(KmFile kmFile,KmDoc kmDoc);

    KmDoc getDocByFileId(String fileId);

    void downloadKmDoc(String docId, HttpServletResponse response, HttpServletRequest req) throws IOException, ParseException;

    void viewKmDoc( String docId, HttpServletResponse response,HttpServletRequest req) throws IOException;

    Result<?> editDraft(KmDocParamVO kmDocParamVO);

    Result<?> editAndRelease(KmDocParamVO kmDocParamVO,HttpServletRequest req);
    Result<?> auditDoc(String id,HttpServletRequest req);

    Result<?> editAuditPassed(KmDocParamVO kmdocTarget);

    Result<?> editReleaseFlag(KmDoc kmdocTarget);

    Page<KmDoc> queryTopicPageList(Page<KmDoc> page,String topicId);

    Boolean checkCategoryOfDoc(String category);

    Boolean checkTopicOfDoc(String topidId);

    Boolean checkBusinessTypeOfDoc(String businessType);

    Result<?> addDocToTopic(String topicId, List<String> docIds);

    Result<?> removeDocFromTopic( String topicId, String docId);

    Page<KmDocStatisticsVO> queryKmDocStatistics(Page<KmDocStatisticsVO> page,Integer statisticsType);
    List<KmDocStatisticsVO> queryKmDocStatistics(Integer statisticsType);

    Result<?> auditDocImportedFile(String id);

    Result<?> deleteDoc(String docId, HttpServletRequest req);

    KmDocEsVO getEsDocByDocId(String indexId);

    KmDocSummaryVO queryKmDocSummary();

    List<KmDoc> getReleasedDocs();
}
