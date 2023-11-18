package com.yiling.marketing.presale.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleActivityBuyRecorderDTO extends BaseDTO {

    /**
     * 预售活动id
     */
    private Long presaleActivityId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 生效开始时间
     */
    private Integer countNum;

    /**
     * 购买者id
     */
    private Long eid;


}
