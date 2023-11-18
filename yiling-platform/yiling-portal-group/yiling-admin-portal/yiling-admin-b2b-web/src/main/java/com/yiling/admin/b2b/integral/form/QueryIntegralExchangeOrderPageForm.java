package com.yiling.admin.b2b.integral.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换订单分页 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralExchangeOrderPageForm extends QueryPageListForm {

    /**
     * 开始兑换提交时间
     */
    @ApiModelProperty("开始兑换提交时间")
    private Date startSubmitTime;

    /**
     * 结束兑换提交时间
     */
    @ApiModelProperty("结束兑换提交时间")
    private Date endSubmitTime;

    /**
     * 开始订单兑付时间
     */
    @ApiModelProperty("开始订单兑付时间")
    private Date startExchangeTime;

    /**
     * 结束订单兑付时间
     */
    @ApiModelProperty("结束订单兑付时间")
    private Date endExchangeTime;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long uid;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String uname;

    /**
     * 兑换订单号
     */
    @ApiModelProperty("兑换订单号")
    private String orderNo;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private String goodsName;

    /**
     * 兑换状态：1-未兑换 2-已兑换
     */
    @ApiModelProperty("兑换状态：1-未兑换 2-已兑换")
    private Integer status;

    /**
     * 提交人手机号
     */
    @ApiModelProperty("提交人手机号")
    private String mobile;

}
