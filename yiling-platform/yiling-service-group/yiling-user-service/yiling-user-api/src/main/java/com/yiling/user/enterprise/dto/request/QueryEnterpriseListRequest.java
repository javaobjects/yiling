package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseListRequest extends BaseRequest {

    /**
     * 所属省份编码
     */
    @Eq
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @Eq
    private String cityCode;

    /**
     * 所属区域编码
     */
    @Eq
    private String regionCode;

    /**
     * 企业名称
     */
    @Like
    private String name;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @Eq
    private Integer authStatus;

    /**
     * 状态：1-启用 2-停用
     */
    @Eq
    private Integer status;

    /**
     * 企业ID集合
     */
    @In(name = "id")
    private List<Long> idList;

    /**
     * 企业类型集合
     */
    @In(name = "type")
    private List<Integer> typeList;
}
