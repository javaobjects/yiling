package com.yiling.user.control.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchSaveCustomerControlRequest extends BaseRequest {

    private Long id;

    private Integer customerSet;

    private List<Long> customerIds;

    private Long goodsId;

    private Long eid;

    /**
     * 客户姓名（全模糊查询）
     */
    private String name;

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
     * 客户类型，参见枚举 EnterpriseTypeEnum
     */
    private Integer type;

}

