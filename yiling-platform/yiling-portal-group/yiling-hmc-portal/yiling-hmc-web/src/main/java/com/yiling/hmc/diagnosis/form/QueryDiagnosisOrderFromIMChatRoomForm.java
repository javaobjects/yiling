package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 查询当前聊天室关联的问诊单 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDiagnosisOrderFromIMChatRoomForm extends BaseForm {

    @NotBlank
    @ApiModelProperty(value = "聊天室id")
    private String conversationID;

}