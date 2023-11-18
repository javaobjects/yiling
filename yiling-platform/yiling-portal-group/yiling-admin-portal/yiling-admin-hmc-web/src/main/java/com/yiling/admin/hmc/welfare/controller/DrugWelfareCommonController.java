package com.yiling.admin.hmc.welfare.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareListVO;
import com.yiling.admin.hmc.welfare.vo.EnterpriseListVO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareEnterpriseApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.EnterpriseListDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfarePageRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/28
 */
@Api(tags = "药品福利计划公共接口")
@RestController
@RequestMapping("/drug/welfare/common")
public class DrugWelfareCommonController {

    @DubboReference
    DrugWelfareEnterpriseApi drugWelfareEnterpriseApi;

    @DubboReference
    DrugWelfareApi drugWelfareApi;

    @ApiOperation(value = "列表查询商家下拉选单")
    @PostMapping("/queryEnterpriseList")
    public Result<CollectionObject<EnterpriseListVO>> queryEnterpriseList() {
        List<EnterpriseListDTO> list = drugWelfareEnterpriseApi.getEnterpriseList();
        List<EnterpriseListDTO> unique = list.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(EnterpriseListDTO::getEid))), ArrayList::new));
        return Result.success(new CollectionObject<>(PojoUtils.map(unique, EnterpriseListVO.class)));
    }

    @ApiOperation(value = "列表查询福利计划下拉选单")
    @PostMapping("/queryDrugWelfareList")
    public Result<CollectionObject<DrugWelfareListVO>> queryDrugWelfareList() {
        DrugWelfarePageRequest request = new DrugWelfarePageRequest();
        request.setCurrent(1).setSize(1000);
        Page<DrugWelfareDTO> pageList = drugWelfareApi.pageList(request);
        if (ObjectUtil.isNotNull(pageList) && CollUtil.isNotEmpty(pageList.getRecords())) {
            return Result.success(new CollectionObject<>(PojoUtils.map(pageList.getRecords(), DrugWelfareListVO.class)));
        } else {
            return Result.success(new CollectionObject<>(Collections.EMPTY_LIST));
        }
    }
}
