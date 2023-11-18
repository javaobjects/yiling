package com.yiling.hmc.qrcode.controller;


import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.qrcode.api.QrcodeStatisticsApi;
import com.yiling.hmc.qrcode.dto.request.SaveQrcodeStatisticsRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 二维码统计 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-04-02
 */
@Api(tags = "二维码页面统计")
@RestController
@RequestMapping("/qrcode/statistics")
public class QrcodeStatisticsController extends BaseController {

    @DubboReference
    QrcodeStatisticsApi qrcodeStatisticsApi;

    @ApiOperation("页面打开次数")
    @PostMapping("savePageView")
    public Result<Boolean> savePageView(){
        SaveQrcodeStatisticsRequest request = new SaveQrcodeStatisticsRequest();
        request.setPageView(1);
        qrcodeStatisticsApi.save(request);
        return Result.success(true);
    }

    @ApiOperation("广告点击次数")
    @PostMapping("adClick")
    public Result<Boolean> adClick(){
        SaveQrcodeStatisticsRequest request = new SaveQrcodeStatisticsRequest();
        request.setAdClick(1);
        qrcodeStatisticsApi.save(request);
        return Result.success(true);
    }

}
