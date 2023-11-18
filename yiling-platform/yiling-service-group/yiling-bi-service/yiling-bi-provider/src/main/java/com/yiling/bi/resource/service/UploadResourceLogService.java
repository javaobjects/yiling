package com.yiling.bi.resource.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.dto.UploadResourceLogDTO;
import com.yiling.bi.resource.dto.request.UpdateResourceLogRequest;
import com.yiling.bi.resource.entity.InputLsflCoverDO;
import com.yiling.bi.resource.entity.InputLsflRecordDO;
import com.yiling.bi.resource.entity.UploadResourceDO;
import com.yiling.bi.resource.entity.UploadResourceLogDO;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
public interface UploadResourceLogService {

    boolean updateUploadResourceLog(UpdateResourceLogRequest request);

    String importInputLsflRecord(String id);

    UploadResourceLogDTO getUploadResourceLogById(String id);

    boolean handleInputLsflRecord(UploadResourceDTO uploadResourceDO, UploadResourceLogDO uploadResourceLogDO);

    boolean handleInputLsflCover(UploadResourceDTO uploadResourceDO, UploadResourceLogDO uploadResourceLogDO);

    <T> Future<Set<String>> executeRecordDataByThread(List<InputLsflRecordDO> successList, UploadResourceLogDO uploadResourceLogDO);

    <T> Future<Set<String>> executeCoverDataByThread(List<InputLsflCoverDO> successList, UploadResourceLogDO uploadResourceLogDO);
}
