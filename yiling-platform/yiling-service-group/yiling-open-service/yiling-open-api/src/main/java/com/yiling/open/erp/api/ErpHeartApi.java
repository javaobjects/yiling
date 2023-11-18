package com.yiling.open.erp.api;

import java.util.List;

import com.yiling.open.erp.bo.ErpHeartBeatCountBO;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.QueryHeartBeatCountRequest;

/**
 * 心跳信息
 * @author shuan
 */
public interface ErpHeartApi {

    /**
     * 心跳信息
     * @param sysHeartBeat
     * @return
     */
    Integer insertErpHeart(SysHeartBeatDTO sysHeartBeat);

    /**
     * 根据suid、开始/结束时间  统计心跳数量
     * @param request
     * @return
     */
    List<ErpHeartBeatCountBO> getErpHeartCount(QueryHeartBeatCountRequest request);

}
