package com.yiling.open.cms.diagnosis.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询账号的在线状态
 *
 * @author: yong.zhang
 * @date: 2023/5/29 0029
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAccountOnlineStatusForm extends BaseForm {

    /**
     * 账号集合
     */
    @ApiModelProperty(value = "账号集合", required = true)
    @NotEmpty(message = "账号不能为空")
    private List<String> accountList;
}
