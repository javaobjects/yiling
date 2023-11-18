package com.yiling.admin.data.center.enterprise.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 给企业开通平台 Form
 *
 * @author: xuan.zhou
 * @date: 2021/10/21
 */
@Data
public class OpenPlatformForm {

    /**
     * 企业ID
     */
    @NotNull(groups = { OpenPopValidateGroup.class, OpenHmcValidateGroup.class, OpenOtherValidateGroup.class })
    @Min(value = 1, groups = { OpenPopValidateGroup.class, OpenHmcValidateGroup.class, OpenOtherValidateGroup.class })
    @ApiModelProperty(value = "企业ID", required = true)
    private Long eid;

    /**
     * 开通平台：1-POP 2-B2B 3-销售助手
     */
    @NotEmpty(groups = { OpenPopValidateGroup.class, OpenHmcValidateGroup.class, OpenOtherValidateGroup.class })
    @ApiModelProperty(value = "开通平台：1-POP 2-B2B 3-销售助手 4-互联网医院 5-C端药+险", required = true)
    private List<Integer> platformCodeList;

    /**
     * 渠道类型（开通POP平台时必填），参考数据字典：channel_type
     */
    @NotNull(groups = { OpenPopValidateGroup.class })
    @Min(value = 1, groups = { OpenPopValidateGroup.class })
    @ApiModelProperty(value = "渠道类型（开通POP平台时必填），参考数据字典：channel_type")
    private Long channelCode;

    @NotNull(groups = { OpenHmcValidateGroup.class })
    @Min(value = 1, groups = { OpenHmcValidateGroup.class })
    @ApiModelProperty(value = "C端药+险类型（开通C端药+险时必填），参考数据字典：hmc_type")
    private Integer hmcType;

    public interface OpenPopValidateGroup {
    }

    public interface OpenHmcValidateGroup {
    }

    public interface OpenOtherValidateGroup {
    }
}
