package org.jeecg.modules.KM.controller;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.KM.entity.KmSearchRecord;
import org.jeecg.modules.KM.service.IKmSearchRecordService;
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

@Api(tags="知识文档检索记录")
@RestController
@RequestMapping("/KM/kmSearchRecord")
@Slf4j
public class KmSearchRecordController extends JeecgController<KmSearchRecord, IKmSearchRecordService> {
	@Autowired
	private IKmSearchRecordService kmSearchRecordService;

	 @ApiOperation(value="km_search_record-热词报告", notes="km_search_record-热词报告")
	 @GetMapping(value = "/hotKeywordReport")
	public Result<?> hotKeywordReport(){
		try {
			List<String> result =  kmSearchRecordService.hotKeywordReport();
			return  Result.OK(result);
		} catch (IOException e) {
			e.printStackTrace();
			return Result.error("错误,io异常");
		}
	}

	 @ApiOperation(value="km_search_record-热门专题报告", notes="km_search_record-热门专题报告")
	 @GetMapping(value = "/hotTopicReport")
	 public Result<?> hotTopicReport(){
		 List<String> result =  kmSearchRecordService.hotTopicReport();
		 return  Result.OK(result);

	 }

	/**
	 * 分页列表查询
	 *
	 * @param kmSearchRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_search_record-分页列表查询")
	@ApiOperation(value="km_search_record-分页列表查询", notes="km_search_record-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmSearchRecord kmSearchRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<KmSearchRecord> queryWrapper = QueryGenerator.initQueryWrapper(kmSearchRecord, req.getParameterMap());
		Page<KmSearchRecord> page = new Page<KmSearchRecord>(pageNo, pageSize);
		IPage<KmSearchRecord> pageList = kmSearchRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param kmSearchRecord
	 * @return
	 */
	@AutoLog(value = "km_search_record-添加")
	@ApiOperation(value="km_search_record-添加", notes="km_search_record-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KmSearchRecord kmSearchRecord) {
		kmSearchRecordService.save(kmSearchRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param kmSearchRecord
	 * @return
	 */
	@AutoLog(value = "km_search_record-编辑")
	@ApiOperation(value="km_search_record-编辑", notes="km_search_record-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KmSearchRecord kmSearchRecord) {
		kmSearchRecordService.updateById(kmSearchRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_search_record-通过id删除")
	@ApiOperation(value="km_search_record-通过id删除", notes="km_search_record-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kmSearchRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_search_record-批量删除")
	@ApiOperation(value="km_search_record-批量删除", notes="km_search_record-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kmSearchRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_search_record-通过id查询")
	@ApiOperation(value="km_search_record-通过id查询", notes="km_search_record-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmSearchRecord kmSearchRecord = kmSearchRecordService.getById(id);
		if(kmSearchRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kmSearchRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kmSearchRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KmSearchRecord kmSearchRecord) {
        return super.exportXls(request, kmSearchRecord, KmSearchRecord.class, "km_search_record");
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
        return super.importExcel(request, response, KmSearchRecord.class);
    }

}
