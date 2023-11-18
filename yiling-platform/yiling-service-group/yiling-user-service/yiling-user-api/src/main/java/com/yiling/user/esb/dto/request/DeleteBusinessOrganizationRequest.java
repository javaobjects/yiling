package com.yiling.user.esb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除ESB业务架构打标标签 Request
 *
 * @author: lun.yu
 * @date: 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteBusinessOrganizationRequest extends BaseRequest {

    /**
     * 部门ID
     */
    private Long orgId;

}
