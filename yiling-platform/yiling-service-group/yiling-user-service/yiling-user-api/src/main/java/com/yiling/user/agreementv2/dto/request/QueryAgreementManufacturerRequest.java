package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家管理列表查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementManufacturerRequest extends QueryPageListRequest {

    /**
     * 厂家编号
     */
    private Long eid;

    /**
     * 厂家名称
     */
    private String ename;

}
