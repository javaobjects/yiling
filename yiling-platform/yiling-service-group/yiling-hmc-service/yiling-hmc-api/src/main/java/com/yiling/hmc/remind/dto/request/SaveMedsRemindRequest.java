package com.yiling.hmc.remind.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 保存用药提醒入参
 * @author: fan.shen
 * @date: 2022/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMedsRemindRequest extends BaseRequest {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * id
     */
    private Long id;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 单次用量（例如：3粒、1袋）
     */
    private String useAmount;

    /**
     * 用法用量单位（例如：粒、袋）
     */
    private String useAmountUnit;

    /**
     * 用药次数
     */
    private Integer useTimesType;

    /**
     * 用药天数
     */
    private Integer useDaysType;

    /**
     * 提醒时间
     */
    private List<String> medsRemindTimeList;


}