package org.jeecg.modules.KM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.io.FileUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.KM.common.config.BaseConfig;
import org.jeecg.modules.KM.common.utils.HashUtil;
import org.jeecg.modules.KM.common.utils.StringUtils;
import org.jeecg.modules.KM.entity.KmFile;
import org.jeecg.modules.KM.mapper.KmFileMapper;
import org.jeecg.modules.KM.service.IKmFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class KmFileServiceImpl extends ServiceImpl<KmFileMapper, KmFile> implements IKmFileService {

    @Autowired
    private BaseConfig baseConfig;
    private Logger logger= LoggerFactory.getLogger(KmFileServiceImpl.class);



    @Override
    public KmFile saveFile(MultipartFile file) {
        try {
            File todayDir=baseConfig.getTodayUploadDir();
            String fileId = UUIDGenerator.generate();
            String suffix= StringUtils.getFileSuffix(file.getOriginalFilename());
            File dist=new File(todayDir,fileId+"."+suffix);
            file.transferTo(dist);
            KmFile kmFile=new KmFile();
            kmFile.setId(fileId);
            kmFile.setOriginalName(file.getOriginalFilename());
            kmFile.setPhysicalPath(baseConfig.getRelativePath(dist));
            kmFile.setSha256(HashUtil.sha256(dist));
            baseMapper.insert(kmFile);
            return kmFile;
        } catch (IOException e) {
            logger.error("文件上传transferTo错误",e);
        }
        return null ;
    }

    @Override
    public KmFile saveFile(String content,String suffix) {
        try {
            File todayDir=baseConfig.getTodayUploadDir();
            String fileId = UUIDGenerator.generate();
            //String suffix=StringUtils.getFileSuffix(file.getOriginalFilename());
            File dist=new File(todayDir,fileId+"."+suffix);
            FileUtils.writeStringToFile(dist,content,"utf-8");
            KmFile KmFile=new KmFile();
            KmFile.setId(fileId);
            KmFile.setOriginalName("");
            KmFile.setPhysicalPath(baseConfig.getRelativePath(dist));
            //KmFile.setUrl(""); //下载路径,待定
            KmFile.setSha256(HashUtil.sha256(dist));
            baseMapper.insert(KmFile);
            return KmFile;
        } catch (IOException e) {
            logger.error("文件上传transferTo错误",e);
        }
        return null ;
    }

    @Override
    public KmFile saveFileInfoToDB(String physicalPath,String fileName){
        try {
            String fileId = UUIDGenerator.generate();
            KmFile KmFile=new KmFile();
            KmFile.setId(fileId);
            KmFile.setOriginalName(fileName);
            KmFile.setPhysicalPath(baseConfig.getRelativePath(physicalPath));
            baseMapper.insert(KmFile);
            return KmFile;
        } catch (Exception e) {
            logger.error("文件保存到db错误",e);
        }
        return null ;

    }

    @Override
    public KmFile getKmFileBySha256(String sha256) {
//        EntityWrapper<KmFile>ew=new EntityWrapper<>();
        QueryWrapper<KmFile> ew = new QueryWrapper<>();
        ew.eq("sha256",sha256);
        List<KmFile> list = baseMapper.selectList(ew);
        if(list.isEmpty()){
            return null;
        }else {
            return list.get(0);
        }
    }




    @Override
    public KmFile getKmFile(String fileId) {
        return baseMapper.selectById(fileId);
    }

    @Override
    public String readKmFileString(String fileId) {
        KmFile KmFile = getKmFile(fileId);
        File file = new File(KmFile.getPhysicalPath());
        if(file.exists()){
            try {
                return FileUtils.readFileToString(file,"utf-8");
            } catch (IOException e) {
                logger.error("readKmFileString",e);
                return null;
            }
        }else {
            return null;
        }
    }

    @Override
    public boolean deleteKmFile(String fileId) {
        KmFile KmFile=getKmFile(fileId);
        if(KmFile!=null){
            String filePath=KmFile.getPhysicalPath();
            File f=new File(baseConfig.getUploadDir(),filePath);
            if(f.exists()){
                f.delete();
            }
            super.removeById(fileId);
            return true;
        }else {
            return false;
        }
    }


    private String getNameByPath(String path){
        File f =new File(path);
        return f.getName();
    }
    @Override
    public boolean exist(String fileId){
        if(fileId==null){
            return false;
        }
        QueryWrapper<KmFile> ew = new QueryWrapper<>();
//        EntityWrapper<KmFile>ew=new EntityWrapper<>();
        ew.eq("file_id",fileId);
        return this.count(ew)>0;
    }

}
