package com.yiling.user.enterprise.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存单个企业标签信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveEnterpriseTagsRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    @Min(1)
    private Long eid;

    /**
     * 企业标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 关联方式：1-手动 2-自动
     */
    private Integer type;
}
