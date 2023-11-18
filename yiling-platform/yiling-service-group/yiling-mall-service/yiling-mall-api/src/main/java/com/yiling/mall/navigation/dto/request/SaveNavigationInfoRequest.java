package com.yiling.mall.navigation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveNavigationInfoRequest extends BaseRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 导航名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 链接
     */
    private String link;

    /**
     * 状态1-启用 2-停用
     */
    private Integer state;
}

