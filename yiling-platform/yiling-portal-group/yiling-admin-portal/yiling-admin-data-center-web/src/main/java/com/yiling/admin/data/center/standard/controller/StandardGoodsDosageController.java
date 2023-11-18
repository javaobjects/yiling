package com.yiling.admin.data.center.standard.controller;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.data.center.standard.vo.StandardGoodsDosageVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.standard.api.StandardGoodsDosageApi;
import com.yiling.goods.standard.dto.StandardGoodsDosageDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 质量监管 Controller
 *
 * @author: wei.wang
 * @date: 2021/5/12
 */
@RestController
@RequestMapping("/dosage")
@Api(tags = "基础药品管理")
@Slf4j
public class StandardGoodsDosageController extends BaseController {


    @DubboReference
    StandardGoodsDosageApi standardGoodsDosageApi;

    @ApiOperation(value = "获取第一级别药剂")
    @PostMapping("/get/first")
    public Result<CollectionObject<List<StandardGoodsDosageVO>>> getFirstDosage() {
        List<StandardGoodsDosageDTO> firstDosage = standardGoodsDosageApi.getFirstDosage();
        List<StandardGoodsDosageVO> list = PojoUtils.map(firstDosage, StandardGoodsDosageVO.class);
        CollectionObject<List<StandardGoodsDosageVO>> result = new CollectionObject(list);
        return Result.success(result);
    }

}



