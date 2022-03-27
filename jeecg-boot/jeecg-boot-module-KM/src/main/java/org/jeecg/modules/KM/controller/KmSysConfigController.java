package org.jeecg.modules.KM.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.entity.KmSysConfig;
import org.jeecg.modules.KM.service.IKmSysConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import springfox.documentation.spring.web.json.Json;

@Api(tags="配置管理")
@RestController
@RequestMapping("/KM/kmSysConfig")
@Slf4j
public class KmSysConfigController extends JeecgController<KmSysConfig, IKmSysConfigService> {
	@Autowired
	private IKmSysConfigService kmSysConfigService;

	/**
	 * 分页列表查询
	 *
	 * @param kmSysConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_sys_config-分页列表查询")
	@ApiOperation(value="km_sys_config-分页列表查询", notes="km_sys_config-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmSysConfig kmSysConfig,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<KmSysConfig> queryWrapper = QueryGenerator.initQueryWrapper(kmSysConfig, req.getParameterMap());
		Page<KmSysConfig> page = new Page<KmSysConfig>(pageNo, pageSize);
		IPage<KmSysConfig> pageList = kmSysConfigService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param kmSysConfig
	 * @return
	 */
	@AutoLog(value = "km_sys_config-添加")
	@ApiOperation(value="km_sys_config-添加", notes="km_sys_config-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody KmSysConfig kmSysConfig) {
		kmSysConfigService.save(kmSysConfig);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param kmSysConfig
	 * @return
	 */
	@AutoLog(value = "km_sys_config-编辑")
	@ApiOperation(value="km_sys_config-编辑", notes="km_sys_config-编辑")
	@PutMapping(value = "/edit")
	@CacheEvict(value = "kmSysConfig",allEntries = true)
	public Result<?> edit(@RequestBody KmSysConfig kmSysConfig) {
		kmSysConfigService.updateById(kmSysConfig);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_sys_config-通过id删除")
	@ApiOperation(value="km_sys_config-通过id删除", notes="km_sys_config-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		kmSysConfigService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_sys_config-批量删除")
	@ApiOperation(value="km_sys_config-批量删除", notes="km_sys_config-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kmSysConfigService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_sys_config-通过id查询")
	@ApiOperation(value="km_sys_config-通过id查询", notes="km_sys_config-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmSysConfig kmSysConfig = kmSysConfigService.getById(id);
		if(kmSysConfig==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kmSysConfig);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param kmSysConfig
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, KmSysConfig kmSysConfig) {
        return super.exportXls(request, kmSysConfig, KmSysConfig.class, "km_sys_config");
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
        return super.importExcel(request, response, KmSysConfig.class);
    }

}
