package com.yiling.cms.document.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文献分页列表查询参数
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDocumentPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -295077988347155512L;
    private String title;

    private Date startTime;

    private Date endTime;


    private Long categoryId;

    private String displayLine;

    private Integer status;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;
}