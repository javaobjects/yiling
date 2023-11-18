package com.yiling.user.system.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.enums.UserTypeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 当前登录的企业员工信息
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Data
public class CurrentStaffInfo implements java.io.Serializable {

    public static final String CURRENT_USER_ID = "currentUserId";
    public static final String CURRENT_EID = "currentEid";
    public static final String USER_TYPE = "userType";

    /**
     * 应用类型
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private AppEnum appEnum;

    /**
     * 用户ID
     */
    private Long currentUserId;

    /**
     * 用户类型
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private UserTypeEnum userType;

    /**
     * 当前切换到的企业ID
     */
    private Long currentEid;

    /**
     * 用户对应当前切换到的企业的员工ID
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long currentEmployeeId;

    /**
     * 企业类型
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private EnterpriseTypeEnum enterpriseType;

    /**
     * 渠道类型
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private EnterpriseChannelEnum enterpriseChannel;

    /**
     * 是否是管理员
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private boolean adminFlag;

    /**
     * 是否是以岭本部
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean getYilingFlag() {
        return Constants.YILING_EID.equals(currentEid);
    }

    /**
     * 是否是以岭本部管理员
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public boolean getYilingAdminFlag() {
        return this.getYilingFlag() && this.isAdminFlag();
    }

}
