package com.yiling.b2b.admin.goods.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SavePurchaseRestrictionForm
 * @描述
 * @创建时间 2022/12/8
 * @修改人 shichen
 * @修改时间 2022/12/8
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePurchaseRestrictionForm extends BaseForm {

    @ApiModelProperty("限购id")
    private Long id;
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    @NotNull
    private Long goodsId;

    /**
     * 每单限购数量 0为无限制
     */
    @ApiModelProperty("每单限购数量 0为无限制")
    private Long orderRestrictionQuantity;

    /**
     * 时间内限购数量 0为无限制
     */
    @ApiModelProperty("时间内限购数量 0为无限制")
    private Long timeRestrictionQuantity;

    /**
     * 限购时间类型 1自定义 2 每天 3 每周 4每月
     */
    @ApiModelProperty("限购时间类型 1自定义 2 每天 3 每周 4每月")
    private Integer timeType;

    /**
     * 限购开始时间
     */
    @ApiModelProperty("限购开始时间")
    private Date startTime;

    /**
     * 限购结束时间
     */
    @ApiModelProperty("限购结束时间")
    private Date endTime;

    /**
     * 客户设置类型 0：全部客户  1:部分客户
     */
    @ApiModelProperty("客户设置类型 0：全部客户  1:部分客户")
    private Integer customerSettingType;
}
