package com.yiling.sales.assistant.app.business.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:wei.wang
 * @date:2021/9/22
 */
@RestController
@RequestMapping("/business")
@Api(tags = "代办业务")
@Slf4j
public class OrderBusinessController extends BaseController {

}
