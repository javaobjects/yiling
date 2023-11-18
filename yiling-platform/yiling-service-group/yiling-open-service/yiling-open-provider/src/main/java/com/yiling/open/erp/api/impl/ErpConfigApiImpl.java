package com.yiling.open.erp.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpConfigApi;
import com.yiling.open.erp.dao.ClientPushConfigMapper;
import com.yiling.open.erp.dao.ClientSysConfigMapper;
import com.yiling.open.erp.dao.ClientTaskConfigMapper;
import com.yiling.open.erp.dto.ClientPushConfigDTO;
import com.yiling.open.erp.dto.ClientSysConfigDTO;
import com.yiling.open.erp.dto.ClientTaskConfigDTO;
import com.yiling.open.erp.entity.ClientPushConfigDO;
import com.yiling.open.erp.entity.ClientSysConfigDO;
import com.yiling.open.erp.entity.ClientTaskConfigDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpConfigApiImpl implements ErpConfigApi {

    @Autowired
    private ClientSysConfigMapper  clientSysConfigMapper;
    @Autowired
    private ClientPushConfigMapper clientPushConfigMapper;
    @Autowired
    private ClientTaskConfigMapper clientTaskConfigMapper;

    @Override
    public Integer insertSysConfig(ClientSysConfigDTO sysConfig) {
        try {
            clientSysConfigMapper.deleteBySuId(sysConfig.getSuId());
            int i = clientSysConfigMapper.save(PojoUtils.map(sysConfig, ClientSysConfigDO.class));
            return i;
        }catch (Exception e){
            log.error("[ErpConfigApiImpl][insertSysConfig] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Integer insertPushConfig(ClientPushConfigDTO pushConfig) {
        try {
        clientPushConfigMapper.deleteBySuId(pushConfig.getSuId());
         int i= clientPushConfigMapper.save(PojoUtils.map(pushConfig, ClientPushConfigDO.class));
        return i;
        }catch (Exception e){
            log.error("[ErpConfigApiImpl][insertPushConfig] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Integer insertTaskConfig(ClientTaskConfigDTO taskConfig) {
        try{
        clientTaskConfigMapper.deteleBySuIdAndTaskNo(taskConfig.getSuId(),taskConfig.getTaskNo());
        int i= clientTaskConfigMapper.save(PojoUtils.map(taskConfig, ClientTaskConfigDO.class));
        return i;
        }catch (Exception e){
            log.error("[ErpConfigApiImpl][insertTaskConfig] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ClientSysConfigDTO getSysConfigBySuid(Long suId) {
        try{
            return PojoUtils.map(clientSysConfigMapper.getBySuid(suId), ClientSysConfigDTO.class);
        }catch (Exception e){
            log.error("[ErpConfigApiImpl][getSysConfigBySuid] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ClientPushConfigDTO getPushConfigBySuid(Long suId) {
        try{
            return PojoUtils.map(clientPushConfigMapper.getBySuid(suId), ClientPushConfigDTO.class);
        }catch (Exception e){
            log.error("[ErpConfigApiImpl][getPushConfigBySuid] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public List<ClientTaskConfigDTO> getTaskConfigListBySuid(Long suId) {
        try{
            return PojoUtils.map(clientTaskConfigMapper.getListBySuid(suId), ClientTaskConfigDTO.class);
        }catch (Exception e){
            log.error("[ErpConfigApiImpl][getTaskConfigListBySuid] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ClientTaskConfigDTO getByTaskNoAndSuId(Long suId,String taskNo) {
        return PojoUtils.map(clientTaskConfigMapper.getByTaskNoAndSuId(suId,taskNo),ClientTaskConfigDTO.class);
    }
}
