package com.yiling.hmc.gzh.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGzhGreetingRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 场景id
     */
    private Integer sceneId;

    /**
     * 草稿版本
     */
    private String draftVersion;

    /**
     * 备注
     */
    private String remark;

}
