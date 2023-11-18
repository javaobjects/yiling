package com.yiling.hmc.goods.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveDetailRequest;
import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveRequest;
import com.yiling.hmc.enterprise.enums.HmcEnterpriseErrorCode;
import com.yiling.hmc.goods.dto.SyncGoodsDTO;
import com.yiling.hmc.pharmacy.dto.PharmacyDTO;
import com.yiling.hmc.pharmacy.service.PharmacyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.HmcSaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsLineStatusEnum;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveListRequest;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveRequest;
import com.yiling.hmc.goods.bo.EnterpriseGoodsCountBO;
import com.yiling.hmc.goods.bo.HmcGoodsBO;
import com.yiling.hmc.goods.dao.GoodsMapper;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.goods.dto.request.HmcGoodsPageRequest;
import com.yiling.hmc.goods.dto.request.QueryHmcGoodsRequest;
import com.yiling.hmc.goods.entity.HmcGoodsDO;
import com.yiling.hmc.goods.service.GoodsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * C端保险药品商家提成设置表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Slf4j
@Service
public class GoodsServiceImpl extends BaseServiceImpl<GoodsMapper, HmcGoodsDO> implements GoodsService {

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @Autowired
    PharmacyService pharmacyService;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    StandardGoodsSpecificationApi specificationApi;

    @Override
    public HmcGoodsDTO findById(Long id) {
        HmcGoodsDO hmcGoodsDO = this.getById(id);
        return PojoUtils.map(hmcGoodsDO, HmcGoodsDTO.class);
    }

    @Override
    @Transactional
    public boolean saveGoodsList(GoodsSaveListRequest request) {
        log.info("保险商品保存，请求数据为:[{}]", JSONUtil.toJsonStr(request));
        // 可能会出现一个药品再次添加的情况（查询的时候过滤一下，新增的售后也需要过滤一下）
        List<GoodsSaveRequest> requestList = operateGoodsSaveListRequest(request);
        // 1.id存在的时候说明是修改
        List<GoodsSaveRequest> updateList = requestList.stream().filter(e -> null != e.getId()).collect(Collectors.toList());
        List<HmcGoodsDO> updateGoodsList = PojoUtils.map(updateList, HmcGoodsDO.class);
        if (CollUtil.isNotEmpty(updateGoodsList)) {
            updateGoodsList.forEach(e -> {
                e.setOpUserId(request.getOpUserId());
                e.setOpTime(request.getOpTime());
            });
            this.updateBatchById(updateGoodsList);
        }

        // 2.id不存在，则先删除现在不存在的id(与之前的对比)，再新增
        //        List<HmcGoodsDO> oldList = this.listByEid(request.getEid());
        //        if (CollUtil.isNotEmpty(oldList)) {
        //            List<Long> updateIdList = updateList.stream().map(GoodsSaveRequest::getId).collect(Collectors.toList());
        //            List<Long> oldIdList = oldList.stream().map(HmcGoodsDO::getId).collect(Collectors.toList());
        //            List<Long> deleteIdList = oldIdList.stream().filter(e -> !updateIdList.contains(e)).collect(Collectors.toList());
        //            deleteGoodsList(deleteIdList, request.getOpUserId(), request.getOpTime());
        //        }

        // 3.id不存在，再新增
        List<HmcGoodsDO> addGoodsList = new ArrayList<>();
        List<GoodsSaveRequest> addOrDeleteList = requestList.stream().filter(e -> null == e.getId()).collect(Collectors.toList());
        addOrDeleteList.forEach(e -> {
            HmcGoodsDO hmcGoodsDO = PojoUtils.map(e, HmcGoodsDO.class);
            hmcGoodsDO.setEid(request.getEid());
            hmcGoodsDO.setEname(request.getEname());
            HmcSaveOrUpdateGoodsRequest goodsRequest = new HmcSaveOrUpdateGoodsRequest();
            goodsRequest.setEid(request.getEid());
            goodsRequest.setEname(request.getEname());
            goodsRequest.setSellSpecificationsId(e.getSellSpecificationsId());
            goodsRequest.setEnterpriseType(e.getEnterpriseType());
            goodsRequest.setOpUserId(request.getOpUserId());
            goodsRequest.setOpTime(request.getOpTime());

            log.info("保险商品保存，请求商品中心创建以岭商品，请求数据为:[{}]", goodsRequest);
            Long goodsId = goodsHmcApi.generateGoods(goodsRequest);
            log.info("保险商品保存，请求商品中心创建以岭商品,返回商品goodsId:[{}]", goodsId);

            QueryHmcGoodsRequest hmcGoodsRequest = new QueryHmcGoodsRequest();
            hmcGoodsRequest.setEid(request.getEid());
            hmcGoodsRequest.setSellSpecificationsId(e.getSellSpecificationsId());
            HmcGoodsDTO hmcGoodsDTO = this.findBySpecificationsIdAndEid(hmcGoodsRequest);
            log.info("保险商品保存，商品的信息为:[{}]", hmcGoodsDTO);

            if (null != goodsId && 0 < goodsId && null == hmcGoodsDTO) {
                hmcGoodsDO.setGoodsId(goodsId);
                hmcGoodsDO.setStatus(GoodsLineStatusEnum.ENABLED.getCode());
                hmcGoodsDO.setOpUserId(request.getOpUserId());
                hmcGoodsDO.setOpTime(request.getOpTime());
                addGoodsList.add(hmcGoodsDO);
            }
        });
        if (CollUtil.isNotEmpty(addGoodsList)) {
            this.saveBatch(addGoodsList);
        }
        return true;
    }

    @Override
    public List<SyncGoodsDTO> syncGoodsToHmc(SyncGoodsSaveRequest request) {
        ArrayList<SyncGoodsDTO> result = Lists.newArrayList();
        List<SyncGoodsSaveDetailRequest> detailList = request.getDetailList();
        for (SyncGoodsSaveDetailRequest detailRequest : detailList) {
            PharmacyDTO pharmacyDTO = pharmacyService.findByIhEid(detailRequest.getIhEid());
            if (Objects.isNull(pharmacyDTO)) {
                throw new BusinessException(HmcEnterpriseErrorCode.PHARMACY_NOT_FOUND_ERROR);
            }
            StandardGoodsDTO standardGoodsDTO = standardGoodsApi.getOneById(detailRequest.getStandardId());
            if (ObjectUtil.isNull(standardGoodsDTO)) {
                throw new BusinessException(ResultCode.FAILED, "选择的标准库规格不存在");
            }
            StandardGoodsSpecificationDTO goodsSpecification = specificationApi.getStandardGoodsSpecification(detailRequest.getSellSpecificationsId());
            if (ObjectUtil.isNull(goodsSpecification)) {
                throw new BusinessException(ResultCode.FAILED, "选择的标准库商品不存在");
            }

            // HmcGoodsDO hmcGoodsDO
            SyncGoodsDTO syncGoodsDTO = PojoUtils.map(detailRequest, SyncGoodsDTO.class);
            result.add(syncGoodsDTO);

            QueryWrapper<HmcGoodsDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(HmcGoodsDO::getStandardId, detailRequest.getStandardId());
            wrapper.lambda().eq(HmcGoodsDO::getSellSpecificationsId, detailRequest.getSellSpecificationsId());
            wrapper.lambda().eq(HmcGoodsDO::getEid, pharmacyDTO.getEid());
            wrapper.lambda().eq(HmcGoodsDO::getIhEid, detailRequest.getIhEid());

            HmcGoodsDO one = this.getOne(wrapper);
            if (Objects.nonNull(one)) {
                log.info("当前商品已经存在，跳过处理，商品信息:{}", JSONUtil.toJsonStr(one));
                syncGoodsDTO.setHmcGoodsId(one.getId());
                continue;
            }

            HmcSaveOrUpdateGoodsRequest goodsRequest = new HmcSaveOrUpdateGoodsRequest();
            goodsRequest.setEid(pharmacyDTO.getEid());
            goodsRequest.setEname(pharmacyDTO.getEname());
            goodsRequest.setSellSpecificationsId(detailRequest.getSellSpecificationsId());
            log.info("请求商品中心创建以岭商品，请求数据为:{}", goodsRequest);
            Long goodsId = goodsHmcApi.generateGoods(goodsRequest);
            log.info("请求商品中心创建以岭商品,返回商品goodsId:{}", goodsId);

            HmcGoodsDO hmcGoodsDO = new HmcGoodsDO();
            hmcGoodsDO.setEid(pharmacyDTO.getEid());
            hmcGoodsDO.setEname(pharmacyDTO.getEname());
            hmcGoodsDO.setGoodsId(goodsId);
            hmcGoodsDO.setGoodsName(standardGoodsDTO.getName());
            hmcGoodsDO.setStatus(GoodsLineStatusEnum.ENABLED.getCode());
            hmcGoodsDO.setStandardId(detailRequest.getStandardId());
            hmcGoodsDO.setSellSpecificationsId(detailRequest.getSellSpecificationsId());
            hmcGoodsDO.setIhEid(detailRequest.getIhEid());
            hmcGoodsDO.setIhCPlatformId(detailRequest.getIhCPlatformId());
            hmcGoodsDO.setIhPharmacyGoodsId(detailRequest.getIhPharmacyGoodsId());
            hmcGoodsDO.setCreateTime(DateUtil.date());
            this.save(hmcGoodsDO);
            syncGoodsDTO.setHmcGoodsId(hmcGoodsDO.getId());
        }
        return result;
    }

    /**
     * 清楚重复商品信息
     *
     * @param request 请求参数
     * @return 新增商品信息
     */
    private List<GoodsSaveRequest> operateGoodsSaveListRequest(GoodsSaveListRequest request) {
        List<GoodsSaveRequest> requestList = request.getGoodsRequest();
        return requestList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsSaveRequest::getSellSpecificationsId))), ArrayList::new));
    }

    /**
     * 删除多条数据
     *
     * @param idList   id集合
     * @param opUserId 操作人
     * @param opTime   操作时间
     */
    private void deleteGoodsList(List<Long> idList, Long opUserId, Date opTime) {
        if (CollUtil.isNotEmpty(idList)) {
            HmcGoodsDO deleteEntity = new HmcGoodsDO();
            deleteEntity.setOpUserId(opUserId);
            deleteEntity.setOpTime(opTime);
            QueryWrapper<HmcGoodsDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(HmcGoodsDO::getId, idList);
            this.batchDeleteWithFill(deleteEntity, wrapper);
        }
    }

    @Override
    public List<HmcGoodsDO> listByEid(Long eid) {
        QueryWrapper<HmcGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HmcGoodsDO::getEid, eid);
        wrapper.lambda().orderByDesc(HmcGoodsDO::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public List<HmcGoodsBO> batchQueryGoodsInfo(List<Long> ids) {
        QueryWrapper<HmcGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(HmcGoodsDO::getId, ids);
        List<HmcGoodsDO> list = this.list(queryWrapper);
        List<HmcGoodsBO> goodsBOList = PojoUtils.map(list, HmcGoodsBO.class);
        this.completeGoodsInfo(goodsBOList);
        return goodsBOList;
    }

    @Override
    public List<EnterpriseGoodsCountBO> countGoodsByEids(List<Long> eidList) {
        List<EnterpriseGoodsCountBO> countBOS = this.baseMapper.countGoodsByEids(eidList);
        List<Long> countEidList = countBOS.stream().map(EnterpriseGoodsCountBO::getEid).collect(Collectors.toList());
        eidList.forEach(eid -> {
            if (!countEidList.contains(eid)) {
                EnterpriseGoodsCountBO countBO = new EnterpriseGoodsCountBO();
                countBO.setEid(eid);
                countBO.setGoodsCount(0L);
                countBO.setUnGoodsCount(0L);
                countBO.setUpGoodsCount(0L);
                countBOS.add(countBO);
            }
        });
        return countBOS;
    }

    @Override
    public Page<HmcGoodsBO> pageListByEid(HmcGoodsPageRequest request) {
        QueryWrapper<HmcGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HmcGoodsDO::getEid, request.getEid());
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().like(HmcGoodsDO::getGoodsName, request.getName());
        }
        if (null != request.getGoodsStatus() && 0 < request.getGoodsStatus()) {
            queryWrapper.lambda().eq(HmcGoodsDO::getGoodsStatus, request.getGoodsStatus());
        }
        queryWrapper.lambda().orderByDesc(HmcGoodsDO::getCreateTime);
        Page<HmcGoodsDO> hmcPage = this.page(request.getPage(), queryWrapper);
        Page<HmcGoodsBO> boPage = PojoUtils.map(hmcPage, HmcGoodsBO.class);
        this.completeGoodsInfo(boPage.getRecords());
        return boPage;
    }

    /**
     * 完善C端商品信息
     *
     * @param goodsBOList
     */
    private void completeGoodsInfo(List<HmcGoodsBO> goodsBOList) {
        if (CollectionUtils.isNotEmpty(goodsBOList)) {
            List<Long> goodsIds = goodsBOList.stream().map(HmcGoodsBO::getGoodsId).distinct().collect(Collectors.toList());
            List<Long> standardIds = goodsBOList.stream().map(HmcGoodsBO::getStandardId).distinct().collect(Collectors.toList());
            List<GoodsDTO> goodsInfos = goodsHmcApi.batchQueryGoodsInfo(goodsIds);
            Map<Long, GoodsDTO> goodsInfoMap = goodsInfos.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
            List<StandardGoodsInfoDTO> standards = goodsHmcApi.queryStandardGoodsByStandardIds(standardIds);
            Map<Long, StandardGoodsInfoDTO> standardMap = standards.stream().collect(Collectors.toMap(StandardGoodsInfoDTO::getId, Function.identity()));
            goodsBOList.forEach(hmcGoodsBO -> {
                GoodsDTO goodsDTO = goodsInfoMap.get(hmcGoodsBO.getGoodsId());
                if (null != goodsDTO) {
                    hmcGoodsBO.setLicenseNo(goodsDTO.getLicenseNo());
                    hmcGoodsBO.setSellSpecifications(goodsDTO.getSellSpecifications());
                    hmcGoodsBO.setSpecifications(goodsDTO.getSpecifications());
                    hmcGoodsBO.setSellUnit(goodsDTO.getSellUnit());
                    hmcGoodsBO.setUnit(goodsDTO.getUnit());
                }
                StandardGoodsInfoDTO standardDTO = standardMap.get(hmcGoodsBO.getStandardId());
                if (null != standardDTO) {
                    hmcGoodsBO.setStandardCategoryName(standardDTO.getStandardCategoryName());
                }
            });
        }
    }

    @Override
    public List<HmcGoodsDTO> findBySpecificationsId(QueryHmcGoodsRequest request) {
        QueryWrapper<HmcGoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HmcGoodsDO::getSellSpecificationsId, request.getSellSpecificationsId());
        if (null != request.getGoodsStatus() && request.getGoodsStatus() > 0) {
            queryWrapper.lambda().eq(HmcGoodsDO::getGoodsStatus, request.getGoodsStatus());
        }
        List<HmcGoodsDO> hmcGoodsDOList = this.list(queryWrapper);
        return PojoUtils.map(hmcGoodsDOList, HmcGoodsDTO.class);
    }

    @Override
    public HmcGoodsDTO findBySpecificationsIdAndEid(QueryHmcGoodsRequest request) {
        QueryWrapper<HmcGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HmcGoodsDO::getSellSpecificationsId, request.getSellSpecificationsId());
        wrapper.lambda().eq(HmcGoodsDO::getEid, request.getEid());
        if (null != request.getGoodsStatus() && request.getGoodsStatus() > 0) {
            wrapper.lambda().eq(HmcGoodsDO::getGoodsStatus, request.getGoodsStatus());
        }
        HmcGoodsDO hmcGoodsDO = this.getOne(wrapper);
        return PojoUtils.map(hmcGoodsDO, HmcGoodsDTO.class);
    }
}
