package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockThirdRecordVO extends BaseVO {

    /**
     * 客户机构编码
     */
    @ApiModelProperty(value = "客户机构编码")
    private Long orgCrmId;

    /**
     * 客户机构名称
     */
    @ApiModelProperty(value = "客户机构名称")
    private String customerName;

    /**
     * 所属区域
     */
    @ApiModelProperty(value = "所属区域")
    private String regionName;

    /**
     * 供应链角色 erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    @ApiModelProperty(value = "供应链角色 erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role")
    private Integer supplyChainRole;


    /**
     * 月购进额度（万元）
     */
    @ApiModelProperty(value = "月购进额度（万元）")
    private BigDecimal purchaseQuota;

    /**
     * 生效业务部门
     */
    @ApiModelProperty(value = "列表页面-生效业务部门")
    private String effectiveDepartment;

    @ApiModelProperty(value = "详情页面-生效业务部门")
    private List<DepartmentInfo> departmentList;

    /**
     * 最后操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人id")
    private Long lastOpUser;

    @ApiModelProperty(value = "操作人姓名")
    private String lastOpUserName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


    @Data
    public static class DepartmentInfo {

        /**
         * 部门ID
         */
        @ApiModelProperty("部门ID")
        private Long orgId;

        /**
         * 部门名称
         */
        @ApiModelProperty("部门名称")
        private String orgName;

        /**
         * 全路径
         */
        @ApiModelProperty("全路径")
        private String fullpath;
    }
}
