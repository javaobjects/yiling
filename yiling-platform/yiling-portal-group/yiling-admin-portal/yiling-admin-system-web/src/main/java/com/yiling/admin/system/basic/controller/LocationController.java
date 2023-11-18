package com.yiling.admin.system.basic.controller;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.system.basic.vo.LocationTreeVO;
import com.yiling.admin.system.basic.vo.LocationVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 行政区划 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Slf4j
@RestController
@RequestMapping("/location")
@Api(tags = "行政区划接口")
public class LocationController extends BaseController {

    @DubboReference
    LocationApi locationApi;

    @ApiOperation("获取下级区域列表")
    @GetMapping("/listByParentCode")
    public Result<CollectionObject<LocationVO>> listByParentCode(@ApiParam(value = "上级区域编码", allowEmptyValue = true) @RequestParam String parentCode) {
        List<LocationDTO> list = locationApi.listByParentCode(parentCode);
        return Result.success(new CollectionObject<>(PojoUtils.map(list, LocationVO.class)));
    }

    @ApiOperation(value = "获取区域树结构列表")
    @GetMapping("listTreeByParentCode")
    public Result<CollectionObject<List<LocationTreeVO>>> listTreeByParentCode(
            @ApiParam(value = "上级区域编码", allowEmptyValue = true, required = true) @RequestParam String parentCode,
            @ApiParam(value = "递归深度", required = false, defaultValue = "1") @RequestParam(defaultValue = "1") Integer deep){
        List<LocationTreeDTO> locationTreeDTOList = locationApi.listTreeByParentCode(parentCode, deep);
        List<LocationTreeVO> locationTreeVOList = PojoUtils.map(locationTreeDTOList,LocationTreeVO.class);
        return Result.success(new CollectionObject(locationTreeVOList));
    }
}
