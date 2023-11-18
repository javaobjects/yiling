package com.yiling.hmc.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseAccountSaveRequest extends BaseRequest {

    /**
     * 保险药品商家结算账号表id
     */
    private Long id;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * 账户类型 1-对公账户 2-对私账户
     */
    private Integer accountType;

    /**
     * 账户名
     */
    private String accountName;

    /**
     * 账号
     */
    private String accountNumber;

    /**
     * 开户行
     */
    private String accountBank;

    /**
     * 备注
     */
    private String remark;
}
