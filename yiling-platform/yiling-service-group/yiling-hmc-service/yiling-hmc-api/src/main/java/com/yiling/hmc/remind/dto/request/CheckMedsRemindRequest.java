package com.yiling.hmc.remind.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 设为已用/未用入参
 * @author: fan.shen
 * @date: 2022/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckMedsRemindRequest extends BaseRequest {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * id
     */
    private Long id;

    /**
     * 确认状态 1-已确认，2-未确认
     */
    private Integer confirmStatus;

}