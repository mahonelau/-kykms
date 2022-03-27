package org.jeecg.modules.KM.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Title: OfficeUtils.java
 *
 * @author zxc
 * @time 2018/6/29 下午5:20
 */
@Slf4j
public class OfficeUtils {

//    private static Logger log = LoggerFactory.getLogger(OfficeUtils.class);

    public static boolean convertPdf2(String sofficePath,File srcFile, File targetDir){
        ///Applications/LibreOffice.app/Contents/MacOS/soffice
        // --headless --invisible --convert-to
        // html --outdir pdf-html/ *.pdf

        Runtime runtime =Runtime.getRuntime();
        StringBuilder command = new StringBuilder();
        command.append(sofficePath);
        String outputDir = targetDir.getAbsolutePath();
        command.append(" --headless --invisible --convert-to pdf --outdir ");
        command.append(outputDir);
        command.append(" ");
        command.append(srcFile.getAbsolutePath());
        log.info("开始转换pdf,命令行是:{}",command.toString());
        try {

            ProcessBuilder pb = new ProcessBuilder(command.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();
            printStream(process.getInputStream());
            //Process process = runtime.exec(command.toString());
//            printStream(process.getInputStream());
//            printStream(process.getErrorStream());
            int code = process.waitFor();
            log.info("code:{}",code);

            //String result = IOUtils.toString(process.getInputStream(),"utf-8");
            //String errorResult = IOUtils.toString(process.getErrorStream(),"utf-8");
            //log.info("convertPdf from {} output {}:\n result:{}\n",srcFile.getAbsolutePath(),outputDir,result);
//            if(StringUtil.isNotEmpty(errorResult)){
//                log.error("convertPdf from {} output {}:\n errorResult:{}\n",srcFile.getAbsolutePath(),outputDir,errorResult);
//            }
            File targetFile  = new File(targetDir,StringUtils.getFileNameWithoutSuffix(srcFile.getName())+".pdf");
            log.info("目标文件是:{}",targetFile.getAbsolutePath());
            if(targetFile.exists()){
                log.info("文档转化成功");
                return true;
            }else {
                log.info("文档转化失败");
                return false;
            }

//            if(code==0){
//                log.info("文档转化成功");
//                return true;
//            }else {
//                log.info("文档转化失败");
//                return false;
//            }
        } catch (IOException e) {
            log.error("convertPdf",e);
        } catch (InterruptedException e) {
            log.error("convertPdf",e);
        }
        return false;
    }

    public static boolean convertPdf(String sofficePath,File srcFile, File targetDir){
        ///Applications/LibreOffice.app/Contents/MacOS/soffice
        // --headless --invisible --convert-to
        // html --outdir pdf-html/ *.pdf
        //Thread.sleep();
        List<String>commands=new ArrayList<>();
        String outputDir = targetDir.getAbsolutePath();
        commands.add(sofficePath);
        commands.add("--headless");
        commands.add("--invisible");
        commands.add("--convert-to");
        commands.add("pdf");
        commands.add("--outdir");
        commands.add(outputDir);
        commands.add(srcFile.getAbsolutePath());
        log.info("开始转换pdf,命令行是:{}", Arrays.toString(commands.toArray()));
        try {

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String result = IOUtils.toString(process.getInputStream(),"utf-8");
            log.info("convertPdf from {} output {}:\n result:{}\n",srcFile.getAbsolutePath(),outputDir,result);
            //printStream(process.getInputStream());
            //Process process = runtime.exec(command.toString());
//            printStream(process.getInputStream());
//            printStream(process.getErrorStream());
            //int code = process.waitFor();
            //log.info("code:{}",code);
            process.destroy();

            //String result = IOUtils.toString(process.getInputStream(),"utf-8");
            //String errorResult = IOUtils.toString(process.getErrorStream(),"utf-8");
            //log.info("convertPdf from {} output {}:\n result:{}\n",srcFile.getAbsolutePath(),outputDir,result);
//            if(StringUtil.isNotEmpty(errorResult)){
//                log.error("convertPdf from {} output {}:\n errorResult:{}\n",srcFile.getAbsolutePath(),outputDir,errorResult);
//            }
            File targetFile  = new File(targetDir,StringUtils.getFileNameWithoutSuffix(srcFile.getName())+".pdf");
            log.info("目标文件是:{}",targetFile.getAbsolutePath());
            if(targetFile.exists()){
                log.info("文档转化成功");
                return true;
            }else {
                log.info("文档转化失败");
                return false;
            }

//            if(code==0){
//                log.info("文档转化成功");
//                return true;
//            }else {
//                log.info("文档转化失败");
//                return false;
//            }
        } catch (IOException e) {
            log.error("convertPdf",e);
        }
        return false;
    }

    public static void printStream(InputStream inputStream){
        new Thread(){
            @Override
            public void run() {
                log.info("开始读取流,thread:{}",currentThread().getId());
                try {
                    String s = IOUtils.toString(inputStream,"utf-8");
                    log.info("流内容:{}",s);
                } catch (IOException e) {
                    log.error("读取流异常",e);
                }
                log.info("结束读取流,thread:{}",currentThread().getId());
            }
        }.start();
    }


}
