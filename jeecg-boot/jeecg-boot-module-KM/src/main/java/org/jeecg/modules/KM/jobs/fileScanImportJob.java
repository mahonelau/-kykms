package org.jeecg.modules.KM.jobs;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.KM.VO.KmDocParamVO;
import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.service.IKmDocService;
import org.jeecg.modules.KM.service.IKmSysConfigService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class fileScanImportJob implements Job {

    @Autowired
    private IKmSysConfigService sysConfigService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private IKmDocService docService;

    private Map<String,String> categoryMap = new HashMap<>();

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        log.info("{} - 扫描外部文件启动...", DateTime.now());
        String externalFileFolder = sysConfigService.getSysConfigValue(KMConstant.JobAutoScanFileBasePath);
        if (oConvertUtils.isEmpty(externalFileFolder) ) {
            log.error("扫描导入文件路径启动，但未配置扫描路径...");
            return;
        }
        File baseFolder = new File(externalFileFolder);
        if (!baseFolder.exists() || !baseFolder.isDirectory()) {
            log.error("配置的基础扫描路径错误...");
            return;
        }
        File[] listFiles = baseFolder.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            log.info("扫描外部文件，根目录为空");
            return;
        }
        initCategoryMap();
        for (File listFile : listFiles) {
            if (listFile.isFile()) {
                KmDocParamVO kmDocParamVO = new KmDocParamVO();
                Result<?> result = docService.importExternalFile(listFile, kmDocParamVO);
            }else {
                scanAndImportFolder(listFile,listFile.getPath());
            }
        }

    }

    private void scanAndImportFolder(File dir,String categoryPath){
        File[] listFiles = dir.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return;
        }else {
            for (File file : listFiles) {
                if (file.isFile()) {
                    KmDocParamVO kmDocParamVO = new KmDocParamVO();
                    kmDocParamVO.setCategory(categoryMap.get(categoryPath));
                    docService.importExternalFile(file,kmDocParamVO);
                }else {
                    scanAndImportFolder(file,categoryPath);
                }
            }
        }
    }

    private void initCategoryMap(){
        List<DictModel> dictModelList = sysBaseAPI.queryDictItemList("km_dict_category");
        if (oConvertUtils.isNotEmpty(dictModelList) && dictModelList.size()>0) {
            for (DictModel dictModel : dictModelList) {
                if (!categoryMap.containsKey(dictModel.getText())) {
                    categoryMap.put(dictModel.getText(),dictModel.getValue());
                }
            }
        }

    }

}
