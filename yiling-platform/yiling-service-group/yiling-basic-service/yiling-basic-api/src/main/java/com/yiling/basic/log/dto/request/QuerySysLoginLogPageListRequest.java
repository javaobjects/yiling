package com.yiling.basic.log.dto.request;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统登录日志 Request
 *
 * @author: lun.yu
 * @date: 2021/12/31
 */
@Data
@Accessors(chain = true)
public class QuerySysLoginLogPageListRequest extends QueryPageListRequest {

    /**
     * 应用ID（参考AppEnum）
     */
    private String appId;


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录账号
     */
    private String loginAccount;

    /**
     * 开始登录时间
     */
    private Date startLoginTime;

    /**
     * 结束登录时间
     */
    private Date endLoginTime;

}
