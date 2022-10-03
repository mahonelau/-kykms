package org.jeecg.modules.KM.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * Title: OfficeUtils.java
 *
 * @author lwx
 * @time 2018/6/29 下午5:20
 */
@Slf4j
public class OfficeUtils {

    private static void formatExcel(String srcFile){
        String fileExt = StringUtils.getFileSuffix(srcFile);
        if ( fileExt != null ) {
            try {
                if(fileExt.equals("xlsx")){
                    XSSFWorkbook  wb = new XSSFWorkbook(Files.newInputStream(Paths.get(srcFile)));

                    for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                        XSSFSheet sheet = wb.getSheetAt(i);
                        //打印设置
                        XSSFPrintSetup print = sheet.getPrintSetup();
                        print.setLandscape(true); // 打印方向，true：横向，false：纵向(默认)
                        print.setFitHeight((short)0);//设置高度为自动分页
                        print.setFitWidth((short)1);//设置宽度为一页
                        print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //纸张类型
//                print.setScale((short)55);//自定义缩放①，此处100为无缩放
                        //启用“适合页面”打印选项的标志
                        sheet.setFitToPage(true);
                    }
                    File file = new File(srcFile);
                    OutputStream fos = new FileOutputStream(file);
                    wb.write(fos);
                }else if(fileExt.equals("xls")){
                    POIFSFileSystem fs = new POIFSFileSystem(Files.newInputStream(Paths.get(srcFile)));
                    HSSFWorkbook wb = new HSSFWorkbook(fs);

                    for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                        Sheet sheet = wb.getSheetAt(i);
                        //打印设置
                        PrintSetup print = sheet.getPrintSetup();
                        print.setLandscape(true); // 打印方向，true：横向，false：纵向(默认)
                        print.setFitHeight((short)0);//设置高度为自动分页
                        print.setFitWidth((short)1);//设置宽度为一页
                        print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //纸张类型
                        //启用“适合页面”打印选项的标志
                        sheet.setFitToPage(true);
                    }
                    File file = new File(srcFile);
                    OutputStream fos = new FileOutputStream(file);
                    wb.write(fos);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean convertPdf(String sofficePath,File srcFile, File targetDir){
        //设置excel文件格式为不换行折断
        formatExcel(srcFile.getAbsolutePath());
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
            boolean exitFlag = process.waitFor(10, TimeUnit.SECONDS);
            if(exitFlag) {
                int code = process.exitValue();

                log.info("convertPdf from {} output: {}\n result:{}\n exitValue:{}\n", srcFile.getAbsolutePath(), outputDir, result, code);

                process.destroy();

                File targetFile = new File(targetDir, StringUtils.getFileNameWithoutSuffix(srcFile.getName()) + ".pdf");
                if (targetFile.exists()) {
                    log.info("文档转化成功,{},目标文件是:{}", srcFile.getAbsolutePath(), targetFile.getAbsolutePath());
                    return true;
                } else {
                    log.info("文档转化失败,{},目标文件是:{}", srcFile.getAbsolutePath(), targetFile.getAbsolutePath());
                    return false;
                }
            }
            else {
                log.info("等待命令退出失败");
                return false;
            }
        } catch (IOException e) {
            log.error("convertPdf IOException:",e);
        }catch (InterruptedException e) {
            log.error("convertPdf InterruptedException:",e);
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
