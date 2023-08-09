package org.jeecg.modules.KM.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.KmSearchResultObjVO;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.KM.VO.*;
import org.jeecg.modules.KM.common.config.BaseConfig;
import org.jeecg.modules.KM.common.enums.*;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.common.rules.SerialNumberRule;
import org.jeecg.modules.KM.common.utils.*;
import org.jeecg.modules.KM.entity.*;
import org.jeecg.modules.KM.mapper.KmDocMapper;
import org.jeecg.modules.KM.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KmDocServiceImpl extends ServiceImpl<KmDocMapper, KmDoc> implements IKmDocService {

    @Autowired
    private KmDocMapper kmDocMapper;
    @Autowired
    private IKmFileService kmFileService;
    @Autowired
    private IKmDocTopicTypeService kmDocTopicTypeService;
    @Autowired
    private BaseConfig baseConfig;
    @Autowired
    private IThreadPoolExecutorService executorService;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private KMConstant kmConstant;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private IKmDocBusinessTypeService kmDocBusinessTypeService;
    @Autowired
    private IKmDocVersionService kmDocVersionService;
    @Autowired
    private IKmDocVisitRecordService kmDocVisitRecordService;
    @Autowired
    private IKmSearchRecordService kmSearchRecordService;
    @Autowired
    private IKmDocFavouriteService kmDocFavouriteService;
    @Autowired
    private IKmSysConfigService kmSysConfigService;

    @Override
    public Page<KmDocVO> queryPageList(Page<KmDocVO> page, KmDocParamVO kmDocParamVO,String orderBy){
        String permissionSql = QueryGenerator.installAuthJdbc(KmDocVO.class);
        String dbType = CommonUtils.getDatabaseType();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null)
            return null;

        String userId = sysUser.getId();
        //处理过滤source -> sourceList
        if(kmDocParamVO.getSource() != null && !kmDocParamVO.getSource().isEmpty())
            kmDocParamVO.setSourceList(Arrays.asList(kmDocParamVO.getSource().split(",")));

        return kmDocMapper.getPageList(page,userId,kmDocParamVO,permissionSql,dbType,orderBy);
    }

    //查询首页的最近发布档案列表，公开范围
    @Override
    public Page<KmDocVO> queryPublicPageList(Page<KmDocVO> page, KmDocParamVO kmDocParamVO,String orderBy) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null)
            return null;

        //处理过滤source -> sourceList
        if(kmDocParamVO.getSource() != null && !kmDocParamVO.getSource().isEmpty())
            kmDocParamVO.setSourceList(Arrays.asList(kmDocParamVO.getSource().split(",")));

        kmDocParamVO.setReleaseFlag(DocReleaseFlagEnum.Released.getCode());
        String userId = sysUser.getId();

        String dbType = CommonUtils.getDatabaseType();
        Page<KmDocVO> pageList = kmDocMapper.getPageList(page, userId, kmDocParamVO, "", dbType, orderBy);
//        if(!pageList.getRecords().isEmpty() && sysUser.getThirdType().equals(KMConstant.InnerUser)){
        if(!pageList.getRecords().isEmpty()){
            for (KmDocVO item:pageList.getRecords()) {
                item.setDownloadFlag(KMConstant.AllowDownload);
            }
        }
        return pageList;
//        return kmDocMapper.getPageList(page, userId, kmDocParamVO, "", dbType, orderBy);
    }

    @Override
    @Transactional
    public KmDoc saveDoc(KmFile kmFile) {
        KmDoc kmDoc =new KmDoc();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

         String userId = sysUser.getUsername();

        kmDoc.setCreateBy(userId);
        kmDoc.setCreateTime(DateUtils.getDate());
        kmDoc.setId(UUIDGenerator.generate());
        kmDoc.setDownloads(BigInteger.valueOf(0));
        kmDoc.setViews(BigInteger.valueOf(0));
        kmDoc.setSerialNumber(SerialNumberRule.generate());
        //默认为公开，并允许下载
        kmDoc.setPublicFlag(KMConstant.DocPublic);
        kmDoc.setDownloadFlag(KMConstant.AllowDownload);

        //file properties
        kmDoc.setFileId(kmFile.getId());
        File distFile = baseConfig.getFile(kmFile.getPhysicalPath());
        kmDoc.setFileType(StringUtils.getFileSuffix(kmFile.getOriginalName()));
        kmDoc.setName(kmFile.getOriginalName());
        kmDoc.setTitle(kmFile.getOriginalName().substring(0,kmFile.getOriginalName().lastIndexOf(".")));
        kmDoc.setFileSize(distFile.length());

        //放在审批后再入库
        kmDoc.setStatus(DocStatusEnum.Passed.getCode());
        kmDoc.setReleaseFlag(DocReleaseFlagEnum.Released.getCode());
        kmDoc.setFtiFlag(DocFTIFlagEnum.WaitProcess.getCode());

        //pdf文件自己就是预览文件
        if(kmDoc.getFileType().equals("pdf") ) {
            kmDoc.setConvertFlag(DocConvertFlagEnum.NonConvert.getCode());
            kmDoc.setOriginalPreviewFileId(kmFile.getId());
            kmDoc.setPreviewFileId(kmFile.getId());
        }
        else if(kmConstant.isConvertFileType(kmDoc.getFileType())){
            kmDoc.setConvertFlag(DocConvertFlagEnum.WaitConvert.getCode());

        }
        else {
            //无需转换预览文件
            kmDoc.setConvertFlag(DocConvertFlagEnum.NonConvert.getCode());
            kmDoc.setOriginalPreviewFileId("");
            kmDoc.setPreviewFileId("");
        }

        kmDoc.setFtiFlag(DocFTIFlagEnum.WaitProcess.getCode());
        baseMapper.insert(kmDoc);

        //根据ConvertFlag进行转换处理
        if(kmDoc.getConvertFlag() == null || kmDoc.getConvertFlag() == DocConvertFlagEnum.WaitConvert.getCode()) {
            executorService.singleExecute(() -> convertDocSync(kmDoc));
        }
        return kmDoc;

    }

    @Override
    public void convertDocSync(KmDoc kmDoc) {
        //kmDoc = super.getById(kmDoc.getId());
        if (kmDoc.getConvertFlag() != null && !kmDoc.getConvertFlag().equals(DocConvertFlagEnum.WaitConvert.getCode())) {
            return;
        }
        log.info("开始转换文档:{}", kmDoc.getName());
        if (kmDoc == null) {
            kmDoc.setConvertFlag(DocConvertFlagEnum.Fail.getCode());
            kmDoc.setProcessMsg("[convertDocSync] doc is null");
            log.error("doc is null");
        } else {
            KmFile KmFile = kmFileService.getKmFile(kmDoc.getFileId());
            if (KmFile == null) {
                kmDoc.setConvertFlag(DocConvertFlagEnum.Fail.getCode());
                kmDoc.setProcessMsg("[convertDocSync] upload file is null,fileId:" + kmDoc.getFileId());
                log.error("upload file is null,fileId:{}", kmDoc.getFileId());
            } else {
                File file = baseConfig.getFile(KmFile.getPhysicalPath());
                if (file.exists()) {
                    File targetDir = new File(file.getParentFile(), "pdf");
                    boolean result = OfficeUtils.convertPdf(baseConfig.getSofficePath(), file, targetDir);
                    if (!result) {
                        kmDoc.setConvertFlag(DocConvertFlagEnum.Fail.getCode());
                        kmDoc.setProcessMsg("[convertDocSync] 文档转换成pdf预览文件失败，路径:" + KmFile.getPhysicalPath());
                        log.error("文档转换失败,{}", kmDoc.getName());
                    } else {
                        String fileName = KmFile.getId() + ".pdf";
                        String targetFilePath = targetDir.getPath() + KMConstant.fileSeparator + fileName;
                        KmFile kmFile = kmFileService.saveFileInfoToDB(targetFilePath, fileName);
                        if (kmFile != null) {
                            //转换成功
                            kmDoc.setPreviewFileId(kmFile.getId());
                            kmDoc.setOriginalPreviewFileId(kmFile.getId());
                            kmDoc.setConvertFlag(DocConvertFlagEnum.Converted.getCode());
                            log.info("文档转换成功,{}", kmDoc.getName());
                        } else {
                            kmDoc.setConvertFlag(DocConvertFlagEnum.Fail.getCode());
                            kmDoc.setProcessMsg( "[convertDocSync] 保存预览文件信息到数据库失败:" + fileName);
                            log.error("保存转换文件信息到数据库失败", kmDoc.getName());
                        }
                    }
                } else {
                    kmDoc.setConvertFlag(DocConvertFlagEnum.Fail.getCode());
                    kmDoc.setProcessMsg("[convertDocSync] 文件不存在或打开文件失败,路径:" + KmFile.getPhysicalPath());
                    log.error("文件不存在或打开文件失败,路径:{}", KmFile.getPhysicalPath());
                }
            }
        }
        this.updateById(kmDoc);
    }


    @Override
    public void indexDocSyncBatch(List<String> idList) {
        executorService.singleExecute(() -> indexDocBatch(idList));
    }

    private void indexDocBatch(List<String> idList) {
        for (String id:idList) {
            KmDoc kmDoc = super.getById(id);
            if(kmDoc!= null){
                ftiIndexDoc(kmDoc);
            }
        }
    }

    @Override
    public void indexDocSync(KmDoc kmDoc) {
        executorService.singleExecute(() -> ftiIndexDoc(kmDoc));
    }

    //KmDoc对象分析内容并入库ES
    private void ftiIndexDoc(KmDoc kmDoc){
        kmDoc = super.getById(kmDoc.getId());
        log.info("index Task id:{},docName:{}",kmDoc.getId(),kmDoc.getName());
        if(kmDoc==null){
            kmDoc.setFtiFlag(DocFTIFlagEnum.Fail.getCode());
            kmDoc.setProcessMsg( "[ftiIndexDoc] doc is null");
        }else {
            KmFile KmFile = kmFileService.getKmFile(kmDoc.getFileId());
            if(KmFile==null){
                kmDoc.setFtiFlag(DocFTIFlagEnum.Fail.getCode());
                kmDoc.setProcessMsg( "[ftiIndexDoc]  upload file not exists,id:" + kmDoc.getId());
                log.error("入库ES失败,upload file not exists,id:{}",kmDoc.getId());
            }else {
                KmDocEsVO kmDocEsVO = KmDocToEsVO(kmDoc);
                kmDocEsVO.setReleaseFlag(DocReleaseFlagEnum.Released.getCode());
                if(kmConstant.isIndexFileType(kmDoc.getFileType())){
                    File file = baseConfig.getFile(KmFile.getPhysicalPath());
                    if (file != null && file.exists()) {
                        String content = TikaUtils.parseContent(file);
                        if (content == null) {
                            kmDoc.setFtiFlag(DocFTIFlagEnum.Fail.getCode());
                            kmDoc.setProcessMsg("[ftiIndexDoc] tika解析文件内容出错,路径:" + file.getAbsolutePath() );
                            log.error("[ftiIndexDoc] tika解析文件内容出错,路径:{}", file.getAbsolutePath());
                        } else {
                            //保存提取的全文，准备入库ES
                            kmDocEsVO.setContent(content);
                            //保存数据到ES
                            Result<?> result = this.saveDocToEs(kmDocEsVO,null);
                            if(result.getCode() == CommonConstant.SC_OK_200){
                                kmDoc.setFtiFlag(DocFTIFlagEnum.Processed.getCode());
                                kmDoc.setReleaseFlag(DocReleaseFlagEnum.Released.getCode());
                                //保存ES的index id
                                kmDoc.setIndexId(result.getResult().toString());
                                log.info("入库ES成功:{}",kmDocEsVO.getDocId());
                            }
                            else{
                                kmDoc.setFtiFlag(DocFTIFlagEnum.Fail.getCode());
                                kmDoc.setProcessMsg(  "[ftiIndexDoc] 入库ES失败：" + result.getMessage());
                                kmDoc.setReleaseFlag(DocReleaseFlagEnum.Off.getCode());
                                log.error("入库ES失败,{}",result.getMessage());
                            }
                            log.info("解析文件成功:{}",kmDocEsVO.getDocId());
                        }
                    } else {
                        kmDoc.setFtiFlag(DocFTIFlagEnum.Fail.getCode());
                        kmDoc.setProcessMsg( "[ftiIndexDoc] 解析文件失败,文件不存在或打开文件失败，路径:" +KmFile.getPhysicalPath() );
                        log.error("解析文件失败,文件不存在或打开文件失败,路径:{}",KmFile.getPhysicalPath() );
                    }
                }
                else{
                    kmDoc.setFtiFlag(DocFTIFlagEnum.NonFTI.getCode());
                }
            }
        }
        //保存doc对象
        this.updateById(kmDoc);
    }


    @Override
    public KmDoc getDocByFileId(String fileId){
        return kmDocMapper.getKmDocByFileId(fileId);
    }


    private KmDocEsVO KmDocToEsVO(KmDoc kmDoc){
        KmDocEsVO kmDocEsVO = new KmDocEsVO();
        //kmDocEsVO.setIndexId(kmDoc.getIndexId());
        kmDocEsVO.setCategory(kmDoc.getCategory());
        kmDocEsVO.setDocId(kmDoc.getId());
        kmDocEsVO.setPubTime(kmDoc.getPubTime());
//        kmDocEsVO.setPubTimeTxt(kmDoc.getPubTimeTxt());
        kmDocEsVO.setTitle(kmDoc.getTitle());
        kmDocEsVO.setSource(kmDoc.getSource());
        kmDocEsVO.setReleaseFlag(kmDoc.getReleaseFlag());
        kmDocEsVO.setFileNo(kmDoc.getFileNo());
        kmDocEsVO.setPublicFlag(kmDoc.getPublicFlag());
        if(kmDoc.getKeywords() != null) {
            kmDocEsVO.setKeywords(kmDoc.getKeywords()
                    .replaceAll("  "," ")
                    .replaceAll("  "," ")
                    .replaceAll(" ",",")
                    .replaceAll("，",",")
                    .split(","));
        }

        List<String> docBusinessTypeList = kmDocBusinessTypeService.getBusinessTypes(kmDoc.getId());
        if(docBusinessTypeList != null && docBusinessTypeList.size()>0){
            kmDocEsVO.setBusinessTypes(
                    docBusinessTypeList.toArray(new String[docBusinessTypeList.size()]));
        }

        List<String> docVersionList = kmDocVersionService.getVersions(kmDoc.getId());
        if(docVersionList!=null && docVersionList.size()>0){
            kmDocEsVO.setVersions(
                    docVersionList.toArray(new String[docVersionList.size()]));
        }

        //改为从数据库获取 topicCode
        //kmDocEsVO.setTopicCodes(RandomUtils.generateRamdonTopicCodes());
        List<String> docTopicCodeList = kmDocTopicTypeService.getDocTopicCodes(kmDoc.getId());
        if(docTopicCodeList != null && docTopicCodeList.size()>0) {
            kmDocEsVO.setTopicCodes(
                    docTopicCodeList.toArray(new String[docTopicCodeList.size()]));
        }

        return kmDocEsVO;
    }


    private Result<?> saveDocToEs(KmDoc kmDoc){
        KmDocEsVO kmDocEsVO = KmDocToEsVO(kmDoc);
        Result<?> result =  saveDocToEs(kmDocEsVO,kmDoc.getIndexId());
        if(result.isSuccess()){
            if(result.getResult() != null && !result.getResult().toString().isEmpty() ){
                String newIndexId = result.getResult().toString();
                kmDoc.setIndexId(newIndexId);
            }
            kmDoc.setFtiFlag(DocFTIFlagEnum.Processed.getCode());
        }
        else{
            kmDoc.setFtiFlag(DocFTIFlagEnum.Fail.getCode());
            kmDoc.setProcessMsg(result.getMessage());
        }
        if(this.updateById(kmDoc)) {
            return result;
        }
        else{
            return Result.error("保存文档数据到数据库失败");
        }
    }

    @Override
    public KmDocEsVO getEsDocByDocId(String docId){
        try {
            if(docId != null && !docId.isEmpty()) {
                //通过id查询
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                QueryBuilder queryBuilder = QueryBuilders.termsQuery("docId",docId);//.idsQuery().addIds(indexId);
                searchSourceBuilder.query(queryBuilder);
                //超时 10S
                searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));
                SearchRequest searchRequest = new SearchRequest();
                searchRequest.source(searchSourceBuilder);
                searchRequest.indices(KMConstant.DocIndexAliasName);

                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                if(searchResponse.status() != RestStatus.OK){
                    return  null;
                }
                else{
                    long c = searchResponse.getHits().getTotalHits().value;
                    if(c == 0){
                        return null;
                    }
                    else{
                        //返回ES记录
                        SearchHits hits = searchResponse.getHits();
                        SearchHit[] searchHits = hits.getHits();
                        SearchHit hit = searchHits[0];
                        KmDocEsVO kmDocEsVO = JSON.parseObject(hit.getSourceAsString(), KmDocEsVO.class);
                        return  kmDocEsVO;
                    }
                }
            }
            else{
                return null;
            }

        }
        catch (Exception e){
            return null;
        }
    }

    private Result<?> saveDocToEs(KmDocEsVO kmDocEsVO,String indexId) {
        try {
            boolean indexExistFlag = true ;
            if(indexId != null && !indexId.isEmpty()) {
                //通过索引id查询
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds(indexId);
                searchSourceBuilder.query(queryBuilder);
                //超时 10S
                searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));
                SearchRequest searchRequest = new SearchRequest();
                searchRequest.source(searchSourceBuilder);
                searchRequest.indices(KMConstant.DocIndexAliasName);

                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                if(searchResponse.status() != RestStatus.OK){
                    return  Result.error("从ES查询文档索引失败");
                }
                else{
                    long c = searchResponse.getHits().getTotalHits().value;
                    if(c == 0){
                        indexExistFlag = false;
                    }
                    else{
                        //更新ES记录
                        UpdateRequest updateRequest = new UpdateRequest(KMConstant.DocIndexAliasName,indexId);
                        updateRequest.timeout(TimeValue.timeValueHours(KMConstant.SaveTimeOutHours));
                        updateRequest.doc(new JSONObject(kmDocEsVO,
                                        new JSONConfig().setDateFormat(DatePattern.NORM_DATE_PATTERN)).toString()
                                ,XContentType.JSON);
                        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
                        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
                        if (!updateResponse.status().equals(RestStatus.OK)) {
                            return Result.error("更新ES发生错误，返回码[" + updateResponse.status().toString() + "]");
                        } else {
                            return Result.OK();
                        }
                    }
                }
            }
            else{
                indexExistFlag = false;
            }

            if(!indexExistFlag){
                //插入数据，index不存在则自动根据匹配到的template创建。index没必要每天创建一个，如果是为了灵活管理，最低建议每月一个 yyyyMM。
                IndexRequest indexRequest = new IndexRequest(KMConstant.DocIndexName);
                //考虑大文件，允许1小时超时时间，前提是异步执行入库ES
                indexRequest.timeout(TimeValue.timeValueHours(KMConstant.SaveTimeOutHours));
                indexRequest.source(new JSONObject(kmDocEsVO,
                                new JSONConfig().setDateFormat(DatePattern.NORM_DATE_PATTERN)).toString()
                        , XContentType.JSON);
                IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                if (!response.status().equals(RestStatus.CREATED)) {
                    return Result.error("入库ES发生错误，返回码[" + response.status().toString() + "]");
                } else {
                    return Result.OK(response.getId());
                }
            }
            else{
                return Result.error("未知错误");
            }
        }
        catch (Exception e){
            return Result.error("操作ES发生异常:" + e.getMessage());
        }
    }

    private Result<?> deleteDocFromEs(String indexId){
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds(indexId);
            searchSourceBuilder.query(queryBuilder);
            //超时 10S
            searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.source(searchSourceBuilder);
            searchRequest.indices(KMConstant.DocIndexAliasName);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if(searchResponse.status() != RestStatus.OK){
                return  Result.error("从ES查询文档索引失败");
            }
            else {
                long c = searchResponse.getHits().getTotalHits().value;
                if(c == 0){
                    return Result.OK();
                }
                else{
                    DeleteRequest deleteRequest = new DeleteRequest(KMConstant.DocIndexAliasName,indexId);
                    deleteRequest.timeout(TimeValue.timeValueHours(KMConstant.SaveTimeOutHours));
                    deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
                    DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
                    if (!deleteResponse.status().equals(RestStatus.OK)) {
                        log.info("从ES删除文档失败:{}",indexId);
                        return Result.error("从ES删除文档发生错误，返回码[" + deleteResponse.status().toString() + "]");
                    } else {
                        log.info("从ES删除文档成功:{}",indexId);
                        return Result.OK("从ES删除文档成功");
                    }
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
            return Result.error("操作ES发生异常:" + e.getMessage());
        }
    }

    private List<KmSearchResultVO> retrieveDocDbInfo(List<KmDocEsVO> kmDocEsVOList){
        if(kmDocEsVOList == null || kmDocEsVOList.isEmpty())
            return Collections.EMPTY_LIST;
        //get info from DB
        List<String> docIdList = new ArrayList<>();
        kmDocEsVOList.forEach(e->{docIdList.add(e.getDocId());});
        List<KmSearchResultVO> kmSearchResultVOList = new ArrayList<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if(sysUser == null) {
            return kmSearchResultVOList;
        }
        else {
            String userId = sysUser.getId();

            LambdaQueryWrapper<KmDocFavourite> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(KmDocFavourite::getDocId,docIdList);
            queryWrapper.eq(KmDocFavourite::getUserId,userId);
            List<KmDocFavourite> kmDocFavouriteList = kmDocFavouriteService.list(queryWrapper);
            List<String> favouritDocList = new ArrayList<>();
            if(kmDocFavouriteList != null && kmDocFavouriteList.size()>0){
                kmDocFavouriteList.forEach(e-> favouritDocList.add(e.getDocId()));
            }

            List<KmDoc> kmDocList = super.listByIds(docIdList);
            Map<String, KmDoc> kmDocMap = new HashMap<>();
            kmDocList.forEach(e -> kmDocMap.put(e.getId(), e));

            kmDocEsVOList.forEach((e) -> {
                KmSearchResultVO kmSearchResultVO = new KmSearchResultVO();
                kmSearchResultVO.setCategory(e.getCategory());
                kmSearchResultVO.setId(e.getDocId());
                //kmSearchResultVO.setPubTime(e.getPubTime());
                kmSearchResultVO.setSource(e.getSource());
                kmSearchResultVO.setTitle(e.getTitle());
                kmSearchResultVO.setFileNo(e.getFileNo());

                if (e.getVersions() != null)
                    kmSearchResultVO.setVersions(StringUtils.concatListToString(Arrays.asList(e.getVersions())));
                if (e.getKeywords() != null)
                    kmSearchResultVO.setKeywords(StringUtils.concatListToString(Arrays.asList(e.getKeywords())));
                if (e.getTopicCodes() != null)
                    kmSearchResultVO.setTopicCodes(StringUtils.concatListToString(Arrays.asList(e.getTopicCodes())));
                if (e.getBusinessTypes() != null)
                    kmSearchResultVO.setBusinessType(StringUtils.concatListToString(Arrays.asList(e.getBusinessTypes())));
                //拼装db信息
                if (kmDocMap.containsKey(kmSearchResultVO.getId())) {
                    KmDoc one = kmDocMap.get(kmSearchResultVO.getId());
                    kmSearchResultVO.setDownloads(one.getDownloads());
                    kmSearchResultVO.setViews(one.getViews());
                    kmSearchResultVO.setFileId(one.getFileId());
                    kmSearchResultVO.setPreviewFileId(one.getPreviewFileId());
                    kmSearchResultVO.setFileType(one.getFileType());
                    kmSearchResultVO.setSize(one.getFileSize());
                    kmSearchResultVO.setPubTimeTxt(one.getPubTimeTxt());
                    kmSearchResultVO.setDownloadFlag(one.getDownloadFlag());
                    kmSearchResultVO.setEffectTime(one.getEffectTime());
                    kmSearchResultVO.setRemark(one.getRemark());
                    kmSearchResultVO.setOrgCode(one.getOrgCode());

                }
                //拼装收藏夹标记
                if(favouritDocList.contains(e.getDocId()))
                    kmSearchResultVO.setFavourite(1);
                else
                    kmSearchResultVO.setFavourite(0);


                kmSearchResultVOList.add(kmSearchResultVO);
            });
            return kmSearchResultVOList;
        }
    }

    /*
    普通检索
     */
    public KmSearchResultObjVO searchESKmDoc(Page<KmSearchResultVO> page, KmDocEsParamVO kmDocEsParamVO, HttpServletRequest req ) throws IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        List<KmDocEsVO> kmDocEsVOList = new ArrayList<>();
        Page<KmSearchResultVO> resultVOPage = new Page<KmSearchResultVO>(page.getCurrent(), page.getSize());
        KmSearchResultObjVO kmSearchResultObjVO = new KmSearchResultObjVO();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null){
            kmSearchResultObjVO.setSuccess(false);
            kmSearchResultObjVO.setMessage("用户登陆信息异常");
            return  kmSearchResultObjVO;
        }
        KmSearchRecord kmSearchRecord = new KmSearchRecord();

        HttpSession session = req.getSession();

        //最终条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //普通检索的条件，综合 ： 标题、关键字、全文 合并检索
        BoolQueryBuilder boolQueryBuilderDefault = QueryBuilders.boolQuery();
        List<String> paramPaths = new ArrayList<>();
        List<String> keywords = new ArrayList<>();

        if(kmDocEsParamVO.getWithinSearchFlag() != null
                && kmDocEsParamVO.getWithinSearchFlag() == 1
                && (session.getAttribute("searchParamsMust") != null
                || session.getAttribute("searchParamsFilter") != null
                || session.getAttribute("searchParamsShould") != null)){
            //历史参数处理，历史参数全部用filter
            Object paramObjMust = session.getAttribute("searchParamsMust");
            List<QueryBuilder> must = (List<QueryBuilder>) paramObjMust;
            if(must.size()>0)
                must.forEach(e -> boolQueryBuilder.filter().add(e)) ;

            Object paramObjFilter = session.getAttribute("searchParamsFilter");
            List<QueryBuilder> filter = (List<QueryBuilder>) paramObjFilter;
            if(filter.size()>0)
                filter.forEach(e -> boolQueryBuilder.filter().add(e));

            Object paramObjShould = session.getAttribute("searchParamsShould");
            List<QueryBuilder> should = (List<QueryBuilder>) paramObjShould;
            if(should.size()>0)
                should.forEach(e -> boolQueryBuilder.filter().add(e));

            //参数路径处理
            Object  pathObj = session.getAttribute("paramPaths");
//            Object  pathObj = redisUtil.get(KMConstant.SearchParamPrefix + userId + "_" + "paramPaths");
            if(pathObj != null) {
                List<String> historyParamPath = (List<String>) pathObj;
                if(historyParamPath.size()>0) {
                    for (String s : historyParamPath) {
                        paramPaths.add(s);
                    }
                }
            }
            //搜索关键字处理
            Object  keywordObj = session.getAttribute("keywords");
//            Object  keywordObj = redisUtil.get(KMConstant.SearchParamPrefix + userId + "_" +  "keywords");
            if(keywordObj != null) {
                List<String> historyKeywords = (List<String>) keywordObj;
                if(historyKeywords.size()>0) {
                    for (String s : historyKeywords) {
                        keywords.add(s);
                    }
                }
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String paramPath = "";
        //1、分类为必需条件 用filter
        if (kmDocEsParamVO.getCategory() != null) {
            paramPath = KMConstant.SearchTermSeprator + "分类: ";
            List<String> categorys = kmDocEsParamVO.getCategory();
            for (int i = 0; i < categorys.size(); i++) {
                paramPath = paramPath.concat(dictUtils.getDicText("km_dict_category", categorys.get(i)));
                paramPath = paramPath.concat(",");
            }
            paramPath = paramPath.substring(0, paramPath.length() - 1);
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.termsQuery("category", categorys));
        }

        //2、标题检索 高级用must，高级用should
        if (kmDocEsParamVO.getTitle() != null && !kmDocEsParamVO.getTitle().isEmpty()) {
            paramPath = KMConstant.SearchTermSeprator + "标题: " + kmDocEsParamVO.getTitle() +  paramPath;
            keywords.add(kmDocEsParamVO.getTitle());
            kmSearchRecord.setTitle(kmDocEsParamVO.getTitle());
            if(kmDocEsParamVO.getAdvSearchFlag() != null && kmDocEsParamVO.getAdvSearchFlag() == 1) {
                boolQueryBuilder
                        .must()
                        .add(QueryBuilders.matchQuery("title", kmDocEsParamVO.getTitle())
                                .analyzer("ik_smart").boost(kmConstant.getTitleSearchBoost()));
            }
            else{
                boolQueryBuilderDefault
                        .should()
                        .add(QueryBuilders.matchQuery("title", kmDocEsParamVO.getTitle())
                                .analyzer("ik_smart").boost(kmConstant.getTitleSearchBoost()));
            }
        }
        //3、关键字检索 用term精确匹配;  高级用must，高级用should
        String keywordString = "";
        if (kmDocEsParamVO.getKeywords() != null && kmDocEsParamVO.getKeywords().size() > 0) {
            keywordString = String.join(",",kmDocEsParamVO.getKeywords() );
            if((kmDocEsParamVO.getTitle() != null && !kmDocEsParamVO.getTitle().isEmpty()
                    && keywordString.equals(kmDocEsParamVO.getTitle()))
                    || kmDocEsParamVO.getAdvSearchFlag()!=1){
                paramPath = KMConstant.SearchTermSeprator + "关键字" + paramPath;
            }
            else{
                paramPath = KMConstant.SearchTermSeprator + "关键字: " + keywordString + paramPath ;
            }

            keywords.addAll(kmDocEsParamVO.getKeywords().subList(0, kmDocEsParamVO.getKeywords().size()));
            kmSearchRecord.setKeywords(StringUtils.concatListToString(kmDocEsParamVO.getKeywords()));
            BoolQueryBuilder boolQueryBuilderKeywords = QueryBuilders.boolQuery();
            kmDocEsParamVO.getKeywords().forEach(e -> {
                boolQueryBuilderKeywords
                        .should()
                        .add(QueryBuilders.termsQuery("keywords", e)
                                .boost(kmConstant.getKeywordSearchBoost()));
            });
            if(kmDocEsParamVO.getAdvSearchFlag() != null && kmDocEsParamVO.getAdvSearchFlag() == 1) {
                boolQueryBuilder
                        .must()
                        .add(boolQueryBuilderKeywords);
            }
            else{
                boolQueryBuilderDefault
                        .should()
                        .add(boolQueryBuilderKeywords);
            }
        }
        //4、全文检索  高级用must，高级用should
        if (kmDocEsParamVO.getContent() != null && !kmDocEsParamVO.getContent().isEmpty()) {
            if((keywordString != null && !keywordString.isEmpty()
                    && keywordString.equals(kmDocEsParamVO.getContent()))
                    ||  kmDocEsParamVO.getAdvSearchFlag()!=1){
                paramPath = KMConstant.SearchTermSeprator + "全文" + paramPath;
            }
            else {
                paramPath = KMConstant.SearchTermSeprator + "全文: " + kmDocEsParamVO.getContent() + paramPath ;
            }
            keywords.add(kmDocEsParamVO.getContent());
            kmSearchRecord.setContent(kmDocEsParamVO.getContent());
            if(kmDocEsParamVO.getAdvSearchFlag() != null && kmDocEsParamVO.getAdvSearchFlag() == 1) {
                boolQueryBuilder
                        .must()
                        .add(QueryBuilders.matchQuery("content", kmDocEsParamVO.getContent())
                                .analyzer("ik_smart").boost(kmConstant.getContentSearchBoost()));
            }
            else {
                boolQueryBuilderDefault
                        .should()
                        .add(QueryBuilders.matchQuery("content", kmDocEsParamVO.getContent())
                                .analyzer("ik_smart").boost(kmConstant.getContentSearchBoost()));
            }
        }

        //处理普通检索的合并条件：标题、关键字、全文
        if(kmDocEsParamVO.getAdvSearchFlag() == null || kmDocEsParamVO.getAdvSearchFlag() == 0) {
            boolQueryBuilder
                    .must()
                    .add(boolQueryBuilderDefault);
        }


            //5、发布时间检索 用filter
        if (parameterMap.containsKey("pubTimeEnd")) {
            paramPath = KMConstant.SearchTermSeprator + "发布时间结束: " + kmDocEsParamVO.getPubTimeEnd() + paramPath ;
            boolQueryBuilder.filter().add(
                    QueryBuilders
                            .rangeQuery("pubTime")
                            .lte(kmDocEsParamVO.getPubTimeEnd()));
        }
        if (parameterMap.containsKey("pubTimeStart")) {
            paramPath = KMConstant.SearchTermSeprator + "发布时间开始: " + kmDocEsParamVO.getPubTimeStart() + paramPath ;
            boolQueryBuilder.filter().add(
                    QueryBuilders
                            .rangeQuery("pubTime")
                            .gte(kmDocEsParamVO.getPubTimeStart()));
        }

        //6、来源检索 用filter
        if (kmDocEsParamVO.getSource() != null && !kmDocEsParamVO.getSource().isEmpty()) {
            String tmpSource ="";
            List<String> source = kmDocEsParamVO.getSource();
            for (int i = 0; i < source.size(); i++) {
                tmpSource = tmpSource.concat(dictUtils.getDicText("km_dict_source", source.get(i)));
                tmpSource = tmpSource.concat(",");
            }
            paramPath =  KMConstant.SearchTermSeprator + "来源: " + tmpSource.substring(0, tmpSource.length() - 1) +  paramPath ;
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.termsQuery("source", source));
        }

        //7、业务类型检索（多选） 用filter
        if (kmDocEsParamVO.getBusinessTypes() != null && kmDocEsParamVO.getBusinessTypes().size() > 0) {
            String tmpBusinessType = "";
            List<String> businessTypes = kmDocEsParamVO.getBusinessTypes();
            for (int i = 0; i < businessTypes.size(); i++) {
                tmpBusinessType = tmpBusinessType.concat(dictUtils.getDicText("km_dict_business", businessTypes.get(i)));
                tmpBusinessType = tmpBusinessType.concat(",");
            }
            paramPath =  KMConstant.SearchTermSeprator + "业务类型: " + tmpBusinessType.substring(0, tmpBusinessType.length() - 1) + paramPath  ;
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.termsQuery("businessTypes", businessTypes));
        }

        //8、版本状态检索（多选） 用filter
        if (kmDocEsParamVO.getVersions() != null && kmDocEsParamVO.getVersions().size() > 0) {
            String tmpVersion = "";
            List<String> versions = kmDocEsParamVO.getVersions();
            for (int i = 0; i < versions.size(); i++) {
                tmpVersion = tmpVersion.concat(dictUtils.getDicText("km_dict_versions", versions.get(i)));
                tmpVersion = tmpVersion.concat(",");
            }
            paramPath = KMConstant.SearchTermSeprator + "版本: " +  tmpVersion.substring(0, tmpVersion.length() - 1) + paramPath ;
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.termsQuery("versions", versions));
        }

        //9、专题检索（多选，前缀模糊匹配） 用filter
        if (kmDocEsParamVO.getTopicCodes() != null && kmDocEsParamVO.getTopicCodes().size() > 0) {
            String tmpTopicCodes = "";
            kmSearchRecord.setTopicCodes(StringUtils.concatListToString(kmDocEsParamVO.getTopicCodes()));
            BoolQueryBuilder boolQueryBuilderTopicCodes = QueryBuilders.boolQuery();
            List<String> topicCodes = kmDocEsParamVO.getTopicCodes();
            for (int i = 0; i < topicCodes.size(); i++) {
                //模糊匹配，匹配某个字符串开头的记录prefixQuery
                boolQueryBuilderTopicCodes
                        .should()
                        .add(QueryBuilders.prefixQuery("topicCodes", topicCodes.get(i)));

                String topicName = sysBaseAPI.queryCategoryNameByCode(topicCodes.get(i));
                if (topicName != null)
                    tmpTopicCodes = tmpTopicCodes.concat(topicName);
                else
                    tmpTopicCodes = tmpTopicCodes.concat(topicCodes.get(i));
                tmpTopicCodes = tmpTopicCodes.concat(",");
            }
            paramPath = KMConstant.SearchTermSeprator + "专题: " + tmpTopicCodes.substring(0, tmpTopicCodes.length() - 1) + paramPath ;

            boolQueryBuilder
                    .filter()
                    .add(boolQueryBuilderTopicCodes);
        }

        //10、文号，（前缀模糊匹配） 用filter
        if(kmDocEsParamVO.getFileNo() != null && !kmDocEsParamVO.getFileNo().isEmpty()){
            paramPath = KMConstant.SearchTermSeprator + "文号: " + kmDocEsParamVO.getFileNo() + paramPath  ;
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.wildcardQuery("fileNo", kmDocEsParamVO.getFileNo()));
        }

        //去掉检索参数第一个+
        if(!paramPath.isEmpty() && paramPath.length()>3)
        paramPath = paramPath.substring(3);


        //11、发布状态必须为1
        boolQueryBuilder
                .filter()
                .add(QueryBuilders.termQuery("releaseFlag",1));

        //排序，对字典文本字段，去掉后缀
        if(kmDocEsParamVO.getColumn() != null
                && !kmDocEsParamVO.getColumn().isEmpty()
                && kmDocEsParamVO.getOrder() != null
                && !kmDocEsParamVO.getOrder().isEmpty()) {
            String column = kmDocEsParamVO.getColumn();
            String order = kmDocEsParamVO.getOrder();
            if(column.endsWith(CommonConstant.DICT_TEXT_SUFFIX)) {
                column = column.substring(0, column.lastIndexOf(CommonConstant.DICT_TEXT_SUFFIX));
            }
//            if(column.equals("pubTimeTxt")) {
//                column = "pubTime";
//            }

            FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(column).order(SortOrder.fromString(order));
            searchSourceBuilder.sort(fieldSortBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        // highlight field 仅对title
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:blue\">");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        //分页
        long from = page.getCurrent() < 1 ? 0 : page.getSize() *(page.getCurrent()- 1) ;   //注意分页的坑，from要从0开始
        long size = page.getSize() > 100 ? 100 : page.getSize();
        size = size < 0 ? 10 : size;
        searchSourceBuilder.from((int) from);
        searchSourceBuilder.size((int) size);

        //超时 60S
        searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));

        // 过滤返回结果字段，去掉非必要信息，关键：去掉content
        String excludeFields[] = {"content"}; //{"content","keywords"};
        String includeFields[] = {}; //{"id","title","source","versions","pubTime"};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(KMConstant.DocIndexAliasName);

        log.debug(searchRequest.source().toString());
        //保存session，以便在结果中查询以及记录日志
        //分页、排序变换的时候不更新session，历史条件不叠加
        if(paramPaths == null
                || paramPaths.size() == 0
                || !paramPaths.get(paramPaths.size() -1).equals(paramPath)) {
            paramPaths.add(paramPath);
            session.setAttribute("searchParamsMust", boolQueryBuilder.must());
            session.setAttribute("searchParamsFilter", boolQueryBuilder.filter());
            session.setAttribute("searchParamsShould", boolQueryBuilder.should());
            session.setAttribute("paramPaths", paramPaths);
            session.setAttribute("keywords", keywords);
        }

        kmSearchResultObjVO.setParamPath(paramPaths);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        if(searchResponse.status() != RestStatus.OK || searchResponse.getHits().getTotalHits().value<=0){
            kmSearchResultObjVO.setKmSearchResultVOPage(resultVOPage);
            kmSearchResultObjVO.setSuccess(true);
        }
        else {

            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit hit : searchHits) {
                log.debug(hit.getSourceAsString());
                KmDocEsVO kmDocEsVO = JSON.parseObject(hit.getSourceAsString(), KmDocEsVO.class);
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                //获取title高亮显示
                if (highlightFields != null && highlightFields.size() > 0) {
                    HighlightField highlight = highlightFields.get("title");
                    //获取高亮显示的字段
                    Text[] fragments = highlight.fragments();
                    String fragmentString = fragments[0].string();

                    kmDocEsVO.setTitle(fragmentString);
                }
                kmDocEsVOList.add(kmDocEsVO);

            }

            List<KmSearchResultVO> kmSearchResultVOList = retrieveDocDbInfo(kmDocEsVOList);
            resultVOPage.setRecords(kmSearchResultVOList);
            //set page
            resultVOPage.setTotal(hits.getTotalHits().value);
            resultVOPage.setHitCount(hits.getTotalHits().value > 0);

            kmSearchResultObjVO.setKmSearchResultVOPage(resultVOPage);
            kmSearchResultObjVO.setSuccess(true);
        }
        //日志和限制
        executorService.execute(()->kmSearchRecordService.logSearch(kmSearchRecord.getKeywords(),
                kmSearchRecord.getTitle(),
                kmSearchRecord.getContent(),
                kmSearchRecord.getTopicCodes(),
                IpUtils.getIpAddr(req)));

        return kmSearchResultObjVO;
    }

    /*
    管理排重检索
     */
    @Override
    public KmSearchResultObjVO checkDuplicateESKmDoc(Page<KmSearchResultVO> page, KmDocEsParamVO kmDocEsParamVO, HttpServletRequest req ) throws IOException {
        List<KmDocEsVO> kmDocEsVOList = new ArrayList<>();
        Page<KmSearchResultVO> resultVOPage = new Page<KmSearchResultVO>(page.getCurrent(), page.getSize());
        KmSearchResultObjVO kmSearchResultObjVO = new KmSearchResultObjVO();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null){
            kmSearchResultObjVO.setSuccess(false);
            kmSearchResultObjVO.setMessage("用户登陆信息异常");
            return  kmSearchResultObjVO;
        }
        KmSearchRecord kmSearchRecord = new KmSearchRecord();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
         List<String> keywords = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String duplicateCheckHitRate =  "50%";
        String duplicateCheckHitRateConfig= kmSysConfigService.getSysConfigValue("DuplicateCheckHitRate");
        if(duplicateCheckHitRateConfig !=null && !duplicateCheckHitRateConfig.isEmpty())
            duplicateCheckHitRate= duplicateCheckHitRateConfig;


        //1、标题检索 高级用must，高级用should
        if (kmDocEsParamVO.getTitle() != null && !kmDocEsParamVO.getTitle().isEmpty()) {
            keywords.add(kmDocEsParamVO.getTitle());
            kmSearchRecord.setTitle(kmDocEsParamVO.getTitle());
            boolQueryBuilder
                    .should()
                    .add(QueryBuilders.matchQuery("title", kmDocEsParamVO.getTitle()).minimumShouldMatch(duplicateCheckHitRate)
                            .analyzer("ik_smart"));

        }

        //2、全文检索  高级用must，高级用should
        if (kmDocEsParamVO.getContent() != null && !kmDocEsParamVO.getContent().isEmpty()) {
            keywords.add(kmDocEsParamVO.getContent());
            kmSearchRecord.setContent(kmDocEsParamVO.getContent());
            boolQueryBuilder
                    .should()
                    .add(QueryBuilders.matchQuery("content", kmDocEsParamVO.getContent()).minimumShouldMatch(duplicateCheckHitRate)
                            .analyzer("ik_smart"));

        }

        //排序，对字典文本字段，去掉后缀
        if(kmDocEsParamVO.getColumn() != null
                && !kmDocEsParamVO.getColumn().isEmpty()
                && kmDocEsParamVO.getOrder() != null
                && !kmDocEsParamVO.getOrder().isEmpty()) {
            String column = kmDocEsParamVO.getColumn();
            String order = kmDocEsParamVO.getOrder();
            if(column.endsWith(CommonConstant.DICT_TEXT_SUFFIX)) {
                column = column.substring(0, column.lastIndexOf(CommonConstant.DICT_TEXT_SUFFIX));
            }


            FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(column).order(SortOrder.fromString(order));
            searchSourceBuilder.sort(fieldSortBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);

        // highlight field 仅对title
        if(kmDocEsParamVO.getTitle() != null && !kmDocEsParamVO.getTitle().isEmpty()) {
            HighlightBuilder highlightBuilder = new HighlightBuilder().field("title").requireFieldMatch(false);
            highlightBuilder.preTags("<span style=\"color:blue\">");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        //分页
        long from = page.getCurrent() < 1 ? 0 : page.getSize() *(page.getCurrent()- 1) ;   //注意分页的坑，from要从0开始
        long size = page.getSize() > 100 ? 100 : page.getSize();
        size = size < 0 ? 10 : size;
        searchSourceBuilder.from((int) from);
        searchSourceBuilder.size((int) size);

        //超时 60S
        searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));

        // 过滤返回结果字段，去掉非必要信息，关键：去掉content
        String excludeFields[] = {"content"}; //{"content","keywords"};
        String includeFields[] = {};
        searchSourceBuilder.fetchSource(includeFields,excludeFields);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(KMConstant.DocIndexAliasName);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        if(searchResponse.status() != RestStatus.OK || searchResponse.getHits().getTotalHits().value<=0){
            kmSearchResultObjVO.setKmSearchResultVOPage(resultVOPage);
            kmSearchResultObjVO.setSuccess(true);
        }
        else {

            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit hit : searchHits) {
                log.debug(hit.getSourceAsString());
                KmDocEsVO kmDocEsVO = JSON.parseObject(hit.getSourceAsString(), KmDocEsVO.class);

                if(kmDocEsParamVO.getTitle() != null && !kmDocEsParamVO.getTitle().isEmpty()) {

                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    //获取title高亮显示
                    if (highlightFields != null && highlightFields.size() > 0) {
                        HighlightField highlight = highlightFields.get("title");
                        //获取高亮显示的字段
                        Text[] fragments = highlight.fragments();
                        String fragmentString = fragments[0].string();

                        kmDocEsVO.setTitle(fragmentString);
                    }
                }
                kmDocEsVOList.add(kmDocEsVO);

            }

            List<KmSearchResultVO> kmSearchResultVOList = retrieveDocDbInfo(kmDocEsVOList);
            resultVOPage.setRecords(kmSearchResultVOList);
            //set page
            resultVOPage.setTotal(hits.getTotalHits().value);
            resultVOPage.setHitCount(hits.getTotalHits().value > 0);

            kmSearchResultObjVO.setKmSearchResultVOPage(resultVOPage);
            kmSearchResultObjVO.setSuccess(true);
        }

        return kmSearchResultObjVO;
    }

    @Override
    public Result<?> editReleaseFlag(KmDoc kmdocTarget){
        KmDoc kmDocOrig = super.getById(kmdocTarget.getId());
        if(kmDocOrig == null)
            return Result.error("找不到文档");
        if(kmDocOrig.getStatus() != DocStatusEnum.Passed.getCode()  )
            return Result.error("文档状态不允许修改");

        kmDocOrig.setReleaseFlag(kmdocTarget.getReleaseFlag());
        if(super.updateById(kmDocOrig)) {
            return saveDocToEs(kmDocOrig);
        }
        else
            return Result.error("修改失败");
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Result<?> editAuditPassed(KmDocParamVO kmdocTarget) {
        KmDoc kmDocOrig = super.getById(kmdocTarget.getId());
        if (kmDocOrig == null)
            return Result.error("找不到文档");
        if (kmDocOrig.getStatus() != DocStatusEnum.Passed.getCode() )
            return Result.error("文档状态不允许修改");

        Result result = edit(kmDocOrig,kmdocTarget);
        if(result.getCode() == CommonConstant.SC_OK_200){
            if(kmdocTarget.getReleaseFlag() == DocReleaseFlagEnum.Released.getCode() ){
                //已经发布的，保存到ES
                result =  saveDocToEs(kmdocTarget);
                if(result.getCode() != CommonConstant.SC_OK_200){
                    //事务回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.error(result.getMessage());
                }
            }
            return Result.OK("修改成功");
        }
        else {
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(result.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Result<?> editDraft(KmDocParamVO kmdocTarget ) {
        KmDoc kmDocOrig = super.getById(kmdocTarget.getId());
        if(kmDocOrig == null)
            return Result.error("找不到文档");
        if(kmDocOrig.getStatus() != DocStatusEnum.Draft.getCode()
                && kmDocOrig.getStatus() != DocStatusEnum.Passed.getCode()
                && kmDocOrig.getStatus() != DocStatusEnum.WaitAudit.getCode()
                && kmDocOrig.getStatus() != DocStatusEnum.Reject.getCode() )
            return Result.error("文档状态不允许修改");

        //两个参数，对比多值属性的交集，以增删子表数据
        Result result = edit(kmDocOrig,kmdocTarget);
        if(result.getCode() != CommonConstant.SC_OK_200){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(result.getMessage());
        }
        else {
            //保存到ES
            result = saveDocToEs(kmdocTarget);
            if (result.getCode() != CommonConstant.SC_OK_200) {
                //事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error(result.getMessage());
            }
            return Result.OK("修改成功");
        }
    }

    //editAndRelease
    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.REQUIRED)
    public Result<?> editAndRelease(KmDocParamVO kmdocTarget ,HttpServletRequest req) {
        KmDoc kmDocOrig = super.getById(kmdocTarget.getId());
        if(kmDocOrig == null)
            return Result.error("找不到文档");
        if(kmDocOrig.getStatus() != DocStatusEnum.WaitAudit.getCode() )
            return Result.error("文档状态不允许修改");

        //两个参数，对比多值属性的交集，以增删子表数据
        Result result = edit(kmDocOrig,kmdocTarget);
        if(result.getCode() != CommonConstant.SC_OK_200){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(result.getMessage());
        }
        else {
            return auditDoc(kmdocTarget.getId(),req);
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.SUPPORTS)
    public Result<?> auditDoc(String id,HttpServletRequest req){
        KmDoc kmDoc = super.getById(id);
        if(kmDoc !=null && kmDoc.getStatus().equals(DocStatusEnum.WaitAudit.getCode())){

            kmDoc.setStatus(DocStatusEnum.Passed.getCode());
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if(sysUser == null)
                return Result.error("用户信息异常");

            String userId = sysUser.getUsername();

            kmDoc.setLastUpdateTime(DateUtils.getDate());
            kmDoc.setLastUpdateBy(userId);
            if(super.updateById(kmDoc)) {
                // 审核后才入库ES
                indexDocSync(kmDoc);

                kmDocVisitRecordService.logVisit(id,
                        IpUtils.getIpAddr(req),
                        DocVisitTypeEnum.AuditPass.getCode());
                log.info("审核文档成功:{}",kmDoc.getName());

                return Result.OK("审核成功!");
            }
        }
        return Result.error("审核失败!");
    }


    //编辑doc
    @Transactional(propagation= Propagation.SUPPORTS)
    public Result<?> edit(KmDoc kmDocOrig,KmDocParamVO kmDocParamVO) {
        KmDoc kmDocTarget = new KmDoc();
//        KmDoc kmDocTarget = super.getById(kmDocOrig.getId());
        // 只允许部分字段可以修改
        BeanUtils.copyProperties(kmDocOrig,kmDocTarget);
        kmDocTarget.setPubTimeTxt(kmDocParamVO.getPubTimeTxt());
        kmDocTarget.setTitle(kmDocParamVO.getTitle());
        kmDocTarget.setKeywords(kmDocParamVO.getKeywords());
        kmDocTarget.setCategory(kmDocParamVO.getCategory());
        kmDocTarget.setSource(kmDocParamVO.getSource());
        kmDocTarget.setDownloadFlag(kmDocParamVO.getDownloadFlag());
        kmDocTarget.setEffectTime(kmDocParamVO.getEffectTime());
        kmDocTarget.setFileNo(kmDocParamVO.getFileNo());
        kmDocTarget.setPublicRemark(kmDocParamVO.getPublicRemark());
        kmDocTarget.setPublicFlag(kmDocParamVO.getPublicFlag());
        kmDocTarget.setRemark(kmDocParamVO.getRemark());
        //目前传id，后端需要转换
        String depId = kmDocParamVO.getDepId();
        if(depId != null && !depId.isEmpty()) {
            String orgCode = sysBaseAPI.queryDepartOrgCodeById(depId);
            if(orgCode != null && !orgCode.isEmpty()) {
                kmDocTarget.setOrgCode(orgCode);
                kmDocTarget.setDepId(depId);
            }
        }
        //处理发布年份
        String pubTimeTxt = kmDocTarget.getPubTimeTxt();
        if(pubTimeTxt != null && pubTimeTxt.length()>0){
            String pubTime = "";
            if(pubTimeTxt.indexOf("/") >0  ){
                pubTime = pubTimeTxt.substring(0,pubTimeTxt.indexOf("/"));
                kmDocTarget.setPubTime(pubTime);
            }
            else if(  pubTimeTxt.indexOf("-") >0){
                pubTime = pubTimeTxt.substring(0,pubTimeTxt.indexOf("-"));
                kmDocTarget.setPubTime(pubTime);
            }
            else{
                if(pubTimeTxt.length()==4)
                    kmDocTarget.setPubTime(pubTimeTxt);
                else
                    return Result.error("发布日期格式错误");
            }
        }

        if(kmDocParamVO.getRemoveBusinessTypeList()!=null && kmDocParamVO.getRemoveBusinessTypeList().size()>0) {
            for (String businessType : kmDocParamVO.getRemoveBusinessTypeList()) {
                //KmDocBusinessType tmpKmDocBusinessType = kmDocBusinessTypeService.getByDocIdAndBusinessType(kmDocOrig.getId(), businessType);
                LambdaQueryWrapper<KmDocBusinessType> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(KmDocBusinessType::getDocId,kmDocOrig.getId());
                queryWrapper.eq(KmDocBusinessType::getBusinessType,businessType);
                KmDocBusinessType tmpKmDocBusinessType = kmDocBusinessTypeService.getOne(queryWrapper);
                if (tmpKmDocBusinessType != null && !kmDocBusinessTypeService.removeById(tmpKmDocBusinessType))
                    return Result.error("删除业务类型错误");
            }
        }
        if(kmDocParamVO.getAddBusinessTypeList()!=null && kmDocParamVO.getAddBusinessTypeList().size()>0) {
            for (String businessType : kmDocParamVO.getAddBusinessTypeList()) {
                KmDocBusinessType tmpKmDocBusinessType = new KmDocBusinessType();
                tmpKmDocBusinessType.setDocId(kmDocOrig.getId());
                tmpKmDocBusinessType.setBusinessType(businessType);
                if (!kmDocBusinessTypeService.save(tmpKmDocBusinessType))
                    return Result.error("保存业务类型错误");
            }
        }

        if(kmDocParamVO.getRemoveVersionList()!=null && kmDocParamVO.getRemoveVersionList().size()>0) {
            for (String version : kmDocParamVO.getRemoveVersionList()) {
                //KmDocVersion tmpKmDocVersion = kmDocVersionService.getByDocIdAndVersion(kmDocOrig.getId(), version);
                LambdaQueryWrapper<KmDocVersion> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(KmDocVersion::getDocId,kmDocOrig.getId());
                queryWrapper.eq(KmDocVersion::getVersion,version);
                KmDocVersion tmpKmDocVersion = kmDocVersionService.getOne(queryWrapper);
                if (tmpKmDocVersion != null && !kmDocVersionService.removeById(tmpKmDocVersion))
                    return Result.error("删除版本状态错误");
            }
        }
        if(kmDocParamVO.getAddVersionList()!=null && kmDocParamVO.getAddVersionList().size()>0) {
            for (String version : kmDocParamVO.getAddVersionList()) {
                KmDocVersion tmpKmDocVersion = new KmDocVersion();
                tmpKmDocVersion.setDocId(kmDocOrig.getId());
                tmpKmDocVersion.setVersion(version);
                if (!kmDocVersionService.save(tmpKmDocVersion))
                    return Result.error("保存版本状态错误");
            }
        }

        if(kmDocParamVO.getRemoveTopicIdList()!=null && kmDocParamVO.getRemoveTopicIdList().size()>0){
            for (String topic :kmDocParamVO.getRemoveTopicIdList()) {
                //KmDocTopicType tmpKmDocTopicType = kmDocTopicTypeService.getByDocIdAndTopicId(kmDocOrig.getId(),topic);
                LambdaQueryWrapper<KmDocTopicType> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(KmDocTopicType::getDocId,kmDocOrig.getId());
                queryWrapper.eq(KmDocTopicType::getTopicId,topic);
                KmDocTopicType tmpKmDocTopicType = kmDocTopicTypeService.getOne(queryWrapper);
                if(tmpKmDocTopicType!=null && !kmDocTopicTypeService.removeById(tmpKmDocTopicType))
                    return  Result.error("删除知识专题错误");
            }
        }
        if(kmDocParamVO.getAddTopicIdList()!=null && kmDocParamVO.getAddTopicIdList().size()>0){
            for (String topic : kmDocParamVO.getAddTopicIdList()) {
                KmDocTopicType tmpKmDocTopicType = new KmDocTopicType();
                tmpKmDocTopicType.setTopicId(topic);
                tmpKmDocTopicType.setDocId(kmDocOrig.getId());
                if(!kmDocTopicTypeService.save(tmpKmDocTopicType))
                    return Result.error("保存知识专题数据错误");
            }
        }

        if(super.updateById(kmDocTarget)) {
            return Result.OK("编辑成功!");
        }
        else
            return  Result.error("更新文档失败");
    }

    public Result<?> deleteDoc(String docId,HttpServletRequest req){
        KmDoc kmDoc = this.getById(docId);
        if(kmDoc!= null) {
            //先从ES删除，如果失败直接返回
            if( kmDoc.getStatus().equals(DocStatusEnum.Passed.getCode())
                    && kmDoc.getIndexId() != null
                    && !kmDoc.getIndexId().isEmpty()){
                Result<?> result = deleteDocFromEs(kmDoc.getIndexId());
                if(!result.isSuccess())
                    return result;
            }

            if (kmDoc.getFileId()!=null){
                kmFileService.deleteKmFile(kmDoc.getFileId());
            }
            if (kmDoc.getPreviewFileId()!=null){
                kmFileService.deleteKmFile(kmDoc.getPreviewFileId());
            }
            //逻辑删除
            kmDoc.setStatus(DocStatusEnum.Delete.getCode());
            boolean updateFlag = super.updateById(kmDoc);
            if (updateFlag) {
                log.info("删除文件成功；删除文档记录成功:{}",kmDoc.getName());
                //日志
                kmDocVisitRecordService.logVisit(docId,
                        IpUtils.getIpAddr(req),
                        DocVisitTypeEnum.Delete.getCode());

                return Result.OK("删除成功!");
            }

        }
        return Result.error("删除失败!");

    }

    //流方式获取文件或预览文件
    private  void getKmDoc( KmDoc kmDoc, HttpServletRequest httpServletRequest,HttpServletResponse response, String getMethod) throws IOException{

        KmFile kmFile = null;
        String filename = "";

        if(getMethod.equals("Download")) {
            kmFile = kmFileService.getKmFile(kmDoc.getFileId());
            if(kmDoc.getTitle() != null && !kmDoc.getTitle().isEmpty())
                filename = kmDoc.getTitle() + "." + kmDoc.getFileType(); //根据title命名文件名
            else
                filename = "未命名." + kmDoc.getFileType();
        }
        else{
            if(kmDoc.getPreviewFileId() == null || kmDoc.getPreviewFileId().isEmpty()){
                response.sendError(HttpStatus.NOT_FOUND.value(), "预览文件不存在");
                return;
            }
            kmFile = kmFileService.getKmFile(kmDoc.getPreviewFileId());
            if(kmDoc.getTitle() != null && !kmDoc.getTitle().isEmpty())
                filename = kmDoc.getTitle() + ".pdf" ; //预览为pdf文件
            else
                filename =  "未命名.pdf" ;
        }

        if(kmFile == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(),"文件没找到");
            return;
        }

        String filePath = baseConfig.getFilePath(kmFile.getPhysicalPath());

        InputStream inputStream =null;
        ServletOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filePath);// 文件的存放路径
            response.reset();
            response.setContentType("application/octet-stream");
//            String filename = kmDoc.getTitle() + "." + kmDoc.getFileType(); //根据title命名文件名
            //解决跨域
            response.addHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,token");
            response.addHeader("Access-Control-Max-Age", "600000");
            //response.setHeader("Content-Length", "" + kmDoc.getFileSize());

            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Access-Control-Expose-Headers","Content-disposition");
            outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
            response.flushBuffer();
            //更新下载数和view数
            if(getMethod.equals("Download")){
                kmDoc.setDownloads(kmDoc.getDownloads().add(BigInteger.valueOf(1)));
            }
            else{
                kmDoc.setViews(kmDoc.getViews().add(BigInteger.valueOf(1)));
            }
            super.updateById(kmDoc);

        }catch (FileNotFoundException e){
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"内部错误");
            response.flushBuffer();
            if(outputStream !=null)
                outputStream.close();
            if(inputStream != null)
                inputStream.close();

        } finally {
            response.flushBuffer();
            if(outputStream !=null)
                outputStream.close();
            if(inputStream != null)
                inputStream.close();
        }
    }

    //下载文件
    @SuppressWarnings("ALL")
    public void downloadKmDoc(String docId, HttpServletResponse response, HttpServletRequest req) throws IOException {
        KmDoc kmDoc = super.getById(docId);
        if(kmDoc == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(),"无效的文档");
            return;
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null ) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "登录用户信息异常");
            return;
        }

        //关键字处理
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Object keywordsObj = session.getAttribute("keywords");

        getKmDoc(kmDoc, req,response, "Download");

        //日志
        String concatKeyword = "";
        if(keywordsObj !=null){
            List<String> keywordsList = (List<String>)keywordsObj;
            concatKeyword = StringUtils.concatListToString(keywordsList);
        }
        kmDocVisitRecordService.logVisit(docId,
                IpUtils.getIpAddr(req),
                DocVisitTypeEnum.View.getCode(),concatKeyword);
    }

    //预览文件
    @SuppressWarnings("ALL")
    public void viewKmDoc( String docId, HttpServletResponse response,HttpServletRequest req)  {
        try {
            KmDoc kmDoc = super.getById(docId);
            if(kmDoc == null) {
                response.sendError(HttpStatus.NOT_FOUND.value(),"无效的文档");
                return;
            }
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (sysUser == null ) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "登录用户信息异常");
                return;
            }

            //日志，关键字处理
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            Object keywordsObj = session.getAttribute("keywords");

            getKmDoc(kmDoc,req, response, "View");

            String concatKeyword = "";
            if(keywordsObj !=null){
                List<String> keywordsList = (List<String>)keywordsObj;
                concatKeyword = StringUtils.concatListToString(keywordsList);
            }
            kmDocVisitRecordService.logVisit(docId,
                    IpUtils.getIpAddr(req),
                    DocVisitTypeEnum.View.getCode(),
                    concatKeyword);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Page<KmDoc> queryTopicPageList(Page<KmDoc> page,String topicId){
        return  kmDocMapper.queryTopicPageList(page,topicId);
    }

    @Override
    public Result<?> addDocToTopic( String topicId, List<String> docIds) {
        List<String> failDocIds = new ArrayList<>();
        Integer failCount = 0;
        if (docIds != null && docIds.size() > 0) {
            for (String docId:docIds) {
                if(kmDocTopicTypeService.getByDocIdAndTopicId(docId,topicId) != null)
                {
                    failDocIds.add(docId);
                    failCount += 1;
                }
                else {
                    KmDocTopicType kmDocTopicType = new KmDocTopicType();
                    kmDocTopicType.setDocId(docId);
                    kmDocTopicType.setTopicId(topicId);
                    if (!kmDocTopicTypeService.save(kmDocTopicType)) {
                        failDocIds.add(docId);
                        failCount += 1;
                    }
                }
            }
        }
        if(failCount == docIds.size())
            return Result.error("全部失败");
        else {
            String failTitles = "";
            for (int i = 0; i < failDocIds.size() ; i++) {
                KmDoc oneDoc  = super.getById(failDocIds.get(i));
                if(oneDoc != null)
                    failTitles = failTitles + oneDoc.getTitle() + ",";
            }
            if(failTitles.length()>0)
                failTitles = failTitles.substring(0,failTitles.length()-1);
            return Result.OK(failTitles);
        }
    }

    @Override
    public Result<?> removeDocFromTopic(String topicId,String docId){
        KmDocTopicType tmpKmDocTopicType = kmDocTopicTypeService.getByDocIdAndTopicId(docId,topicId);
        if(tmpKmDocTopicType!=null && !kmDocTopicTypeService.removeById(tmpKmDocTopicType))
            return  Result.error("删除知识专题错误");

        return Result.OK();

    }

    @Override
    public Page<KmDocStatisticsVO> queryKmDocStatistics(Page<KmDocStatisticsVO> page,Integer statisticsType){
        String dbType = CommonUtils.getDatabaseType();
        return  kmDocMapper.queryKmDocStatistics(page,statisticsType,dbType);
    }

    @Override
    public void initESIndex(){
        KmDocEsVO kmEsVO = new KmDocEsVO();
        kmEsVO.setTitle("for init");
        kmEsVO.setDocId("1");
        Result<?> result = saveDocToEs(kmEsVO, "");
        if (result.isSuccess()) {
            String indexId = (String) result.getResult();
            if (indexId != null && !indexId.isEmpty()) {
                Result<?> result1 = deleteDocFromEs(indexId);
                if (result1.isSuccess()) {
                    log.info("init index success!");
                }
            }
        }
    }
}
