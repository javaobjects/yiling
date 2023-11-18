package com.yiling.hmc.insurance.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 后台保险缴费记录查询
 * @author gxl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryInsuranceRecordPayPageRequest extends QueryPageListRequest {


    private static final long serialVersionUID = -799130935170521482L;
    private List<Long> insuranceIdList;

    private Long insuranceCompanyId;

    private Date startPayTime;

    private Date endPayTime;

    /**
     * 药店
     */
    private List<Long> eidList;

    private List<Long> sellerEidList;

    private Long sellerEid;

    /**
     * 商家后台查询使用
     */
    private Long eid;

    /**
     * 保司单号
     */
    private String policyNo;
    /**
     * 支付流水号
     */
    private String transactionId;
    /**
     * 被报人姓名
     */
    private String issueName;

    private String issuePhone;

    private String holderName;
    /**
     * 销售人员id
     */
    private List<Long> sellerUserIds;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    private Integer policyStatus;

    /**
     * 定额方案类型 1-季度，2-年
     */
    private Integer billType;

    /**
     * 0 线上 1 线下
     */
    private Integer sourceType;

    /**
     * 是否导出
     */
    private boolean isExport;
}
