package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 获取IM聊天记录 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@Accessors(chain = true)
public class GetIMChatHistoryRecordPageForm {

    @ApiModelProperty("最后一条消息id")
    private Integer lastId;

    @NotBlank
    @ApiModelProperty("群组id")
    private String groupId;


}