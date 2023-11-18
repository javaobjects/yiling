package com.yiling.hmc.cms.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 点赞文章 Form
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LikeContentForm extends BaseForm {

    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long id;

    /**
     * 点赞标志：1-点赞，2-取消点赞
     */
    @ApiModelProperty("点赞标志：1-点赞，2-取消点赞")
    private Long likeFlag;

}
