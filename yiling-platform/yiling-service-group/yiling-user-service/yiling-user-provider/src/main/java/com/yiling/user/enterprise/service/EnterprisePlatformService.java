package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.dto.request.ImportEnterprisePlatformRequest;
import com.yiling.user.enterprise.entity.EnterprisePlatformDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;

/**
 * <p>
 * 企业开通平台表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-31
 */
public interface EnterprisePlatformService extends BaseService<EnterprisePlatformDO> {

    /**
     * 获取企业开通的平台信息
     *
     * @param eid 企业ID
     * @return
     */
    EnterprisePlatformDO getByEid(Long eid);

    /**
     * 批量获取企业开通的平台信息
     *
     * @param eids 企业ID列表
     * @return
     */
    List<EnterprisePlatformDO> listByEids(List<Long> eids);

    /**
     * 给企业开通平台
     *
     * @param eid 企业ID
     * @param platformEnumList 开通平台列表
     * @param enterpriseChannelEnum 渠道类型枚举，开通POP时必传
     * @param enterpriseHmcTypeEnum hmc类型枚举
     * @param opUserId 操作人ID
     * @return
     */
    boolean openPlatform(Long eid, List<PlatformEnum> platformEnumList, EnterpriseChannelEnum enterpriseChannelEnum, EnterpriseHmcTypeEnum enterpriseHmcTypeEnum, Long opUserId);

    /**
     * 关闭平台
     *
     * @param eid 企业ID
     * @param platformEnumList 关闭平台列表
     * @param opUserId 操作人ID
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/15
     **/
    boolean closePlatform(Long eid, List<PlatformEnum> platformEnumList, Long opUserId);

    /**
     * 导入企业开通平台
     *
     * @param request
     * @return
     */
    boolean importEnterprisePlatform(ImportEnterprisePlatformRequest request);
}
