
package org.jeecg.modules.KM.common.config;

import org.jeecg.modules.KM.common.rules.KMConstant;
import org.jeecg.modules.KM.common.utils.KMDateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;

@Configuration
@ConfigurationProperties(prefix = "base")
@EnableConfigurationProperties(BaseConfig.class)
public class BaseConfig {

    @PostConstruct
    public void init(){
        File file=new File(uploadDir);
        if(!file.exists()){
            file.mkdirs();
        }
    }


    private String uploadDir;
    private String sofficePath;
    private String oneSystemUrl;
    private String oneMapUrl;

    public String getOneSystemUrl() {
        return oneSystemUrl;
    }

    public void setOneSystemUrl(String oneSystemUrl) {
        this.oneSystemUrl = oneSystemUrl;
    }

    public String getOneMapUrl() {
        return oneMapUrl;
    }

    public void setOneMapUrl(String oneMapUrl) {
        this.oneMapUrl = oneMapUrl;
    }

    public String getSofficePath() {
        return sofficePath;
    }

    public void setSofficePath(String sofficePath) {
        this.sofficePath = sofficePath;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }


    public File getTodayUploadDir(){
        String today= KMDateUtils.formatDate1(new Date());
        File t=new File(uploadDir,today);
        if(!t.exists()){
            t.mkdirs();
        }
        return t;
    }

    public String getRelativePath(File file){
        String absPath = file.getAbsolutePath();
//        String a =absPath.substring(0,uploadDir.length());
        if(absPath.isEmpty() || absPath.length()<uploadDir.length() || !absPath.substring(0,uploadDir.length()).equals(uploadDir))
            return null;
        return absPath.substring(uploadDir.length());
    }

    public String getRelativePath(String filePath){
        if(filePath.isEmpty() || filePath.length()<uploadDir.length() || !filePath.substring(0,uploadDir.length()).equals(uploadDir))
            return null;
        return filePath.substring(uploadDir.length());
    }

    public File getFile(String relativePath){
        String absPath;
        if(uploadDir != "/") {
            absPath = uploadDir + KMConstant.fileSeparator + relativePath;
        }
        else{
            absPath = uploadDir  + relativePath;
        }
        File t = new File(absPath);
        if(t.exists())
            return  t;
        else
            return null;
    }

    public String  getFilePath(String relativePath){
        String absPath;
        if(uploadDir != "/") {
            absPath = uploadDir + KMConstant.fileSeparator + relativePath;
        }
        else{
            absPath = uploadDir  + relativePath;
        }
        return absPath;
    }


}
