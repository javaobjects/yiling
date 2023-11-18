package com.yiling.open.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/22
 */
@Data
@Accessors(chain = true)
public class PatientLikeContentForm extends BaseForm {

    @ApiModelProperty("id")
    private Long id;

/*    @ApiModelProperty("文章id")
    private Long contentId;*/

    @ApiModelProperty("点赞标志：1-点赞，2-取消点赞")
    private Integer likeFlag;

    @ApiModelProperty("微信用户id")
    private Long opUserId;

}
