package com.yiling.dataflow.crm.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmHospitalDrugstoreRelRequest extends BaseRequest {

    private static final long serialVersionUID = -6079356928729883022L;

    private Long id;

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
     * 是否停用 0-否 1-是
     */
    private Integer disableFlag;

    /**
     * 数据来源 1-导入数据 2-审批流数据
     */
    private Integer dataSource;

    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 操作人
     */
    private Long lastOpUser;
}
