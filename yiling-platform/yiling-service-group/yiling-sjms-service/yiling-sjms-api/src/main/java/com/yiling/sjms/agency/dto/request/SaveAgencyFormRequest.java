package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sjms.form.enums.FormTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构新增修改表单请求参数
 *
 * @author: yong.zhang
 * @date: 2023/2/22 0022
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgencyFormRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    private Integer supplyChainRole;
    /**
     * 机构名称
     */
    private String name;
    /**
     * 社会信用统一代码
     */
    private String licenseNumber;
    
    /**
     * 修改之前的crm系统对应客户名称
     */
    private String beforeName;

    /**
     * 修改之前的社会信用统一代码
     */
    private String beforeLicenseNumber;

    /**
     * 电话
     */
    private String phone;
    /**
     * 所属省份名称
     */
    private String provinceName;
    /**
     * 所属城市名称
     */
    private String cityName;
    /**
     * 所属区区域名称
     */
    private String regionName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 所属省份编码
     */
    private String provinceCode;
    /**
     * 所属城市编码
     */
    private String cityCode;
    /**
     * 所属区域编码
     */
    private String regionCode;
    /**
     * 备注
     */
    private String notes;

    /**
     * 1-团购单据 2-机构新增 3-机构修改 4-机构锁定 5-机构解锁 6-机构扩展信息修改 7-机构三者关系变更
     */
    private FormTypeEnum formTypeEnum;

    /**
     * 变更项目 1-机构名称、2-社会统一信用代码、3-电话、4-所属区域、5-地址
     */
    private String changeItem;


    /**
     * 发起人姓名
     */
    private String empName;


}
