package org.jeecg.modules.KM.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.KM.service.IKmEsMgntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags="ES模板管理")
@RestController
@RequestMapping("/KM/EsMgnt")
@Slf4j
public class EsMgntController {
    @Autowired
    private IKmEsMgntService kmEsMgntService;

    @ApiOperation(value="ES-management-km_doc模板初始化", notes="ES-management-km_doc模板初始化")
    @PutMapping(value = "/initKmDocTemplate")
    public Result<?> initKmDocTemplate()  throws IOException  {
        return kmEsMgntService.initKmDocTemplate();

    }


    @ApiOperation(value="ES-management-km_doc_visit_record模板初始化", notes="ES-management-km_doc_visit_record模板初始化")
    @PutMapping(value = "/initKmDocVisitTemplate")
    public Result<?> initKmDocVisitTemplate() throws IOException {
        return kmEsMgntService.initKmDocVisitTemplate();

    }


    @ApiOperation(value="ES-management-km_search_record模板初始化", notes="ES-management-km_search_record模板初始化")
    @PutMapping(value = "/initKmSearchRecordTemplate")
    public Result<?> initKmSearchRecordTemplate() throws IOException {
        return kmEsMgntService.initKmSearchRecordTemplate();
    }

}
