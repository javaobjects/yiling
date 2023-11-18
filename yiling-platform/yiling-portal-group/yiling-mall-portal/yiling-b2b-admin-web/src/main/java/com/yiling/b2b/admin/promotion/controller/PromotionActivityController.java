package com.yiling.b2b.admin.promotion.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.goods.vo.GoodsDisableVO;
import com.yiling.b2b.admin.goods.vo.GoodsListItemVO;
import com.yiling.b2b.admin.promotion.form.ImportPromotionGoodsForm;
import com.yiling.b2b.admin.promotion.form.PromotionActivityPageForm;
import com.yiling.b2b.admin.promotion.form.PromotionActivityStatusForm;
import com.yiling.b2b.admin.promotion.form.PromotionGoodsGiftUsedForm;
import com.yiling.b2b.admin.promotion.form.PromotionSaveForm;
import com.yiling.b2b.admin.promotion.handler.ImportPromotionGoodsDataHandler;
import com.yiling.b2b.admin.promotion.vo.PromotionActivityPageVO;
import com.yiling.b2b.admin.promotion.vo.PromotionActivityVO;
import com.yiling.b2b.admin.promotion.vo.PromotionEnterpriseLimitVO;
import com.yiling.b2b.admin.promotion.vo.PromotionGoodsGiftLimitVO;
import com.yiling.b2b.admin.promotion.vo.PromotionGoodsGiftUsedVO;
import com.yiling.b2b.admin.promotion.vo.PromotionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.api.PromotionGoodsGiftLimitApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionEnterpriseLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivitySaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftLimitSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.enums.PromotionBearTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionSponsorTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 促销活动主表 前端控制器
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Slf4j
@Api(tags = "促销活动管理接口")
@RestController
@RequestMapping("/promotion/activity")
public class PromotionActivityController extends BaseController {
    @DubboReference
    PromotionActivityApi            promotionActivityApi;
    @DubboReference
    GoodsGiftApi                    goodsGiftApi;
    @DubboReference
    PromotionGoodsGiftLimitApi      giftLimitApi;
    @DubboReference
    B2bGoodsApi                     b2bGoodsApi;
    @DubboReference
    InventoryApi                    inventoryApi;
    @Autowired
    ImportPromotionGoodsDataHandler importPromotionGoodsDataHandler;
    @DubboReference
    EnterpriseApi                   enterpriseApi;
    @DubboReference
    UserApi                         userApi;
    @DubboReference
    GoodsApi                        goodsApi;


    @ApiOperation(value = "分页列表查询B2B中促销活动-商家后台")
    @PostMapping("/pageList")
    public Result<Page<PromotionActivityPageVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody PromotionActivityPageForm form) {
        form.check();
        PromotionActivityPageRequest request = PojoUtils.map(form, PromotionActivityPageRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setEid(staffInfo.getCurrentEid());
        request.setBear(PromotionBearTypeEnum.MERCHANT.getType());
        request.setSponsorType(PromotionSponsorTypeEnum.MERCHANT.getType());
        Page<PromotionActivityPageDTO> pageDTO = promotionActivityApi.pageList(request);
        pageDTO.getRecords().forEach(item -> {
            if (StringUtils.isNotBlank(item.getPlatformSelected())) {
                List<Integer> platformSelectedList = JSON.parseArray(item.getPlatformSelected(), Integer.class);
                item.setPlatformSelectedList(platformSelectedList);
            }
        });
        Page<PromotionActivityPageVO> pageVO = PojoUtils.map(pageDTO, PromotionActivityPageVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "编辑和查询详情-商家后台")
    @GetMapping("/queryById")
    public Result<PromotionVO> queryById(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "id") Long id) {
        PromotionVO vo = new PromotionVO();
        // 促销活动主表
        PromotionActivityDTO promotionActivityDTO = promotionActivityApi.queryById(id);
        PromotionActivityVO activityVO = PojoUtils.map(promotionActivityDTO, PromotionActivityVO.class);
        if (StringUtils.isNotBlank(promotionActivityDTO.getPlatformSelected())) {
            List<Integer> platformSelectedList = JSON.parseArray(promotionActivityDTO.getPlatformSelected(), Integer.class);
            activityVO.setPlatformSelected(platformSelectedList);
        }
        vo.setPromotionActivity(activityVO);
        // 促销活动企业限制表
        List<PromotionEnterpriseLimitDTO> promotionEnterpriseLimitDTOList = promotionActivityApi.queryEnterpriseByActivityId(id);
        List<PromotionEnterpriseLimitVO> enterpriseLimitVOS = PojoUtils.map(promotionEnterpriseLimitDTOList, PromotionEnterpriseLimitVO.class);
        vo.setPromotionEnterpriseLimitList(enterpriseLimitVOS);
        // 促销活动赠品表
        List<PromotionGoodsGiftLimitDTO> promotionGoodsGiftLimitDTO = promotionActivityApi.queryGoodsGiftByActivityId(id);
        List<PromotionGoodsGiftLimitVO> goodsGiftLimitVO = PojoUtils.map(promotionGoodsGiftLimitDTO, PromotionGoodsGiftLimitVO.class);
        vo.setPromotionGoodsGiftLimitList(goodsGiftLimitVO);
        if (CollUtil.isNotEmpty(promotionGoodsGiftLimitDTO)) {
            GoodsGiftDTO goodsGiftDTO = goodsGiftApi.getOneById(promotionGoodsGiftLimitDTO.get(0).getGoodsGiftId());
            vo.setGoodsGiftName(goodsGiftDTO.getName());
            activityVO.setPromotionAmount(promotionGoodsGiftLimitDTO.get(0).getPromotionAmount());
        }
        // 促销活动商品表
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryGoodsByActivityId(id);
        List<Long> goodsIdList = promotionGoodsLimitDTOList.stream().map(PromotionGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
        List<GoodsInfoDTO> goodsInfoDTOS = b2bGoodsApi.batchQueryInfo(goodsIdList);
        List<GoodsListItemVO> goodsListItemVOS = PojoUtils.map(goodsInfoDTOS, GoodsListItemVO.class);
        Map<Long, Long> longMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
        List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList,subEids);
        goodsListItemVOS.forEach(e -> {
            e.setGoodsId(e.getId());
            e.setGoodsName(e.getName());
            Long count = longMap.get(e.getId());
            e.setCount(count);
            e.setYilingGoodsFlag(false);
            if(goodsMap.get(e.getId())!=null&&goodsMap.get(e.getId())>0){
                e.setYilingGoodsFlag(true);
            }
        });
        vo.setPromotionGoodsLimitList(goodsListItemVOS);

        return Result.success(vo);
    }

    @ApiOperation(value = "提交按钮-商家后台")
    @PostMapping("/submit")
    @Log(title = "提交促销活动", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> submit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid PromotionSaveForm form) {

        form.check();

        PromotionSaveRequest request = PojoUtils.map(form, PromotionSaveRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        // 促销活动主信息需要修改
        PromotionActivitySaveRequest activity = request.getActivity();
        activity.setSponsorType(PromotionSponsorTypeEnum.MERCHANT.getType());
        activity.setBear(PromotionBearTypeEnum.MERCHANT.getType());

        // 这里兼容运营后台数据结构
        BigDecimal promotionAmount = form.getActivity().getPromotionAmount();
        request.setGoodsGiftLimit(Arrays.asList(PromotionGoodsGiftLimitSaveRequest.builder().promotionAmount(promotionAmount).build()));

        Long id = promotionActivityApi.savePromotionActivity(request);
        return Result.success(id);
    }

    @ApiOperation(value = "状态修改-商家后台")
    @PostMapping("/status")
    @Log(title = "编辑促销活动状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody PromotionActivityStatusForm form) {
        PromotionActivityStatusRequest request = PojoUtils.map(form, PromotionActivityStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = promotionActivityApi.editStatusById(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("修改失败");
        }
    }

    @ApiOperation(value = "复制-商家后台")
    @PostMapping("/copy")
    @Log(title = "复制促销活动", businessType = BusinessTypeEnum.OTHER)
    public Result<Long> copy(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody PromotionActivityStatusForm form) {
        PromotionActivityStatusRequest request = PojoUtils.map(form, PromotionActivityStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        PromotionActivityDTO activityDTO = promotionActivityApi.copy(request);
        if (null != activityDTO) {
            return Result.success(activityDTO.getId());
        } else {
            return Result.failed("修改失败");
        }
    }

    @ApiOperation(value = "查询参与满赠活动的订单信息")
    @PostMapping("/pageGiftOrder")
    public Result<Page<PromotionGoodsGiftUsedVO>> pageGiftOrder(@CurrentUser CurrentStaffInfo staffInfo,
                                                                @RequestBody PromotionGoodsGiftUsedForm form) {
        PromotionGoodsGiftUsedRequest request = PojoUtils.map(form, PromotionGoodsGiftUsedRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Page<PromotionGoodsGiftUsedDTO> dtoPage = giftLimitApi.pageGiftOrder(request);
        if (CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(PojoUtils.map(dtoPage, PromotionGoodsGiftUsedVO.class));
        }
        List<Long> userIdList = dtoPage.getRecords().stream().map(PromotionGoodsGiftUsedDTO::getCreateUser).collect(Collectors.toList());
        List<Long> buyerEIdList = dtoPage.getRecords().stream().map(PromotionGoodsGiftUsedDTO::getBuyerEid).collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(buyerEIdList);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, o -> o, (k1, k2) -> k1));
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));
        dtoPage.getRecords().stream().forEach(item -> {
            item.setBuyerName(Optional.ofNullable(userDTOMap.get(item.getCreateUser())).map(UserDTO::getName).orElse(null));
            item.setBuyerTel(Optional.ofNullable(userDTOMap.get(item.getCreateUser())).map(UserDTO::getMobile).orElse(null));
            item.setAddress(Optional.ofNullable(enterpriseDTOMap.get(item.getBuyerEid())).map(EnterpriseDTO::getAddress).orElse(null));
        });
        Page<PromotionGoodsGiftUsedVO> voPage = PojoUtils.map(dtoPage, PromotionGoodsGiftUsedVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "营销活动商品信息导入", httpMethod = "POST")
    @PostMapping(value = "importPromotionGoods", headers = "content-type=multipart/form-data")
    public Result<List<GoodsListItemVO>> importPromotionGoods(@RequestParam(value = "file", required = true) MultipartFile file,
                                                              @RequestParam("eidList") String eidList,
                                                              @RequestParam(value = "promotionActivityId", required = false) Long promotionActivityId,
                                                              @RequestParam(value = "type", required = false) Integer type,
                                                              @CurrentUser CurrentStaffInfo staffInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importPromotionGoodsDataHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel<ImportPromotionGoodsForm> importResultModel;
        try {
            //包含了插入数据库失败的信息
            Long start = System.currentTimeMillis();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, staffInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportPromotionGoodsForm.class, params, importPromotionGoodsDataHandler,
                paramMap);
            log.info("营销活动商品信息导入耗时：{},导入数据为:[{}]", System.currentTimeMillis() - start, importResultModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        if (null == importResultModel) {
            return Result.failed("导入信息失败");
        }
        List<ImportPromotionGoodsForm> modelList = importResultModel.getList();
        List<GoodsListItemVO> goodsListItemVOList = checkPromotionGoods(modelList, eidList, promotionActivityId, type);
        if (CollUtil.isEmpty(goodsListItemVOList)) {
            return Result.failed("导入符合条件的商品为空");
        }
        return Result.success(goodsListItemVOList);
    }

    /**
     * 校验商品信息
     *
     * @param list
     * @param eidStringList
     * @param promotionActivityId
     * @return
     */
    private List<GoodsListItemVO> checkPromotionGoods(List<ImportPromotionGoodsForm> list, String eidStringList, Long promotionActivityId,Integer type) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        log.info("开始校验商品信息,商品数据为:[{}],企业为:[{}],活动为:[{}]", list, eidStringList, promotionActivityId);
        List<Long> eidList = Arrays.stream(eidStringList.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Long> goodsIdList = list.stream().map(ImportPromotionGoodsForm::getGoodsId).collect(Collectors.toList());
        List<GoodsInfoDTO> goodsInfoDTOS = b2bGoodsApi.batchQueryInfo(goodsIdList);
        List<GoodsInfoDTO> goodsInfoDTOList = goodsInfoDTOS.stream().filter(goodsInfoDTO -> 1 == goodsInfoDTO.getGoodsStatus())
            .collect(Collectors.toList());
        List<GoodsListItemVO> resultList = new ArrayList<>();
        List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(goodsInfoDTOList, GoodsListItemVO.class);
        Map<Long, Long> longMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);

        // 类型（1-平台；2-商家）
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryNotRepeatByGoodsIdList(goodsIdList, PromotionSponsorTypeEnum.MERCHANT.getType(), type);
        Map<Long, List<PromotionGoodsLimitDTO>> finalLongListMap = promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));

        log.info("对应的商品信息为:[{}],已参与活动的商品信息为:[{}]", goodsInfoDTOList, finalLongListMap);

        for (GoodsListItemVO goodsListItemVO : goodsListItemVOList) {
            if (eidList.contains(goodsListItemVO.getEid())) {
                GoodsDisableVO goodsDisableVO = new GoodsDisableVO();

                List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = finalLongListMap.get(goodsListItemVO.getId());
                goodsDisableVO.setIsAllowSelect(0);
                if (null != promotionGoodsLimitDTOS && promotionGoodsLimitDTOS.size() > 0) {
                    goodsDisableVO.setIsAllowSelect(1);
                    PromotionGoodsLimitDTO promotionGoodsLimitDTO = promotionGoodsLimitDTOS.get(0);
                    if (null != promotionActivityId && promotionActivityId.equals(promotionGoodsLimitDTO.getPromotionActivityId())) {
                        goodsDisableVO.setIsAllowSelect(0);
                    }
                }

                goodsListItemVO.setGoodsDisableVO(goodsDisableVO);
                goodsListItemVO.setGoodsName(goodsListItemVO.getName());
                goodsListItemVO.setGoodsId(goodsListItemVO.getId());
                Long count = longMap.get(goodsListItemVO.getId());
                goodsListItemVO.setCount(count);
                if (0 == goodsDisableVO.getIsAllowSelect()) {
                    resultList.add(goodsListItemVO);
                }
            }
        }
        log.info("校验成功的返回数据为:[{}]", resultList);
        return resultList;
    }
}
