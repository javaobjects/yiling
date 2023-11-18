package com.yiling.f2b.admin.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业渠道商分页列表 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/4 0004
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryChannelCustomerPageListForm extends QueryPageListForm {

    @ApiModelProperty("客户ID")
    private Long customerEid;

    @ApiModelProperty("客户名称")
    private String name;

    @ApiModelProperty("客户省份编码")
    private String provinceCode;

    @ApiModelProperty("客户城市编码")
    private String cityCode;

    @ApiModelProperty("客户区域编码")
    private String regionCode;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    @ApiModelProperty("商务联系人")
    private String contactUserName;

    @ApiModelProperty("是否设置支付方式：0-全部 1-已设置 2-未设置")
    private Integer paymentMethodScope;

    @ApiModelProperty("是否设置采购关系：0-全部 1-已设置 2-未设置")
    private Integer purchaseRelationScope;

    @ApiModelProperty("是否设置商务负责人：0-全部 1-已设置 2-未设置")
    private Integer customerContactScope;

    @ApiModelProperty("渠道ID")
    private Long channelId;

    @ApiModelProperty("客户分组ID")
    private Long customerGroupId;

    @ApiModelProperty("企业类型：具体类型见字典")
    private Integer type;
}
