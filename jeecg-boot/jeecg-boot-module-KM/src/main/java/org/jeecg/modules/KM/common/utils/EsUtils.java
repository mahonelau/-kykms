package org.jeecg.modules.KM.common.utils;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EsUtils {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

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
}
