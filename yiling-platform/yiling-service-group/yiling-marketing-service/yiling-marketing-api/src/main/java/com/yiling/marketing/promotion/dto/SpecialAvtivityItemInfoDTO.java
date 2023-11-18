package com.yiling.marketing.promotion.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动商品表
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialAvtivityItemInfoDTO extends BaseDTO {

    /**
     * 专场活动ID
     */
    private Long specialActivityId;

    /**
     * 专场活动企业表ID
     */
    private Long specialActivityEnterpriseId;

    /**
     * 专场活动名称
     */
    private String specialActivityName;

    /**
     * 专场活动开始时间-实际取的营销活动开始时间
     */
    private Date startTime;

    /**
     * 专场活动开始时间-实际取的营销活动开始时间
     */
    private Date endTime;

    /**
     * 营销活动id
     */
    private Long promotionActivityId;

    /**
     * 营销活动名称
     */
    private String promotionActivityName;

    /**
     * 专场活动图片
     */
    private String pic;

    /**
     * 是否预约过
     */
    private Boolean isAppointment;

    /**
     * 企业id
     */
    private Long eid;
}