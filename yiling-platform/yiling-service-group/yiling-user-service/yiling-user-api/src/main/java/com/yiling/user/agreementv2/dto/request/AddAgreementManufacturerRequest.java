package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新增厂家 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementManufacturerRequest extends BaseRequest {

    /**
     * 厂家编号
     */
    private Long eid;

    /**
     * 厂家名称
     */
    private String ename;

    /**
     * 厂家类型 1-生产厂家 2-品牌厂家
     */
    private Integer type;

}
