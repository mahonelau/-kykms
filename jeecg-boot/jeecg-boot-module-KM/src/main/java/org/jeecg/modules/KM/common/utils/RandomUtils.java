//package org.jeecg.modules.KM.common.utils;
//
//import cn.hutool.core.util.RandomUtil;
//import io.swagger.models.auth.In;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RandomUtils {
//
//    private final  static String[] keywords = {"中国","技术","系统","基础","运维","功夫","科亿信息技术","程序员","进口红酒","红酒","进口","中国人民","中国人民银行","全国","银行","人民银行","中国人","国人","员工","程序"};
//    private final  static String[] topicCodes = {"A01","A01B01","A01B02","A01B01C01","A01B01C02","A01B01C03","A01C01","A01C02","A01C03","A01C03D01","A01C03D02","B01","B01C03D01","B01C01D01","B01C03D02","B01A03D01","B01A03A01","B01C03D04","A03","B09"};
//
//    public static String[] generateRamdonTopicCodes(){
//        String topicCodeString[] = new String[5];
//        for(int i=0;i<5;i++){
//            topicCodeString[i]=topicCodes[RandomUtil.randomInt(0,20)];
//        }
//
//        return topicCodeString;
//    }
//
//    public static String generateRamdonKeywords(){
//        String keyWordString = "关键字";
//        int count = RandomUtil.randomInt(5,10);
//        for(int i=0;i<count;i++){
//            keyWordString = keyWordString + "," + keywords[RandomUtil.randomInt(0,20)];
//        }
//
//        return keyWordString;
//    }
//
//    public static Integer[] generateRandomIntArray(){
//        Integer[] resultArray = new Integer[10];
//        for(int i=0;i<10;i++){
//            Integer t = RandomUtil.randomInt(1,10);
//            resultArray[i] = t;
//        }
//        return resultArray;
//    }
//
//    public static String generateRandomIntString(){
//        List<Integer> intList = new ArrayList<Integer>();
//        for(int i=0;i<10;i++){
//            Integer t = RandomUtil.randomInt(1,10);
//            intList.add(t);
//        }
//
//        String a=  intList.toString();
//        return a.substring(1,a.length()-1);
//    }
//
//}
