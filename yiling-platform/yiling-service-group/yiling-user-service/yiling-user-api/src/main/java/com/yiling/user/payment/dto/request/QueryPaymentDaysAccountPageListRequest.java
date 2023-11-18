package com.yiling.user.payment.dto.request;/**
 * @author: ray
 * @date: 2021/5/21
 */

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:gxl
 * @description:
 * @date: Created in 17:34 2021/5/21
 * @modified By:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryPaymentDaysAccountPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -3360334557002774069L;

    /**
     * 当前登录人eid
     */
    private Long currentEid;
    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 采购商名称
     */
    private String customerName;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;
    /**
     * eidlist
     */
    private List<Long> eidList;

    /**
     * 类型：1-账期额度管理列表 2-采购商账期列表
     */
    private Integer type;

    /**
     * 商务联系人
     */
    private List<Long> contactUserList;
}
