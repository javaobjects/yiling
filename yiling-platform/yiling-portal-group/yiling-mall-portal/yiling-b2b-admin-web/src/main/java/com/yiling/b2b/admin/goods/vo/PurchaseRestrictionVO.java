package com.yiling.b2b.admin.goods.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 PurchaseRestrictionVO
 * @描述
 * @创建时间 2022/12/8
 * @修改人 shichen
 * @修改时间 2022/12/8
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PurchaseRestrictionVO extends BaseVO {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", example = "1")
    private Long goodsId;

    /**
     * 每单限购数量 0为无限制
     */
    @ApiModelProperty(value = "每单限购数量 0为无限制")
    private Long orderRestrictionQuantity;

    /**
     * 时间内限购数量 0为无限制
     */
    @ApiModelProperty(value = "时间内限购数量 0为无限制")
    private Long timeRestrictionQuantity;

    /**
     * 限购时间类型 1 自定义 2 每天 3 每周 4每月
     */
    @ApiModelProperty(value = "限购时间类型 1 自定义 2 每天 3 每周 4每月")
    private Integer timeType;

    /**
     * 限购开始时间
     */
    @ApiModelProperty(value = "限购开始时间")
    private Date startTime;

    /**
     * 限购结束时间
     */
    @ApiModelProperty(value = "限购结束时间")
    private Date endTime;

    /**
     * 客户设置类型 0：全部客户  1:部分客户
     */
    @ApiModelProperty(value = "客户设置类型 0：全部客户  1:部分客户")
    private Integer customerSettingType;

    /**
     * 0 正常状态 1 关闭状态
     */
    @ApiModelProperty(value = "0 正常状态 1 关闭状态")
    private Integer status;
}
