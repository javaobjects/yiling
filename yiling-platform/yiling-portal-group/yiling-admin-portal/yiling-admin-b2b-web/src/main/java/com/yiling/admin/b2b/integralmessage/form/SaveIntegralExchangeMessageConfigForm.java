package com.yiling.admin.b2b.integralmessage.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分兑换消息配置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralExchangeMessageConfigForm extends BaseForm {

    /**
     * ID（编辑时传入）
     */
    @ApiModelProperty("ID（编辑时传入）")
    private Long id;

    /**
     * 标题
     */
    @NotEmpty
    @Length(max = 10)
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 图标
     */
    @NotNull
    @ApiModelProperty(value = "图标", required = true)
    private String icon;

    /**
     * 投放开始时间
     */
    @NotNull
    @ApiModelProperty(value = "投放开始时间", required = true)
    private Date startTime;

    /**
     * 投放结束时间
     */
    @NotNull
    @ApiModelProperty(value = "投放结束时间", required = true)
    private Date endTime;

    /**
     * 排序
     */
    @NotNull
    @Range(min = 1, max = 200)
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    /**
     * 状态：1-启用 2-禁用
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-禁用", required = true)
    private Integer status;

    /**
     * 页面配置：1-活动链接
     */
    @ApiModelProperty("页面配置：1-活动链接")
    private Integer pageConfig;

    /**
     * 超链接
     */
    @ApiModelProperty("超链接")
    private String link;

}
