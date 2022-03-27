//package org.jeecg.modules.KM.common.utils;
//
//import org.apache.shiro.SecurityUtils;
//import org.jeecg.common.system.vo.LoginUser;
//import org.jeecg.common.util.DateUtils;
//import org.jeecg.modules.KM.common.rules.KMConstant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//import java.util.Calendar;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//
//@Component
//public class KMRedisUtils {
//
//    @Autowired
//    private StringRedisTemplate  stringRedisTemplate;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    //检查时间内下载次数是否超限制
//    public boolean validUserLimit(String limitType, Integer limitAmount,Integer calendarUnit,Integer amount) throws ParseException {
//        resetUserLimit(limitType,calendarUnit,amount);
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        if(sysUser == null)
//            return false;
//
//        String userId = sysUser.getUsername();
//        String userKey = limitType + userId;
//        return  !redisTemplate.hasKey(userKey)
//                || redisTemplate.opsForList().size(userKey) < limitAmount;
//    }
//
//    //重设下载次数缓存，从左到右的时间升序列表
//    private void resetUserLimit(String limitType, Integer timeUnit, Integer amount) throws ParseException {
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        if(sysUser == null)
//            return;
//
//        String userId = sysUser.getUsername();
//        String userKey = limitType + userId;
//        if(redisTemplate.hasKey(userKey)
//                && redisTemplate.opsForList().size(userKey) >0){
//            //清除掉xx时间以前的记录
//            Calendar startDate = DateUtils.getCalendar();
//            startDate.add(timeUnit,-1 * amount);
//
//            Calendar downloadDate = DateUtils.parseCalendar(
//                    redisTemplate
//                            .opsForList()
//                            .range(userKey,0,1)
//                            .get(0)
//                            .toString() ,"yyyyMMddHHmmss");
//            while(DateUtils.dateDiff('s',downloadDate,startDate) < 0
//                    && redisTemplate.opsForList().size(userKey) > 0){
//                redisTemplate.opsForList().leftPop(userKey);
//                downloadDate = DateUtils.parseCalendar(
//                        redisTemplate
//                                .opsForList()
//                                .range(userKey,0,1)
//                                .get(0)
//                                .toString() ,"yyyyMMddHHmmss");
//            }
//        }
//    }
//
//    //在右边push当前时间，并设置失效时间
//    public  void refreshUserLimit(String limitType, TimeUnit unit, Integer amount){
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        if(sysUser == null)
//            return;
//
//        String userId = sysUser.getUsername();
//        String userKey =limitType + userId;
//        redisTemplate.opsForList()
//                .rightPush(userKey, DateUtils.formatDate("yyyyMMddHHmmss"));
//        //设置一天后过期
//        redisTemplate.opsForList()
//                .getOperations()
//                .expire(userKey,amount, unit);
//    }
//
//    //设置用户访问历史文档，保留最近十个
//    public void logPersonalDocHistory(String userId,String docId){
//        String userKey = KMConstant.UserDocVisitHistoryPrefix + userId;
//        if(stringRedisTemplate.hasKey(userKey)){
////            计数参数以下列方式影响操作：
////            count> 0：删除等于从头到尾移动的值的元素。
////            count <0：删除等于从尾到头移动的值的元素。
////            count = 0：删除等于value的所有元素。
//            stringRedisTemplate.opsForList().remove(userKey,0,docId);
//            if(stringRedisTemplate.opsForList().size(userKey) >= 10){
//                stringRedisTemplate.opsForList().rightPop(userKey);
//            }
//        }
//        stringRedisTemplate.opsForList().leftPush(userKey,docId);
//    }
//
//    //获取用户最近访问的档案
//    public List<String>  getPersonalDocHistory(String userId){
//        String userKey = KMConstant.UserDocVisitHistoryPrefix + userId;
//        List<String> result = stringRedisTemplate
//                .opsForList()
//                .range(userKey,0,-1);
//        return result;
//    }
//
//}
