package com.yiling.admin.hmc.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class EnterpriseAccountVO extends BaseVO {

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("商家名称")
    private String ename;

    @ApiModelProperty("账户类型 1-对公账户 2-对私账户")
    private Integer accountType;

    @ApiModelProperty("账户名")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNumber;

    @ApiModelProperty("开户行")
    private String accountBank;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;
}
