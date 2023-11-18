package com.yiling.sjms.gb.dto;

import java.util.Date;

import com.yiling.sjms.form.dto.FormDTO;

import lombok.Data;

@Data
public class GbFormInfoDTO extends FormDTO {
    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 来源团购编号（操作类型为取消时）
     */
    private String srcGbNo;

    /**
     * 业务类型：1-提报 2-取消 11-费用申请
     */
    private Integer bizType;

    /**
     * 是否关联取消: 1-否 2-是
     */
    private Integer cancelFlag;

    /**
     * 团购ID
     */
    private Long gbId;

    /**
     * 是否复核: 1-否 2-是
     */
    private Integer reviewStatus;

    /**
     * 复核意见
     */
    private String reviewReply;


    /**
     * 复核时间
     */
    private Date reviewTime;

    /**
     * 团购费用申请原因
     */
    private String feeApplicationReply;
}
