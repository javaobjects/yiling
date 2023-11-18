package com.yiling.open.cms.collect.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.api.MyCollectApi;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectPageRequest;
import com.yiling.cms.collect.dto.request.SaveCollectRequest;
import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.cms.collect.form.QueryMyCollectPageForm;
import com.yiling.open.cms.collect.form.SaveCollectForm;
import com.yiling.open.cms.collect.vo.MyCollectVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 我的收藏 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-07-29
 */
@Api(tags = "收藏")
@RestController
@RequestMapping("/collect")
public class MyCollectController extends BaseController {

    @DubboReference
    MyCollectApi myCollectApi;

    @ApiOperation("收藏/取消收藏")
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody @Valid SaveCollectForm form){
        SaveCollectRequest request = new SaveCollectRequest();
        request.setOpUserId(form.getWxDoctorId());
        PojoUtils.map(form,request);
        request.setSource(BusinessLineEnum.DOCTOR.getCode());
        myCollectApi.save(request);
        return Result.success(true);
    }

    @ApiOperation("分页列表")
    @PostMapping("queryPage")
    public Result<Page<MyCollectVO>> queryPage(@RequestBody @Valid QueryMyCollectPageForm form){
        QueryCollectPageRequest request = new QueryCollectPageRequest();
        PojoUtils.map(form,request);
        request.setOpUserId(form.getWxDoctorId());
        request.setSource(BusinessLineEnum.DOCTOR.getCode());
        Page<MyCollectDTO> myCollectDTOPage = myCollectApi.queryPage(request);
        Page<MyCollectVO> myReadVOPage = PojoUtils.map(myCollectDTOPage,MyCollectVO.class);
        return Result.success(myReadVOPage);
    }
}
