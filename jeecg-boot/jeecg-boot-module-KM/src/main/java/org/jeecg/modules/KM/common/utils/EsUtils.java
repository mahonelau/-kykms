package org.jeecg.modules.KM.common.utils;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.KM.VO.KmDocEsFilterParamVO;
import org.jeecg.modules.KM.VO.KmDocEsParamVO;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.entity.KmSearchRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class EsUtils {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private KMConstant kmConstant;

    public List<String> getIkAnalyzeSearchTerms(String searchContent){
        AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(KMConstant.DocIndexAliasName,
                "ik_smart",searchContent);
        List<String> result = new ArrayList<>();
        try {
            if(!searchContent.isEmpty()) {
                AnalyzeResponse analyzeResponse = restHighLevelClient.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);
                analyzeResponse.getTokens().forEach((e) -> result.add(e.getTerm()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public BoolQueryBuilder buildESQueryParams(List<KmDocEsFilterParamVO> filterParams){
        //最终条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (KmDocEsFilterParamVO filterParam : filterParams) {
            BoolQueryBuilder oneFilter = buildESQueryParam(filterParam);
            boolQueryBuilder.filter().addAll(oneFilter.filter());
            boolQueryBuilder.filter().addAll(oneFilter.must());
            boolQueryBuilder.filter().addAll(oneFilter.should());
        }
        return boolQueryBuilder;
    }

    public BoolQueryBuilder buildESQueryParam(KmDocEsFilterParamVO kmDocEsFilterParamVO){

        //最终条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //普通检索的条件，综合 ： 标题、关键字、全文 合并检索
        BoolQueryBuilder boolQueryBuilderDefault = QueryBuilders.boolQuery();

        //1、分类检索，用filter
        if (kmDocEsFilterParamVO.getCategory() != null) {
            List<String> categorys = kmDocEsFilterParamVO.getCategory();
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.termsQuery("category", categorys));
        }

        //准备好标题、全文检索的条件
        AbstractQueryBuilder titleBoolQueryBuilder = null;
        AbstractQueryBuilder contentBoolQueryBuilder  = null;
        if (oConvertUtils.isNotEmpty(kmDocEsFilterParamVO.getTitle())) {
            if(kmDocEsFilterParamVO.getPhraseMatchSearchFlag() != null && kmDocEsFilterParamVO.getPhraseMatchSearchFlag()) {
                titleBoolQueryBuilder = QueryBuilders.
                        matchPhraseQuery("title", kmDocEsFilterParamVO.getTitle())
                        .slop(2);
            }
            else{
                titleBoolQueryBuilder = QueryBuilders.matchQuery("title", kmDocEsFilterParamVO.getTitle())
                        .analyzer("ik_smart").boost(kmConstant.getTitleSearchBoost());
            }
        }
        if (oConvertUtils.isNotEmpty(kmDocEsFilterParamVO.getContent())) {
            if(kmDocEsFilterParamVO.getPhraseMatchSearchFlag() != null && kmDocEsFilterParamVO.getPhraseMatchSearchFlag()) {
                contentBoolQueryBuilder = QueryBuilders.matchPhraseQuery("content", kmDocEsFilterParamVO.getContent())
                        .slop(2);
            }
            else{
                contentBoolQueryBuilder = QueryBuilders.matchQuery("content", kmDocEsFilterParamVO.getContent())
                        .analyzer("ik_smart").boost(kmConstant.getContentSearchBoost());
            }
        }

        //2、标题检索 高级用must，快速用should
        if (kmDocEsFilterParamVO.getTitle() != null && !kmDocEsFilterParamVO.getTitle().isEmpty()) {
            if(kmDocEsFilterParamVO.getAdvSearchFlag() ) {
                boolQueryBuilder.must().add(titleBoolQueryBuilder);
            }
            else{
                boolQueryBuilderDefault.should().add(titleBoolQueryBuilder);
            }
        }
        //3、全文检索  高级用must，快速用should
        if (kmDocEsFilterParamVO.getContent() != null && !kmDocEsFilterParamVO.getContent().isEmpty()) {
            if(kmDocEsFilterParamVO.getAdvSearchFlag() != null && kmDocEsFilterParamVO.getAdvSearchFlag() ) {
                boolQueryBuilder.must().add(contentBoolQueryBuilder);
            }
            else {
                boolQueryBuilderDefault.should().add(contentBoolQueryBuilder);
            }
        }

        //4、关键字检索 用term精确匹配;  高级用must，快速用should
        String keywordString = "";
        if (kmDocEsFilterParamVO.getKeywords() != null && kmDocEsFilterParamVO.getKeywords().size() > 0) {
             BoolQueryBuilder boolQueryBuilderKeywords = QueryBuilders.boolQuery();
            kmDocEsFilterParamVO.getKeywords().forEach(e -> {
                boolQueryBuilderKeywords
                        .should()
                        .add(QueryBuilders.termsQuery("keywords", e)
                                .boost(kmConstant.getKeywordSearchBoost()));
            });
            if(kmDocEsFilterParamVO.getAdvSearchFlag() != null && kmDocEsFilterParamVO.getAdvSearchFlag()) {
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

        //处理快速检索的合并条件：标题、关键字、全文
        if(kmDocEsFilterParamVO.getAdvSearchFlag() == null ||! kmDocEsFilterParamVO.getAdvSearchFlag() ) {
            boolQueryBuilder
                    .must()
                    .add(boolQueryBuilderDefault);
        }


        //5、发布时间检索 用filter
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(kmDocEsFilterParamVO.getCreateTimeEnd() != null){
            boolQueryBuilder.filter().add(
                    QueryBuilders
                            .rangeQuery("createTime")
                            .lte(format.format(kmDocEsFilterParamVO.getCreateTimeEnd() )));
        }
        if(kmDocEsFilterParamVO.getCreateTimeStart() != null){
            boolQueryBuilder.filter().add(
                    QueryBuilders
                            .rangeQuery("createTime")
                            .gte( format.format(kmDocEsFilterParamVO.getCreateTimeStart())));
        }

        //7、标签检索（多选） 用filter
        if (kmDocEsFilterParamVO.getBusinessTypes() != null && kmDocEsFilterParamVO.getBusinessTypes().size() > 0) {
            String tmpBusinessType = "";
            List<String> businessTypes = kmDocEsFilterParamVO.getBusinessTypes();
            boolQueryBuilder
                    .filter()
                    .add(QueryBuilders.termsQuery("businessTypes", businessTypes));
        }

        //9、专题检索（多选，前缀模糊匹配） 用filter
        if( kmDocEsFilterParamVO.getTopicCodes() != null && kmDocEsFilterParamVO.getTopicCodes().size() > 0) {
            BoolQueryBuilder boolQueryBuilderTopicCodes = QueryBuilders.boolQuery();
            List<String> topicCodes = kmDocEsFilterParamVO.getTopicCodes();
            for (int i = 0; i < topicCodes.size(); i++) {
                //模糊匹配，匹配某个字符串开头的记录prefixQuery
                boolQueryBuilderTopicCodes
                        .should()
                        .add(QueryBuilders.prefixQuery("topicCodes", topicCodes.get(i)));
            }
            boolQueryBuilder
                    .filter()
                    .add(boolQueryBuilderTopicCodes);
        }

        return boolQueryBuilder;
    }

}
