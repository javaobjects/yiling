package com.yiling.cms.document.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateDocumentRequest extends BaseRequest {

    private static final long serialVersionUID = 4049542424261571377L;

    private Long id;
    /**
     * cms_document_category表id
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 简述
     */
    private String resume;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 状态：1-未发布 2-已发布
     */
    private Integer status;

    /**
     * 引用业务线：多个用，号分隔
     */
    private List<Long> displayLines;


    /**
     * 内容
     */
    private String content;

    /**
     * 文献pdf oss key
     */
    private String documentFileUrl;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;

    /**
     * 备注
     */
    private String remark;

    /**
     *
     */
    private List<Long> standardGoodsIdList;

    private String documentFileName;
}