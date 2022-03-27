package org.jeecg.modules.KM.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.KM.entity.KmDoc;
import org.jeecg.modules.KM.entity.KmDocVisitRecord;
import org.jeecg.modules.KM.service.IKmDocService;
import org.jeecg.modules.KM.service.IKmDocVisitRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

@Api(tags="知识文档访问记录")
@RestController
@RequestMapping("/KM/kmDocVisitRecord")
@Slf4j
public class KmDocVisitRecordController extends JeecgController<KmDocVisitRecord, IKmDocVisitRecordService> {
	@Autowired
	private IKmDocVisitRecordService kmDocVisitRecordService;
	@Autowired
	private IKmDocService kmDocService;

	 /**
	 * 分页列表查询
	 *
	 * @param kmDocVisitRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_doc_visit_record-分页列表查询")
	@ApiOperation(value="km_doc_visit_record-分页列表查询", notes="km_doc_visit_record-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmDocVisitRecord kmDocVisitRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<KmDocVisitRecord> queryWrapper = QueryGenerator.initQueryWrapper(kmDocVisitRecord, req.getParameterMap());
		Page<KmDocVisitRecord> page = new Page<KmDocVisitRecord>(pageNo, pageSize);
		IPage<KmDocVisitRecord> pageList = kmDocVisitRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param kmDocVisitRecord
	 * @return
	 */
	@AutoLog(value = "km_doc_visit_record-添加")
	@ApiOperation(value="km_doc_visit_record-添加", notes="km_doc_visit_record-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KmDocVisitRecord kmDocVisitRecord) {
		kmDocVisitRecordService.save(kmDocVisitRecord);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param kmDocVisitRecord
	 * @return
	 */
	@AutoLog(value = "km_doc_visit_record-编辑")
	@ApiOperation(value="km_doc_visit_record-编辑", notes="km_doc_visit_record-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KmDocVisitRecord kmDocVisitRecord) {
		kmDocVisitRecordService.updateById(kmDocVisitRecord);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_visit_record-通过id删除")
	@ApiOperation(value="km_doc_visit_record-通过id删除", notes="km_doc_visit_record-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kmDocVisitRecordService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_doc_visit_record-批量删除")
	@ApiOperation(value="km_doc_visit_record-批量删除", notes="km_doc_visit_record-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kmDocVisitRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_visit_record-通过id查询")
	@ApiOperation(value="km_doc_visit_record-通过id查询", notes="km_doc_visit_record-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmDocVisitRecord kmDocVisitRecord = kmDocVisitRecordService.getById(id);
		if(kmDocVisitRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kmDocVisitRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kmDocVisitRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KmDocVisitRecord kmDocVisitRecord) {
        return super.exportXls(request, kmDocVisitRecord, KmDocVisitRecord.class, "km_doc_visit_record");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, KmDocVisitRecord.class);
    }

}
