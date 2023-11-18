package com.yiling.user.procrelation.dto.request;

import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryProcTemplatePageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 8520999678848784514L;

    /**
     * 模板编号
     */
    private String templateNumber;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 操作人
     */
    private List<Long> userIdList;
}
