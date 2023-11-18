package com.yiling.ih.pharmacy.api;

import com.yiling.ih.pharmacy.dto.IHPharmacyDTO;
import com.yiling.ih.pharmacy.dto.request.SyncPharmacyRequest;
import com.yiling.ih.pharmacy.dto.request.UpdateIHPharmacyStatusRequest;

/**
 * 互联网医院终端药店API
 *
 * @author: fan.shen
 * @date: 2022-11-18
 */
public interface IHPharmacyApi {

    /**
     * 同步药店
     *
     * @param request
     * @return
     */
    IHPharmacyDTO syncPharmacy(SyncPharmacyRequest request);

    /**
     * 修改药店合作状态
     *
     * @param request
     * @return
     */
    Boolean updatePharmacyStatus(UpdateIHPharmacyStatusRequest request);

}
