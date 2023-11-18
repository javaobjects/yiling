package com.yiling.admin.hmc.insurance.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保险公司信息
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class InsuranceCompanyVO extends BaseVO {

    @ApiModelProperty("保险服务商名称")
    private String companyName;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String insuranceNo;

    @ApiModelProperty("保险服务商状态 1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    @ApiModelProperty("所属省份名称")
    private String provinceName;

    @ApiModelProperty("所属城市编码")
    private String cityCode;

    @ApiModelProperty("所属城市名称")
    private String cityName;

    @ApiModelProperty("所属区域编码")
    private String regionCode;

    @ApiModelProperty("所属区域名称")
    private String regionName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("退保客服电话")
    private String cancelInsuranceTelephone;

    @ApiModelProperty("退保地址")
    private String cancelInsuranceAddress;

    @ApiModelProperty("续保地址")
    private String renewInsuranceAddress;

    @ApiModelProperty("互联网问诊地址")
    private String internetConsultationUrl;
    
    @ApiModelProperty("代理理赔协议地址")
    private String claimProtocolUrl;

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
