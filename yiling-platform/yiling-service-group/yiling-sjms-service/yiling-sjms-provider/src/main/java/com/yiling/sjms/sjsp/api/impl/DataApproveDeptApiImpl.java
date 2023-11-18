package com.yiling.sjms.sjsp.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbAttachmentApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.request.SaveGBFileRequest;
import com.yiling.sjms.gb.service.GbAttachmentService;
import com.yiling.sjms.sjsp.api.DataApproveDeptApi;
import com.yiling.sjms.sjsp.dto.DataApproveDeptDTO;
import com.yiling.sjms.sjsp.service.DataApproveDeptService;

/**
 * 数据审核板块与部门信息对应
 *
 * @author: dexi.yao
 * @date: 2022/11/28
 */
@DubboService
public class DataApproveDeptApiImpl implements DataApproveDeptApi {

    @Autowired
    DataApproveDeptService deptService;

    @Override
    public DataApproveDeptDTO getDataApproveDeptByDeptName(String deptName) {
        return deptService.getDataApproveDeptByDeptName(deptName);
    }

    @Override
    public DataApproveDeptDTO getDataApproveDeptByDeptId(String deptId) {
        return deptService.getDataApproveDeptByDeptId(deptId);
    }
}
