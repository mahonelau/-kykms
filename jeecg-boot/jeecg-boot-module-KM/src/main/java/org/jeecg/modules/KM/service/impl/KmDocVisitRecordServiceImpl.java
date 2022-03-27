package org.jeecg.modules.KM.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.KM.VO.KmDocEsVO;
import org.jeecg.modules.KM.VO.KmDocVisitRecordEsVO;
import org.jeecg.modules.KM.common.enums.DocVisitTypeEnum;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.common.utils.EsUtils;
import org.jeecg.modules.KM.common.utils.KMDateUtils;
import org.jeecg.modules.KM.common.utils.StringUtils;
import org.jeecg.modules.KM.entity.KmDocVisitRecord;
import org.jeecg.modules.KM.mapper.KmDocVisitRecordMapper;
import org.jeecg.modules.KM.service.IKmDocVisitRecordService;
import org.jeecg.modules.KM.service.IThreadPoolExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class KmDocVisitRecordServiceImpl extends ServiceImpl<KmDocVisitRecordMapper, KmDocVisitRecord> implements IKmDocVisitRecordService {

    @Autowired
    private IThreadPoolExecutorService executorService;
    @Autowired
    private EsUtils esUtils;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void logVisit(String docId,String ip,Integer visitType) {
        logVisit(docId,ip,visitType,"");
    }

    @Override
    public void logVisit(String docId,String ip,Integer visitType,String keyword) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //String userId = "1";
        if(sysUser != null) {
            String userId = sysUser.getUsername();
            logVisit(docId, ip, visitType, keyword, userId);
        }
    }

    @Override
    public void logVisit(String docId,String ip,Integer visitType,String keyword,String userId){
        KmDocVisitRecord kmDocVisitRecord = new KmDocVisitRecord();
        kmDocVisitRecord.setId(UUIDGenerator.generate());
        kmDocVisitRecord.setDocId(docId);
        kmDocVisitRecord.setCreateBy(userId);
        kmDocVisitRecord.setCreateTime(DateUtils.getDate());
        kmDocVisitRecord.setVisitType(visitType);
        kmDocVisitRecord.setSourceIp(ip);
        kmDocVisitRecord.setKeywords(keyword);

        //通过es获取分词结果
        List<String> paramKeywordList = esUtils.getIkAnalyzeSearchTerms(keyword);
        String keywordsMax = StringUtils.concatListToString(paramKeywordList);
        kmDocVisitRecord.setKeywordsMax(keywordsMax);
        executorService.execute(()->super.save(kmDocVisitRecord));

        //下载、预览:保存最近访问的个人文档记录与ES日志
        if(visitType == DocVisitTypeEnum.View.getCode()
                || visitType == DocVisitTypeEnum.Download.getCode()) {
            //入库ES
            executorService.execute(()->saveToEs(convertToEsVo(kmDocVisitRecord)));
        }
    }


    private KmDocVisitRecordEsVO convertToEsVo(KmDocVisitRecord kmDocVisitRecord){
        KmDocVisitRecordEsVO kmDocVisitRecordEsVO = new KmDocVisitRecordEsVO();
        kmDocVisitRecordEsVO.setCreateBy(kmDocVisitRecord.getCreateBy());
        kmDocVisitRecordEsVO.setCreateTime(kmDocVisitRecord.getCreateTime());
        kmDocVisitRecordEsVO.setDocId(kmDocVisitRecord.getDocId());
        kmDocVisitRecordEsVO.setKeywords(kmDocVisitRecord.getKeywords());
        if(kmDocVisitRecord.getKeywordsMax() != null&& kmDocVisitRecord.getKeywordsMax().length()>0)
            kmDocVisitRecordEsVO.setKeywordsMax(kmDocVisitRecord.getKeywordsMax().split(","));
        kmDocVisitRecordEsVO.setSourceIp(kmDocVisitRecord.getSourceIp());
        kmDocVisitRecordEsVO.setVisitType(kmDocVisitRecord.getVisitType());
        return  kmDocVisitRecordEsVO;
    }


    private void saveToEs(KmDocVisitRecordEsVO kmDocVisitRecordEsVO) {
        try {
            //插入数据，index不存在则自动根据匹配到的template创建。index没必要每天创建一个，如果是为了灵活管理，最低建议每月一个 yyyyMM。
            String indexSuffix = KMDateUtils.formatDateyyyyMM(DateUtils.getDate());
            IndexRequest indexRequest = new IndexRequest(KMConstant.DocVisitIndexName + "_" + indexSuffix);
            indexRequest.timeout(TimeValue.timeValueHours(KMConstant.SaveTimeOutMinutes));
            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            indexRequest.source(new JSONObject(kmDocVisitRecordEsVO,
                            new JSONConfig().setDateFormat(DatePattern.NORM_DATETIME_PATTERN)).toString()
                    , XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if (!response.status().equals(RestStatus.CREATED)) {
                log.error("入库ES发生错误，返回码:" + response.status().toString() );
            }
            else
                log.debug("访问记录入库ES成功");
        }
        catch (Exception e){
            log.error("入库ES发生错误" ,e );
        }
    }


    @Deprecated
    @Override
    public List<String> recentlyVisitedDocs(String createBy)  throws IOException {
        List<String> result = new ArrayList<>();

        QueryBuilder queryBuilder = QueryBuilders
                .termQuery("createBy",createBy);
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        String[] includeFields = new String[] {"docId"};
        String[] excludeFields = new String[] {};
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .query(queryBuilder)
                .sort(fieldSortBuilder)
                .size(10);
//                .fetchSource(includeFields,excludeFields);
        //超时 60S
        searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(KMConstant.DocVisitIndexAliasName);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        if(searchResponse.status() == RestStatus.OK && searchResponse.getHits().getTotalHits().value>0){

            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();

            for (SearchHit hit : searchHits) {
                KmDocEsVO kmDocEsVO = JSON.parseObject(hit.getSourceAsString(), KmDocEsVO.class);
                result.add(kmDocEsVO.getDocId());
            }
        }

        return  result;
    }

}
