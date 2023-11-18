package com.yiling.admin.pop.banner.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 校验banner信息 form
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckBannerForm extends BaseForm {

    /**
     * banner标题
     */
    @ApiModelProperty(value = "banner标题")
    @NotEmpty
    private String title;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 链接url
     */
    @ApiModelProperty(value = "链接url")
    private String linkUrl;

}
