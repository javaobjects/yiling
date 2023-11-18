package com.yiling.admin.hmc.welfare.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.admin.hmc.welfare.form.DrugWelfarePageForm;
import com.yiling.admin.hmc.welfare.form.DrugWelfareUpdateForm;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareCouponVO;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareDetailVO;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareCouponApi;
import com.yiling.hmc.welfare.dto.DrugWelfareCouponDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareCouponUpdateRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfarePageRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareUpdateRequest;
import com.yiling.hmc.welfare.enums.DrugWelfareTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 药品福利计划 前端控制器
 * </p>
 *
 * @author: benben.jia
 * @data: 2022/09/26
 */
@Api(tags = "药品福利计划接口")
@RestController
@RequestMapping("/drug/welfare")
public class DrugWelfareController extends BaseController {

    @DubboReference
    DrugWelfareApi drugWelfareApi;

    @DubboReference
    DrugWelfareCouponApi drugWelfareCouponApi;

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @ApiOperation(value = "药品福利计划列表")
    @PostMapping("/queryPage")
    @Log(title = "药品福利计划列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<DrugWelfareVO>> queryPage(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody DrugWelfarePageForm form) {
        DrugWelfarePageRequest request = PojoUtils.map(form, DrugWelfarePageRequest.class);
        Page<DrugWelfareDTO> pageList = drugWelfareApi.pageList(request);

        if (ObjectUtil.isNotNull(pageList) && CollUtil.isNotEmpty(pageList.getRecords())) {
            List<DrugWelfareDTO> records = pageList.getRecords();
            // 商品规格
            List<Long> specificationIdList = records.stream().filter(o -> ObjectUtil.isNotNull(o.getSellSpecificationsId()) && o.getSellSpecificationsId().intValue() != 0)
                    .map(DrugWelfareDTO::getSellSpecificationsId).distinct().collect(Collectors.toList());
            Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = Maps.newHashMap();
            if (CollUtil.isNotEmpty(specificationIdList)) {
                List<StandardGoodsSpecificationDTO> standardGoodsList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(specificationIdList);
                if (CollUtil.isNotEmpty(standardGoodsList)) {
                    standardGoodsMap = standardGoodsList.stream().collect(Collectors.toMap(StandardGoodsSpecificationDTO::getId, Function.identity(), (k1, k2) -> k2));
                }
            }
            for (DrugWelfareDTO record : records) {
                Long sellSpecificationsId = record.getSellSpecificationsId();
                StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsMap.get(sellSpecificationsId);
                if(ObjectUtil.isNotNull(standardGoodsSpecificationDTO)){
                    record.setDrugSellSpecifications(String.format("%s(%s,%s)",standardGoodsSpecificationDTO.getName(),
                            standardGoodsSpecificationDTO.getLicenseNo(),standardGoodsSpecificationDTO.getSellSpecifications()));
                }
            }
        }
        Page<DrugWelfareVO> voPage = PojoUtils.map(pageList, DrugWelfareVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "获取福利计划详情")
    @GetMapping("/getDrugWelfareDetailById")
    @Log(title = "获取福利计划详情", businessType = BusinessTypeEnum.OTHER)
    public Result<DrugWelfareDetailVO> queryStandardSpecificationPage(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestParam("id") @ApiParam("id") Long id) {
        DrugWelfareDTO drugWelfareDTO = drugWelfareApi.queryById(id);
        if (ObjectUtil.isNull(drugWelfareDTO)) {
            return Result.failed("未获取到用药福利计划");
        }
        DrugWelfareDetailVO drugWelfareDetailVO = PojoUtils.map(drugWelfareDTO, DrugWelfareDetailVO.class);
        List<DrugWelfareCouponDTO> drugWelfareCouponDTOList = drugWelfareCouponApi.queryByDrugWelfareId(drugWelfareDTO.getId());

        StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(drugWelfareDTO.getSellSpecificationsId());
        drugWelfareDetailVO.setDrugSellSpecifications(String.format("%s(%s,%s)",standardGoodsSpecification.getName(),
                standardGoodsSpecification.getLicenseNo(),standardGoodsSpecification.getSellSpecifications()));
        drugWelfareDetailVO.setDrugWelfareCouponList(PojoUtils.map(drugWelfareCouponDTOList, DrugWelfareCouponVO.class));
        return Result.success(drugWelfareDetailVO);
    }

    @ApiOperation(value = "编辑福利计划详情")
    @PostMapping("/updateDrugWelfare")
    @Log(title = "编辑福利计划详情", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> updateDrugWelfare(@CurrentUser CurrentAdminInfo currentAdminInfo,@RequestBody @Valid DrugWelfareUpdateForm form) {
        DrugWelfareUpdateRequest request = PojoUtils.map(form, DrugWelfareUpdateRequest.class);
        request.setDrugWelfareType(DrugWelfareTypeEnum.TONG_XIN_LUO.getCode());
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        request.setDrugWelfareCouponList(PojoUtils.map(form.getDrugWelfareCouponList(), DrugWelfareCouponUpdateRequest.class));
        boolean isSuccess =drugWelfareApi.updateDrugWelfare(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("编辑出现问题");
        }
    }
}
