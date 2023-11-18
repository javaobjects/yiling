package com.yiling.b2b.admin.coupon.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityEnterpriseGiveRecordFrom extends BaseForm {

    /**
     * 统一设置发放数量
     */
    @NotNull
    @ApiModelProperty(value = "统一设置发放数量", required = true)
    private Integer unifyGiveNum;

    /**
     * 添加发放供应商列表
     */
    @NotNull
    @ApiModelProperty(value = "添加发放供应商列表", required = true)
    private List<SaveCouponActivityEnterpriseGiveRecordDetailFrom> giveDetailList;

}
