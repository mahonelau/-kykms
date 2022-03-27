package org.jeecg.modules.KM.VO;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

@Data
@ToString
public class KmDocEsVO implements Serializable {

    //private String indexId;  //todo:考虑是否改名，跟es的_id字段一致
    private String docId;
    private String fileNo;

    /*
    状态：0-下架 1-有效
     */
    private Integer releaseFlag;
    /*
    状态：0-内部公开 1-对外公开
     */
    private Integer publicFlag;

    private String title;

    private String category;

    private String source;

    private String pubTime;

    private String pubTimeTxt;

    private String[] versions;

    private String[] businessTypes;

    private String[] keywords;

	private String[] topicCodes;

	private String content;
}
