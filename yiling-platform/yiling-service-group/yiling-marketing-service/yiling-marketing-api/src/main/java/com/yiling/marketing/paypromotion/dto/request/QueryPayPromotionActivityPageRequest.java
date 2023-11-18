package com.yiling.marketing.paypromotion.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPayPromotionActivityPageRequest extends QueryPageListRequest {

    /**
     * 活动名称
     */
    private String name;

    /**
     * 状态：0-全部 1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 活动进度 0-全部 1-未开始 2-进行中 3-已结束
     */
    private Integer progress;

    /**
     * 费用承担方（0全部 1-平台承担；2-商家承担；）
     */
    private Integer bear;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String createTel;

    /**
     * 创建开始时间
     */
    private Date startTime;

    /**
     * 创建截止时间
     */
    private Date stopTime;

    /**
     * 创建人id集合
     */
    private List<Long> creaters;
    /**
     * 运营备注
     */
    private String operatingRemark;

    /**
     * 活动名称id
     */
    private Long id;
}
