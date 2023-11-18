package com.yiling.sjms.crm.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HospitalDrugstoreRelationFormDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 院外药店机构编码
     */
    private Long drugstoreOrgId;

    /**
     * 院外药店机构名称
     */
    private String drugstoreOrgName;

    /**
     * 医疗机构编码
     */
    private Long hospitalOrgId;

    /**
     * 医疗机构名称
     */
    private String hospitalOrgName;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 品种名称
     */
    private String categoryName;

    /**
     * 标准产品编码
     */
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    private String crmGoodsName;

    /**
     * 标准产品规格
     */
    private String crmGoodsSpec;

    /**
     * 开始生效时间
     */
    private Date effectStartTime;

    /**
     * 结束生效时间
     */
    private Date effectEndTime;

    /**
     * 是否已同步至主库 0-否 1-是
     */
    private Integer syncData;

    private String syncErrMsg;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
