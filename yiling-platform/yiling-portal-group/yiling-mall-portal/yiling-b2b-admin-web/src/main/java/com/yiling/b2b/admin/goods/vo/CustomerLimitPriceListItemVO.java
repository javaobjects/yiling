package com.yiling.b2b.admin.goods.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业客户列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
public class CustomerLimitPriceListItemVO {

    /**
     * 客户限价Id
     */
    @ApiModelProperty("客户限价Id")
    private Long id;

    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 客户类型
     */
    @ApiModelProperty("客户类型")
    private String customerType;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private List<String> labelNameList;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String contactorPhone;

    /**
     * 通讯地址
     */
    @ApiModelProperty("通讯地址")
    private String address;


    /**
     * 是否控价
     */
    @ApiModelProperty("是否控制价格：0否 1是")
    private Integer limitFlag;

    /**
     * 是否推荐
     */
    @ApiModelProperty("是否推荐：0否 1是")
    private Integer recommendationFlag;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operatorName;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date operatorTime;
}
