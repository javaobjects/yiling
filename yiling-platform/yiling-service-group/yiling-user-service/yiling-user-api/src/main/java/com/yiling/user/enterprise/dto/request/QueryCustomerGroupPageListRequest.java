package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业客户分组分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerGroupPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分组名称（全模糊查询）
     */
    private String  name;

    /**
     * 分组类型：0-全部 1-平台创建 2-ERP同步
     */
    private Integer type;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;
}
