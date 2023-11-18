package com.yiling.hmc.wechat.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.api.GoodsControlApi;
import com.yiling.hmc.goods.dto.GoodsControlDTO;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.goods.dto.request.QueryHmcGoodsRequest;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.api.InsuranceDetailApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.dto.InsuranceGoodsDTO;
import com.yiling.hmc.order.enums.HmcSourceTypeEnum;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.form.WxQueryMedicineForm;
import com.yiling.hmc.wechat.form.WxQueryShopForm;
import com.yiling.hmc.wechat.vo.*;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 药品首页控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/28
 */
@Slf4j
@RestController
@RequestMapping("/wx/medicine")
@Api(tags = "福利药品控制器")
public class MedicineHomeController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    InsuranceDetailApi insuranceDetailApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    GoodsControlApi goodsControlApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @Autowired
    FileService fileService;


    /**
     * 查询药品福利列表
     *
     * @param form
     * @return
     */
    @Log(title = "查询药品福利列表", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/list")
    @ApiOperation(value = "查询药品福利列表")
    public Result<MedicineHomeVO> list(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid WxQueryMedicineForm form) {

        log.info("[MedicineList]查询药品福利列表入参：{}", JSONUtil.toJsonStr(form));

        /**
         * 若销售者所在企业已经开通药品兑付功能，其所在企业能兑付的药品进行展示
         * 若此销售员所在企业未开通药品兑付功能，此处显示的药品为所有保险关联的保险福利药品
         */

        // 格式：注册来源_企业id_员工id
//        String qrCodeValue = form.getQrCodeValue();

        form.convertQrCodeValue();

        boolean flag = Boolean.FALSE;

        if (Objects.nonNull(form.getSellerEid())) {

            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getSellerEid());

            if (ObjectUtil.isNull(enterpriseDTO)) {
                log.error("通过企业id未查询到企业信息：eId:{}", form.getSellerEid());
                return Result.failed("通过企业id未查询到企业信息，企业id：" + form.getSellerEid());
            }

            log.info("[MedicineList]eId:{}对应企业信息：{}", form.getSellerEid(), enterpriseDTO);

            // 2.1、开通了药+险兑付 --> 展示当前企业下的药品
            if (EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK.getCode().equals(enterpriseDTO.getHmcType())) {
                flag = Boolean.TRUE;
            }

        }

        MedicineHomeVO homeVO = MedicineHomeVO.builder().build();
        List<MedicineItemVO> medicineItemList = Lists.newArrayList();

        log.info("[MedicineList]flag：{}", flag);
        // 1、有eId & 药+险销售与药品兑付的 -> 展示当前企业下的药品信息
        if (flag) {

            List<HmcGoodsDTO> goodsDTOS = goodsApi.listByEid(form.getSellerEid());
            log.info("[MedicineList]查询指定店铺药品福利结果：{}，sellerEId:{}", JSONUtil.toJsonStr(goodsDTOS), form.getSellerEid());

            if (CollUtil.isEmpty(goodsDTOS)) {
                log.info("通过企业id未查询到药品信息：eId:{}", form.getSellerEid());
                return Result.failed("通过企业id未查询到药品信息，企业id：" + form.getSellerEid());
            }

            // 获取管控基本信息
            List<Long> sellSpecificationsIdList = goodsDTOS.stream().map(HmcGoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
            List<GoodsControlDTO> goodsControlList = goodsControlApi.batchGetGoodsControlBySpecificationsIds(sellSpecificationsIdList);
            Map<Long, GoodsControlDTO> controlMap = goodsControlList.stream().collect(Collectors.toMap(GoodsControlDTO::getSellSpecificationsId, o -> o, (k1, k2) -> k1));

            // 获取保险明细信息
            List<Long> controlIdList = goodsControlList.stream().map(GoodsControlDTO::getId).collect(Collectors.toList());
            List<InsuranceDetailDTO> insuranceDetailList = insuranceDetailApi.listByControlId(controlIdList);
            Map<Long, InsuranceDetailDTO> InsuranceDetailMap = insuranceDetailList.stream().collect(Collectors.toMap(InsuranceDetailDTO::getControlId, o -> o, (k1, k2) -> k1));

            List<Long> insuranceIdList = insuranceDetailList.stream().map(InsuranceDetailDTO::getInsuranceId).collect(Collectors.toList());

            // 获取保险信息
            List<InsuranceDTO> insuranceList = insuranceApi.queryByIdList(insuranceIdList);
            Map<Long, InsuranceDTO> insuranceMap = insuranceList.stream().collect(Collectors.toMap(InsuranceDTO::getId, o -> o, (k1, k2) -> k1));

            // 获取保司
            List<Long> insuranceCompanyIdList = insuranceList.stream().map(InsuranceDTO::getInsuranceCompanyId).collect(Collectors.toList());
            List<InsuranceCompanyDTO> insuranceCompanyList = insuranceCompanyApi.listByIdList(insuranceCompanyIdList);
            Map<Long, InsuranceCompanyDTO> insuranceCompanyMap = insuranceCompanyList.stream().collect(Collectors.toMap(InsuranceCompanyDTO::getId, o -> o, (k1, k2) -> k1));


            // 获取商品基本信息
            List<Long> goodsIdList = goodsDTOS.stream().map(HmcGoodsDTO::getGoodsId).collect(Collectors.toList());
            List<GoodsDTO> goodsDTOList = goodsHmcApi.batchQueryGoodsInfo(goodsIdList);
            Map<Long, GoodsDTO> goodsDTOMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, o -> o, (k1, k2) -> k1));

            // 构建商品信息
            for (HmcGoodsDTO item : goodsDTOS) {
                MedicineItemVO medicineItem = MedicineItemVO.builder().build();
                medicineItem.setName(item.getGoodsName());
                medicineItem.setEId(item.getEid());
                medicineItem.setSellSpecificationsId(item.getSellSpecificationsId());
                Long hmcGoodsId = item.getId();
                medicineItem.setHmcGoodsId(hmcGoodsId);

                medicineItem.setSpecifications(Optional.ofNullable(goodsDTOMap.get(item.getGoodsId())).map(GoodsDTO::getSellSpecifications).orElse(null));
                String picUrl = Optional.ofNullable(goodsDTOMap.get(item.getGoodsId())).map(GoodsDTO::getPic).orElse(null);
                medicineItem.setPicUrl(fileService.getUrl(picUrl, FileTypeEnum.GOODS_PICTURE));
                BigDecimal marketPrice = Optional.ofNullable(controlMap.get(item.getSellSpecificationsId())).map(GoodsControlDTO::getMarketPrice).orElse(null);
                BigDecimal insurancePrice = Optional.ofNullable(controlMap.get(item.getSellSpecificationsId())).map(GoodsControlDTO::getInsurancePrice).orElse(null);

                medicineItem.setMarketPrice(marketPrice);

                medicineItem.setSavePrice(marketPrice.subtract(insurancePrice));

                GoodsControlDTO controlDTO = controlMap.get(item.getSellSpecificationsId());
                if (Objects.isNull(controlDTO)) {
                    log.info("规格id为{}的商品没有管控数据", item.getSellSpecificationsId());
                    continue;
                }

                InsuranceDetailDTO insuranceDetailDTO = InsuranceDetailMap.get(controlDTO.getId());
                if (Objects.isNull(insuranceDetailDTO)) {
                    log.info("规格id为{}的商品没有保险详情", item.getSellSpecificationsId());
                    continue;
                }

                InsuranceDTO insuranceDTO = insuranceMap.get(insuranceDetailDTO.getInsuranceId());
                if (Objects.isNull(insuranceDTO)) {
                    log.info("规格id为{}的商品没有保险", item.getSellSpecificationsId());
                    continue;
                }

                InsuranceCompanyDTO insuranceCompany = insuranceCompanyMap.get(insuranceDTO.getInsuranceCompanyId());
                if (Objects.isNull(insuranceCompany)) {
                    log.info("规格id为{}的商品没有对应保司", item.getSellSpecificationsId());
                    continue;
                }

                String url = insuranceDTO.getUrl();
                url += "?eId=" + item.getEid();
                url += "&insuranceId=" + insuranceDTO.getId();
                url += "&hmcGoodsId=" + hmcGoodsId;
                if (Objects.nonNull(currentUser)) {
                    url += "&userId=" + currentUser.getCurrentUserId();
                }

                if (Objects.nonNull(form.getSellerEid())) {
                    url += "&sellerEid=" + form.getSellerEid();
                }
                if (Objects.nonNull(form.getSellerUserId())) {
                    url += "&sellerUserId=" + form.getSellerUserId();
                }
                url += "&isHideHome=true";
                url += "&insuranceCompanyId=" + insuranceCompany.getId();

                medicineItem.setBuyInsuranceUrl(url);

                medicineItem.setInsuranceId(insuranceDTO.getId());

                medicineItemList.add(medicineItem);
            }

            // 显示弹框：FALSE
            homeVO.setShowShopDialog(Boolean.FALSE);

        } else {
            // 2、自然流量 、扫药盒 、企业未开通药+险兑付 -> 查询所有药品去重展示  此处显示的药品为所有保险关联的保险福利药品

            List<InsuranceGoodsDTO> insuranceGoodsList = insuranceApi.queryGoods();
            log.info("[MedicineList]自然流量查询药品福利结果：{}", JSONUtil.toJsonStr(insuranceGoodsList));
            List<Long> specificationIds = insuranceGoodsList.stream().map(InsuranceGoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
            Map<Long, InsuranceGoodsDTO> insuranceGoodsMap = insuranceGoodsList.stream().collect(Collectors.toMap(InsuranceGoodsDTO::getSellSpecificationsId, o -> o, (k1, k2) -> k1));

            List<StandardGoodsBasicDTO> goodsList = goodsHmcApi.batchQueryStandardGoodsBasicBySpecificationsIds(specificationIds);

            medicineItemList = goodsList.stream().map(item -> {
                MedicineItemVO medicineItem = MedicineItemVO.builder().build();
                medicineItem.setName(item.getName());
                medicineItem.setPicUrl(fileService.getUrl(item.getPic(), FileTypeEnum.GOODS_PICTURE));
                medicineItem.setSpecifications(item.getSellSpecifications());
                medicineItem.setSellSpecificationsId(item.getSellSpecificationsId());

                InsuranceGoodsDTO insuranceGoodsDTO = insuranceGoodsMap.get(item.getSellSpecificationsId());

                medicineItem.setMarketPrice(insuranceGoodsDTO.getMarketPrice());
                medicineItem.setSavePrice(insuranceGoodsDTO.getMarketPrice().subtract(insuranceGoodsDTO.getInsurancePrice()));

                medicineItem.setInsuranceId(insuranceGoodsDTO.getInsuranceId());

                return medicineItem;
            }).collect(Collectors.toList());

            // 显示弹框：TRUE
            homeVO.setShowShopDialog(Boolean.TRUE);

        }

        homeVO.setMedicineItemList(medicineItemList);

        log.info("[MedicineList]查询药品福利列表返回值：{}", JSONUtil.toJsonStr(homeVO));
        return Result.success(homeVO);

    }

    /**
     * 根据售卖规格 -> 店铺
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "根据售卖规格查询店铺")
    @Log(title = "根据售卖规格id查询店铺", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/get_shop_by_sellSpecifications_Id")
    public Result<MedicineShopVO> getShopBySellSpecificationsId(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid WxQueryShopForm form) {

        log.info("[get_shop_by_sellSpecifications_Id]查询入参：{}", JSONUtil.toJsonStr(form));

        form.convertQrCodeValue();

        QueryHmcGoodsRequest request = new QueryHmcGoodsRequest();
        request.setSellSpecificationsId(form.getSellSpecificationsId());
        List<HmcGoodsDTO> goodsDTOList = goodsApi.findBySpecificationsId(request);
        if (CollUtil.isEmpty(goodsDTOList)) {
            return Result.failed("根据售卖规格id未查询到商品");
        }

        List<Long> eIdList = goodsDTOList.stream().map(HmcGoodsDTO::getEid).collect(Collectors.toList());

        InsuranceDTO insuranceDTO = insuranceApi.queryById(form.getInsuranceId());
        if (Objects.isNull(insuranceDTO)) {
            log.error("根据保险id未查询到保险数据，保险id：{}", form.getInsuranceId());
            return Result.failed("根据保险id未查询到保险数据");
        }

        InsuranceCompanyDTO insuranceCompany = insuranceCompanyApi.queryById(insuranceDTO.getInsuranceCompanyId());
        if (Objects.isNull(insuranceCompany)) {
            log.error("根据保司id未查询到保司数据，保司id：{}", insuranceDTO.getInsuranceCompanyId());
            return Result.failed("根据保司id未查询到保司数据");
        }

        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eIdList);
        enterpriseList = enterpriseList.stream().filter(item -> EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK.getCode().equals(item.getHmcType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(enterpriseList)) {
            log.error("根据保司id未查询到保司数据，保司id：{}", insuranceDTO.getInsuranceCompanyId());
            return Result.failed("根据保司id未查询到保司数据");
        }
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));

        List<MedicineShopDetailVO> ShopDetailList = Lists.newArrayList();
        goodsDTOList.stream().forEach(item -> {

            if (!enterpriseMap.containsKey(item.getEid())) {
                log.info("未获取eid:{}到企业信息", item.getEid());
                return;
            }

            MedicineShopDetailVO shopDetail = MedicineShopDetailVO.builder().build();
            shopDetail.setEId(item.getEid());
            shopDetail.setEName(Optional.ofNullable(enterpriseMap.get(item.getEid())).map(EnterpriseDTO::getName).orElse(null));
            shopDetail.setAddress(Optional.ofNullable(enterpriseMap.get(item.getEid())).map(EnterpriseDTO::getAddress).orElse(null));
            shopDetail.setHmcGoodsId(item.getId());


            String url = insuranceDTO.getUrl();
            url += "?eId=" + item.getEid();
            url += "&insuranceId=" + insuranceDTO.getId();
            url += "&hmcGoodsId=" + item.getId();
            url += "&userId=" + currentUser.getCurrentUserId();

            if (Objects.nonNull(form.getSellerEid())) {
                url += "&sellerEid=" + form.getSellerEid();
            }
            if (Objects.nonNull(form.getSellerUserId())) {
                url += "&sellerUserId=" + form.getSellerUserId();
            }

            url += "&isHideHome=true";
            url += "&insuranceCompanyId=" + insuranceCompany.getId();

            shopDetail.setBuyInsuranceUrl(url);

            ShopDetailList.add(shopDetail);

        });

        MedicineShopVO shopVO = MedicineShopVO.builder().shopDetailList(ShopDetailList).build();
        log.info("[get_shop_by_sellSpecifications_Id]根据售卖规格查询店铺返回参数：{}", JSONUtil.toJsonStr(shopVO));
        return Result.success(shopVO);
    }

    /**
     * 查询用户是否参保
     *
     * @param currentUser
     * @return
     */
    @ApiOperation(value = "查询用户是否参保")
    @ApiResponses({
            @ApiResponse(code = 200, message = "true - 已参保，false - 未参保")
    })
    @Log(title = "查询用户是否参保", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/has_insurance")
    public Result<Boolean> hasInsurance(@CurrentUser CurrentUserInfo currentUser) {
        Long userId = Optional.ofNullable(currentUser).map(CurrentUserInfo::getCurrentUserId).orElse(null);
        if (Objects.nonNull(userId)) {
            boolean hasInsurance = insuranceRecordApi.hasInsurance(userId);
            log.info("查询用户是否参保返回：{}", hasInsurance);
            return Result.success(hasInsurance);
        }
        return Result.success(Boolean.FALSE);

    }

    /**
     * 展示所有开通药+险兑付的店铺
     *
     * @return
     */
    @ApiOperation(value = "展示所有开通药+险兑付的店铺")
    @Log(title = "展示所有开通药+险兑付的店铺", businessType = BusinessTypeEnum.OTHER)
    @PostMapping("/show_hmc_shops")
    public Result<Page<HmcEnterpriseVO>> showAllHmcShops(@RequestBody QueryPageListForm queryPageListForm) {
        QueryEnterprisePageListRequest request = PojoUtils.map(queryPageListForm, QueryEnterprisePageListRequest.class);
        request.setHmcType(EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK.getCode());
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(request);
        Page<HmcEnterpriseVO> pageVO = PojoUtils.map(enterpriseDTOPage, HmcEnterpriseVO.class);
        return Result.success(pageVO);
    }


}
