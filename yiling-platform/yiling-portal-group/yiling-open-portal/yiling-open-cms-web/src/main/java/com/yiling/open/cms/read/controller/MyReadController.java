package com.yiling.open.cms.read.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.cms.read.api.MyReadApi;
import com.yiling.cms.read.dto.MyReadDTO;
import com.yiling.cms.read.dto.request.QueryMyReadPageRequest;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.cms.read.form.QueryMyReadPageForm;
import com.yiling.open.cms.read.vo.MyReadVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Api(tags = "我的阅读")
@RestController
@RequestMapping("/read")
public class MyReadController {
    @DubboReference
    private MyReadApi myReadApi;

    @ApiOperation("分页列表")
    @PostMapping("queryPage")
    public Result<Page<MyReadVO>> queryPage(@RequestBody QueryMyReadPageForm form){
        QueryMyReadPageRequest request = new QueryMyReadPageRequest();
        PojoUtils.map(form,request);
        request.setOpUserId(form.getWxDoctorId());
        request.setSource(BusinessLineEnum.DOCTOR.getCode());
        Page<MyReadDTO> myReadDTOPage = myReadApi.queryPage(request);
        Page<MyReadVO> myReadVOPage = PojoUtils.map(myReadDTOPage,MyReadVO.class);
        return Result.success(myReadVOPage);
    }
}