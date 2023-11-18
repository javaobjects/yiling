package com.yiling.sjms.esb.form;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 生成ESB业务架构 Form
 *
 * @author: lun.yu
 * @date: 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveBusinessOrganizationForm extends BaseForm {

    /**
     * 新增的打标部门架构集合
     */
    @NotEmpty
    @ApiModelProperty(value = "新增的打标部门架构集合", required = true)
    private List<@Valid MarkingOrg> markingOrgList;

    @Data
    public static class MarkingOrg implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 部门ID
         */
        @NotNull
        @ApiModelProperty(value = "部门ID", required = true)
        private Long orgId;

        /**
         * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
         */
        @NotNull
        @ApiModelProperty(value = "打标类型：1-事业部打标 2-业务省区打标 3-区办打标", required = true)
        private Integer tagType;
    }


}
