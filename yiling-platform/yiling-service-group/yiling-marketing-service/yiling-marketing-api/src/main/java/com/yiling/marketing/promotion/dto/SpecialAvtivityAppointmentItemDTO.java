package com.yiling.marketing.promotion.dto;

import java.util.Date;

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
public class SpecialAvtivityAppointmentItemDTO extends BaseDTO {

    private static final long                serialVersionUID = 1L;


    /**
     * 专场活动ID
     */
    private Long specialActivityId;

    /**
     * 专场活动ID
     */
    private Long promotionActivityId;

    /**
     * 专场活动名称
     */
    private String specialActivityName;

    /**
     * 专场活动图片
     */
    private String pic;

    /**
     * 营销活动名称
     */
    private String promotionName;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 专场活动企业表id
     */
    private Long specialActivityEnterpriseId;

    /**
     * eid
     */
    private Long eid;

    /**
     * 活动类型
     */
    private Integer type;

    /**
     * 总预约个数
     */
    private Integer count;
}