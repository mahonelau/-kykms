package org.jeecg.modules.KM.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.KM.entity.KmDocTopicType;
import org.jeecg.modules.KM.service.IKmDocTopicTypeService;
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

@Api(tags="知识专题")
@RestController
@RequestMapping("/KM/kmDocTopicType")
@Slf4j
public class KmDocTopicTypeController extends JeecgController<KmDocTopicType, IKmDocTopicTypeService> {
	@Autowired
	private IKmDocTopicTypeService kmDocTopicTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param kmDocTopicType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_doc_topic_type-分页列表查询")
	@ApiOperation(value="km_doc_topic_type-分页列表查询", notes="km_doc_topic_type-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmDocTopicType kmDocTopicType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<KmDocTopicType> queryWrapper = QueryGenerator.initQueryWrapper(kmDocTopicType, req.getParameterMap());
		Page<KmDocTopicType> page = new Page<KmDocTopicType>(pageNo, pageSize);
		IPage<KmDocTopicType> pageList = kmDocTopicTypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param kmDocTopicType
	 * @return
	 */
	@AutoLog(value = "km_doc_topic_type-添加")
	@ApiOperation(value="km_doc_topic_type-添加", notes="km_doc_topic_type-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KmDocTopicType kmDocTopicType) {
		kmDocTopicTypeService.save(kmDocTopicType);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param kmDocTopicType
	 * @return
	 */
	@AutoLog(value = "km_doc_topic_type-编辑")
	@ApiOperation(value="km_doc_topic_type-编辑", notes="km_doc_topic_type-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KmDocTopicType kmDocTopicType) {
		kmDocTopicTypeService.updateById(kmDocTopicType);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_topic_type-通过id删除")
	@ApiOperation(value="km_doc_topic_type-通过id删除", notes="km_doc_topic_type-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kmDocTopicTypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_doc_topic_type-批量删除")
	@ApiOperation(value="km_doc_topic_type-批量删除", notes="km_doc_topic_type-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kmDocTopicTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_topic_type-通过id查询")
	@ApiOperation(value="km_doc_topic_type-通过id查询", notes="km_doc_topic_type-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmDocTopicType kmDocTopicType = kmDocTopicTypeService.getById(id);
		if(kmDocTopicType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kmDocTopicType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kmDocTopicType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KmDocTopicType kmDocTopicType) {
        return super.exportXls(request, kmDocTopicType, KmDocTopicType.class, "km_doc_topic_type");
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
        return super.importExcel(request, response, KmDocTopicType.class);
    }

}
