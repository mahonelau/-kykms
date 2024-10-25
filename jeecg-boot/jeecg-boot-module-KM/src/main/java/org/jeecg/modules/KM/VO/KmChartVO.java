package org.jeecg.modules.KM.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="km_doc首页chart对象", description="km_doc")
public class KmChartVO implements Serializable {

    //分类：1
    List<KmDocStatisticsVO> categoryChartList;
    //标签:4
    List<KmDocStatisticsVO> businessChartList;
    //专题:5
    List<KmDocStatisticsVO> topicChartList;
    //热词
    List<String> hotKeywordList;
    //热门专题
    List<String> hotTopicList;
    //全局数据：知识数，存储空间
    KmDocSummaryVO kmDocSummaryVO;
}
