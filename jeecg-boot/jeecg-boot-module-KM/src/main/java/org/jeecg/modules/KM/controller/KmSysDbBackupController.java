package org.jeecg.modules.KM.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.entity.KmSysDbBackup;
import org.jeecg.modules.KM.entity.KmSysDbBackup;
import org.jeecg.modules.KM.service.IKmSysDbBackupService;
import org.jeecg.modules.KM.service.IKmSysDbBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Api(tags="km_sys_db_backup")
@RestController
@RequestMapping("/KM/kmSysDbBackup")
@Slf4j
public class KmSysDbBackupController extends JeecgController<KmSysDbBackup, IKmSysDbBackupService> {
	@Autowired
	private IKmSysDbBackupService kmSysDbBackupService;

	@Value("${spring.datasource.dynamic.datasource.master.url}")
	private String dbServerUrl;
	@Value("${spring.datasource.dynamic.datasource.master.username}")
	private String dbServerUserName;
	@Value("${spring.datasource.dynamic.datasource.master.password}")
	private String dbServerPassword;
	@Value("${base.db-backup-dir}")
	private String dbBackupDir;

	 /**
	 * 分页列表查询
	 *
	 * @param KmSysDbBackup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-分页列表查询")
	@ApiOperation(value="km_sys_db_backup-分页列表查询", notes="km_sys_db_backup-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(KmSysDbBackup KmSysDbBackup,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<KmSysDbBackup> queryWrapper = QueryGenerator.initQueryWrapper(KmSysDbBackup, req.getParameterMap());
		Page<KmSysDbBackup> page = new Page<KmSysDbBackup>(pageNo, pageSize);
		IPage<KmSysDbBackup> pageList = kmSysDbBackupService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-添加")
	@ApiOperation(value="km_sys_db_backup-添加", notes="km_sys_db_backup-添加")
	@PostMapping(value = "/add")
	public Result<?> add() {
		String dbType = CommonUtils.getDatabaseType();
		if (!dbType.equalsIgnoreCase("mysql")) {
			return Result.error("只支持mysql数据库备份");
		}
		if (oConvertUtils.isEmpty(dbBackupDir) || oConvertUtils.isEmpty(dbServerUrl) || oConvertUtils.isEmpty(dbServerUserName) || oConvertUtils.isEmpty(dbServerPassword)) {
			return Result.error("备份配置缺失");
		}
		String fileName = "backup_" + new Date().getTime() + ".sql";
		String filePath = dbBackupDir + File.separator + fileName;

		String temp = dbServerUrl.substring(dbServerUrl.indexOf("//"));
		String dbServerIp = temp.substring(2,temp.indexOf(":"));
		temp = temp.substring(temp.indexOf(":"));
		String dbServerPort = temp.substring(1,temp.indexOf("/"));
		temp = temp.substring(temp.indexOf("/"));
		String dbName = temp.substring(1,temp.indexOf("?"));

		String cmd = "cmd /c mysqldump -u" + dbServerUserName + " -p" + dbServerPassword + " -h " + dbServerIp + " -P " + dbServerPort  + " " + dbName +" > " + filePath;
		//-u后的root为mysql数据库用户名，-p后接的123456为该用户密码，注意不要有空格；dbName填写需要备份数据的数据库名称，大于号后接生成文件路径
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			boolean exitFlag = process.waitFor(10*60, TimeUnit.SECONDS);
			if(exitFlag) {
				int code = process.exitValue();
				log.info("code:"+code);
				if(code == 0){
					KmSysDbBackup kmSysDbBackup = new KmSysDbBackup();
					File backupFile = new File(filePath);
					kmSysDbBackup.setSize(backupFile.length());
					kmSysDbBackup.setFileName(fileName);
					kmSysDbBackup.setFilePath(filePath);
					kmSysDbBackup.setCreateTime(new Date());
					kmSysDbBackupService.save(kmSysDbBackup);
					log.info("【备份数据库】成功，SQL文件：{}", fileName);
					return Result.OK("备份成功！");
				}else {
					return Result.error("备份命令执行失败，返回码：",code);
				}
			}else {
				return Result.error("备份超时");
			}
		}catch (Exception e){
			log.error("【备份数据库】失败：{}", e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 通过id下载
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-通过id下载")
	@ApiOperation(value="km_sys_db_backup-通过id下载", notes="km_sys_db_backup-通过id下载")
	@GetMapping(value = "/downloadById")
	public void downloadById(@RequestParam(name="id",required=true) String id, HttpServletResponse response) throws IOException {
		KmSysDbBackup kmSysDbBackup = kmSysDbBackupService.getById(id);
		if(kmSysDbBackup==null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		assert kmSysDbBackup != null;
		String filePath = kmSysDbBackup.getFilePath();
		if(oConvertUtils.isEmpty(filePath)){
			return;
		}
		// 其余处理略
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			File file = new File(filePath);
			if(!file.exists()){
				response.setStatus(404);
				throw new RuntimeException("文件["+filePath+"]不存在..");
			}
			response.setContentType("application/force-download");// 设置强制下载不打开
//			response.setContentType("application/octet-stream");
			response.addHeader("Access-Control-Expose-Headers","Content-disposition");
			response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(kmSysDbBackup.getFileName(), "UTF-8"));
			inputStream = new BufferedInputStream(new FileInputStream(filePath));
			outputStream = response.getOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, len);
			}
			response.flushBuffer();
		} catch (IOException e) {
			log.error("下载备份文件失败" + e.getMessage());
			response.setStatus(404);
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-通过id删除")
	@ApiOperation(value="km_sys_db_backup-通过id删除", notes="km_sys_db_backup-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		KmSysDbBackup kmSysDbBackup = kmSysDbBackupService.getById(id);
		if(kmSysDbBackup==null) {
			return Result.error("未找到对应数据");
		}
		String filePath = kmSysDbBackup.getFilePath();
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		kmSysDbBackupService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-批量删除")
	@ApiOperation(value="km_sys_db_backup-批量删除", notes="km_sys_db_backup-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.kmSysDbBackupService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-通过id查询")
	@ApiOperation(value="km_sys_db_backup-通过id查询", notes="km_sys_db_backup-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		KmSysDbBackup kmSysDbBackup = kmSysDbBackupService.getById(id);
		if(kmSysDbBackup==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(kmSysDbBackup);
	}


	/**
	 *   添加
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "km_sys_db_backup-恢复")
	@ApiOperation(value="km_sys_db_backup-恢复", notes="km_sys_db_backup-恢复")
	@PostMapping(value = "/recover")
	public Result<?> recover(@RequestParam(name="id",required=true) String id) {
		String dbType = CommonUtils.getDatabaseType();
		if (!dbType.equalsIgnoreCase("mysql")) {
			return Result.error("只支持mysql数据库备份");
		}
		if (oConvertUtils.isEmpty(dbBackupDir) || oConvertUtils.isEmpty(dbServerUrl) || oConvertUtils.isEmpty(dbServerUserName) || oConvertUtils.isEmpty(dbServerPassword)) {
			return Result.error("备份配置缺失");
		}
		KmSysDbBackup kmSysDbBackup = kmSysDbBackupService.getById(id);
		if(kmSysDbBackup==null) {
			return Result.error("未找到对应数据");
		}

		String filePath = kmSysDbBackup.getFilePath();

		String temp = dbServerUrl.substring(dbServerUrl.indexOf("//"));
		String dbServerIp = temp.substring(2,temp.indexOf(":"));
		temp = temp.substring(temp.indexOf(":"));
		String dbServerPort = temp.substring(1,temp.indexOf("/"));
		temp = temp.substring(temp.indexOf("/"));
		String dbName = temp.substring(1,temp.indexOf("?"));

		String cmd = "cmd /c mysqldump -u" + dbServerUserName + " -p" + dbServerPassword + " -h " + dbServerIp + " -P " + dbServerPort  + " " + dbName +" < " + filePath;
		//-u后的root为mysql数据库用户名，-p后接的123456为该用户密码，注意不要有空格；dbName填写需要备份数据的数据库名称，大于号后接生成文件路径
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			boolean exitFlag = process.waitFor(10*60, TimeUnit.SECONDS);
			if(exitFlag) {
				int code = process.exitValue();
				log.info("code:"+code);
				if(code == 0){
					log.info("【恢复数据库】成功，SQL文件：{}", kmSysDbBackup.getFileName());
					return Result.OK("恢复数据库成功！");
				}else {
					return Result.error("命令执行失败，返回码：",code);
				}
			}else {
				return Result.error("备份超时");
			}
		}catch (Exception e){
			log.error("【恢复数据库】失败：{}", e.getMessage());
			return Result.error(e.getMessage());
		}
	}

}
