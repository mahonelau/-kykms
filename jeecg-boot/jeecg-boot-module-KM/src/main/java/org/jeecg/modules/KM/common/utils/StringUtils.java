package org.jeecg.modules.KM.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StringUtils {
    public static String byteToStringUTF8(byte[]bytes){
        try {
            return new String (bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] stringToByteUTF8(String s){
        try {
            return s.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileSuffix(String fileName){
        int pos=fileName.lastIndexOf(".");
        if(pos>=0){
            if(pos+1!=fileName.length()){
                return fileName.substring(pos+1,fileName.length()).toLowerCase();
            }
        }
        return "";
    }

    public static String getFileNameWithoutSuffix(String fileName){
        int pos=fileName.lastIndexOf(".");
        if(pos>=0){
            if(pos+1!=fileName.length()){
                return fileName.substring(0,pos);
            }
        }
        return "";
    }


    public  static List<String> splitStrListToList(List<String> sourceList){
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            resultList.addAll(splitStringToList(sourceList.get(i)));
        }

        return resultList;
    }

    //更换其他字符或空格为","，最后得到string数组
    public static List<String> splitStringToList(String source){
        List<String> resultList = new ArrayList<>();
        List<String> tmptList = new ArrayList<>();
        //String regExpress = "-~!@#%&_`=}:\"<>]\\\\;\'/\\$\\(\\)\\*\\+\\.\\[\\?\\\\\\^\\{\\|";
        String regExpress = "[<~!@#%&_`=}:\">\\;'/\\$\\(\\)\\*\\+\\.\\[\\?\\\\\\^\\{\\|\\-\\]]";
        if(source !=null && source.length()>0){
            source = source.replaceAll(regExpress,",")
                    .replace(" ",",");
            tmptList = Arrays.asList(source.split(","));
            tmptList.forEach((e)->{
                if(!e.isEmpty()){
                    resultList.add(e);
                }
            });
        }
        return resultList;
    }

    //合并字符list成为字符串，用分隔符","
    public static String concatListToString(List<String> stringList){
        String result = "";
        if(stringList != null & stringList.size()>0) {
            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i) != null && stringList.get(i).length()>0)
                    result = result + stringList.get(i) + ",";
            }
            if(result.length()>0)
                result = result.substring(0,result.length()-1);
        }
        return  result;
    }
}
