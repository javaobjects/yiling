package com.yiling.b2b.app.system.vo;

import java.util.List;

import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;

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

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private StaffVO userInfo;

    /**
     * 用户当前选择的企业信息
     */
    @ApiModelProperty("用户当前选择的企业信息")
    private CurrentEnterpriseVO currentEnterpriseInfo;

    /**
     * 用户所属企业列表
     */
    @ApiModelProperty("用户所属企业列表")
    private List<SimpleEnterpriseVO> userEnterpriseList;

    /**
     * token
     */
    @ApiModelProperty("token")
    private String token;

    /**
     * 是否为特殊号段
     */
    @ApiModelProperty("是否为特殊号段")
    private Boolean specialPhone;

    /**
     * 是否必须换绑
     */
    @ApiModelProperty("是否必须换绑")
    private Boolean mustChangeBind;


    @Data
    public static class CurrentEnterpriseVO {

        @ApiModelProperty("企业ID")
        private Long id;

        @ApiModelProperty("企业名称")
        private String name;

        @ApiModelProperty("企业类型：数据字典enterprise_type")
        private Integer type;

        @ApiModelProperty("企业员工ID")
        private Long employeeId;

        @ApiModelProperty("是否企业管理员")
        private Boolean adminFlag;

    }
}
