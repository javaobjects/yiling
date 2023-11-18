package com.yiling.hmc.pharmacy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.pharmacy.dto.PharmacyDTO;
import com.yiling.hmc.pharmacy.dto.request.PharmacyPageRequest;
import com.yiling.hmc.pharmacy.dto.request.SubmitPharmacyRequest;
import com.yiling.hmc.pharmacy.dto.request.UpdatePharmacyStatusRequest;
import com.yiling.hmc.pharmacy.entity.PharmacyDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 终端药店 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-05-06
 */
public interface PharmacyService extends BaseService<PharmacyDO> {

    /**
     * 保存
     * @param request
     * @return
     */
    Long savePharmacy(SubmitPharmacyRequest request);

    /**
     * 停启用
     * @param request
     * @return
     */
    Boolean updatePharmacyStatus(UpdatePharmacyStatusRequest request);

    /**
     * 列表
     * @param request
     * @return
     */
    Page<PharmacyDTO> pharmacyPageList(PharmacyPageRequest request);

    /**
     * 更新
     * @param pharmacyDTO
     */
    void updatePharmacyQrCode(PharmacyDTO pharmacyDTO);

    /**
     *
     * @return
     */
    List<PharmacyDTO> pharmacyList();

    /**
     * 根据ihEid查询
     * @param ihEid
     * @return
     */
    PharmacyDTO findByIhEid(Long ihEid);

    /**
     * 校验是否添加过
     * @param request
     * @return
     */
    boolean check(SubmitPharmacyRequest request);
}
