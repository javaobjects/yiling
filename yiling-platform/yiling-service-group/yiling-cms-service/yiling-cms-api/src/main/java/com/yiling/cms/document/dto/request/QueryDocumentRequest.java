package com.yiling.cms.document.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文献分页列表查询参数
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@Accessors(chain = true)
public class QueryDocumentRequest  implements Serializable {

    private static final long serialVersionUID = -3079022193287534238L;
    /**
     * 状态：1-未发布 2-已发布
     */
    private Integer status;


}