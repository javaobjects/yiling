package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpHeartApi;
import com.yiling.open.erp.bo.ErpHeartBeatCountBO;
import com.yiling.open.erp.dao.SysHeartBeatMapper;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.QueryHeartBeatCountRequest;
import com.yiling.open.erp.entity.SysHeartBeatDO;
import com.yiling.open.erp.util.OpenStringUtils;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpHeartApiImpl implements ErpHeartApi {

    @Autowired
    private SysHeartBeatMapper sysHeartBeatMapper;

    @Override
    public Integer insertErpHeart(SysHeartBeatDTO sysHeartBeat) {
        try {
            String processId = OpenStringUtils.clearAllSpace(sysHeartBeat.getProcessId());
            if (processId.contains(",")){
                List<String> list = Convert.toList(String.class, processId);
                processId = String.join(",", list.stream().distinct().collect(Collectors.toList()));
            }
            if(processId.length() > 800){
                processId = processId.substring(0, 800);
            }
            sysHeartBeat.setProcessId(processId);
            return sysHeartBeatMapper.insertSelective(PojoUtils.map(sysHeartBeat, SysHeartBeatDO.class));
        } catch (Exception e) {
            log.error("[ErpHeartApiImpl][insertErpHeart] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ErpHeartBeatCountBO> getErpHeartCount(QueryHeartBeatCountRequest request) {
        try {
            return sysHeartBeatMapper.getErpHeartCount(request);
        } catch (Exception e) {
            log.error("[ErpHeartApiImpl][getErpHeartBySuid] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
