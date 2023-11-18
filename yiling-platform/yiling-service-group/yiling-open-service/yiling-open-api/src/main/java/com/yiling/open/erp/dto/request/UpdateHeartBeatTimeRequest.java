package com.yiling.open.erp.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateHeartBeatTimeRequest extends BaseRequest {

    /**
     * 父类企业id
     */
    @NotNull
    private Long suId;

    private String versions;
}