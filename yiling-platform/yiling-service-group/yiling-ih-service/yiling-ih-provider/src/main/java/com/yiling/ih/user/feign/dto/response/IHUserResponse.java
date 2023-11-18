package com.yiling.ih.user.feign.dto.response;

import com.yiling.ih.common.BaseResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 互联网医院用户
 */
@Data
@Accessors(chain = true)
public class IHUserResponse extends BaseResponse {

    /**
     * id
     */
    private Integer id;

    /**
     * 	联系人
     */
    private String name;

    /**
     * 	联系人手机
     */
    private String phone;

    /**
     * 	登录名称
     */
    private String username;
}
