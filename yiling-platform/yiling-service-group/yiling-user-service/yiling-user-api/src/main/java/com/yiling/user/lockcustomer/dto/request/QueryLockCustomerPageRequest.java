package com.yiling.user.lockcustomer.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售助手-锁定用户分页查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLockCustomerPageRequest extends QueryPageListRequest {

    /**
     * 企业名称
     */
    @Like
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @Eq
    private String licenseNumber;

    /**
     * 状态：1-启用 2-停用
     */
    @Eq
    private Integer status;

    /**
     * 创建时间排序
     */
    @Order(asc = false)
    private Date createTime;

}
