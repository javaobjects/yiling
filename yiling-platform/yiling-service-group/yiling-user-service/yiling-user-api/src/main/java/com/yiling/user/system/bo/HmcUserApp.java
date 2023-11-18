package com.yiling.user.system.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 健康管理中心用户应用信息
 *
 * @author: fan.shen
 * @date: 2022-09-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmcUserApp implements java.io.Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 小程序id
     */
    private String appId;

    /**
     * 小程序openId
     */
    private String openId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
