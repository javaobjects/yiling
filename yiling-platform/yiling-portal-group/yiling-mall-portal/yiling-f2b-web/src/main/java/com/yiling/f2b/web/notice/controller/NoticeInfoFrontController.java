package com.yiling.f2b.web.notice.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.web.notice.vo.NoticeInfoDetailsVO;
import com.yiling.f2b.web.notice.vo.NoticeInfoFrontVO;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.notice.api.NoticeInfoApi;
import com.yiling.mall.notice.dto.NoticeBeforeAfterDTO;
import com.yiling.mall.notice.dto.NoticeInfoDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */

@RestController
@Api(tags = "公告后台管理")
@RequestMapping("/notice/info/front")
public class NoticeInfoFrontController {

    @DubboReference
    NoticeInfoApi noticeInfoApi;

    @ApiOperation(value = "POP首页获取公告详情列表")
    @PostMapping("/get")
    public Result<Page<NoticeInfoFrontVO>> getNoticeInfoPage(@RequestBody @Valid QueryPageListForm form){
        QueryPageListRequest request = PojoUtils.map(form, QueryPageListRequest.class);
        Page<NoticeInfoDTO> page = noticeInfoApi.getAvailableNoticeInfoPage(request);
        return Result.success(PojoUtils.map(page,NoticeInfoFrontVO.class));
    }

    @ApiOperation(value = "POP首页获取公告单个详情信息")
    @GetMapping("/getDetails")
    public Result<NoticeInfoDetailsVO> getNoticeInfoDetails(@ApiParam(value = "公告ID", required = true) @RequestParam("id") Long id){
        NoticeInfoDTO noticeInfoDTO = noticeInfoApi.getDetails(id);
        NoticeBeforeAfterDTO noticeBeforeAfterDTO = noticeInfoApi.getBeforeAfterNotice(id);
        NoticeInfoDetailsVO noticeInfoDetailsVO = PojoUtils.map(noticeInfoDTO, NoticeInfoDetailsVO.class);
        PojoUtils.map(noticeBeforeAfterDTO, noticeInfoDetailsVO);
        return Result.success(noticeInfoDetailsVO);
    }

    
}
