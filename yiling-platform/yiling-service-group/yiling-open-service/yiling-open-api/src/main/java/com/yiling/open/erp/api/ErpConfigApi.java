package com.yiling.open.erp.api;

import java.util.List;

import com.yiling.open.erp.dto.ClientPushConfigDTO;
import com.yiling.open.erp.dto.ClientSysConfigDTO;
import com.yiling.open.erp.dto.ClientTaskConfigDTO;

/**
 * 客户端工具配置信息
 * @author shuan
 */
public interface ErpConfigApi {

    /**
     * 配置信息
     * @param sysConfig
     * @return
     */
    Integer insertSysConfig(ClientSysConfigDTO sysConfig);

    /**
     * 配置信息
     * @param pushConfig
     * @return
     */
    Integer insertPushConfig(ClientPushConfigDTO pushConfig);

    /**
     * 配置信息
     * @param taskConfig
     * @return
     */
    Integer insertTaskConfig(ClientTaskConfigDTO taskConfig);

    /**
     * 根据suid获取详情
     * @param suId
     * @return
     */
    ClientSysConfigDTO getSysConfigBySuid(Long suId);

    /**
     * 根据suid获取详情
     * @param suId
     * @return
     */
    ClientPushConfigDTO getPushConfigBySuid(Long suId);

    /**
     * 根据suid获取详情
     * @param suId
     * @return
     */
    List<ClientTaskConfigDTO> getTaskConfigListBySuid(Long suId);

    /**
     * 根据suid获取详情
     * @param suId
     * @param taskNo
     * @return
     */
    ClientTaskConfigDTO getByTaskNoAndSuId(Long suId,String taskNo);
}
