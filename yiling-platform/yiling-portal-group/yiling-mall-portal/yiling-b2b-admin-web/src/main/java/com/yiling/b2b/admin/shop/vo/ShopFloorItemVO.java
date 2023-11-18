package com.yiling.b2b.admin.shop.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺楼层列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopFloorItemVO extends BaseVO {

    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long shopId;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 包含商品数
     */
    @ApiModelProperty("包含商品数")
    private Integer goodsNum;

    /**
     * 楼层名称
     */
    @ApiModelProperty("楼层名称")
    private String name;

    /**
     * 权重值
     */
    @ApiModelProperty("权重值")
    private Integer sort;

    /**
     * 执行状态：1-启用 2-停用
     */
    @ApiModelProperty("执行状态：1-启用 2-停用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;




}
