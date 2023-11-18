package com.yiling.hmc.insurance.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 后台参保记录查询参数
 * @author gxl
 * @date 2022/4/12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryBackInsuranceRecordPageRequest extends QueryPageListRequest {


    private static final long serialVersionUID = -7518958980454303322L;


    private List<Long> insuranceIdList;

    private List<Long> insuranceCompanyIdList;

    private Long insuranceCompanyId;
    /**
     * 投保时间
     */
    private Date startProposalTime;

    private Date endProposalTime;

    /**
     * 药店
     */
    private List<Long> eidList;

    /**
     * 保司单号
     */
    private String policyNo;

    /**
     * 被报人姓名
     */
    private String issueName;

    private String issuePhone;

    /**
     * 投保人姓名
     */
    private String holderName;
    /**
     * 销售人员id
     */
    private List<Long> sellerUserIdList;

    private List<Long> sellerEidList;

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

    private boolean isExport;
}
