package com.yiling.user.esb.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 生成业务架构 Request
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveBusinessOrganizationRequest extends BaseRequest {

    /**
     * 新增的打标部门架构集合
     */
    private List<MarkingOrg> markingOrgList;

    @Data
    public static class MarkingOrg implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 部门ID
         */
        private Long orgId;

        /**
         * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
         */
        private Integer tagType;
    }


}
