package org.jeecg.modules.KM.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
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
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysCategoryModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.KM.VO.KmSearchRecordEsVO;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.common.utils.EsUtils;
import org.jeecg.modules.KM.common.utils.KMDateUtils;
import org.jeecg.modules.KM.common.utils.StringUtils;
import org.jeecg.modules.KM.entity.KmSearchRecord;
import org.jeecg.modules.KM.mapper.KmSearchRecordMapper;
import org.jeecg.modules.KM.service.IKmSearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KmSearchRecordServiceImpl extends ServiceImpl<KmSearchRecordMapper, KmSearchRecord> implements IKmSearchRecordService {

    @Autowired
    private EsUtils esUtils;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void logSearch(String keyword,String title,String content,String topicCode,String ip){
        KmSearchRecord kmSearchRecord = new KmSearchRecord();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser == null)
            return ;

        String userId = sysUser.getUsername();
        kmSearchRecord.setCreateBy(userId);

        //todo id generation
        kmSearchRecord.setSourceIp(ip);
        kmSearchRecord.setKeywords(keyword);
        kmSearchRecord.setTitle(title);
        kmSearchRecord.setContent(content);
        kmSearchRecord.setTopicCodes(topicCode);
        kmSearchRecord.setCreateBy(userId);
        kmSearchRecord.setCreateTime(DateUtils.getDate());

        String keywordString = "";
        if(content != null) keywordString += content;
        if(title != null) keywordString = keywordString + "," + title;
        List<String> paramKeywordList = esUtils.getIkAnalyzeSearchTerms(keywordString);
        //keyword不做分词
        if(keyword != null)
            paramKeywordList.add(keyword);

        kmSearchRecord.setKeywordsMax(StringUtils.concatListToString(paramKeywordList));

        super.save(kmSearchRecord);

        //入库ES
        saveToEs(convertToEsVo(kmSearchRecord));
    }

    @Override
    public List<String> hotKeywordReport() throws IOException {
        List<String> result = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregationBuilder =
                AggregationBuilders
                        .terms("keyword")
                        .field("keywordsMax")
                        .size(10);

        searchSourceBuilder.aggregation(aggregationBuilder);
//
        //超时 60S
        searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(KMConstant.KMSearchRecordIndexAliasName);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        if(searchResponse.status() != RestStatus.OK || searchResponse.getHits().getTotalHits().value<=0){
            return null;
        }
        else {
            Aggregations responseAggregations = searchResponse.getAggregations();
            ParsedStringTerms terms = responseAggregations.get("keyword");
            List<? extends Terms.Bucket> buckets = terms.getBuckets();

            for (Terms.Bucket bucket : buckets) {
                String term = bucket.getKeyAsString();
                //Integer docCount = new Long( bucket.getDocCount()).intValue();
                result.add(term);
            }
        }

        return  result;
    }

    @Override
    public List<SysCategoryModel> retriveHotTopic() throws IOException{
        List<SysCategoryModel> result = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregationBuilder =
                AggregationBuilders
                        .terms("topicCode")
                        .field("topicCodes")
                        .size(10);

        searchSourceBuilder.aggregation(aggregationBuilder);
        //超时 60S
        searchSourceBuilder.timeout(new TimeValue(KMConstant.SearchTimeOutSeconds, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(KMConstant.KMSearchRecordIndexAliasName);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        if(searchResponse.status() != RestStatus.OK || searchResponse.getHits().getTotalHits().value<=0){
            return null;
        }
        else {
            Aggregations responseAggregations = searchResponse.getAggregations();
            ParsedStringTerms terms = responseAggregations.get("topicCode");
            List<? extends Terms.Bucket> buckets = terms.getBuckets();

            for (Terms.Bucket bucket : buckets) {
                String term = bucket.getKeyAsString();
                SysCategoryModel sysCategoryModel = sysBaseAPI.queryCategoryByCode(term);
                if(sysCategoryModel!=null )
                    result.add(sysCategoryModel);
            }
        }
        return  result;
    }

    @Override
    public List<String> hotTopicReport()  {
        List<String> result = new ArrayList<>();
        try {
            List<SysCategoryModel> sysCategoryModels = retriveHotTopic();
            if(sysCategoryModels != null && !sysCategoryModels.isEmpty()) {
                sysCategoryModels
                        .stream()
                        .forEach(
                                e -> {
                                    result.add(e.getName());
                                });
                return result;
            }
        }
        catch (IOException e){
            e.printStackTrace();
            return result;
        }
        return result;
    }

    private KmSearchRecordEsVO convertToEsVo(KmSearchRecord kmSearchRecord){
        KmSearchRecordEsVO kmSearchRecordEsVO = new KmSearchRecordEsVO();
        kmSearchRecordEsVO.setContent(kmSearchRecord.getContent());
        kmSearchRecordEsVO.setCreateBy(kmSearchRecord.getCreateBy());
        kmSearchRecordEsVO.setCreateTime(kmSearchRecord.getCreateTime());
        kmSearchRecordEsVO.setKeywords(kmSearchRecord.getKeywords());
        if(kmSearchRecord.getKeywordsMax() !=null && kmSearchRecord.getKeywordsMax().length()>0)
            kmSearchRecordEsVO.setKeywordsMax(kmSearchRecord.getKeywordsMax().split(","));
        kmSearchRecordEsVO.setTitle(kmSearchRecord.getTitle());
        if(kmSearchRecord.getTopicCodes() != null && kmSearchRecord.getTopicCodes().length()>0)
            kmSearchRecordEsVO.setTopicCodes(kmSearchRecord.getTopicCodes().split(","));
        kmSearchRecordEsVO.setSourceIp(kmSearchRecord.getSourceIp());
        return  kmSearchRecordEsVO;
    }


    private void saveToEs(KmSearchRecordEsVO kmSearchRecordEsVO) {
        try {
            //插入数据，index不存在则自动根据匹配到的template创建。index没必要每天创建一个，如果是为了灵活管理，最低建议每月一个 yyyyMM。
            String indexSuffix = KMDateUtils.formatDateyyyyMM(DateUtils.getDate());
            IndexRequest indexRequest = new IndexRequest(KMConstant.KMSearchRecordIndexName + "_" + indexSuffix);
            indexRequest.timeout(TimeValue.timeValueHours(KMConstant.SaveTimeOutMinutes));
            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            indexRequest.source(new JSONObject(kmSearchRecordEsVO,
                            new JSONConfig().setDateFormat(DatePattern.NORM_DATETIME_PATTERN)).toString()
                    , XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if (!response.status().equals(RestStatus.CREATED)) {
                log.error("入库ES发生错误，返回码:" + response.status().toString() );
            }
            else
                log.debug("搜索记录入库ES成功");
        }
        catch (Exception e){
            log.error("入库ES发生错误" ,e );
        }
    }
}
