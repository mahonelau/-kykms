package org.jeecg.modules.KM.service;

import org.jeecg.modules.KM.entity.KmFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IKmFileService extends IService<KmFile> {

    KmFile saveFile(MultipartFile file);

    KmFile saveFile(String content, String suffix);

    KmFile saveFileInfoToDB(String physicalPath,String fileName);

    KmFile getKmFileBySha256(String sha256);

    KmFile getKmFile(String fileId);

    String readKmFileString(String fileId);

    boolean deleteKmFile(String fileId);

    boolean exist(String fileId);
}
