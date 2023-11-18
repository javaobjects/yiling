package com.yiling.ih.pharmacy.api.impl;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.patient.enums.IHErrorCode;
import com.yiling.ih.pharmacy.api.IHPharmacyApi;
import com.yiling.ih.pharmacy.dto.IHPharmacyDTO;
import com.yiling.ih.pharmacy.dto.request.SyncPharmacyRequest;
import com.yiling.ih.pharmacy.dto.request.UpdateIHPharmacyStatusRequest;
import com.yiling.ih.pharmacy.feign.IHPharmacyFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * IH 终端药店服务api
 *
 * @author: fan.shen
 * @date: 2022-11-18
 */
@Slf4j
@DubboService
public class IHPharmacyApiImpl implements IHPharmacyApi {

    @Autowired
    private IHPharmacyFeignClient pharmacyFeignClient;

    @Override
    public IHPharmacyDTO syncPharmacy(SyncPharmacyRequest request) {
        ApiResult<IHPharmacyDTO> apiResult = pharmacyFeignClient.syncPharmacy(request);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            log.error("[syncPharmacy]调用IH服务同步终端药店失败");
            throw new BusinessException(IHErrorCode.SYNC_PHARMACY_ERROR);
        }
        return apiResult.getData();
    }

    @Override
    public Boolean updatePharmacyStatus(UpdateIHPharmacyStatusRequest request) {
        ApiResult<Boolean> apiResult = pharmacyFeignClient.updatePharmacyStatus(request);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            log.error("[updatePharmacyStatus]调用IH服务修改终端药店合作状态失败");
            throw new BusinessException(IHErrorCode.SYNC_PHARMACY_ERROR);
        }
        return apiResult.getData();
    }
}
