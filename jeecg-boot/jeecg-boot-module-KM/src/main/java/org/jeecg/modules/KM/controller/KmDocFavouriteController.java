package org.jeecg.modules.KM.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.VO.KmDocVO;
import org.jeecg.modules.KM.entity.KmDocFavourite;
import org.jeecg.modules.KM.service.IKmDocFavouriteService;
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

@Api(tags="知识文档收藏")
@RestController
@RequestMapping("/KM/kmDocFavourite")
@Slf4j
public class KmDocFavouriteController extends JeecgController<KmDocFavourite, IKmDocFavouriteService> {
	@Autowired
	private IKmDocFavouriteService kmDocFavouriteService;
	
	/**
	 * 分页列表查询
	 *
	 * @param
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_doc_favourite-分页列表查询")
	@ApiOperation(value="km_doc_favourite-分页列表查询", notes="km_doc_favourite-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmDocParamVO kmDocParamVO,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUser != null) {
            //处理过滤source -> sourceList
            if(kmDocParamVO.getSource() != null && !kmDocParamVO.getSource().isEmpty())
                kmDocParamVO.setSourceList(Arrays.asList(kmDocParamVO.getSource().split(",")));

			KmDocVO kmDocVO = new KmDocVO();
			QueryWrapper<KmDocVO> queryWrapper = QueryGenerator.initQueryWrapper(kmDocVO, req.getParameterMap());
			String orderBy = queryWrapper.getExpression().getOrderBy().getSqlSegment();
			Page<KmDocVO> page = new Page<>(pageNo, pageSize);
			IPage<KmDocVO> pageList = kmDocFavouriteService.queryPageList(page, sysUser.getId(),kmDocParamVO,orderBy);
			return Result.OK(pageList);
		}
		else
			return Result.error("登录信息异常");
	}
	
	/**
	 *   添加
	 *
	 * @param docId
	 * @return
	 */
	@AutoLog(value = "km_doc_favourite-添加")
	@ApiOperation(value="km_doc_favourite-添加", notes="km_doc_favourite-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestParam(name = "docId") String docId) {

		return kmDocFavouriteService.addFavouriteDoc(docId);
	}
	
	/**
	 *  编辑
	 *
	 * @param kmDocFavourite
	 * @return
	 */
	@AutoLog(value = "km_doc_favourite-编辑")
	@ApiOperation(value="km_doc_favourite-编辑", notes="km_doc_favourite-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody KmDocFavourite kmDocFavourite) {
		kmDocFavouriteService.updateById(kmDocFavourite);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param docId
	 * @return
	 */
	@AutoLog(value = "km_doc_favourite-通过id删除")
	@ApiOperation(value="km_doc_favourite-通过id删除", notes="km_doc_favourite-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="docId",required=true) String docId) {

		return kmDocFavouriteService.delFavouriteDoc(docId);
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_doc_favourite-批量删除")
	@ApiOperation(value="km_doc_favourite-批量删除", notes="km_doc_favourite-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kmDocFavouriteService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_doc_favourite-通过id查询")
	@ApiOperation(value="km_doc_favourite-通过id查询", notes="km_doc_favourite-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmDocFavourite kmDocFavourite = kmDocFavouriteService.getById(id);
		if(kmDocFavourite==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kmDocFavourite);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kmDocFavourite
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KmDocFavourite kmDocFavourite) {
        return super.exportXls(request, kmDocFavourite, KmDocFavourite.class, "km_doc_favourite");
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
        return super.importExcel(request, response, KmDocFavourite.class);
    }

}
