package com.yiling.sales.assistant.userteam.dto.request;


import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询团队成员 Form
 * 
 * @author lun.yu
 * @date 2021/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserTeamRequest extends QueryPageListRequest {

    /**
     * 队长ID
     */
    @Eq
    private Long parentId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 姓名或手机号
     */
    private String nameOrPhone;

    /**
     * 联系方式
     */
    @Like
    private String mobilePhone;

    /**
     * 注册状态：0-未注册 1-已注册
     */
    @Eq
    private Integer registerStatus;

    /**
     * 注册开始时间
     */
    @Before(name = "register_time")
    private Date registerStartTime;

    /**
     * 注册结束时间
     */
    @After(name = "register_time")
    private Date registerEndTime;

    /**
     * 时间排序：0-正序 1-倒序
     */
    private Integer dateOrder;

}
