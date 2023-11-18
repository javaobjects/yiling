package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业副本信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/3
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseAuthInfoUpdateVO extends BaseVO {

    /**
     * 原企业信息
     */
    @ApiModelProperty("原企业信息")
    private CommonEnterpriseInfoVO originEnterpriseInfo;

    /**
     * 更新后的企业信息
     */
    @ApiModelProperty("更新后的企业信息（为空字段则表示未更新）")
    private CommonEnterpriseInfoVO updateEnterpriseInfo;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty("认证状态：1-未认证 2-认证通过 3-认证不通过")
    private Integer authStatus;

    /**
     * 数据来源：1-B2B 2-销售助手
     */
    @ApiModelProperty("数据来源：1-B2B 2-销售助手")
    private Integer source;

    /**
     * 审核类型：1-首次认证 2-资质更新 3-驳回后再次认证
     */
    @ApiModelProperty("审核类型：1-首次认证 2-资质更新 3-驳回后再次认证")
    private Integer authType;

    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")
    private Date createTime;

}
