package com.yiling.hmc.pharmacy.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.pharmacy.api.HmcPharmacyApi;
import com.yiling.hmc.pharmacy.dto.PharmacyDTO;
import com.yiling.hmc.pharmacy.dto.request.PharmacyPageRequest;
import com.yiling.hmc.pharmacy.dto.request.SubmitPharmacyRequest;
import com.yiling.hmc.pharmacy.dto.request.UpdatePharmacyStatusRequest;
import com.yiling.hmc.pharmacy.entity.PharmacyDO;
import com.yiling.hmc.pharmacy.service.PharmacyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: fan.shen
 * @date: 2023-04-12
 */
@Slf4j
@DubboService
public class HmcPharmacyApiImpl implements HmcPharmacyApi {

    @Autowired
    PharmacyService pharmacyService;

    @Override
    public Long savePharmacy(SubmitPharmacyRequest request) {
        return pharmacyService.savePharmacy(request);
    }

    @Override
    public Boolean updatePharmacyStatus(UpdatePharmacyStatusRequest request) {
        return pharmacyService.updatePharmacyStatus(request);
    }

    @Override
    public Page<PharmacyDTO> pharmacyPageList(PharmacyPageRequest request) {
        return pharmacyService.pharmacyPageList(request);
    }

    @Override
    public PharmacyDTO getById(Long id) {
        PharmacyDO pharmacy = pharmacyService.getById(id);
        return PojoUtils.map(pharmacy, PharmacyDTO.class);
    }

    @Override
    public void updatePharmacyQrCode(PharmacyDTO pharmacyDTO) {
        pharmacyService.updatePharmacyQrCode(pharmacyDTO);
    }

    @Override
    public List<PharmacyDTO> pharmacyList() {
        return pharmacyService.pharmacyList();
    }

    @Override
    public boolean check(SubmitPharmacyRequest request) {
        return pharmacyService.check(request);
    }
}
