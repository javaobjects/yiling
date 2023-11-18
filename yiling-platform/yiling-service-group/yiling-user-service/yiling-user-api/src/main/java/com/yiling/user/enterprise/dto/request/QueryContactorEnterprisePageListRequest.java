package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业分页列表 Request
 *
 * @author: zhigang.guo
 * @date: 2021/09/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryContactorEnterprisePageListRequest extends QueryPageListRequest {

    /**
     * 商务联系人ID
     */
    private Long contactUserId;

    /**
     * 用户企业ID
     */
    private Long eid;

    /**
     * 企业名称或者联系人电话
     */
    private String nameOrPhone;

    /**
     * 企业联系人
     */
    private String customerContactName;

    /**
     * 排序，1:按照订单下单时间排序 2:按照下单数量进行排序
     */
    private Integer orderSort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 不在此社会信用代码集合
     */
    private List<String> licenseNumberList;
}
