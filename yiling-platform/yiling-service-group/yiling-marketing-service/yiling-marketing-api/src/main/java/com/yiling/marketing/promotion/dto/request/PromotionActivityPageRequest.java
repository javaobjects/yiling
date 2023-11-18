package com.yiling.marketing.promotion.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动主表新增请求参数
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionActivityPageRequest extends QueryPageListRequest {

    /**
     * 促销活动名称
     */
    private String name;

    /**
     * 活动类型（1-满赠）
     */
    private Integer type;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String createUserTel;

    /**
     * 费用承担方（1-平台；2-商家；3-分摊）
     */
    private Integer bear;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 备注
     */
    private String remark;
}
