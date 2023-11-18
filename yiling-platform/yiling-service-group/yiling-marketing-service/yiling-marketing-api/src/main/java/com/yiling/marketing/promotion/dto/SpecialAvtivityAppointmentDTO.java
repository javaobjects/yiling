package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
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
public class SpecialAvtivityAppointmentDTO extends BaseDTO {

    /**
     * 已预约
     */
    private List<SpecialAvtivityAppointmentItemDTO> notStarted;

    /**
     * 已开始
     */
    private List<SpecialAvtivityAppointmentItemDTO> started;

    /**
     * 已结束
     */
    private List<SpecialAvtivityAppointmentItemDTO> ended;
}