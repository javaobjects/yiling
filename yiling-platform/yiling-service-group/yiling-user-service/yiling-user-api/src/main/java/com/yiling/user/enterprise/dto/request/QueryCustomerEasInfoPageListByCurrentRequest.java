package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据当前登录人查询 负责的企业 eas账号列表  Request
 *
 * @author: lun.yu
 * @date: 2021/9/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerEasInfoPageListByCurrentRequest extends QueryPageListRequest {

    /**
     * 企业名称/联系人（全模糊匹配）
     */
    private String customerName;
}
