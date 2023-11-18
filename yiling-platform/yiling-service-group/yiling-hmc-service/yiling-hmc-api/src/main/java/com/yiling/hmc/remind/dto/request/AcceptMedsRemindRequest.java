package com.yiling.hmc.remind.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 接收用药提醒入参
 * @author: fan.shen
 * @date: 2022/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AcceptMedsRemindRequest extends BaseRequest {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * id
     */
    private Long id;


}