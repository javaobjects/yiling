package com.yiling.hmc.system.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.system.vo.DictVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据字典 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@Api(tags = "数据字典接口")
public class DictController extends BaseController {

    @DubboReference
    DictApi dictApi;

    @ApiOperation(value = "获取所有字典数据")
    @GetMapping("/all")
    public Result<CollectionObject<DictVO>> all() {
        List<DictBO> list = dictApi.getEnabledList();
        return Result.success(new CollectionObject<>(PojoUtils.map(list, DictVO.class)));
    }
}
