package com.yiling.framework.common.pojo.bo;

import java.util.Date;

import lombok.Data;

/**
 * JWT 中存储数据的对象
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Data
public class JwtDataModel implements java.io.Serializable {

    public JwtDataModel(Integer appId, Long userId) {
        this.appId = appId;
        this.userId = userId;
    }

    public JwtDataModel(Integer appId, Long userId, Long eid) {
        this.appId = appId;
        this.userId = userId;
        this.eid = eid;
    }

    public JwtDataModel(Integer appId, Long userId, String userCode) {
        this.appId = appId;
        this.userId = userId;
        this.userCode = userCode;
    }

    public JwtDataModel(Integer appId, Long userId, Integer userType) {
        this.appId = appId;
        this.userId = userId;
        this.userType = userType;
    }

    public JwtDataModel(Integer appId, Long userId, Long eid, Integer etype, Long channelId, Long employeeId, Boolean adminFlag) {
        this.appId = appId;
        this.userId = userId;
        this.eid = eid;
        this.etype = etype;
        this.channelId = channelId;
        this.employeeId = employeeId;
        this.adminFlag = adminFlag;
    }

    public JwtDataModel(Integer appId, Long userId, Integer userType, Long eid, Integer etype, Long channelId, Long employeeId, Boolean adminFlag) {
        this.appId = appId;
        this.userId = userId;
        this.userType = userType;
        this.eid = eid;
        this.etype = etype;
        this.channelId = channelId;
        this.employeeId = employeeId;
        this.adminFlag = adminFlag;
    }

    public JwtDataModel(Integer appId, Long userId, Long eid, Integer etype, Long channelId, Long employeeId, Boolean adminFlag, Boolean virtualAccountFlag, Date virtualCreateTime) {
        this.appId = appId;
        this.userId = userId;
        this.eid = eid;
        this.etype = etype;
        this.channelId = channelId;
        this.employeeId = employeeId;
        this.adminFlag = adminFlag;
    }

    /**
     * 应用ID
     */
    private Integer appId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 员工工号（神机妙算系统使用）
     */
    private String userCode;

    /**
     * 销售助手中的用户类型：1-以岭人员 2-小三元 3-自然人
     */
    private Integer userType;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业类型
     */
    private Integer etype;

    /**
     * 渠道类型
     */
    private Long channelId;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 当前用户是否是企业管理员
     */
    private Boolean adminFlag;

    /**
     * 过期时间
     */
    private Date expiration;
}
