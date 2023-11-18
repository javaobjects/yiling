package com.yiling.ih.user.feign;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.user.dto.request.IdCardFrontPhotoOcrRequest;
import com.yiling.ih.user.feign.dto.response.HealthCardCommonInResponse;
import com.yiling.ih.user.feign.dto.response.IdCardFrontPhotoOcrResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 互联网医院 用户模块接口调用
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@FeignClient(name = "idCardFrontPhotoOcrClient", url = "${ih.service.baseUrl}")
public interface IdCardFrontPhotoOcrClient {

    /**
     * 身份证照片正面照 OCR
     *
     * @param request 身份证照片正面照 base64
     * @return
     */
    @PostMapping("/hmc/patient/ocrInfo")
    ApiResult<IdCardFrontPhotoOcrResponse> idCardFrontPhotoOcr(IdCardFrontPhotoOcrRequest request);

    /**
     * 获取健康卡信息
     *
     * @return
     */
    @GetMapping("/hmc/patient/getHealthCardCommonIn")
    ApiResult<HealthCardCommonInResponse> getHealthCardCommonIn();

}
