package com.yiling.open.cms.content.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AllCategoryListVO extends BaseVO {

    private Long moduleId;

    private List<CategoryListVO> categoryList;
}
