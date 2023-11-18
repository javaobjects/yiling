package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据id删除
 * @author: shixing.sun
 * @date: 2023/2/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseUnLockApproveRequest extends BaseRequest {

    private static final long serialVersionUID = -7865460597856242347L;

    private Long formId;

    private String userCode;

    /**
     * 1 机构解锁审核通过/驳回 2三者关系变更审核通过/驳回
     */
    private String type;

    /**
     * 创建人
     */
    private Long createUser;
}