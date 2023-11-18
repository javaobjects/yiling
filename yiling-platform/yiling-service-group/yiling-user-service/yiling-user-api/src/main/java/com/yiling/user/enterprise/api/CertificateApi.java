package com.yiling.user.enterprise.api;

import java.util.List;

import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;

/**
 * 企业资质 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
public interface CertificateApi {

    /**
     * 获取企业资质列表
     *
     * @param eid 企业ID
     * @return
     */
    List<EnterpriseCertificateDTO> listByEid(Long eid);

    /**
     * 保存企业资质
     * @param certificateList
     * @return
     */
    boolean save(List<CreateEnterpriseCertificateRequest> certificateList);

    /**
     * 删除企业资质
     * @param eid
     * @return
     */
    boolean deleteByEid(Long eid);

}
