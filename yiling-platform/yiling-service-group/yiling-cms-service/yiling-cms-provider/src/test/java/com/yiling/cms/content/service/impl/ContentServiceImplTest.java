package com.yiling.cms.content.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.QueryQAPageRequest;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.content.service.QaService;
import com.yiling.cms.evaluate.dto.request.EvaluateResultDetailRequest;
import com.yiling.cms.evaluate.dto.request.SubmitEvaluateResultRequest;
import com.yiling.cms.evaluate.service.HealthEvaluateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.BaseTest;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.service.ContentService;

import java.util.List;

/**
 * @author: gxl
 * @date: 2022/6/14
 */
class ContentServiceImplTest extends BaseTest {

    @Autowired
    ContentService contentService;

    @Autowired
    HealthEvaluateService healthEvaluateService;

    @Autowired
    QaService qaService;

    @Test
    public void t(){
        QueryQAPageRequest request = new QueryQAPageRequest();
        request.setQaId(1L);
        request.setContentIdList(Lists.newArrayList(1L,2L,3L));
        Page<QaDTO> qaDTOPage = qaService.listPage(request);
    }

    @Test
    public void testContent(){
        QueryAppContentPageRequest request=new QueryAppContentPageRequest();
        request.setIsOpen(1);
        request.setLineId(7L);
        request.setModuleId(1L);
//        request.setContentType(2);
        request.setCategoryId(0L);
        Page<AppContentDTO> dtoPage = contentService.listAppContentPage(request);
        System.out.println(JSON.toJSONString(dtoPage));
    }

    @Test
    public void test1(){
        AppContentDetailDTO contentDetail = contentService.getContentDetail(1051L, LineEnum.HMC);
        System.out.println(contentDetail);
    }

    @Test
    public void testSub() {
        SubmitEvaluateResultRequest request = new SubmitEvaluateResultRequest();
        request.setOpUserId(17426L);
        request.setStartEvaluateId(234L);
        request.setHealthEvaluateId(7L);

        String str="[{\"healthEvaluateQuestionId\":29,\"selectIdList\":[39]},{\"healthEvaluateQuestionId\":30,\"selectIdList\":[]},{\"healthEvaluateQuestionId\":31,\"selectIdList\":[56]},{\"healthEvaluateQuestionId\":32,\"selectIdList\":[61]},{\"healthEvaluateQuestionId\":33,\"selectIdList\":[]}]";
        JSONArray objects = JSONUtil.parseArray(str);
        List<EvaluateResultDetailRequest> detailRequests = JSONUtil.toList(objects, EvaluateResultDetailRequest.class);
        request.setEvaluateResultDetailList(detailRequests);
        healthEvaluateService.submitEvaluateResult(request);
    }

    @Test
    void listAppContentPage() {
        QueryAppContentPageRequest request = new QueryAppContentPageRequest();
        request.setLineId(1L).setStandardGoodsId(8987L);
        Page<AppContentDTO> appContentDTOPage = contentService.listAppContentPage(request);
        System.out.println(JSON.toJSONString(appContentDTOPage));
    }
}