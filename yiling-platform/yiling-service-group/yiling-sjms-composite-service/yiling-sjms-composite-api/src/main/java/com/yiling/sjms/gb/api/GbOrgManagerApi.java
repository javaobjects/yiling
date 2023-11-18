package com.yiling.sjms.gb.api;

import java.util.List;
import java.util.Map;

import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

/**
 * 团购表单提交流程
 *
 * @author: shixing.sun
 * @date: 2022/11/28
 */
public interface GbOrgManagerApi {

    /**
     * 批量根据部门ID获取部门对应的事业部信息
     *
     * @param orgIds 部门ID列表
     * @return java.util.Map<java.lang.Long,com.yiling.user.esb.dto.EsbOrganizationDTO>
     * @author shixing.sun
     * @date 2023/3/10
     **/
     Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> orgIds);
}
