package com.yiling.admin.system.system.form;

import java.util.List;

import com.yiling.admin.system.system.vo.EsWordVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveEsWordForm
 * @描述
 * @创建时间 2022/4/27
 * @修改人 shichen
 * @修改时间 2022/4/27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveEsWordForm extends BaseForm {
    @ApiModelProperty(value = "词语id")
    private Long id;
    /**
     * 扩展词
     */
    @ApiModelProperty(value = "词语")
    private String word;
    /**
     * 扩展词类型 1：扩展词，2：停止词，3：单向同义词，4：双向同义词
     */
    @ApiModelProperty(value = "1：扩展词，2：停止词，3：单向同义词，4：双向同义词")
    private Integer type;

    /**
     * 状态 0：正常 1：停用
     */
    @ApiModelProperty(value = "0：正常 1：停用")
    private Integer status;

    /**
     * 关联id（双向同义词refId为组id，单向同义词主词refId为0 关联词的refId为主词id）
     */
    @ApiModelProperty(value = "关联id")
    private Long refId;

    /**
     * 关联词
     */
    @ApiModelProperty(value = "关联词")
    private List<EsWordForm> refWordList;

    /**
     * 是否上传词库oss文件
     */
    @ApiModelProperty(value = "是否上传词库oss文件")
    private Boolean uploadFlag = false;
}
