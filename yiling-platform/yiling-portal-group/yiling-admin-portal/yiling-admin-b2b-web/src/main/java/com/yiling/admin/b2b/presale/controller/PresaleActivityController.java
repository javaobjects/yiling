package com.yiling.admin.b2b.presale.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.presale.form.AddPresaleActivityForm;
import com.yiling.admin.b2b.presale.form.DeletePresaleGoodsLimitForm;
import com.yiling.admin.b2b.presale.form.QueryPresaleActivityPageForm;
import com.yiling.admin.b2b.presale.form.SavePresaleActivityForm;
import com.yiling.admin.b2b.presale.vo.PresaleActivityOrderVO;
import com.yiling.admin.b2b.presale.vo.PresaleActivityVO;
import com.yiling.admin.b2b.presale.vo.PresaleGoodsLimitVO;
import com.yiling.admin.b2b.strategy.form.AddStrategyActivityForm;
import com.yiling.admin.b2b.strategy.form.CopyStrategyForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyActivityPageForm;
import com.yiling.admin.b2b.strategy.form.SaveStrategyActivityForm;
import com.yiling.admin.b2b.strategy.form.StopStrategyForm;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityDetailVO;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityGoodsPageVO;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityPageVO;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityVO;
import com.yiling.admin.b2b.strategy.vo.StrategyAmountLadderVO;
import com.yiling.admin.b2b.strategy.vo.StrategyCycleLadderVO;
import com.yiling.admin.b2b.strategy.vo.StrategyGiftVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.PresaleActivityOrderDTO;
import com.yiling.marketing.presale.dto.request.AddPresaleActivityRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleOrderRequest;
import com.yiling.marketing.presale.dto.request.SavePresaleActivityRequest;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.api.StrategyActivityRecordApi;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.api.StrategyEnterpriseGoodsApi;
import com.yiling.marketing.strategy.api.StrategyGiftApi;
import com.yiling.marketing.strategy.api.StrategyMemberApi;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.api.StrategyStageMemberEffectApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.enums.StrategyConditionBuyerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 营销活动主表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Slf4j
@Api(tags = "预售-营销活动主表管理接口-运营后台")
@RestController
@RequestMapping("/presale/activity")
public class PresaleActivityController extends BaseController {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    PresaleActivityApi presaleActivityApi;

    @DubboReference
    B2bGoodsApi b2bGoodsApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InventoryApi inventoryApi;

    @ApiOperation(value = "分页列表营销活动预售-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PresaleActivityVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryPresaleActivityPageForm form) {
        QueryStrategyActivityPageRequest request = PojoUtils.map(form, QueryStrategyActivityPageRequest.class);
        Page<PresaleActivityDTO> activityDtoPage = strategyActivityApi.pageListForPresale(request);

        Page<PresaleActivityVO> voPage = PojoUtils.map(activityDtoPage, PresaleActivityVO.class);
        for (PresaleActivityVO record : voPage.getRecords()) {
            {
                // 创建人名称    创建人手机号
                UserDTO userDTO = userApi.getById(record.getCreateUser());
                record.setCreateUserName(userDTO.getName());
                record.setCreateUserTel(userDTO.getMobile());
            }
            {
                // 活动进度 0-全部 1-未开始 2-进行中 3-已结束
                if (record.getBeginTime().compareTo(new Date()) > 0) {
                    record.setProgress(1);
                } else if (record.getEndTime().compareTo(new Date()) > 0) {
                    record.setProgress(2);
                } else {
                    record.setProgress(3);
                }
                // 状态：1-启用 2-停用 3-废弃
                if (record.getStatus() != 1) {
                    record.setProgress(3);
                }
            }
            {
                record.setLookFlag(true);
                record.setUpdateFlag(1 == record.getProgress() || 2 == record.getProgress());
                record.setCopyFlag(true);
                record.setStopFlag(1 == record.getProgress() || 2 == record.getProgress());
            }
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "查询预售详情-运营后台")
    @GetMapping("queryDetail")
    public Result<PresaleActivityVO> queryDetail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam("id") Long id) {
        PresaleActivityDTO strategyActivityDTO = strategyActivityApi.queryPresaleActivityById(id);
        PresaleActivityVO detailVO = PojoUtils.map(strategyActivityDTO, PresaleActivityVO.class);
        if (StringUtils.isNotBlank(strategyActivityDTO.getPlatformSelected())) {
            String platformSelected = strategyActivityDTO.getPlatformSelected();
            List<String> platformSelectedList = StrUtil.isBlank(platformSelected) ? new ArrayList<>() : Arrays.asList(platformSelected.split(","));
            detailVO.setPlatformSelected(platformSelectedList);
        }

        if (StringUtils.isNotBlank(strategyActivityDTO.getConditionEnterpriseTypeValue())) {
            String enterpriseTypeValue = strategyActivityDTO.getConditionEnterpriseTypeValue();
            List<String> platformSelectedList = StrUtil.isBlank(enterpriseTypeValue) ? new ArrayList<>() : Arrays.asList(enterpriseTypeValue.split(","));
            detailVO.setConditionEnterpriseTypeValue(platformSelectedList);
        }

        if (StringUtils.isNotBlank(strategyActivityDTO.getConditionOther())) {
            String conditionOther1 = strategyActivityDTO.getConditionOther();
            List<String> other = StrUtil.isBlank(conditionOther1) ? new ArrayList<>() : Arrays.asList(conditionOther1.split(","));
            detailVO.setConditionOther(other);
        }
        List<PresaleGoodsLimitVO> presaleGoodsLimitVOS = detailVO.getPresaleGoodsLimitForms();
        if(CollectionUtils.isNotEmpty(presaleGoodsLimitVOS)){
            List<Long> goodsIdList = presaleGoodsLimitVOS.stream().map(PresaleGoodsLimitVO::getGoodsId).collect(Collectors.toList());
            Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
            List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(goodsIdList);
            Map<Long, GoodsInfoDTO> goodsInfoMap = goodsInfoDTOList.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, e -> e, (k1, k2) -> k1));
            for (PresaleGoodsLimitVO record : presaleGoodsLimitVOS) {
                GoodsInfoDTO goodsDTO = goodsInfoMap.get(record.getGoodsId());
                record.setEname(goodsDTO.getEname());
                record.setGoodsName(goodsDTO.getName());
                record.setManufacturer(goodsDTO.getManufacturer());
                record.setGoodsType(goodsDTO.getGoodsType());
                record.setSellSpecifications(goodsDTO.getSellSpecifications());
                record.setPrice(goodsDTO.getPrice());
                record.setSellUnit(goodsDTO.getSellUnit());
                // 商品库存
                Long inventory = inventoryMap.get(record.getGoodsId());
                if (ObjectUtil.isNull(inventory) || inventory < 0) {
                    inventory = 0L;
                }
                record.setGoodsInventory(inventory);
            }
        }
        detailVO.setRunning(false);
        // 活动进度 0-全部 1-未开始 2-进行中 3-已结束
        if (strategyActivityDTO.getBeginTime().compareTo(new Date()) > 0) {
            detailVO.setProgress(1);
        } else if (strategyActivityDTO.getEndTime().compareTo(new Date()) > 0) {
            detailVO.setProgress(2);
            detailVO.setRunning(true);
        } else {
            detailVO.setProgress(3);
        }
        // 状态：1-启用 2-停用 3-废弃
        if (strategyActivityDTO.getStatus() != 1) {
            detailVO.setProgress(3);
        }
        return Result.success(detailVO);
    }

    @ApiOperation(value = "预售主信息保存--上面的保存按钮")
    @PostMapping("/save")
    public Result<PresaleGoodsLimitVO> save(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddPresaleActivityForm form) {
        AddPresaleActivityRequest request = PojoUtils.map(form, AddPresaleActivityRequest.class);
        request.setStatus(1);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPortSelected("1");
        PresaleActivityDTO strategyActivityDTO = presaleActivityApi.save(request);
        return Result.success(PojoUtils.map(strategyActivityDTO, PresaleGoodsLimitVO.class));
    }

    @ApiOperation(value = "预售主信息保存--删除关联商品")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeletePresaleGoodsLimitForm form) {
        DeletePresaleMemberLimitRequest request = PojoUtils.map(form, DeletePresaleMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyMemberApi.deleteForPresaleGoodsLimit(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "预售主信息--停用")
    @PostMapping("/updateStatus")
    public Result<Object> updateStatus(@CurrentUser CurrentAdminInfo staffInfo,@RequestBody @Valid DeletePresaleGoodsLimitForm form) {
        boolean isSuccess = strategyMemberApi.updatePresaleStatus(staffInfo.getCurrentUserId(),form.getId());
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "预售主信息--复制")
    @PostMapping("/copy")
    public Result<Object> copy(@CurrentUser CurrentAdminInfo staffInfo,@RequestBody @Valid DeletePresaleGoodsLimitForm form) {
        Long id = presaleActivityApi.presaleActivityApi(staffInfo.getCurrentUserId(),form.getId());
        return Result.success(id);
    }

    @ApiOperation(value = "预售主信息保存--具体内容")
    @PostMapping("/saveAll")
    public Result<Object> saveAll(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SavePresaleActivityForm form) {
        SavePresaleActivityRequest request = PojoUtils.map(form, SavePresaleActivityRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = presaleActivityApi.saveAll(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "分页列表预售活动关联的订单信息")
    @PostMapping("/pageListForPresaleOrderInfo")
    public Result<Page<PresaleActivityOrderVO>> pageListForPresaleOrderInfo(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryPresaleActivityPageForm form) {
        QueryPresaleOrderRequest request = PojoUtils.map(form, QueryPresaleOrderRequest.class);
        Page<PresaleActivityOrderDTO> activityDtoPage = presaleActivityApi.queryOrderInfoByPresaleId(request);
        return Result.success(PojoUtils.map(activityDtoPage,PresaleActivityOrderVO.class));
    }
}
