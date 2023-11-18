package com.yiling.dataflow.flowcollect.api.impl;

import com.yiling.dataflow.flowcollect.api.FlowCollectUnuploadReasonApi;
import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowCollectUnUploadRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectUnuploadReasonRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectUnuploadReasonDO;
import com.yiling.dataflow.flowcollect.service.FlowCollectUnuploadReasonService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@DubboService
public class FlowCollectUnuploadReasonApiImpl implements FlowCollectUnuploadReasonApi {
    @Resource
    private FlowCollectUnuploadReasonService flowCollectUnuploadReasonService;
    @Override
    public Long saveOrUpdate(SaveFlowCollectUnuploadReasonRequest request) {
        FlowCollectUnuploadReasonDO unuploadReasonDO=new FlowCollectUnuploadReasonDO();
        PojoUtils.map(request,unuploadReasonDO);
        if(Objects.isNull(unuploadReasonDO.getId())){
            unuploadReasonDO.setCreateTime(request.getOpTime());
            unuploadReasonDO.setCreateUser(request.getOpUserId());
        }
        unuploadReasonDO.setUpdateTime(request.getOpTime());
        unuploadReasonDO.setUpdateUser(request.getOpUserId());
        flowCollectUnuploadReasonService.saveOrUpdate(unuploadReasonDO);
        return unuploadReasonDO.getId();
    }

    @Override
    public List<FlowCollectUnuploadReasonDTO> listByCrmIdAndDate(QueryFlowCollectUnUploadRequest request) {
        return flowCollectUnuploadReasonService.listByCrmIdAndDate(request);
    }
}
