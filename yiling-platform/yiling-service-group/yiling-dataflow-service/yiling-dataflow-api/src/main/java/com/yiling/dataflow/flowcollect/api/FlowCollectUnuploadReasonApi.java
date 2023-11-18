package com.yiling.dataflow.flowcollect.api;

import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowCollectUnUploadRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectUnuploadReasonRequest;

import java.util.List;

public interface FlowCollectUnuploadReasonApi {
    Long saveOrUpdate(SaveFlowCollectUnuploadReasonRequest request);

    List<FlowCollectUnuploadReasonDTO> listByCrmIdAndDate(QueryFlowCollectUnUploadRequest request);
}
