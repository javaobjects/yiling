package com.yiling.ih.question.feign;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.question.feign.dto.request.QuestionReplySendMessageRequest;

/**
 * 互联网医院 医生模块接口调用
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@FeignClient(name = "questionReplySendMessageFeignClient", url = "${ih.service.baseUrl}")
public interface QuestionReplySendMessageFeignClient {


    @PostMapping("/cms/message/sendMessage")
    ApiResult questionReplySendMessage(@Valid @RequestBody QuestionReplySendMessageRequest request);
}
