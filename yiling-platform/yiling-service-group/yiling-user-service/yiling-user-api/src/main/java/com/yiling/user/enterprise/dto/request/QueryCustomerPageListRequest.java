package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID列表
     */
    private List<Long> eids;

    /**
     * 客户姓名（全模糊查询）
     */
    private String name;

    /**
     * 客户联系方式
     */
    private String contactorPhone;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;

    /**
     * 客户分组ID
     */
    private Long customerGroupId;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 客户类型，参见枚举 EnterpriseTypeEnum
     */
    private Integer type;

    /**
     * 数据范围：0或空-所有客户 1-已绑定分组客户 2-未绑定分组客户
     */
    private Integer dataScope;

    /**
     * 客户ID
     */
    private Long customerEid;

    /**
     * 商务联系人
     */
    private String contactUserName;

    /**
     * 是否设置支付方式：0-全部 1-已设置 2-未设置
     */
    private Integer paymentMethodScope;

    /**
     * 是否设置采购关系：0-全部 1-已设置 2-未设置
     */
    private Integer purchaseRelationScope;

    /**
     * 是否设置商务负责人：0-全部 1-已设置 2-未设置
     */
    private Integer customerContactScope;

    /**
     * 渠道ID
     */
    private List<Long> channelIds;

    /**
     * 客户eid集合
     */
    private List<Long> customerEids;

    /**
     * 使用产品线：1-POP 2-B2B
     */
    private Integer useLine;

    /**
     * 关联状态
     */
    private Integer unionFlag;
}
