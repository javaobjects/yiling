package com.yiling.user.shop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDO;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDetailDO;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseHighQualitySupplierService;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaDetailService;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.shop.dao.ShopMapper;
import com.yiling.user.shop.dao.ShopPaymentMethodMapper;
import com.yiling.user.shop.dto.AreaChildrenDTO;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.ShopPaymentMethodDTO;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.shop.dto.request.SaveShopRequest;
import com.yiling.user.shop.dto.request.UpdateAnnouncementRequest;
import com.yiling.user.shop.entity.ShopDO;
import com.yiling.user.shop.entity.ShopPaymentMethodDO;
import com.yiling.user.shop.service.ShopService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 店铺 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Service
@Slf4j
public class ShopServiceImpl extends BaseServiceImpl<ShopMapper, ShopDO> implements ShopService {

    @Autowired
    ShopPaymentMethodMapper    shopPaymentMethodMapper;
    @Autowired
    EnterpriseSalesAreaService enterpriseSalesAreaService;
    @Autowired
    EnterpriseSalesAreaDetailService enterpriseSalesAreaDetailService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    EnterpriseHighQualitySupplierService enterpriseHighQualitySupplierService;

    public static final String SHOP_LOGO_DEFAULT_PIC = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/shop_default_pic.png";


    @Override
    public ShopDTO getShop(Long currentEid) {
        LambdaQueryWrapper<ShopDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopDO::getShopEid,currentEid);

        ShopDO shopDO = getOne(queryWrapper);
        if (Objects.isNull(shopDO)) {
            //如果没有设置店铺信息，给默认信息返回
            shopDO = new ShopDO();
            shopDO.setShopEid(currentEid);
            shopDO.setStartAmount(BigDecimal.ZERO);
            shopDO.setShopName(Optional.ofNullable(enterpriseService.getById(currentEid)).orElse(new EnterpriseDO()).getName());
            shopDO.setShopLogo(SHOP_LOGO_DEFAULT_PIC);

        }

        ShopDTO shopDTO = PojoUtils.map(shopDO, ShopDTO.class);
        shopDTO.setHighQualitySupplierFlag(enterpriseHighQualitySupplierService.getHighQualitySupplierFlag(currentEid));

        return shopDTO;
    }

    @Override
    public EnterpriseSalesAreaDO getShopAreaJson(Long shopId) {
        ShopDO shopDO = Optional.ofNullable(this.getById(shopId)).orElseThrow(()->new BusinessException(UserErrorCode.SHOP_NOT_EXISTS));

        return enterpriseSalesAreaService.getByEid(shopDO.getShopEid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setShop(SaveShopRequest request) {
        ShopDO shopDO = PojoUtils.map(request,ShopDO.class);

        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getShopEid,request.getShopEid());
        ShopDO shop = this.getOne(wrapper);
        if(Objects.nonNull(shop)){
            shopDO.setId(shop.getId());
        }
        log.info("当前企业ID：{}，设置店铺信息请求参数：{}",request.getShopEid(), JSONObject.toJSONString(request));

        this.saveOrUpdate(shopDO);

        if(StrUtil.isNotEmpty(request.getAreaJsonString())){
            //新增或更新销售区域JSON
            enterpriseSalesAreaService.save(shopDO.getShopEid(),request.getAreaJsonString(),request.getOpUserId());

            // 获取已经设置的店铺销售区域
            List<EnterpriseSalesAreaDetailDO> saleAreaDetailList = enterpriseSalesAreaDetailService.getEnterpriseSaleAreaDetail(shopDO.getShopEid());
            List<String> areaCodeList = saleAreaDetailList.stream().map(EnterpriseSalesAreaDetailDO::getAreaCode).distinct().collect(Collectors.toList());

            List<AreaChildrenDTO> areaChildrenList = JSONObject.parseArray(request.getAreaJsonString(), AreaChildrenDTO.class);

            List<String> shopAreaCodeList = ListUtil.toList();
            getShopAreaCodeList(areaChildrenList , shopAreaCodeList);

            // 移除
            List<String> removeCodes = areaCodeList.stream().filter(code -> !shopAreaCodeList.contains(code)).distinct().collect(Collectors.toList());
            removeShopAreaCode(removeCodes,shopDO.getShopEid(),request.getOpUserId());

            // 新增
            List<String> addCodes = shopAreaCodeList.stream().filter(code -> !areaCodeList.contains(code)).distinct().collect(Collectors.toList());
            addShopAreaCode(addCodes,shopDO.getShopEid(),request.getOpUserId());

        }

        if(CollUtil.isNotEmpty(request.getPaymentMethodList())){
            // 支付方式处理
            LambdaQueryWrapper<ShopPaymentMethodDO> queryWrapper = new LambdaQueryWrapper<ShopPaymentMethodDO>().eq(ShopPaymentMethodDO::getShopId, shopDO.getId());
            List<Integer> payMethodList = shopPaymentMethodMapper.selectList(queryWrapper).stream().map(ShopPaymentMethodDO::getPaymentMethod).distinct().collect(Collectors.toList());

            List<Integer> paymentMethodList = request.getPaymentMethodList();
            // 移除
            List<Integer> removeMethods = payMethodList.stream().filter(code -> !paymentMethodList.contains(code)).distinct().collect(Collectors.toList());
            removePayMethods(removeMethods,shopDO.getId(),request.getOpUserId());

            // 新增
            List<Integer> addMethods = paymentMethodList.stream().filter(code -> !payMethodList.contains(code)).distinct().collect(Collectors.toList());
            addPayMethods(addMethods, shopDO.getId(), shopDO.getShopEid(), request.getOpUserId());

        }

        return true;
    }

    private void getShopAreaCodeList(List<AreaChildrenDTO> areaChildrenList , List<String> shopAreaCodeList ) {
        if(CollUtil.isEmpty(areaChildrenList)) {
            return;
        }

        for (AreaChildrenDTO provinceDTO : areaChildrenList) {
            for (AreaChildrenDTO cityDTO : provinceDTO.getChildren()) {
                for (AreaChildrenDTO areaCode : cityDTO.getChildren()) {
                    shopAreaCodeList.add(areaCode.getCode());
                }
            }
        }
    }

    @Override
    public List<ShopPaymentMethodDTO> getShopPayMethods(Long shopId) {
        LambdaQueryWrapper<ShopPaymentMethodDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopPaymentMethodDO::getShopId,shopId);

        return PojoUtils.map(shopPaymentMethodMapper.selectList(queryWrapper), ShopPaymentMethodDTO.class);
    }

    @Override
    public Page<ShopListItemDTO> queryShopListPage(QueryShopRequest request) {

        LambdaQueryWrapper<ShopDO> queryWrapper = new LambdaQueryWrapper<>();
        if(CollUtil.isNotEmpty(request.getShopEidList())){
            queryWrapper.in(ShopDO::getShopEid,request.getShopEidList());
        }
        if(StrUtil.isNotEmpty(request.getShopName())){
            queryWrapper.like(ShopDO::getShopName,request.getShopName());
        }
        queryWrapper.orderByDesc(ShopDO::getCreateTime);

        return PojoUtils.map(this.page(request.getPage(),queryWrapper),ShopListItemDTO.class);
    }

    @Override
    public Page<ShopListItemDTO> queryPurchaseShopListPage(QueryShopRequest request) {
        Page<ShopDO> doPage = this.baseMapper.queryPurchaseShopListPage(new Page<>(request.getCurrent(), request.getSize()),request);
        return PojoUtils.map(doPage,ShopListItemDTO.class);
    }

    @Override
    public ShopListItemDTO getShopDetail(Long shopId) {

        return PojoUtils.map(this.getById(shopId),ShopListItemDTO.class);
    }

    @Override
    public List<ShopDTO> listShopByEidList(List<Long> currentEidList) {
        if(CollUtil.isEmpty(currentEidList)){
            return ListUtil.toList();
        }

        LambdaQueryWrapper<ShopDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ShopDO::getShopEid,currentEidList);

        return PojoUtils.map(list(queryWrapper),ShopDTO.class);
    }

    /**
     * 判断店铺是否可以销售给当前登录企业
     *
     * @param customerEid 当前登录的企业ID
     * @param eidList 卖方企业ID（支持多个卖家）
     * @return key 表示卖方企业ID , value 表示 是否可以销售
     */
    @Override
    public Map<Long, Boolean> checkSaleAreaByCustomerEid(Long customerEid, List<Long> eidList) {
        Map<Long, Boolean> map = MapUtil.newHashMap();
        if(Objects.isNull(customerEid) || CollUtil.isEmpty(eidList)){
            return map;
        }

        //获取当前登录企业的信息
        EnterpriseDO enterpriseDto = Optional.ofNullable(enterpriseService.getById(customerEid)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

        // 根据批量企业ID和区域编码，获取可销售该区域的企业
        List<Long> list = enterpriseSalesAreaDetailService.getEnterpriseSaleAreaDetailByListAndArea(eidList, enterpriseDto.getRegionCode());

        eidList.forEach(eid -> map.put(eid, list.contains(eid)));

        return map;

    }

    /**
     * 根据当前登录企业的销售区域，获取可以买哪些企业的接口
     * @param eid
     * @return
     */
    @Override
    public List<Long> getSellEidByEidSaleArea(Long eid) {
        List<Long> list = ListUtil.toList();
        //校验数据
        if(Objects.isNull(eid) || eid == 0){
            return list;
        }

        //当前企业的区域
        String regionCode = Optional.ofNullable(enterpriseService.getById(eid)).orElse(new EnterpriseDO()).getRegionCode();
        // 获取所有开通了B2B的店铺
        LambdaQueryWrapper<ShopDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ShopDO::getShopEid);
        List<Long> shopEidList = this.list(queryWrapper).stream().map(ShopDO::getShopEid).collect(Collectors.toList());

        //获取所有开通了B2B的企业信息（不包括工业的）对应的销售区域
        QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
        request.setIds(shopEidList);
        request.setMallFlag(1);
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        request.setNotInTypeList(ListUtil.toList(EnterpriseTypeEnum.INDUSTRY.getCode()));
        List<Long> eidList = enterpriseService.queryList(request).stream().map(EnterpriseDO::getId).collect(Collectors.toList());

        //根据开通了B2B店铺的企业ID和区域编码，获取可销售该区域的企业
        return enterpriseSalesAreaDetailService.getEnterpriseSaleAreaDetailByListAndArea(eidList, regionCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateShopAnnouncement(UpdateAnnouncementRequest request) {
        LambdaQueryWrapper<ShopDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopDO::getShopEid,request.getShopEid());
        ShopDO shopDO = Optional.ofNullable(getOne(queryWrapper)).orElseThrow(() -> new BusinessException(UserErrorCode.SHOP_NOT_EXISTS_PLEASE_SET));

        ShopDO shop = new ShopDO();
        shop.setId(shopDO.getId());
        shop.setShopAnnouncement(request.getShopAnnouncement());
        shop.setOpUserId(request.getOpUserId());
        return this.updateById(shop);
    }

    @Override
    public ShopDTO getShopAnnouncement(Long currentEid) {
        LambdaQueryWrapper<ShopDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopDO::getShopEid,currentEid);
        ShopDO shopDO = Optional.ofNullable(getOne(queryWrapper)).orElse(new ShopDO());
        return PojoUtils.map(shopDO,ShopDTO.class);
    }

    private boolean addPayMethods(List<Integer> addMethods, Long shopId, Long eid, Long opUserId) {
        if(CollUtil.isEmpty(addMethods)){
            return true;
        }

        List<ShopPaymentMethodDO> list = addMethods.stream().map(payMethod -> {
            ShopPaymentMethodDO shopPaymentMethodDO = new ShopPaymentMethodDO();
            shopPaymentMethodDO.setShopId(shopId);
            shopPaymentMethodDO.setEid(eid);
            shopPaymentMethodDO.setPaymentMethod(payMethod);
            shopPaymentMethodDO.setOpUserId(Objects.isNull(opUserId) ? 0L : opUserId);
            return shopPaymentMethodDO;
        }).collect(Collectors.toList());

        return shopPaymentMethodMapper.addPayMethods(list) > 0;
    }

    private boolean removePayMethods(List<Integer> removeMethods, Long shopId, Long opUserId) {
        if (CollUtil.isEmpty(removeMethods)) {
            return true;
        }

        LambdaQueryWrapper<ShopPaymentMethodDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopPaymentMethodDO::getShopId, shopId);
        queryWrapper.in(ShopPaymentMethodDO::getPaymentMethod, removeMethods);

        ShopPaymentMethodDO entity = new ShopPaymentMethodDO();
        entity.setOpUserId(opUserId);

        return shopPaymentMethodMapper.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    /**
     * 添加店铺关联区域
     * @param removeCodes
     * @param eid
     * @param opsUserId
     */
    private boolean addShopAreaCode(List<String> removeCodes , Long eid , Long opsUserId){
        if(CollUtil.isEmpty(removeCodes)){
            return true;
        }

        List<EnterpriseSalesAreaDetailDO> list = removeCodes.stream().map(code -> {
            EnterpriseSalesAreaDetailDO salesAreaDetailDO = new EnterpriseSalesAreaDetailDO();
            salesAreaDetailDO.setEid(eid);
            salesAreaDetailDO.setAreaCode(code);
            salesAreaDetailDO.setOpUserId(Objects.isNull(opsUserId) ? 0L : opsUserId);
            return salesAreaDetailDO;
        }).collect(Collectors.toList());

        return enterpriseSalesAreaDetailService.insertEnterpriseSaleAreaDetail(list);
    }

    /**
     * 移除店铺关联区域代码
     * @param removeCodes
     * @param eid
     * @param opsUserId
     * @return
     */
    public boolean removeShopAreaCode(List<String> removeCodes , Long eid , Long opsUserId) {
        if (CollUtil.isEmpty(removeCodes)) {
            return true;
        }

        LambdaQueryWrapper<EnterpriseSalesAreaDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseSalesAreaDetailDO::getEid, eid);
        queryWrapper.in(EnterpriseSalesAreaDetailDO::getAreaCode, removeCodes);

        EnterpriseSalesAreaDetailDO entity = new EnterpriseSalesAreaDetailDO();
        entity.setOpUserId(opsUserId);

        return enterpriseSalesAreaDetailService.batchDeleteWithFill(entity, queryWrapper) > 0;
    }


}
