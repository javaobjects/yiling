package com.yiling.admin.system.system.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 EsWordForm
 * @描述
 * @创建时间 2022/5/9
 * @修改人 shichen
 * @修改时间 2022/5/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class EsWordForm extends BaseForm {
    @ApiModelProperty(value = "词语id")
    private Long id;
    /**
     * 词语
     */
    @ApiModelProperty("词语")
    private String word;
    /**
     * 扩展词类型 1：扩展词，2：停止词，3：单向同义词，4：双向同义词
     */
    @ApiModelProperty("1：扩展词，2：停止词，3：单向同义词，4：双向同义词")
    private Integer type;
    /**
     * 关联id
     */
    @ApiModelProperty("关联id")
    private Long refId;
    /**
     * 状态 0：正常 1：停用
     */
    @ApiModelProperty("0：正常 1：停用")
    private Integer status;
}
