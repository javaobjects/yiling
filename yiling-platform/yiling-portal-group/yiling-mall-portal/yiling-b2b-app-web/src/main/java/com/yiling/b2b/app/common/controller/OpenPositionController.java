package com.yiling.b2b.app.common.controller;


import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.common.form.OpenPositionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.mall.openposition.api.OpenPositionApi;
import com.yiling.mall.openposition.dto.OpenPositionDTO;
import com.yiling.mall.openposition.enums.OpenPositionPlatformEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B-开屏位 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-12
 */
@Slf4j
@Api(tags = "开屏位接口")
@RestController
@RequestMapping("/openPosition")
public class OpenPositionController extends BaseController {

    @DubboReference
    OpenPositionApi openPositionApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "获取首页开屏位图")
    @GetMapping("/get")
    public Result<OpenPositionVO> get(@CurrentUser CurrentStaffInfo staffInfo) {
        OpenPositionDTO openPositionDTO = openPositionApi.getOpenPositionPicture(OpenPositionPlatformEnum.DYH.getCode());
        OpenPositionVO positionVO = PojoUtils.map(openPositionDTO, OpenPositionVO.class);
        if (Objects.nonNull(positionVO)) {
            positionVO.setPicture(fileService.getUrl(openPositionDTO.getPicture(), FileTypeEnum.B2B_OPEN_POSITION));
        }
        return Result.success(positionVO);
    }


}
