package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 内容分页列表查询参数
 *
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ContentRankRequest extends BaseRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * id
     */
    private Long id;

    /**
     * 栏目排序
     */
    private Integer categoryRank;

    /**
     * 是否置顶 0-否，1-是
     */
    private Integer topFlag;

}