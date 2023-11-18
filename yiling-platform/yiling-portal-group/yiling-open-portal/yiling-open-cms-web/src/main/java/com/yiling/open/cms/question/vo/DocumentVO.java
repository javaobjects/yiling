package com.yiling.open.cms.question.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关联文献内容
 */
@Data
public class DocumentVO implements java.io.Serializable  {

    private static final long serialVersionUID = -7631547954155154406L;

    /**
     * 文献id
     */
    @ApiModelProperty("文献id")
    private Long documentId;

    /**
     *文献标题
     */
    @ApiModelProperty("文献标题")
    private String documentTitle;
}
