package com.yiling.f2b.admin.goods.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.admin.goods.form.BatchUpdateGoodsOverSoldForm;
import com.yiling.f2b.admin.goods.form.BatchUpdateGoodsStatusForm;
import com.yiling.f2b.admin.goods.form.QueryGoodsAuditRecordPageForm;
import com.yiling.f2b.admin.goods.form.QueryGoodsLogForm;
import com.yiling.f2b.admin.goods.form.QueryGoodsPageListForm;
import com.yiling.f2b.admin.goods.form.QueryOverSoldGoodsPageListForm;
import com.yiling.f2b.admin.goods.form.QueryOverSoldPopGoodsPageListForm;
import com.yiling.f2b.admin.goods.form.QueryPopGoodsPageListForm;
import com.yiling.f2b.admin.goods.form.SaveInventorySubscriptionForm;
import com.yiling.f2b.admin.goods.form.SaveSubscriptionForm;
import com.yiling.f2b.admin.goods.form.UpdateGoodsForm;
import com.yiling.f2b.admin.goods.form.UpdateGoodsSkuForm;
import com.yiling.f2b.admin.goods.vo.GoodsAuditListVO;
import com.yiling.f2b.admin.goods.vo.GoodsDetailsVO;
import com.yiling.f2b.admin.goods.vo.GoodsDisableVO;
import com.yiling.f2b.admin.goods.vo.GoodsLimitPriceVO;
import com.yiling.f2b.admin.goods.vo.GoodsListItemPageVO;
import com.yiling.f2b.admin.goods.vo.GoodsListItemVO;
import com.yiling.f2b.admin.goods.vo.GoodsLogPageListItemVO;
import com.yiling.f2b.admin.goods.vo.GoodsSkuVO;
import com.yiling.f2b.admin.goods.vo.InventoryDetailVO;
import com.yiling.f2b.admin.goods.vo.StandardGoodsAllInfoVO;
import com.yiling.f2b.admin.goods.vo.StandardGoodsCategoryInfoAllVO;
import com.yiling.f2b.admin.goods.vo.StatusCountListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.api.InventorySubscriptionApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.BatchSaveSubscriptionRequest;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.enums.SubscriptionTypeEnum;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsAuditApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsLogPageListItemDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsOverSoldRequest;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditRecordPageRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLogRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsOverSoldEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsCategoryApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.enums.StandardGoodsStatusEnum;
import com.yiling.pricing.goods.api.PopGoodsLimitPriceApi;
import com.yiling.pricing.goods.dto.PopGoodsLimitPriceDTO;
import com.yiling.pricing.goods.enums.PopGoodsLimitPriceStatusEnum;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@RestController
@Api(tags = "供应商商品模块")
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @DubboReference
    PopGoodsLimitPriceApi popGoodsLimitPriceApi;

    @DubboReference
    UserApi     userApi;

    @DubboReference
    GoodsAuditApi goodsAuditApi;

    @DubboReference
    InventoryApi inventoryApi;

    @DubboReference
    InventorySubscriptionApi inventorySubscriptionApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    StandardGoodsCategoryApi standardGoodsCategoryApi;

    @DubboReference
    StaffApi staffApi;

    @ApiOperation(value = "pop后台商品弹框查询", httpMethod = "POST")
    @PostMapping("/popList")
    public Result<Page<GoodsListItemVO>> popList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPopGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (form.getEid() != null && form.getEid() != 0) {
            request.setEidList(Arrays.asList(form.getEid()));
        } else {
            if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
                List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
                request.setEidList(list);
            } else {
                request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
            }
        }
        request.setName(form.getGoodsName());
        Page<GoodsListItemVO> page = PojoUtils.map(popGoodsApi.queryPopGoodsPageList(request), GoodsListItemVO.class);
        Long agreementId = form.getAgreementId();
        List<AgreementGoodsDTO> agreementGoodsList = Collections.emptyList();
        if (null!=agreementId && agreementId != 0) {
            agreementGoodsList = agreementGoodsApi.getAgreementGoodsAgreementIdByList(agreementId);
        }

        List<Long> agreementGoodsIdList = agreementGoodsList.stream().map(AgreementGoodsDTO::getGoodsId).collect(Collectors.toList());
        page.getRecords().forEach(e -> {
            GoodsDisableVO goodsDisableVO = new GoodsDisableVO();
            //判断渠道商品是否已经被选了
            if (CollectionUtils.isNotEmpty(agreementGoodsIdList) && agreementGoodsIdList.contains(e.getId())) {
                goodsDisableVO.setAgreementDisable(true);
                goodsDisableVO.setAgreementDesc("已被占用");
            }
            e.setGoodsDisableVO(goodsDisableVO);

            e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
        });
        return Result.success(page);
    }

    @ApiOperation(value = "pop后台商品管理查询", httpMethod = "POST")
    @PostMapping("/list")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> list(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(list);
        } else {
            request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        }
        Page<GoodsListItemBO> page = popGoodsApi.queryPopGoodsPageList(request);
        GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);
            List<Long> goodsIds = goodsListItemVOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
            Map<Long, List<GoodsSkuDTO>> goodsSkuMap = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
            goodsListItemVOList.forEach(e -> {
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                e.setGoodsSkuList(PojoUtils.map(goodsSkuMap.get(e.getId()), GoodsSkuVO.class));
            });
            newPage.setRecords(goodsListItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            //统计商品需要 排除状态条件
            request.setGoodsStatus(null);
            List<QueryStatusCountBO> listByCondition = popGoodsApi.queryPopStatusCountListByCondition(request);
            Map<Integer, Long> map = listByCondition.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            newPage.setUpShelfCount(map.get(GoodsStatusEnum.UP_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UP_SHELF.getCode()));
            newPage.setUnShelfCount(map.get(GoodsStatusEnum.UN_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UN_SHELF.getCode()));
            newPage.setWaitSetCount(map.get(GoodsStatusEnum.WAIT_SET.getCode()) == null ? 0L : map.get(GoodsStatusEnum.WAIT_SET.getCode()));
        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "pop商家后台商品基础信息", httpMethod = "GET")
    @GetMapping("/getInfo")
    public Result<GoodsListItemVO> getInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long goodsId){
        GoodsInfoDTO goodsInfo = popGoodsApi.queryInfo(goodsId);
        if(null==goodsInfo){
            return Result.failed("商品不存在");
        }
        GoodsListItemVO itemVO = PojoUtils.map(goodsInfo, GoodsListItemVO.class);
        itemVO.setPic(pictureUrlUtils.getGoodsPicUrl(itemVO.getPic()));
        //获取限价信息，限价只针对工业直属，一级商，二级商，专二普一
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(goodsInfo.getEid());
        if(EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode().equals(enterpriseDTO.getChannelId())
                ||EnterpriseChannelEnum.LEVEL_1.getCode().equals(enterpriseDTO.getChannelId())
                ||EnterpriseChannelEnum.LEVEL_2.getCode().equals(enterpriseDTO.getChannelId())
                ||EnterpriseChannelEnum.Z2P1.getCode().equals(enterpriseDTO.getChannelId())){
            PopGoodsLimitPriceDTO limitPriceDTO = popGoodsLimitPriceApi.getLimitPriceBySpecificationsId(goodsInfo.getSellSpecificationsId(), PopGoodsLimitPriceStatusEnum.NORMAL.getCode());
            itemVO.setGoodsLimitPrice(PojoUtils.map(limitPriceDTO, GoodsLimitPriceVO.class));
        }
        return Result.success(itemVO);
    }

    @ApiOperation(value = "pop后台超卖商品弹框查询", httpMethod = "POST")
    @PostMapping("/overSoldPopList")
    public Result<Page<GoodsListItemVO>> overSoldPopList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOverSoldPopGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(list);
        } else {
            return Result.failed("只允许以岭账户访问");
            //request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        }
        Page<GoodsListItemVO> page = PojoUtils.map(popGoodsApi.queryPopGoodsPageList(request), GoodsListItemVO.class);
        page.getRecords().forEach(e -> {
            e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
        });
        return Result.success(page);
    }

    @ApiOperation(value = "pop后台超卖商品管理查询", httpMethod = "POST")
    @PostMapping("/overSoldList")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> overSoldTypeList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOverSoldGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (null == request.getOverSoldType()) {
            request.setOverSoldType(GoodsOverSoldEnum.OVER_SOLD.getType());
        }
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(list);
        } else {
            return Result.failed("只允许以岭账户访问");
            //request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        }
        Page<GoodsListItemBO> page = popGoodsApi.queryPopGoodsPageList(request);
        GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);
            List<Long> goodsIds = goodsListItemVOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
            Map<Long, List<GoodsSkuDTO>> goodsSkuMap = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
            goodsListItemVOList.forEach(e -> {
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                e.setGoodsSkuList(PojoUtils.map(goodsSkuMap.get(e.getId()), GoodsSkuVO.class));
            });
            newPage.setRecords(goodsListItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            //统计商品需要 排除状态条件
            request.setGoodsStatus(null);
            List<QueryStatusCountBO> listByCondition = popGoodsApi.queryPopStatusCountListByCondition(request);
            Map<Integer, Long> map = listByCondition.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            newPage.setUpShelfCount(map.get(GoodsStatusEnum.UP_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UP_SHELF.getCode()));
            newPage.setUnShelfCount(map.get(GoodsStatusEnum.UN_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UN_SHELF.getCode()));
            newPage.setWaitSetCount(map.get(GoodsStatusEnum.WAIT_SET.getCode()) == null ? 0L : map.get(GoodsStatusEnum.WAIT_SET.getCode()));
        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "批量添加或移除超卖商品", httpMethod = "POST")
    @PostMapping("/updateOverSold")
    @Log(title = "批量添加或移除超卖商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateOverSold(@RequestBody @Valid BatchUpdateGoodsOverSoldForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = ListUtil.empty();
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        } else {
            return Result.failed("只允许以岭账户修改");
            //eidList = Arrays.asList(staffInfo.getCurrentEid());
        }

        List<Long> ids = form.getGoodsIds().stream().distinct().collect(Collectors.toList());
        List<PopGoodsDTO> popGoodsList = popGoodsApi.getPopGoodsListByGoodsIds(form.getGoodsIds());
        if (popGoodsList.size() != ids.size()) {
            return Result.failed("选择了非pop的商品");
        }
        List<GoodsInfoDTO> goodsInfoDTOList = popGoodsApi.batchQueryInfo(form.getGoodsIds());
        // 验证是否是自己的商业公司商品
        List<Long> goodsEidList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getEid).distinct().collect(Collectors.toList());
        if (goodsEidList == null) {
            return Result.failed("选择了无效的商品");
        }

        for (Long eid : goodsEidList) {
            if (!eidList.contains(eid)) {
                return Result.failed("只能修改自己商业公司里面的商品信息");
            }
        }
        BatchUpdateGoodsOverSoldRequest request = PojoUtils.map(form, BatchUpdateGoodsOverSoldRequest.class);
        request.setOpUserId(staffInfo.getCurrentEid());
        return Result.success(new BoolObject(goodsApi.batchUpdateGoodsOverSold(request)));
    }

    @ApiOperation(value = "查询商品审核记录列表", httpMethod = "POST")
    @PostMapping("/auditList")
    public Result<Page<GoodsAuditListVO>> auditList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsAuditRecordPageForm form) {
        QueryGoodsAuditRecordPageRequest request = PojoUtils.map(form, QueryGoodsAuditRecordPageRequest.class);
        Page<GoodsAuditDTO> list = goodsAuditApi.queryPageListGoodsAuditRecord(request);
        Page<GoodsAuditListVO> page = PojoUtils.map(list, GoodsAuditListVO.class);
        List<Long> userIds = list.getRecords().stream().map(e -> e.getUpdateUser()).distinct().filter(p -> p.intValue() > 0).collect(Collectors.toList());
        Map<Long, UserDTO> userDTOMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userIds)) {
            List<UserDTO> userDTOList = userApi.listByIds(userIds);
            userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        }
        Map<Long, UserDTO> finalUserDTOMap = userDTOMap;
        page.getRecords().forEach(e -> {
            e.setAuditUser(finalUserDTOMap.get(e.getUpdateUser()) != null ? finalUserDTOMap.get(e.getUpdateUser()).getName() : "");
        });
        return Result.success(page);
    }

    @ApiOperation(value = "pop后台查询商品明细", httpMethod = "GET")
    @GetMapping("/detail")
    public Result<GoodsDetailsVO> detail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long goodsId) {
        GoodsDetailsVO resultData = null;
        Long currentEid = staffInfo.getCurrentEid();
        GoodsFullDTO goodsFullDTO = goodsApi.queryFullInfo(goodsId);
        if (goodsFullDTO != null) {
            resultData = PojoUtils.map(goodsFullDTO, GoodsDetailsVO.class);
            //图片转换
            if (resultData != null && resultData.getStandardGoodsAllInfo() != null) {
                StandardGoodsAllInfoVO standardGoodsAllInfoVO = resultData.getStandardGoodsAllInfo();
                if (CollUtil.isNotEmpty(standardGoodsAllInfoVO.getPicBasicsInfoList())) {
                    standardGoodsAllInfoVO.getPicBasicsInfoList().forEach(e -> {
                        e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                    });
                }
            }
            //获取销售包装和库存信息
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIdAndStatus(goodsId,ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
            goodsSkuDTOList = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
            resultData.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));
            Boolean showSubscription = Constants.YILING_EID.equals(currentEid) || Constants.DAYUNHE_BUSINESS_EID.equals(currentEid);
            resultData.getGoodsSkuList().forEach(skuVO->{
                if(GoodsSkuStatusEnum.HIDE.getCode().equals(skuVO.getStatus())){
                    skuVO.setHideFlag(true);
                }else {
                    skuVO.setHideFlag(false);
                }
                skuVO.setShowSubscriptionButton(showSubscription);
            });

            //获取上下状态
            PopGoodsDTO popGoodsDTO = popGoodsApi.getPopGoodsByGoodsId(goodsId);
            resultData.setGoodsStatus(popGoodsDTO.getGoodsStatus());
            resultData.setIsPatent(popGoodsDTO.getIsPatent());

            //获取限价信息，限价只针对工业直属，一级商，二级商，专二普一
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(goodsFullDTO.getEid());
            if(EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode().equals(enterpriseDTO.getChannelId())
                    ||EnterpriseChannelEnum.LEVEL_1.getCode().equals(enterpriseDTO.getChannelId())
                    ||EnterpriseChannelEnum.LEVEL_2.getCode().equals(enterpriseDTO.getChannelId())
                    ||EnterpriseChannelEnum.Z2P1.getCode().equals(enterpriseDTO.getChannelId())){
                PopGoodsLimitPriceDTO limitPriceDTO = popGoodsLimitPriceApi.getLimitPriceBySpecificationsId(goodsFullDTO.getSellSpecificationsId(), PopGoodsLimitPriceStatusEnum.NORMAL.getCode());
                resultData.setGoodsLimitPrice(PojoUtils.map(limitPriceDTO, GoodsLimitPriceVO.class));
            }
            return Result.success(resultData);
        } else {
            return Result.failed("商品不存在");
        }
    }

    @ApiOperation(value = "pop后台编辑商品", httpMethod = "POST")
    @PostMapping("/edit")
    @Log(title = "pop后台编辑商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> edit(@RequestBody @Valid UpdateGoodsForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        form.getGoodsSkuList().forEach(skuForm->{
            if(skuForm.getHideFlag()!=null && skuForm.getHideFlag()){
                skuForm.setStatus(GoodsSkuStatusEnum.HIDE.getCode());
            }else {
                skuForm.setStatus(GoodsSkuStatusEnum.NORMAL.getCode());
            }
        });
        SaveOrUpdateGoodsRequest request = PojoUtils.map(form, SaveOrUpdateGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        SaveGoodsLineRequest goodsLineInfo = new SaveGoodsLineRequest();
        goodsLineInfo.setPopFlag(1);
        request.setGoodsLineInfo(goodsLineInfo);
        request.setId(form.getGoodsId());
        List<Long> eidList = ListUtil.empty();
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
        }
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
        if (form.getGoodsId() != null && form.getGoodsId() != 0) {
            //查询原始数据
            GoodsInfoDTO goodsInfoDTO = popGoodsApi.queryInfo(form.getGoodsId());

            //只能修改自己的商品
            if (!eidList.contains(goodsInfoDTO.getEid())) {
                return Result.failed("只能修改自己的商品信息");
            }

            //判断商品是否已经在平台上禁止销售
            if (goodsInfoDTO.getStandardId() != null && goodsInfoDTO.getStandardId() != 0) {
                StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(goodsInfoDTO.getStandardId());
                if (standardGoodsByIdInfoDTO == null || standardGoodsByIdInfoDTO.getBaseInfo().getGoodsStatus().equals(StandardGoodsStatusEnum.FORBID.getCode())) {
                    return Result.failed("该商品已经在平台禁止销售");
                }
            }

            if (form.getGoodsStatus() != null) {
                //验证上下架状态
                //判断当前商品状态是否可编辑商品
                if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsInfoDTO.getAuditStatus())) {
                    return Result.failed("该商品还没有审核通过不能编辑");
                }
                //如果是上架，修改下架原因0
                if (GoodsStatusEnum.UP_SHELF.getCode().equals(form.getGoodsStatus())) {
                    if (goodsInfoDTO.getOutReason().equals(GoodsOutReasonEnum.PLATFORM.getCode())) {
                        return Result.failed("该商品已经在平台禁止销售");
                    }
                    if (goodsInfoDTO.getOutReason().equals(GoodsOutReasonEnum.QUALITY_CONTROL.getCode())) {
                        return Result.failed("该商品已经被质管禁止销售");
                    }
                }
                //如果是下架架，修改下架原因2
                if (!Arrays.asList(GoodsStatusEnum.UNDER_REVIEW.getCode(), GoodsStatusEnum.REJECT.getCode()).contains(goodsInfoDTO.getAuditStatus())) {
                    request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
                }
                request.setEname(goodsInfoDTO.getEname());
                request.setEid(goodsInfoDTO.getEid());
            }
        } else {
            request.setEname(enterpriseDTO.getName());
            request.setEnterpriseType(EnterpriseTypeEnum.getByCode(enterpriseDTO.getType()).getCategory().getCode());
            request.setEid(enterpriseDTO.getId());
        }

        //判断商品内码是否有空格存在和重复的情况
        if (CollUtil.isNotEmpty(form.getGoodsSkuList())) {
            Set<String> inSnSet=new HashSet<>();
            for (UpdateGoodsSkuForm updateGoodsSkuForm : form.getGoodsSkuList()) {
                if (StrUtil.isNotEmpty(updateGoodsSkuForm.getInSn())) {
                    if (StrUtil.trim(updateGoodsSkuForm.getInSn()).length()!=updateGoodsSkuForm.getInSn().length()) {
                        return Result.failed("商品内码前后不能空格");
                    }
                    if(!inSnSet.contains(updateGoodsSkuForm.getInSn())){
                        inSnSet.add(updateGoodsSkuForm.getInSn());
                    }else{
                        return Result.failed("供应商商品内码已经存在");
                    }
                }
            }
        }
        //更新商品
        request.setPopEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        goodsApi.editGoods(request);
        return Result.success(new BoolObject(true));
    }


    @ApiOperation(value = "根据商品状态查询各自商品数量")
    @PostMapping("/queryStatusCountList")
    public Result<CollectionObject<List<StatusCountListItemVO>>> queryStatusCountList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = ListUtil.empty();
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
        }
        List<StatusCountListItemVO> list = PojoUtils.map(popGoodsApi.queryPopStatusCountList(eidList), StatusCountListItemVO.class);
        return Result.success(new CollectionObject(list));
    }

    /**
     * 批量上下架商品
     *
     * @param form
     * @return 对象主键
     */
    @ApiOperation(value = "批量上下架商品", notes = "批量上下架商品", httpMethod = "POST")
    @PostMapping(path = "/updateStatus")
    @Log(title = "批量上下架商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateStatus(@RequestBody @Valid BatchUpdateGoodsStatusForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = ListUtil.empty();
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
        }

        List<GoodsInfoDTO> goodsInfoDTOList = popGoodsApi.batchQueryInfo(form.getGoodsIds());
        // 验证是否是自己的商业公司商品
        List<Long> goodsEidList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getEid).distinct().collect(Collectors.toList());
        if (goodsEidList == null) {
            return Result.failed("选择了无效的商品");
        }

        for (Long eid : goodsEidList) {
            if (!eidList.contains(eid)) {
                return Result.failed("只能修改自己商业公司里面的商品信息");
            }
        }

        List<Integer> goodsStatusList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getGoodsStatus).distinct()
                .collect(Collectors.toList());
        if (!Arrays.asList(GoodsStatusEnum.UP_SHELF.getCode(), GoodsStatusEnum.UN_SHELF.getCode()).containsAll(goodsStatusList)) {
            return Result.failed("商品状态不符合上下架状态操作");
        }

        BatchUpdateGoodsStatusRequest request = PojoUtils.map(form, BatchUpdateGoodsStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentEid());
        // 上架
        if (GoodsStatusEnum.UP_SHELF.getCode().equals(form.getGoodsStatus())) {
            // 先检查商品是否是自己下架的
            for (GoodsInfoDTO goodsInfo : goodsInfoDTOList) {
                if (!GoodsOutReasonEnum.REJECT.getCode().equals(goodsInfo.getOutReason())) {
                    return Result.failed(
                            "上架商品存在[" + GoodsOutReasonEnum.getByCode(goodsInfo.getOutReason()).getName() + "]");
                }
            }
        }
        request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
        request.setGoodsLine(GoodsLineEnum.POP.getCode());
        return Result.success(new BoolObject(goodsApi.batchUpdateGoodsStatus(request)));
    }

    /**
     * 获取药品分类列表
     *
     * @return
     */
    @ApiOperation(value = "获取药品分类列表")
    @PostMapping("/getAll")
    public Result<CollectionObject<List<StandardGoodsCategoryInfoAllVO>>> getAllCateInfo() {
        List<StandardGoodsCategoryInfoAllDTO> allCateInfo = standardGoodsCategoryApi.getAllCateInfo();
        List<StandardGoodsCategoryInfoAllVO> voList = PojoUtils.map(allCateInfo, StandardGoodsCategoryInfoAllVO.class);
        return Result.success(new CollectionObject(voList));
    }

    /**
     * 查询商品修改日志
     *
     * @return
     */
    @ApiOperation(value = "查询商品修改日志")
    @PostMapping("/getGoodsLog")
    public Result<Page<GoodsLogPageListItemVO>> getGoodsLog(@Valid @RequestBody QueryGoodsLogForm form) {
        QueryGoodsLogRequest request = PojoUtils.map(form, QueryGoodsLogRequest.class);
        if (StrUtil.isNotBlank(form.getName())) {
            Staff staff = staffApi.getByUsername(form.getName());
            if (ObjectUtil.isNotNull(staff)) {
                request.setOperUserList(ListUtil.toList(staff.getId()));
            }
        }
        //查询日志
        Page<GoodsLogPageListItemDTO> page = goodsApi.queryGoodsLogPageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(PojoUtils.map(page, GoodsLogPageListItemVO.class));
        }
        //操作人id列表
        Set<Long> userIds = new HashSet<>();
        //查询用户信息
        for (int i = 0; i < page.getRecords().size(); i++) {
            GoodsLogPageListItemDTO item = page.getRecords().get(i);
            userIds.add(item.getCreateUser());
        }
        List<UserDTO> userDTOS = userApi.listByIds(new ArrayList<>(userIds));
        Map<Long, String> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        //补全操作人
        for (int i = 0; i < page.getRecords().size(); i++) {
            GoodsLogPageListItemDTO item = page.getRecords().get(i);
            item.setOprUser(userDTOMap.getOrDefault(item.getCreateUser(), ""));
        }
        return Result.success(PojoUtils.map(page, GoodsLogPageListItemVO.class));
    }

    @ApiOperation(value = "库存明细", httpMethod = "GET")
    @GetMapping("/getInventoryDetail")
    public Result<List<InventoryDetailVO>> getInventoryDetail(@RequestParam("skuId")Long skuId){
        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(skuId);
        if(null==inventoryDTO){
            return Result.failed("库存信息不存在");
        }
        List<InventorySubscriptionDTO> subscriptionDTOList = goodsApi.getInventoryDetailByInventoryId(inventoryDTO.getId());
        List<InventoryDetailVO> detailVOS = Lists.newLinkedList();
        if(CollectionUtil.isNotEmpty(subscriptionDTOList)){
            subscriptionDTOList.forEach(subscription->{
                InventoryDetailVO detailVO = new InventoryDetailVO();
                detailVO.setSubscriptionEid(subscription.getSubscriptionEid());
                detailVO.setSubscriptionEname(subscription.getSubscriptionEname());
                detailVO.setQty(subscription.getQty());
                detailVO.setSourceInSn(inventoryDTO.getInSn());
                detailVO.setSubscriptionType(subscription.getSubscriptionType());
                detailVO.setInSn(subscription.getInSn());
                detailVOS.add(detailVO);
            });
        }
        return Result.success(detailVOS);
    }

    @ApiOperation(value = "获取订阅列表", httpMethod = "GET")
    @GetMapping("/getSubscriptionList")
    public Result<List<InventoryDetailVO>> getSubscriptionList(@RequestParam("skuId")Long skuId,@CurrentUser CurrentStaffInfo staffInfo){

        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(skuId);
        if(null==inventoryDTO){
            return Result.failed("库存信息不存在");
        }
        QueryInventorySubscriptionRequest request = new QueryInventorySubscriptionRequest();
        request.setInventoryId(inventoryDTO.getId());
        request.setIsIncludeSelf(false);
        List<InventorySubscriptionDTO> list = inventorySubscriptionApi.getInventorySubscriptionList(request);
        List<InventoryDetailVO> detailVOS = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(list)){
            list.forEach(subscription -> {
                InventoryDetailVO detailVO = new InventoryDetailVO();
                detailVO.setId(subscription.getId());
                detailVO.setSubscriptionEid(subscription.getSubscriptionEid());
                detailVO.setSubscriptionEname(subscription.getSubscriptionEname());
                detailVO.setInSn(subscription.getInSn());
                detailVO.setSubscriptionType(subscription.getSubscriptionType());
                detailVOS.add(detailVO);
            });
        }
        return Result.success(detailVOS);
    }

    @ApiOperation(value = "保存订阅关系", httpMethod = "POST")
    @PostMapping("/saveInventorySubscription")
    public Result<Boolean> saveInventorySubscription(@Valid @RequestBody SaveInventorySubscriptionForm form,@CurrentUser CurrentStaffInfo staffInfo){
        Long currentEid = staffInfo.getCurrentEid();
        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(form.getSkuId());
        if(null==inventoryDTO){
            return Result.failed("库存信息不存在");
        }
        if(CollectionUtil.isNotEmpty(form.getSubscriptionList())){
            String filterMsg = "";
            for(SaveSubscriptionForm saveSubscription : form.getSubscriptionList()){
                if(Constants.YILING_EID.equals(currentEid)){
                    if(!SubscriptionTypeEnum.ERP.getType().equals(saveSubscription.getSubscriptionType())){
                        filterMsg = "当前企业只能订阅erp库存";
                    }
                }else if(Constants.DAYUNHE_BUSINESS_EID.equals(currentEid)){
                    if(!SubscriptionTypeEnum.POP.getType().equals(saveSubscription.getSubscriptionType())){
                        filterMsg = "当前企业只能订阅pop可售库存";
                    }
                }else {
                    filterMsg = "当前企业没有库存订阅权限";
                }
//                if(SubscriptionTypeEnum.SELF.getType().equals(saveSubscription.getSubscriptionType())){
//                    filterMsg = "不能编辑本店库存订阅关系";
//                }
                if(form.getEid().equals(saveSubscription.getSubscriptionEid())){
                    filterMsg = "被订阅方不能是自己";
                }
                if(StringUtils.isNotBlank(filterMsg)){
                    return Result.failed(filterMsg);
                }
            }
        }
        BatchSaveSubscriptionRequest request = PojoUtils.map(form, BatchSaveSubscriptionRequest.class);
        request.setInventoryId(inventoryDTO.getId());
        inventorySubscriptionApi.saveInventorySubscription(request);
        return Result.success(true);
    }
}
