package com.yiling.hmc.wechat.form;

import cn.hutool.core.util.StrUtil;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.bo.TerminalInfo;
import com.yiling.hmc.wechat.enums.SourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 同步用户信息入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/8/24
 */
@Data
@ToString
@ApiModel(value = "WxSyncUserInfoForm", description = "同步用户信息参数")
@Slf4j
public class WxSyncUserInfoForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;

    @NotEmpty
    @ApiModelProperty(value = "小程序appId")
    private String appId;

    @NotEmpty
    @ApiModelProperty(value = "小程序openId")
    private String openId;

    @NotEmpty
    @ApiModelProperty(value = "开放平台unionId")
    private String unionId;

    @NotEmpty
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @NotEmpty
    @ApiModelProperty(value = "头像路径")
    private String avatarUrl;

    @NotEmpty
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @NotEmpty
    @ApiModelProperty(value = "gender")
    private String gender;

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;

    @ApiModelProperty(value = "注册来源", hidden = true)
    private Integer source = SourceEnum.NATURE.getType();

}
