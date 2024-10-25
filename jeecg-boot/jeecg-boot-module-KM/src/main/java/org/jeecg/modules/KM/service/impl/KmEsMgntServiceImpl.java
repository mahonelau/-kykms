package org.jeecg.modules.KM.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.IndexTemplatesExistRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.KM.VO.KmDocEsVO;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.entity.KmDoc;
import org.jeecg.modules.KM.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;

@Service
@Slf4j
public class KmEsMgntServiceImpl  implements IKmEsMgntService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private KmDocServiceImpl kmDocService;


    private  boolean checkTemplateExists(String templateName)throws IOException {
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(templateName);
        boolean response = restHighLevelClient.indices().existsTemplate(request, RequestOptions.DEFAULT);
        return response;
    }


    private  boolean checkIndexExists(String indexName)throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean response = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return response;
    }

    //为初始化索引
    @Override
    public void initEXIndex(){
        KmDocEsVO kmEsVO = new KmDocEsVO();
        kmEsVO.setTitle("for init");
        kmEsVO.setDocId("1");
        Result<?> result = kmDocService.saveDocToEs(kmEsVO, "");
        if (result.isSuccess()) {
            String indexId = (String) result.getResult();
            if (indexId != null && !indexId.isEmpty()) {
                Result<?> result1 = kmDocService.deleteDocFromEs(indexId);
                if (result1.isSuccess()) {
                    log.info("init index success!");
                }
            }
        }
    }

    @Override
    public Result<?> syncReleasedDocToES(){
        Result result = Result.OK("初始化:同步发布文档到ES");;

        List<KmDoc> releasedDocs = kmDocService.getReleasedDocs();
        for (KmDoc releasedDoc : releasedDocs) {
            kmDocService.indexDocSync(releasedDoc);
        }
        return result;
    }

    @Override
    public Result<?> initTemplateAndSyncDocs() throws IOException {
        Result result = Result.OK("创建模版");;
        if(!checkTemplateExists(KMConstant.DocIndexName)){
            result = initKmDocTemplate();
            if(!result.isSuccess()){
                log.error(result.getMessage());
                return result;
            }
            //首次建索引
            initEXIndex();
        }
        if (!checkIndexExists(KMConstant.DocIndexName)) {
            log.info("start sync docs record to ES...");
            //for手工删索引，重新对知识入库
            syncReleasedDocToES();
        }
        if(!checkTemplateExists(KMConstant.KMSearchRecordIndexName)){
            result = initKmSearchRecordTemplate();
            if(!result.isSuccess()){
                log.error(result.getMessage());
                return result;
            }
        }
        if(!checkTemplateExists(KMConstant.DocVisitIndexName)){
            result = initKmDocVisitTemplate();
            if(!result.isSuccess()){
                log.error(result.getMessage());
                return result;
            }
        }
        if(!result.isSuccess()){
            log.error("初始化ES模版失败：",result.getMessage());
        }
        return result;
    }

    @Override
    public Result<?> initKmDocTemplate() throws IOException {
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(KMConstant.DocIndexName);
        //别名，所有根据该模版创建的index 都会添加这个别名。查询时可查询别名，就可以同时查询多个名称不同的index，根据此方法可实现index每天或每月生成等逻辑。
        request.alias(new Alias(KMConstant.DocIndexAliasName));
        //匹配哪些index。在创建index时会生效。
        request.patterns(CollUtil.newArrayList(KMConstant.DocIndexName + "*"));
        request.order(3);
        request.settings(Settings.builder()
                //数据插入后多久能查到，实时性要求高可以调低
                .put("index.refresh_interval", "10s")
                //传输日志，对数据安全性要求高的 设置 request，默认值:request
                .put("index.translog.durability", "async")
                .put("index.translog.sync_interval", "120s")
                //分片数量 todo :待优化
                .put("index.number_of_shards", "2")
                //副本数量
                .put("index.number_of_replicas", "0")
                //单次最大查询数据的数量。默认10000。不要设置太高，如果有导出需求可以根据查询条件分批次查询。
                .put("index.max_result_window", "1000"));
        //使用官方提供的工具构建json。可以直接拼接一个json字符串，也可以使用map嵌套。
        XContentBuilder jsonMapping = XContentFactory.jsonBuilder();
        //所有数据类型 看官方文档:https://www.elastic.co/guide/en/elasticsearch/reference/7.4/mapping-types.html#_core_datatypes
        //keyword类型不会分词存储
        jsonMapping.startObject().startObject("properties")
                .startObject("id").field("type", "keyword").endObject()
                .startObject("releaseFlag").field("type", "integer").endObject()
                .startObject("publicRemark").field("type", "integer").endObject()
                .startObject("category").field("type", "integer").endObject()
                .startObject("fileNo").field("type", "keyword").endObject()
                .startObject("orgCode").field("type", "keyword").endObject()
                .startObject("createTime").field("type", "date").field("format", "yyyy-MM-dd").endObject()
                //指定分词器
//                .startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .startObject("title").field("type", "text").field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word").endObject()
                .startObject("content").field("type", "text").field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word").endObject()
//                .startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .startObject("businessTypes").field("type","integer").endObject()
                .startObject("keywords").field("type","keyword").endObject()
                .startObject("topicCodes").field("type","keyword").endObject()
                .endObject().endObject();
        request.mapping(jsonMapping);
        //设置为true只强制创建，而不是更新索引模板。如果它已经存在，它将失败
        request.create(false);
        AcknowledgedResponse response = restHighLevelClient.indices().putTemplate(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged()) {
            log.info(KMConstant.DocIndexName + "模版创建成功!");
            return  Result.OK("创建模版成功!");
        } else {
            log.error(KMConstant.DocIndexName + "模版创建失败!");
            return  Result.error("创建模版失败!");
        }
    }

    @Override
    public Result<?> initKmDocVisitTemplate() throws IOException {
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(KMConstant.DocVisitIndexName);
        request.alias(new Alias(KMConstant.DocVisitIndexAliasName));
        request.patterns(CollUtil.newArrayList(KMConstant.DocVisitIndexName + "*"));
        request.order(1);
        request.settings(Settings.builder()
                .put("index.refresh_interval", "60s")
                .put("index.translog.durability", "async")
                .put("index.translog.sync_interval", "600s")
                .put("index.number_of_shards", "2")
                .put("index.number_of_replicas", "0")
                .put("index.max_result_window", "10000"));
        XContentBuilder jsonMapping = XContentFactory.jsonBuilder();
        jsonMapping.startObject().startObject("properties")
                .startObject("id").field("type", "keyword").endObject()
                .startObject("visitType").field("type", "integer").endObject()
                .startObject("createBy").field("type", "keyword").endObject()
                .startObject("keywords").field("type", "text").field("analyzer", "ik_smart").endObject()
                .startObject("keywordsMax").field("type", "keyword").endObject()
                .startObject("createTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                .startObject("sourceIp").field("type", "ip").endObject()
                .endObject().endObject();
        request.mapping(jsonMapping);
        request.create(false);
        AcknowledgedResponse response = restHighLevelClient.indices().putTemplate(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged()) {
            log.info(KMConstant.DocVisitIndexName + "模版创建成功!");
            return  Result.OK("创建模版成功!");
        } else {
            log.error(KMConstant.DocVisitIndexName + "模版创建失败!");
            return  Result.error("创建模版失败!");
        }
    }

    @Override
    public Result<?> initKmSearchRecordTemplate() throws IOException {
        PutIndexTemplateRequest request = new PutIndexTemplateRequest(KMConstant.KMSearchRecordIndexName);
        request.alias(new Alias(KMConstant.KMSearchRecordIndexAliasName));
        request.patterns(CollUtil.newArrayList(KMConstant.KMSearchRecordIndexName + "*"));
        request.settings(Settings.builder()
                .put("index.refresh_interval", "60s")
                .put("index.translog.durability", "async")
                .put("index.translog.sync_interval", "600s")
                .put("index.number_of_shards", "2")
                .put("index.number_of_replicas", "0")
                .put("index.max_result_window", "10000"));
        XContentBuilder jsonMapping = XContentFactory.jsonBuilder();
        jsonMapping.startObject().startObject("properties")
                .startObject("title").field("type", "text").field("analyzer", "ik_smart").endObject()
                .startObject("content").field("type", "text").field("analyzer", "ik_smart").endObject()
                .startObject("keywords").field("type", "text").field("analyzer", "ik_smart").endObject()
                .startObject("keywordsMax").field("type", "keyword").endObject()
                .startObject("topicCodes").field("type", "keyword").endObject()
                .startObject("createBy").field("type", "keyword").endObject()
                .startObject("createTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                .startObject("sourceIp").field("type", "ip").endObject()
                .endObject().endObject();
        request.mapping(jsonMapping);
        request.create(false);
        AcknowledgedResponse response = restHighLevelClient.indices().putTemplate(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged()) {
            log.info(KMConstant.KMSearchRecordIndexName + "模版创建成功!");
            return  Result.OK("创建模版成功!");
        } else {
            log.error(KMConstant.KMSearchRecordIndexName + "模版创建失败!");
            return  Result.error("创建模版失败!");
        }
    }
}
