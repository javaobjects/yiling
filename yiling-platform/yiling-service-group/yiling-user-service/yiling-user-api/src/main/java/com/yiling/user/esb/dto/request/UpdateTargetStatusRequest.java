package com.yiling.user.esb.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新上传指标状态 Request
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTargetStatusRequest extends BaseRequest {

    /**
     * 业务架构ID
     */
    private Long id;

    /**
     * 是否可以上传指标：0-否 1-是
     */
    private Integer targetStatus;

}
