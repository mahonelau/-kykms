package org.jeecg.modules.KM.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.KM.VO.KmDocCommentsVO;
import org.jeecg.modules.KM.entity.KmDocComments;
import org.jeecg.modules.KM.service.IKmDocCommentsService;

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

@Api(tags="km_doc_comments")
@RestController
@RequestMapping("/KM/KmDocComments")
@Slf4j
public class KmDocCommentsController extends JeecgController<KmDocComments, IKmDocCommentsService> {
	@Autowired
	private IKmDocCommentsService KmDocCommentsService;

	/**
	 * 分页列表查询
	 *
	 * @param KmDocComments
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_doc_comments-分页列表查询")
	@ApiOperation(value="km_doc_comments-分页列表查询", notes="km_doc_comments-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmDocComments KmDocComments,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<KmDocComments> queryWrapper = QueryGenerator.initQueryWrapper(KmDocComments, req.getParameterMap());
		Page<KmDocCommentsVO> page = new Page<>(pageNo, pageSize);
		IPage<KmDocCommentsVO> pageList = KmDocCommentsService.queryPageList(page, KmDocComments);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param KmDocComments
	 * @return
	 */
	@AutoLog(value = "km_doc_comments-添加")
	@ApiOperation(value="km_doc_comments-添加", notes="km_doc_comments-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KmDocComments KmDocComments) {
		KmDocCommentsService.save(KmDocComments);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param KmDocComments
	 * @return
	 */
	@AutoLog(value = "km_doc_comments-编辑")
	@ApiOperation(value="km_doc_comments-编辑", notes="km_doc_comments-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KmDocComments KmDocComments) {
		KmDocCommentsService.updateById(KmDocComments);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_comments-通过id删除")
	@ApiOperation(value="km_doc_comments-通过id删除", notes="km_doc_comments-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		KmDocCommentsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_doc_comments-批量删除")
	@ApiOperation(value="km_doc_comments-批量删除", notes="km_doc_comments-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.KmDocCommentsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_comments-通过id查询")
	@ApiOperation(value="km_doc_comments-通过id查询", notes="km_doc_comments-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmDocComments KmDocComments = KmDocCommentsService.getById(id);
		if(KmDocComments==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(KmDocComments);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param KmDocComments
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KmDocComments KmDocComments) {
        return super.exportXls(request, KmDocComments, KmDocComments.class, "km_doc_comments");
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
        return super.importExcel(request, response, KmDocComments.class);
    }

}
