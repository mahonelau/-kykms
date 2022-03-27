package org.jeecg.modules.KM.VO;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class KmDocEsParamVO implements Serializable {

    //高级检索标识  2-全文内容匹配度，时需要检索的目标文档id
//    private String indexIdFTI;

    //高级检索标识 0-普通 1-高级
    private Integer advSearchFlag;

    private Integer withinSearchFlag;

    private String title;

    private String fileNo;

    private List<String> category;

    private List<String> source;

    private String pubTimeStart;

    private String pubTimeEnd;

    private List<String> versions;

    private List<String> businessTypes;

    private List<String> keywords;

	private List<String> topicCodes;

	private String content;

	//结果排序字段
	private String column;
	private String order;
}
