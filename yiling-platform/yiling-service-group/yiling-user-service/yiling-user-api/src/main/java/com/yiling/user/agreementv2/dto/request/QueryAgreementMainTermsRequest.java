package com.yiling.user.agreementv2.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议主条款查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementMainTermsRequest extends BaseRequest {

    /**
     * 甲方ID
     */
    private Long eid;

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    private Integer firstType;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
     */
    private Integer agreementType;

    /**
     * 乙方ID
     */
    private Long secondEid;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    private Integer authStatus;

    /**
     * 协议生效状态：1-正常 2-中止 3-作废
     */
    private Integer effectStatus;

}
