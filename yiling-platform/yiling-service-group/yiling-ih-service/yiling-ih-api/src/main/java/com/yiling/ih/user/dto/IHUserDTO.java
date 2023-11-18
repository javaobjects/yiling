package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * IH 用户信息 DTO
 *
 * @author: fan.shen
 * @date: 2022-11-18
 */
@Data
public class IHUserDTO implements java.io.Serializable {

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
