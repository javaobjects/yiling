package com.yiling.cms.content.dto.request;


import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 业务线模块设置
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-01
 */
@Data
@Accessors(chain = true)
public class LineModuleRequest implements java.io.Serializable {

    /**
     * 业务线Id
     */
    private Long lineId;

    /**
     * 业务线名称
     */
    private String lineName;

    /**
     * 内容权限 1-仅登录，2-需认证通过
     */
    private Integer contentAuth;

    /**
     * 是否精选 0-否，1-是
     */
    private Integer choseFlag;

    /**
     * 板块list
     */
    private List<ModuleRequest> moduleList;

}
