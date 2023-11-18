package com.yiling.hmc.pharmacy.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.pharmacy.dto.PharmacyDTO;
import com.yiling.hmc.pharmacy.dto.request.PharmacyPageRequest;
import com.yiling.hmc.pharmacy.dto.request.SubmitPharmacyRequest;
import com.yiling.hmc.pharmacy.dto.request.UpdatePharmacyStatusRequest;

import java.util.List;

/**
 * HMC 会议签到 API
 *
 * @Author fan.shen
 * @Date 2023-04-12
 */
public interface HmcPharmacyApi {

    /**
     * 保存
     * @param request
     */
    Long savePharmacy(SubmitPharmacyRequest request);

    /**
     * 停启用
     * @param request
     * @return
     */
    Boolean updatePharmacyStatus(UpdatePharmacyStatusRequest request);

    /**
     * 终端列表
     * @param request
     * @return
     */
    Page<PharmacyDTO> pharmacyPageList(PharmacyPageRequest request);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    PharmacyDTO getById(Long id);

    /**
     * 更新
     * @param pharmacyDTO
     */
    void updatePharmacyQrCode(PharmacyDTO pharmacyDTO);

    /**
     * 列表
     * @return
     */
    List<PharmacyDTO> pharmacyList();

    /**
     * 校验是否添加过
     * @param request
     * @return
     */
    boolean check(SubmitPharmacyRequest request);
}
