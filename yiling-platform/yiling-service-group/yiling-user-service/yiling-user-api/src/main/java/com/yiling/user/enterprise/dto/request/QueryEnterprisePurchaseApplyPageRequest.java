package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业采购申请分页列表 Request
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterprisePurchaseApplyPageRequest extends QueryPageListRequest {

    /**
     * 供应商企业ID
     */
    private Long eid;

    /**
     * 采购商企业ID
     */
    private Long customerEid;

    /**
     * 企业名称（全模糊查询）
     */
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

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
     * 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    private Integer authStatus;

    /**
     * 数据类型：1-查询采购商列表 2-查询供应商列表 （枚举EnterprisePurchaseApplyDataTypeEnum）
     */
    @NotNull
    private Integer dataType;

}
