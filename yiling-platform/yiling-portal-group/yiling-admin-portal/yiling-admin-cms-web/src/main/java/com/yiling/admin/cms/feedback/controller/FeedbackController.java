package com.yiling.admin.cms.feedback.controller;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.feedback.form.QueryFeedbackPageListForm;
import com.yiling.admin.cms.feedback.vo.FeedbackVO;
import com.yiling.cms.feedback.api.FeedbackApi;
import com.yiling.cms.feedback.dto.FeedbackDTO;
import com.yiling.cms.feedback.dto.request.QueryFeedbackPageListRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 使用反馈 前端控制器
 * </p>
 *
 * @author gxl
 * @date 2022-07-27
 */
@Api(tags = "用户反馈")
@RestController
@RequestMapping("/feedback")
public class FeedbackController extends BaseController {

    @DubboReference
    private FeedbackApi feedbackApi;

    @Autowired
    private FileService fileService;
    @ApiOperation("列表")
    @GetMapping("queryPage")
    public Result<Page<FeedbackVO>>  queryPage(QueryFeedbackPageListForm form){
        QueryFeedbackPageListRequest request = new QueryFeedbackPageListRequest();
        PojoUtils.map(form,request);
        Page<FeedbackDTO> feedbackDTOPage = feedbackApi.queryPage(request);
        List<FeedbackDTO> records = feedbackDTOPage.getRecords();
        if(CollUtil.isEmpty(records)){
            return Result.success(form.getPage());
        }
        records.forEach(feedbackDTO -> {
            if(StrUtil.isNotEmpty(feedbackDTO.getFeedbackPic())){
                List<String> list = Arrays.asList(feedbackDTO.getFeedbackPic().split(","));
                List<String> newlist = Lists.newArrayList();
                list.forEach(s->{
                    newlist.add(fileService.getUrl(s, FileTypeEnum.FEEDBACK_PIC)) ;
                });
                feedbackDTO.setFeedbackPicList(newlist);
            }
        });
        Page<FeedbackVO> feedbackVOPage = PojoUtils.map(feedbackDTOPage,FeedbackVO.class);
        return Result.success(feedbackVOPage);
    }

}
