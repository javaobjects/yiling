package com.yiling.hmc.gzh.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查询公众号欢迎语request
 *
 * @author: fan.shen
 * @date: 2023/03/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryGzhGreetingRequest extends QueryPageListRequest {

    /**
     * 场景id
     */
    private Integer sceneId;
}
