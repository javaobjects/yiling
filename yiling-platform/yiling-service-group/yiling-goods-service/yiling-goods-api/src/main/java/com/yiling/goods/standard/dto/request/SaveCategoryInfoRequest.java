package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCategoryInfoRequest extends BaseRequest {

    private static final long serialVersionUID = -3331121332221L;

    /**
     * 父类ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

}
