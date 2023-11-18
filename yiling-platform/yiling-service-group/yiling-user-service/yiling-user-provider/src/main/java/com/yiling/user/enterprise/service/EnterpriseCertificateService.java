package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.entity.EnterpriseCertificateDO;

/**
 * <p>
 * 企业资质 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-21
 */
public interface EnterpriseCertificateService extends BaseService<EnterpriseCertificateDO> {

    /**
     * 获取企业资质列表
     *
     * @param eid 企业ID
     * @return
     */
    List<EnterpriseCertificateDO> listByEid(Long eid);

    /**
     * 保存企业资质
     * @param certificateList
     * @return
     */
    boolean saveCertificateList(List<CreateEnterpriseCertificateRequest> certificateList);

    /**
     * 删除企业资质
     * @param eid
     * @return
     */
    boolean deleteByEid(Long eid);


}
