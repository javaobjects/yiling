package com.yiling.dataflow.flowcollect.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowCollectUnUploadRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectUnuploadReasonDO;
import com.yiling.dataflow.flowcollect.dao.FlowCollectUnuploadReasonMapper;
import com.yiling.dataflow.flowcollect.service.FlowCollectUnuploadReasonService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 日流向统计未上传原因表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Service
public class FlowCollectUnuploadReasonServiceImpl extends BaseServiceImpl<FlowCollectUnuploadReasonMapper, FlowCollectUnuploadReasonDO> implements FlowCollectUnuploadReasonService {

    @Override
    public List<FlowCollectUnuploadReasonDTO> listByCrmIdAndDate(QueryFlowCollectUnUploadRequest request) {
        QueryWrapper<FlowCollectUnuploadReasonDO> queryWrapper = new QueryWrapper<>();
        if(Objects.nonNull(request.getCrmEnterpriseId())){
            queryWrapper.lambda().eq(FlowCollectUnuploadReasonDO::getCrmEnterpriseId, request.getCrmEnterpriseId());
        }
        if(Objects.nonNull(request.getStatisticsTime())){
            queryWrapper.lambda().eq(FlowCollectUnuploadReasonDO::getStatisticsTime, request.getStatisticsTime());
        }

        if(CollUtil.isNotEmpty(request.getCrmEnterpriseIdList())){
            queryWrapper.lambda().in(FlowCollectUnuploadReasonDO::getCrmEnterpriseId,request.getCrmEnterpriseIdList());
        }
        if(Objects.nonNull(request.getStartDate())){
            queryWrapper.lambda().ge(FlowCollectUnuploadReasonDO::getStatisticsTime,request.getStartDate());
        }
        if(Objects.nonNull(request.getEndDate())){
            queryWrapper.lambda().le(FlowCollectUnuploadReasonDO::getStatisticsTime,request.getEndDate());
        }
        if(Objects.nonNull(request.getOpUserId())&&request.getOpUserId().longValue()>0){
            queryWrapper.lambda().eq(FlowCollectUnuploadReasonDO::getUpdateUser,request.getOpUserId());
        }
        queryWrapper.lambda().orderByAsc(FlowCollectUnuploadReasonDO::getUpdateTime);
        return PojoUtils.map(list(queryWrapper), FlowCollectUnuploadReasonDTO.class);
    }
}
