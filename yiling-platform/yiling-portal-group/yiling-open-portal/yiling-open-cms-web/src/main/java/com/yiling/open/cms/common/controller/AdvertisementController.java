package com.yiling.open.cms.common.controller;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.common.api.AdvertisementApi;
import com.yiling.hmc.common.dto.AdvertisementDTO;
import com.yiling.hmc.common.dto.request.AdvertisementListRequest;
import com.yiling.open.cms.common.form.AdvertisementForm;
import com.yiling.open.cms.common.vo.AdvertisementVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 广告
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-03-23
 */
@Api(tags = "广告")
@RestController
@RequestMapping("/advertisement/")
public class AdvertisementController extends BaseController {
    @DubboReference
    AdvertisementApi advertisementApi;

    @Autowired
    FileService fileService;

    @ApiOperation("医生小程序查询有效广告信息")
    @GetMapping("listByCondition")
    public Result<CollectionObject<List<AdvertisementVO>>> listByCondition(AdvertisementForm form) {
        AdvertisementListRequest request = PojoUtils.map(form, AdvertisementListRequest.class);
        List<AdvertisementDTO> dtoList = advertisementApi.listByCondition(request);
        List<AdvertisementVO> advertisementVOList = PojoUtils.map(dtoList, AdvertisementVO.class);
        advertisementVOList.forEach(advertisementVO -> {
            advertisementVO.setPic(fileService.getUrl(advertisementVO.getPic(), FileTypeEnum.ADVERTISEMENT_PIC));
        });
        CollectionObject<List<AdvertisementVO>> collectionObject = new CollectionObject(advertisementVOList);
        return Result.success(collectionObject);
    }
}
