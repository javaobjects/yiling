package com.yiling.dataflow.flowcollect.service;

import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowCollectUnUploadRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectUnuploadReasonDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 日流向统计未上传原因表 服务类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
public interface FlowCollectUnuploadReasonService extends BaseService<FlowCollectUnuploadReasonDO> {

    List<FlowCollectUnuploadReasonDTO> listByCrmIdAndDate(QueryFlowCollectUnUploadRequest request);
}
