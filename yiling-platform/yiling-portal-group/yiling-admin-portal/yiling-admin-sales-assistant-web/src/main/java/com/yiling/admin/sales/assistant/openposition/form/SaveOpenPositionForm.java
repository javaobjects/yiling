package com.yiling.admin.sales.assistant.openposition.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存销售助手-开屏位 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOpenPositionForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("ID（新增不传入，编辑必传）")
    private Long id;

    /**
     * 标题
     */
    @Length(max = 50)
    @NotEmpty
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 配置链接
     */
    @Length(max = 1000)
    @ApiModelProperty("配置链接")
    private String link;

    /**
     * 图片
     */
    @NotEmpty
    @ApiModelProperty(value = "图片", required = true)
    private String picture;

    /**
     * 发布状态：1-暂不发布 2-立即发布
     */
    @NotNull
    @ApiModelProperty(value = "发布状态：1-暂不发布 2-立即发布", required = true)
    private Integer status;

}
