package com.yiling.admin.pop.navigation.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.navigation.form.QueryNavigationInfoPageForm;
import com.yiling.admin.pop.navigation.form.SaveNavigationInfoForm;
import com.yiling.admin.pop.navigation.form.UpdateNavigationInfoForm;
import com.yiling.admin.pop.navigation.vo.NavigationInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.mall.navigation.api.NavigationInfoApi;
import com.yiling.mall.navigation.dto.NavigationInfoDTO;
import com.yiling.mall.navigation.dto.request.QueryNavigationInfoPageRequest;
import com.yiling.mall.navigation.dto.request.SaveNavigationInfoRequest;
import com.yiling.mall.navigation.dto.request.UpdateNavigationInfoRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@RestController
@Api(tags = "导航管理")
@RequestMapping("/navigation/info/")
public class NavigationInfoController extends BaseController {

    @DubboReference
    NavigationInfoApi navigationInfoApi;

    @ApiOperation(value = "管理后台获取导航详情列表")
    @PostMapping("/get")
    public Result<Page<NavigationInfoVO>> getNavigationInfo(@CurrentUser CurrentAdminInfo staffInfo,
                                                            @RequestBody @Valid QueryNavigationInfoPageForm form){
        QueryNavigationInfoPageRequest request = PojoUtils.map(form, QueryNavigationInfoPageRequest.class);


        request.setEid(Constants.YILING_EID);
        Page<NavigationInfoDTO> page = navigationInfoApi.getNavigationInfo(request);
        Page<NavigationInfoVO> map = PojoUtils.map(page, NavigationInfoVO.class);
        return Result.success(map);
    }

    @ApiOperation(value = "管理后台保存导航信息")
    @PostMapping("/save")
    public Result<BoolObject> saveNavigationInfo(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveNavigationInfoForm form){
        SaveNavigationInfoRequest request = PojoUtils.map(form, SaveNavigationInfoRequest.class);
        request.setEid(Constants.YILING_EID);
        Boolean result = navigationInfoApi.saveNavigationInfo(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "管理后台修改导航信息")
    @PostMapping("/update")
    public Result<BoolObject> updateNavigationInfo(@RequestBody @Valid UpdateNavigationInfoForm form){
        Boolean result = navigationInfoApi.updateNavigationInfo(PojoUtils.map(form, UpdateNavigationInfoRequest.class));
        return Result.success(new BoolObject(result));
    }

}
