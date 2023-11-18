package com.yiling.sjms.crm.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sjms.form.enums.FormTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateHospitalDrugstoreRelFormRequest extends BaseRequest {

    private static final long serialVersionUID = -8430078016240803096L;

    private Long id;

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


    private FormTypeEnum formTypeEnum;

    private String empName;
}
