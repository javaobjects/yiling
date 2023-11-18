package com.yiling.hmc.remind.form;

import cn.hutool.core.collection.CollUtil;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.hmc.remind.enums.HmcMedsRemindUseDaysEnum;
import com.yiling.hmc.remind.enums.HmcMedsRemindUseTimesEnum;
import com.yiling.hmc.remind.enums.HmcRemindErrorCode;
import com.yiling.hmc.remind.enums.HmcRemindTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 保存用药提醒入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/5/31
 */
@Data
@ToString
@ApiModel(value = "SaveMedsRemindForm", description = "保存用药提醒入参")
@Slf4j
public class SaveMedsRemindForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 售卖规格id
     */
    @ApiModelProperty(value = "售卖规格id")
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    @NotEmpty
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 单次用量（例如：3、1）
     */
    @NotEmpty
    @ApiModelProperty(value = "单次用量（例如：1、3）")
    private String useAmount;

    /**
     * 用法用量单位（例如：粒、袋）
     */
    @NotEmpty
    @ApiModelProperty(value = "用法用量单位（例如：粒、袋）")
    private String useAmountUnit;

    /**
     * 用药次数
     */
    @NotNull
    @ApiModelProperty(value = "用药次数")
    private Integer useTimesType;

    /**
     * 用药天数
     */
    @NotNull
    @ApiModelProperty(value = "用药天数")
    private Integer useDaysType;

    /**
     * 提醒时间
     */
    @NotNull
    @ApiModelProperty(value = "提醒时间")
    private List<String> medsRemindTimeList;

    public void check() {
        HmcMedsRemindUseTimesEnum useTimesEnum = HmcMedsRemindUseTimesEnum.getByCode(useTimesType);

        if (Objects.isNull(useTimesEnum)) {
            throw new BusinessException(HmcRemindErrorCode.PARAM_MISS_USE_TIMES_TYPE);
        }

        if (CollUtil.isEmpty(medsRemindTimeList)) {
            throw new BusinessException(HmcRemindErrorCode.PARAM_MISS_REMIND_TIMES);
        }

        Integer times = useTimesEnum.getTimes();
        if (HmcRemindTypeEnum.DAY.getType().equals(useTimesEnum.getType()) && this.medsRemindTimeList.size() != times) {
            throw new BusinessException(HmcRemindErrorCode.PARAM_ERROR_REMIND_TIMES);
        }

        HmcMedsRemindUseDaysEnum useDaysEnum = HmcMedsRemindUseDaysEnum.getByCode(useDaysType);

        if (Objects.isNull(useDaysEnum)) {
            throw new BusinessException(HmcRemindErrorCode.PARAM_ERROR_REMIND_DAYS);
        }

    }


}
