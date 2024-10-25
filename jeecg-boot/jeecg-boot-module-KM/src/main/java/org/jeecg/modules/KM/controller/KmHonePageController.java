package org.jeecg.modules.KM.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.SysCategoryModel;
import org.jeecg.modules.KM.VO.KmChartVO;
import org.jeecg.modules.KM.VO.KmDocStatisticsVO;
import org.jeecg.modules.KM.VO.KmDocSummaryVO;
import org.jeecg.modules.KM.entity.KmDoc;
import org.jeecg.modules.KM.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

@Api(tags="km_doc")
@RestController
@RequestMapping("/KM/HomePage")
@Slf4j
public class KmHonePageController extends JeecgController<KmDoc, IKmDocService> {
    @Autowired
    private IKmSearchRecordService kmSearchRecordService;
    @Autowired
    private IKmSysConfigService kmSysConfigService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IKmDocService kmDocService;


    @ApiOperation(value="km_doc-首页专题", notes="km_doc-首页专题")
    @GetMapping(value = "/listRecommendTopic")
    public Result<?> listRecommendTopic(  ) {
        Map<String,SysCategoryModel> sysCategoryModelsTarget = new HashMap<>();
        List<SysCategoryModel> sysCategoryModels =sysBaseAPI.queryCategoryRecommend();
        for (SysCategoryModel model : sysCategoryModels){
            sysCategoryModelsTarget.put(model.getCode(),model);
        }

        String recommendHotTopic = kmSysConfigService.getSysConfigValue("RecommendHotTopic");
        if(recommendHotTopic != null && recommendHotTopic.equals("1")) {
            try {
                List<SysCategoryModel> sysCategoryModelsHot = kmSearchRecordService.retriveHotTopic();
                if(sysCategoryModelsHot != null && !sysCategoryModelsHot.isEmpty()){

                    for (SysCategoryModel modelHot:sysCategoryModelsHot){
                        if(!sysCategoryModelsTarget.containsKey(modelHot.getCode()))
                            sysCategoryModelsTarget.put(modelHot.getCode(),modelHot);
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
                //return Result.error("io异常");
            }
        }
        if(!sysCategoryModelsTarget.isEmpty()) {
            List<SysCategoryModel> sysCategoryModelsResult = new ArrayList<>(sysCategoryModelsTarget.values());
            if(sysCategoryModelsResult.size()>20)
                sysCategoryModelsResult = sysCategoryModelsResult.subList(0,19);
            return Result.OK(sysCategoryModelsResult);
        }
        else
            return Result.OK();
    }

    @ApiOperation(value="km_doc-首页业务列表", notes="km_doc-首页业务列表")
    @GetMapping(value = "/listBusinessType")
    public Result<?> listBusinessType( ) {
        List<DictModel> dictModelList = sysBaseAPI.queryDictItemList("km_dict_business");
        if(dictModelList.size()>20)
            dictModelList = dictModelList.subList(0,19);
        return Result.OK(dictModelList);
    }

    @ApiOperation(value="km_doc-后台数据统计页面接口", notes="km_doc-后台数据统计页面接口")
    @GetMapping(value = "/getCharData")
    public Result<?> getCharData( ) throws IOException {
        KmChartVO kmChartVO = new KmChartVO();
        List<KmDocStatisticsVO> categoryList = kmDocService.queryKmDocStatistics(1);
        kmChartVO.setCategoryChartList(categoryList);
        List<KmDocStatisticsVO> businessList = kmDocService.queryKmDocStatistics(4);
        kmChartVO.setBusinessChartList(businessList);
        List<KmDocStatisticsVO> topicList = kmDocService.queryKmDocStatistics(5);
        kmChartVO.setTopicChartList(topicList);
        List<String> hotKeyword =  kmSearchRecordService.hotKeywordReport();
        kmChartVO.setHotKeywordList(hotKeyword);
        List<String> hotTopic =  kmSearchRecordService.hotTopicReport();
        kmChartVO.setHotTopicList(hotTopic);
        KmDocSummaryVO kmDocSummaryVO = kmDocService.queryKmDocSummary();
        kmChartVO.setKmDocSummaryVO(kmDocSummaryVO);
        return Result.OK(kmChartVO);
    }




}
