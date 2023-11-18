package com.yiling.mall.navigation.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryNavigationInfoPageRequest extends QueryPageListRequest {
    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     *结束创建时间
     */
    private Date endCreateTime;

    /**
     * 状态1-启用 2-停用
     */
    private Integer state;

    /**
     * 导航名称
     */
    private String name;

    /**
     * 企业id
     */
    private Long eid;

}
