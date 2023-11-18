package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业渠道商分页列表 Request
 *
 * @author: yuecheng.chen
 * @date: 2021/6/4 0004
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryChannelCustomerPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业ID集合
     */
    private List<Long> eids;

    /**
     * 客户企业ID
     */
    private Long customerEid;

    /**
     * 渠道商名称（全模糊查询）
     */
    private String name;

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
     * 商务联系人
     */
    private Long contactUserId;

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
    private Long channelId;

    /**
     * 联系人姓名
     */
    private String contactUserName;

    /**
     * 分组ID
     */
    private Long customerGroupId;
}
