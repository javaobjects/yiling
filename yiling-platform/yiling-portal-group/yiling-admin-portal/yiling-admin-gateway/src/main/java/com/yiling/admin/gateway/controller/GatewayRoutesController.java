package com.yiling.admin.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.gateway.service.RefreshRouteService;
import com.yiling.framework.common.pojo.Result;

@RestController
@RequestMapping("/gateway")
public class GatewayRoutesController {

    @Autowired
    private RefreshRouteService refreshRouteService;

    @GetMapping("/refreshRoutes")
    public Result refreshRoutes() {
        refreshRouteService.refreshRoutes();
        return Result.success();
    }
}