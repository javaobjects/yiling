package com.yiling.user.shop.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * B2B-店铺楼层列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-20
 */
@Data
public class QueryShopFloorPageRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @Eq
    private Long eid;

    /**
     * 楼层名称
     */
    @Like
    private String name;

    /**
     * 楼层状态：1-启用 2-停用
     */
    @Eq
    private Integer status;

    /**
     * 开始创建时间
     */
    @Before(name = "create_time")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @After(name = "create_time")
    private Date endCreateTime;

}
