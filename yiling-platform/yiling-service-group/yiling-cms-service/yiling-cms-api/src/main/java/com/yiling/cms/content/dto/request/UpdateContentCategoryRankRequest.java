package com.yiling.cms.content.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateContentCategoryRankRequest extends BaseRequest {

    /**
     * 内容id
     */
    private Long id;
    /**
     * 栏目集合
     */
    private List<AddOrUpdateIHPatientContentCategoryRequest> categoryList;
}
