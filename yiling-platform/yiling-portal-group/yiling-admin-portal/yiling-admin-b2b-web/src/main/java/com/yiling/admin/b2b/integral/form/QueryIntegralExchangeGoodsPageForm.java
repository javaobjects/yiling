package com.yiling.admin.b2b.integral.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换商品分页 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralExchangeGoodsPageForm extends QueryPageListForm {

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    @ApiModelProperty("上架状态：1-已上架 2-已下架")
    private Integer shelfStatus;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @ApiModelProperty("商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券")
    private Integer goodsType;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建人手机号
     */
    @ApiModelProperty("创建人手机号")
    private String mobile;

}
