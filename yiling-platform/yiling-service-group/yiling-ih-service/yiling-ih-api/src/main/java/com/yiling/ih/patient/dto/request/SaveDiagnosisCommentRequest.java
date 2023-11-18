package com.yiling.ih.patient.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 保存诊后评价
 *
 * @author: fan.shen
 * @date: 2023/4/7
 */
@Data
@Accessors(chain = true)
public class SaveDiagnosisCommentRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * 用户id
     */
    private Integer fromUserId;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 整体满意度_星级
     */
    private Integer starLevel;

    /**
     * 整体满意度_星级满意说明
     */
    private String starLevelExplan;

    /**
     * 用户评价描述
     */
    private String userDescribe;

    /**
     * 选中标签项
     */
    private String selectLabelItem;

}