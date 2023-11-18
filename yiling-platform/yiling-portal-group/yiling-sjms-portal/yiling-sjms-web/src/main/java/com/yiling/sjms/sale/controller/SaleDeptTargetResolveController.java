package com.yiling.sjms.sale.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetDetailApi;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetResolveDetailApi;
import com.yiling.dataflow.sale.api.SaleDepartmentTargetApi;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetDetailBO;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetResolveDetailBO;
import com.yiling.dataflow.sale.bo.SaleDepartmentTargetBO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentTargetPageRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.sale.form.QueryResolveDetailPageForm;
import com.yiling.sjms.sale.form.QuerySaleDepartmentTargetPageForm;
import com.yiling.sjms.sale.form.QuerySaleDeptSubTargetDetailForm;
import com.yiling.sjms.sale.vo.SaleDepartmentSubTargetDetailVO;
import com.yiling.sjms.sale.vo.SaleDepartmentSubTargetResolveDetailVO;
import com.yiling.sjms.sale.vo.SaleDepartmentTargetVO;
import com.yiling.sjms.sale.vo.TargetResolveDetailVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2023/4/13
 */
@Slf4j
@RestController
@RequestMapping("/sale/depart/resolve")
@Api(tags = "销售指标分解")
public class SaleDeptTargetResolveController extends BaseController {

    @DubboReference
    SaleDepartmentSubTargetDetailApi saleDepartmentSubTargetDetailApi;

    @DubboReference
    SaleDepartmentSubTargetResolveDetailApi saleDepartmentSubTargetResolveDetailApi;

    @DubboReference
    SaleDepartmentTargetApi saleDepartmentTargetApi;


    @Autowired
    private FileService fileService;

    @ApiOperation(value = "销售指标查看-配置详情")
    @GetMapping("querySaleDeptSubTargetDetailPage")
    public Result<Page<SaleDepartmentSubTargetDetailVO>> querySaleDeptSubTargetDetailPage(@CurrentUser CurrentSjmsUserInfo userInfo, @Valid QuerySaleDeptSubTargetDetailForm form){
        QuerySaleDeptSubTargetDetailRequest request = new QuerySaleDeptSubTargetDetailRequest();
        PojoUtils.map(form, request);
        Page<SaleDepartmentSubTargetDetailBO> saleDepartmentSubTargetDetailBOPage = saleDepartmentSubTargetDetailApi.queryPage(request);
        Page<SaleDepartmentSubTargetDetailVO> saleDepartmentSubTargetDetailVOPage = PojoUtils.map(saleDepartmentSubTargetDetailBOPage, SaleDepartmentSubTargetDetailVO.class);
        return Result.success(saleDepartmentSubTargetDetailVOPage);
    }

    @ApiOperation(value = "销售指标查看")
    @GetMapping("querySaleDepartmentTargetPage")
    public Result<Page<SaleDepartmentTargetVO>>  querySaleDepartmentTargetPage(@CurrentUser CurrentSjmsUserInfo userInfo,@Valid QuerySaleDepartmentTargetPageForm form){
        QuerySaleDepartmentTargetPageRequest request = new QuerySaleDepartmentTargetPageRequest();
        PojoUtils.map(form, request);
        Page<SaleDepartmentTargetBO> saleDepartmentTargetBOPage = saleDepartmentTargetApi.querySaleDepartmentTargetPage(request);
        Page<SaleDepartmentTargetVO> saleDepartmentTargetVOPage = PojoUtils.map(saleDepartmentTargetBOPage, SaleDepartmentTargetVO.class);
        return Result.success(saleDepartmentTargetVOPage);
    }

    @ApiOperation(value = "分解模板列表")
    @GetMapping("queryResolveDetailPage")
    public Result<TargetResolveDetailVO> queryResolveDetailPage(@CurrentUser CurrentSjmsUserInfo userInfo, @Valid QueryResolveDetailPageForm form){
        SaleDepartmentTargetDTO saleDepartmentTargetDTO = saleDepartmentTargetApi.listBySaleTargetIdAndDepartId(form.getSaleTargetId(), form.getDepartId());
        QueryResolveDetailPageRequest resolveDetailPageRequest = new QueryResolveDetailPageRequest();
        PojoUtils.map(form,resolveDetailPageRequest);
        Page<SaleDepartmentSubTargetResolveDetailBO> resolveDetailBOPage = saleDepartmentSubTargetResolveDetailApi.queryPage(resolveDetailPageRequest);
        Page<SaleDepartmentSubTargetResolveDetailVO> resolveDetailVOPage = PojoUtils.map(resolveDetailBOPage,SaleDepartmentSubTargetResolveDetailVO.class);
        TargetResolveDetailVO targetResolveDetailVO = new TargetResolveDetailVO();
        if(StrUtil.isNotEmpty(saleDepartmentTargetDTO.getTemplateUrl())){
            targetResolveDetailVO.setTemplateUrl(fileService.getUrl(saleDepartmentTargetDTO.getTemplateUrl(),FileTypeEnum.SALE_TARGET_RESOLVE_FILE));
        }
        PojoUtils.map(resolveDetailVOPage,targetResolveDetailVO);
        return Result.success(targetResolveDetailVO);
    }
}