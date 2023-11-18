package com.yiling.user.agreementv2.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加关联子公司 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddRelationSubEnterpriseRequest extends BaseRequest {

    /**
     * 协议ID
     */
    @NotNull
    private Long agreementId;

    /**
     * 乙方公司ID
     */
    @NotNull
    private Long secondEid;

    /**
     * 子公司ID集合
     */
    @NotEmpty
    private List<Long> subEidList;

}
