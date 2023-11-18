package com.yiling.hmc.pharmacy.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 获取会议签到信息
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitPharmacyRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

}
