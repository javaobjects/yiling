package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/20 0020
 */
@Data
public class SaveFleeingGoodsForm extends BaseForm {

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String name;

    /**
     * 文件地址
     */
    private String key;
}
