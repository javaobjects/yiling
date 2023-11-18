package com.yiling.sjms.agency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机构解锁表单
 * </p>
 *
 * @author handy
 * @date 2023-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agency_unlock_form")
public class AgencyUnlockFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * crm系统对应客户名称
     */
    private String name;

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * 社会信用统一代码
     */
    private String licenseNumber;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    private Integer supplyChainRole;

    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 电话
     */
    private String phone;

    /**
     * 变更项目 1-机构名称、2-社会统一信用代码、3-电话、4-所属区域、5-地址
     */
    private String changeItem;

    /**
     * 备注：1-潜力低、2-商业 (终端) 关停/重组、3-商业 (终端) 所属划归、4-供应商停控、5-渠道不畅、6-公司批复锁定/解锁、7-返利点位高、8-位置偏远、9-终端改制/统购分销、10-终端改制/转公共卫生、11-终端改制/专科医院
     */
    private Integer notes;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 解锁三者关系数量
     */
    private Integer unlockNum;

    /**
     * 修改备注
     */
    private String businessRemark;


    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;

}
