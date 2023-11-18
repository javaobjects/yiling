package com.yiling.admin.pop.notice.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.notice.form.QueryNoticeInfoPageForm;
import com.yiling.admin.pop.notice.form.SaveNoticeInfoForm;
import com.yiling.admin.pop.notice.vo.NoticeInfoDetailsVO;
import com.yiling.admin.pop.notice.vo.NoticeInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.mall.notice.api.NoticeInfoApi;
import com.yiling.mall.notice.dto.NoticeInfoDTO;
import com.yiling.mall.notice.dto.request.QueryNoticeInfoPageRequest;
import com.yiling.mall.notice.dto.request.SaveNoticeInfoRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */

@RestController
@Api(tags = "公告后台管理")
@RequestMapping("/notice/info/")
public class NoticeInfoController  extends BaseController {

    @DubboReference
    NoticeInfoApi noticeInfoApi;

    @ApiOperation(value = "管理后台获取公告详情列表")
    @PostMapping("/get")
    public Result<Page<NoticeInfoVO>> getNoticeInfoPage(@RequestBody @Valid QueryNoticeInfoPageForm form){
        QueryNoticeInfoPageRequest request = PojoUtils.map(form, QueryNoticeInfoPageRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<NoticeInfoDTO> page = noticeInfoApi.getNoticeInfoPage(request);
        return Result.success(PojoUtils.map(page,NoticeInfoVO.class));
    }

    @ApiOperation(value = "管理后台获取公告单个详情信息")
    @GetMapping("/getDetails")
    public Result<NoticeInfoDetailsVO> getNoticeInfoDetails(@ApiParam(value = "公告ID", required = true) @RequestParam("id") Long id){
        NoticeInfoDTO noticeInfoDTO = noticeInfoApi.getDetails(id);
        return Result.success(PojoUtils.map(noticeInfoDTO, NoticeInfoDetailsVO.class));
    }

    @ApiOperation(value = "管理后台保存公告")
    @PostMapping("/save")
    public Result<BoolObject> saveNoticeInfo(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveNoticeInfoForm form){
        SaveNoticeInfoRequest request = PojoUtils.map(form, SaveNoticeInfoRequest.class);
        request.setEid(Constants.YILING_EID);
        Boolean result = noticeInfoApi.saveNoticeInfo(request);
        return Result.success(new BoolObject(result));
    }
    
}
