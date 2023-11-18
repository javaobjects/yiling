package com.yiling.ih.user.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动医生列表
 *
 * @author: fan.shen
 * @data: 2023/02/01
 */
@Data
@Accessors(chain = true)
public class HmcActivityDocDTO extends BaseDTO {

    private static final long serialVersionUID = 6232095217976779033L;
    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 医生姓名
     */
    private String name;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 所属省
     */
    private String provinceName;

    /**
     * 报道总人数
     */
    private Long reportCount;

    /**
     * 新用户报道人数
     */
    private Long newReportCount;

    /**
     * 处方审核通过人数
     */
    private Long prescriptionCount;

    /**
     * 活动码
     */
    private String qrcodeUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否拥有活动资格 1-正常 2-取消
     */
    private Integer doctorStatus;

}
