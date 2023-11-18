package com.yiling.admin.b2b.promotion.controller;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.yiling.admin.b2b.promotion.form.PromotionActivityStatusForm;


import com.yiling.admin.b2b.promotion.form.PromotionSpecialActivityPageForm;
import com.yiling.admin.b2b.promotion.form.PromotionSpecialSaveForm;
import com.yiling.admin.b2b.promotion.handler.ImportPromotionGoodsDataHandler;


import com.yiling.admin.b2b.promotion.vo.PromotionSpecialActivityPageVO;
import com.yiling.admin.b2b.promotion.vo.PromotionSpecialEnterpriseVO;
import com.yiling.admin.b2b.promotion.vo.PromotionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.api.PromotionGoodsGiftLimitApi;


import com.yiling.marketing.promotion.dto.SpecialActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityPageDTO;

import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;

import com.yiling.marketing.promotion.dto.request.PromotionSpecialActivitySaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialEnterpriseSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialSaveRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 专场活动主表 前端控制器
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-18
 */
@Slf4j
@RestController
@Api(tags = "专场活动管理接口-运营后台")
@RequestMapping("/promotion/special/activity")
public class PromotionSpecialActivityController extends BaseController {
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    PromotionGoodsGiftLimitApi giftLimitApi;
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    InventoryApi inventoryApi;
    @Autowired
    ImportPromotionGoodsDataHandler importPromotionGoodsDataHandler;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "分页列表查询B2B中专场活动-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PromotionSpecialActivityPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionSpecialActivityPageForm form) {
        SpecialActivityPageRequest request = PojoUtils.map(form, SpecialActivityPageRequest.class);
        request.setOpTime(new Date());
        if (request.getType() != null && request.getType() == 0) {
            request.setType(null);
        }
        if (request.getStatus() != null && request.getStatus() == 0) {
            request.setStatus(null);
        }
        Page<SpecialActivityPageDTO> specialActivityPageDTOPage = promotionActivityApi.querySpecialActivity(request);
        Page<PromotionSpecialActivityPageVO> pageVO = PojoUtils.map(specialActivityPageDTOPage, PromotionSpecialActivityPageVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "查询专场活动中关联的营销活动信息")
    @PostMapping("/pageActivityInfo")
    public Result<Page<PromotionSpecialEnterpriseVO>> pageActivityInfo(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionSpecialActivityPageForm form) {
        SpecialActivityPageRequest request = PojoUtils.map(form, SpecialActivityPageRequest.class);
        Page<SpecialActivityEnterpriseDTO> enterpriseDTOPage = promotionActivityApi.pageActivityInfo(request);
        Page<PromotionSpecialEnterpriseVO> voPage = PojoUtils.map(enterpriseDTOPage, PromotionSpecialEnterpriseVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "提交专场活动-运营后台")
    @PostMapping("/submit")
    @Log(title = "提交促销活动", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> submit(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid PromotionSpecialSaveForm form) {

        PromotionSpecialSaveRequest request = PojoUtils.map(form, PromotionSpecialSaveRequest.class);
        Boolean isUpdate = request.getId() == null;
        PromotionSpecialActivitySaveRequest specialActivitySaveRequest = request.getPromotionSpecialActivitySave();
        if (specialActivitySaveRequest != null) {
            specialActivitySaveRequest.setOpUserId(staffInfo.getCurrentUserId());
            specialActivitySaveRequest.setOpTime(new Date());
            specialActivitySaveRequest.setStatus(1);
        }
        List<PromotionSpecialEnterpriseSaveRequest> enterpriseLimitList = request.getEnterpriseSaves();
        enterpriseLimitList.forEach(item -> {
            item.setOpUserId(staffInfo.getCurrentUserId());
            item.setOpTime(new Date());
        });
        request.setOpUserId(staffInfo.getCurrentUserId());
        long id = promotionActivityApi.savePromotionSpecialActivity(request);
        return Result.success(id);
    }

    @ApiOperation(value = "查询详情-运营后台")
    @GetMapping("/queryById")
    public Result<PromotionSpecialActivityPageVO> queryById(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "id") Long id) {
        SpecialActivityPageDTO specialActivityPageDTOPage = promotionActivityApi.querySpecialActivityInfo(id);
        PromotionSpecialActivityPageVO activityPageVO = PojoUtils.map(specialActivityPageDTOPage, PromotionSpecialActivityPageVO.class);
        return Result.success(activityPageVO);
    }

    @ApiOperation(value = "停用专场活动-运营后台")
    @PostMapping("/status")
    @Log(title = "编辑专场活动状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editStatus(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionActivityStatusForm form) {
        PromotionActivityStatusRequest request = PojoUtils.map(form, PromotionActivityStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = promotionActivityApi.editSpecialActivityStatusById(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("修改失败");
        }
    }
}
