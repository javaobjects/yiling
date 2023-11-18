package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 视频拒接及未接通记录 form
 *
 * @author: fan.shen
 * @date: 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class VideoRefuseOrNotThroughIMChatRoomForm extends BaseForm {

    @NotBlank
    @ApiModelProperty(value = "房间号")
    private String roomId;

    @NonNull
    @ApiModelProperty("状态 1：未拨通 2：用户拒接")
    private Integer status;
}