package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建医药代表用户注册信息 Request
 *
 * @author: xuan.zhou
 * @date: 2023/1/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMrUserRegisterRequest extends BaseRequest {

    private static final long serialVersionUID = -4179529601621998205L;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属企业名称
     */
    private String ename;
}
