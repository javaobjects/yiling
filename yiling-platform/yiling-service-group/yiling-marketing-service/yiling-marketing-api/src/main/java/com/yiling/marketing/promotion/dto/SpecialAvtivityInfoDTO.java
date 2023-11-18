package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
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
public class SpecialAvtivityInfoDTO extends BaseDTO {

    /**
     * 活动数量
     */
    private Integer size;

    /**
     * 起配数量
     */
    private BigDecimal minSize;

    /**
     * 是否建材 true false
     */
    private Boolean contract;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 商户下面的专场活动信息
     */
    private List<SpecialAvtivityItemInfoDTO> activityInfo;
}