package com.yiling.ih.pharmacy.feign;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.pharmacy.dto.IHGoodsDTO;
import com.yiling.ih.pharmacy.dto.IHPharmacyDTO;
import com.yiling.ih.pharmacy.dto.request.GetGoodsInfoRequest;
import com.yiling.ih.pharmacy.dto.request.ModifyGoodsInfoRequest;
import com.yiling.ih.pharmacy.dto.request.SyncPharmacyRequest;
import com.yiling.ih.pharmacy.dto.request.UpdateIHPharmacyStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 互联网医院 终端药店接口调用
 *
 * @author: fan.shen
 * @date: 2023-05-06
 */
@FeignClient(name = "iHPharmacyFeignClient", url = "${ih.service.baseUrl}")
public interface IHPharmacyFeignClient {

    /**
     * 同步终端药店
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/cooperationpharmacy/sync")
    ApiResult<IHPharmacyDTO> syncPharmacy(@RequestBody SyncPharmacyRequest request);

    /**
     * 终端药店状态
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/cooperationpharmacy/updateStatus")
    ApiResult<Boolean> updatePharmacyStatus(@RequestBody UpdateIHPharmacyStatusRequest request);

    /**
     * 获取销售价格库存信息
     *
     * @param request
     * @return
     */
    @PostMapping("")
    ApiResult<List<IHGoodsDTO>> getGoodsInfoByIdList(@RequestBody GetGoodsInfoRequest request);

    /**
     * 修改销售价格库存信息
     *
     * @param request
     * @return
     */
    @PostMapping("")
    ApiResult<Boolean> modifyGoodsInfoById(@RequestBody ModifyGoodsInfoRequest request);

}
