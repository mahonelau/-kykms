package org.jeecg.modules.KM.common.rules;
import org.jeecg.modules.KM.common.OSinfo;
import org.jeecg.modules.KM.service.IKmSysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KMConstant {
    @Autowired
    private IKmSysConfigService kmSysConfigService;

    public static String SearchTermSeprator = " + ";
    public static char fileSeparator = '/';
    public static String UserDocVisitHistoryPrefix = "UserDocVisitHistoryPrefix_";
    public static String UserDownloadLimitPrefix = "UserDownloadLimitPerDay_";
    public static String UserSearchLimitPrefix = "UserSearchLimitPerTenSeconds_";
    public static String UserViewLimitPrefix = "UserViewLimitPerTenSeconds_";
    public static String DocIndexName = "km_doc";
    public static String DocIndexAliasName = "km_doc_alias";
    public static String KMSearchRecordIndexName = "log_km_search_record";
    public static String KMSearchRecordIndexAliasName = "log_km_search_record_alias";
    public static String DocVisitIndexName = "log_km_doc_visit_record";
    public static String DocVisitIndexAliasName = "log_km_doc_visit_record_alias";
    public static Integer SearchTimeOutSeconds = 10;     //秒
    public static Integer SaveTimeOutHours = 1;     //1小时
    public static Integer SaveTimeOutMinutes = 1;     //1分钟
    public static String InnerUser = "1";
    public static String PublicUser = "0";
    public static Integer DocPublic = 1;
    public static Integer DocNotPublic = 0;
    public static Integer AllowDownload = 1;
    public static Integer ForbideDownload = 0;


    private Float TitleSearchBoost = Float.valueOf("1");
    private Float ContentSearchBoost =  Float.valueOf("1");
    private Float KeywordSearchBoost =  Float.valueOf("1");
    private Integer DownloadLimit =  1000;
    private Integer SearchLimit = 10;
    private Integer ViewLimit = 10;

    public Float getTitleSearchBoost() {
        String titleSearchBoostConfig = kmSysConfigService.getSysConfigValue("TitleSearchBoostConfig");
        if( titleSearchBoostConfig!= null && !titleSearchBoostConfig.isEmpty())
            TitleSearchBoost = Float.valueOf(titleSearchBoostConfig);
        return TitleSearchBoost;
    }

    public Float getContentSearchBoost() {
        String contentSearchBoostConfig = kmSysConfigService.getSysConfigValue("ContentSearchBoostConfig");
        if( contentSearchBoostConfig!= null && !contentSearchBoostConfig.isEmpty())
            ContentSearchBoost = Float.valueOf(contentSearchBoostConfig);
        return ContentSearchBoost;
    }

    public Float getKeywordSearchBoost() {
        String keywordSearchBoostConfig = kmSysConfigService.getSysConfigValue("KeywordSearchBoostConfig");
        if( keywordSearchBoostConfig!= null && !keywordSearchBoostConfig.isEmpty())
            KeywordSearchBoost = Float.valueOf(keywordSearchBoostConfig);
        return KeywordSearchBoost;
    }

    public Integer getDownloadLimit() {
        String downloadLimitConfig = kmSysConfigService.getSysConfigValue("downloadLimitConfig");
        if( downloadLimitConfig!= null && !downloadLimitConfig.isEmpty())
            DownloadLimit = Integer.valueOf(downloadLimitConfig);
        return DownloadLimit;
    }


    public Integer getSearchLimit() {
        String searchLimitConfig = kmSysConfigService.getSysConfigValue("searchLimitConfig");
        if( searchLimitConfig!= null && !searchLimitConfig.isEmpty())
            SearchLimit = Integer.valueOf(searchLimitConfig);
        return SearchLimit;
    }

    public Integer getViewLimit() {
        String viewLimitConfig = kmSysConfigService.getSysConfigValue("viewLimitConfig");
        if( viewLimitConfig!= null && !viewLimitConfig.isEmpty())
            ViewLimit = Integer.valueOf(viewLimitConfig);
        return ViewLimit;
    }

    static {
        if(OSinfo.isWindows()) fileSeparator = '\\';
        if(OSinfo.isLinux()) fileSeparator = '/';
    }

    public  boolean isFileTypeSupport(String suffix){
        String fileTypes = kmSysConfigService.getSysConfigValue("supportFileTypes");
        if(fileTypes!=null && !fileTypes.isEmpty() ){
            for (String s : fileTypes.split(",")) {
                if(suffix.toLowerCase().equals(s))
                    return true;
            }
        }
        else{
            return  true;
        }
        return false;
    }

    public  boolean isIndexFileType(String suffix){
        String fileTypes = kmSysConfigService.getSysConfigValue("IndexFileTypes");
        if(fileTypes!=null && !fileTypes.isEmpty() ){
            for (String s : fileTypes.split(",")) {
                if(suffix.toLowerCase().equals(s))
                    return true;
            }
        }
        return false;
    }

    public  boolean isConvertFileType(String suffix){
        String fileTypes = kmSysConfigService.getSysConfigValue("ConvertFileTypes");
        if(fileTypes!=null && !fileTypes.isEmpty() ){
            for (String s : fileTypes.split(",")) {
                if(suffix.toLowerCase().equals(s))
                    return true;
            }
        }
        return false;
    }


}
