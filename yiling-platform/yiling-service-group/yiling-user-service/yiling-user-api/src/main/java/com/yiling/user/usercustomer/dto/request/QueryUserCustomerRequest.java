package com.yiling.user.usercustomer.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户客户 Request
 * 
 * @author lun.yu
 * @date 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserCustomerRequest extends QueryPageListRequest {

    /**
     * 企业名称或联系电话
     */
    private String nameOrPhone;

    /**
     * 用户企业ID
     */
    private Long eid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 客户id
     */
    private Long customerEid;

    /**
     * 所属城市编码集合
     */
    private List<Long> cityCodeList;

    /**
     * 企业状态：1-启用 2-停用
     */
    private Integer enterpriseStatus;

    /**
     * 不在此社会信用代码集合
     */
    private List<String> licenseNumberList;

}
