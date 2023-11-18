package com.yiling.sales.assistant.app.system.vo;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录用户信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Data
@ApiModel
public class LoginInfoVO {

    public LoginInfoVO() {
    }

    public LoginInfoVO(String verifyCodeToken) {
        this.verifyCodeToken = verifyCodeToken;
    }

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private StaffVO userInfo;

    /**
     * 用户类型：1-以岭人员 2-小三元 3-自然人
     */
    @ApiModelProperty("用户类型：1-以岭人员 2-小三元 3-自然人")
    private Integer userType;

    /**
     * 员工类型：1-商务代表 2-医药代表 100-其他
     */
    @ApiModelProperty("员工类型：1-商务代表 2-医药代表 100-其他")
    private Integer employeeType;

    /**
     * 用户当前选择的企业信息
     */
    @ApiModelProperty("用户当前选择的企业信息")
    private CurrentEnterpriseVO currentEnterpriseInfo;

    /**
     * token
     */
    @ApiModelProperty("token")
    private String token;

    /**
     * 验证码token
     */
    @ApiModelProperty("验证码token，仅当错误编码为100100才会有值，用于短信方式注册")
    private String verifyCodeToken;

    @Data
    public static class CurrentEnterpriseVO {

        @ApiModelProperty("企业ID")
        private Long id;

        @ApiModelProperty("企业名称")
        private String name;

        @ApiModelProperty("企业类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
        private Integer type;

        @ApiModelProperty("渠道类型：1-工业 2-工业直属企业 3-一级商 4-二级商 5-KA用户")
        private Long channelId;

        @ApiModelProperty("企业员工ID")
        private Long employeeId;

        @ApiModelProperty("是否企业管理员")
        private Boolean adminFlag;

        @ApiModelProperty("是否是以岭本部")
        private Boolean yilingFlag;

        @ApiModelProperty("是否是以岭本部企业管理员")
        private Boolean yilingAdminFlag;

        public Boolean getYilingFlag() {
            return Constants.YILING_EID.equals(this.id);
        }

        public Boolean getYilingAdminFlag() {
            return this.getYilingFlag() && this.getAdminFlag();
        }
    }
}
