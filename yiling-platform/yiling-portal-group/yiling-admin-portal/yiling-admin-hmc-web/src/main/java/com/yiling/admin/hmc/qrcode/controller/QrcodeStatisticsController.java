package com.yiling.admin.hmc.qrcode.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.qrcode.form.QueryQrcodeStatisticsForm;
import com.yiling.admin.hmc.qrcode.form.QueryQrcodeStatisticsPageForm;
import com.yiling.admin.hmc.qrcode.vo.QrcodeStatisticsVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.qrcode.api.QrcodeStatisticsApi;
import com.yiling.hmc.qrcode.dto.QrcodeStatisticsDTO;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsPageRequest;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/4/2
 */
@RestController
@Api(tags = "二维码统计")
@RequestMapping("/qrcode/statistics")
@Slf4j
public class QrcodeStatisticsController extends BaseController {

    @DubboReference
    QrcodeStatisticsApi qrcodeStatisticsApi;

    @ApiOperation(value = "分页查询", httpMethod = "GET")
    @GetMapping("queryPage")
    public Result<Page<QrcodeStatisticsVO>> queryPage(@Valid QueryQrcodeStatisticsPageForm form){
        QueryQrcodeStatisticsPageRequest request = new QueryQrcodeStatisticsPageRequest();
        PojoUtils.map(form,request);
        Page<QrcodeStatisticsDTO> statisticsDTOPage = qrcodeStatisticsApi.queryPage(request);
        Page<QrcodeStatisticsVO> qrcodeStatisticsVOPage = PojoUtils.map(statisticsDTOPage,QrcodeStatisticsVO.class);
        return Result.success(qrcodeStatisticsVOPage);
    }
    @ApiOperation(value = "合计", httpMethod = "GET")
    @GetMapping("getTotal")
    public Result<QrcodeStatisticsVO> getTotal(@Valid QueryQrcodeStatisticsForm form){
        QueryQrcodeStatisticsRequest request = new QueryQrcodeStatisticsRequest();
        PojoUtils.map(form,request);
        QrcodeStatisticsDTO total = qrcodeStatisticsApi.getTotal(request);
        QrcodeStatisticsVO qrcodeStatisticsVO = new QrcodeStatisticsVO();
        PojoUtils.map(total,qrcodeStatisticsVO);
        return Result.success(qrcodeStatisticsVO);
    }
}