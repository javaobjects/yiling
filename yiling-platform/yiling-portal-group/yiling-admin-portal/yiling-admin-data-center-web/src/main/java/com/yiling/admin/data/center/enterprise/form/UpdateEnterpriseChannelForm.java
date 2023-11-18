package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业渠道 Form
 *
 * @author: xuan.zhou
 * @date: 2022/3/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseChannelForm extends BaseForm {

    @NotNull
    @Min(1)
    private Long eid;

    @NotNull
    @Min(1)
    private Long channelId;
}
