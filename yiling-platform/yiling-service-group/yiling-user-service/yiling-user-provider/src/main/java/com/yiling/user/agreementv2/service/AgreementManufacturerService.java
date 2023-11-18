package com.yiling.user.agreementv2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerRequest;
import com.yiling.user.agreementv2.entity.AgreementManufacturerDO;

/**
 * <p>
 * 厂家表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
public interface AgreementManufacturerService extends BaseService<AgreementManufacturerDO> {

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<AgreementManufacturerDO> queryListPage(QueryAgreementManufacturerRequest request);

    /**
     * 新增厂家
     * @param request
     * @return
     */
    Boolean addManufacturer(AddAgreementManufacturerRequest request);

    /**
     * 删除厂家
     * @param id
     * @param opUserId
     * @return
     */
    Boolean deleteManufacturer(Long id, Long opUserId);
}
