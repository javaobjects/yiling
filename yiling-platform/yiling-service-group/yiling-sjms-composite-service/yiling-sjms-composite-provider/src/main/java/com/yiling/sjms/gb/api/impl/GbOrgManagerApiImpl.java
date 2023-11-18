package com.yiling.sjms.gb.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.gb.api.GbFormSubmitProcessApi;
import com.yiling.sjms.gb.api.GbOrgManagerApi;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.service.GbFormSubmitService;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

/**
 * 团购表单提交流程
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class GbOrgManagerApiImpl implements GbOrgManagerApi {

    @DubboReference
    private BusinessDepartmentApi businessDepartmentApi;

    @Override
    public Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> orgIds) {
        Map<Long, EsbOrganizationDTO> longEsbOrganizationDTOMap = businessDepartmentApi.listByOrgIds(orgIds);
        return longEsbOrganizationDTOMap;
    }

}
