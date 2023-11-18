package com.yiling.marketing.promotion.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动列表查询
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SpecialActivityEnterpriseDTO extends BaseDTO {
    /**
     * 专场活动id
     */
    private Long specialActivityId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 营销活动id
     */
    private Long promotionActivityId;

    /**
     * 营销活动名称
     */
    private String promotionActivityName;

    /**
     * 营销活动开始时间
     */
    private Date startTime;

    /**
     * 营销活动结束时间
     */
    private Date endTime;

    /**
     * 专场活动图片
     */
    private String pic;

    /**
     * 是否已经添加过
     */
    private Boolean isUsed;
}
