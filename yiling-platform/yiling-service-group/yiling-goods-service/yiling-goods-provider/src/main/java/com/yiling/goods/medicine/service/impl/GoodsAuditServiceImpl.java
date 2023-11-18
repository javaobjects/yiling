package com.yiling.goods.medicine.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.dao.GoodsAuditMapper;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditPageRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditRecordPageRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsSkuRequest;
import com.yiling.goods.medicine.dto.request.SaveSellSpecificationsRequest;
import com.yiling.goods.medicine.dto.request.UpdateAuditStatusByGoodsIdRequest;
import com.yiling.goods.medicine.entity.GoodsAuditDO;
import com.yiling.goods.medicine.enums.GoodsAuditStatusEnum;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.GoodsAuditService;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.service.StandardGoodsService;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品待审核表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
@Slf4j
public class GoodsAuditServiceImpl extends BaseServiceImpl<GoodsAuditMapper, GoodsAuditDO> implements GoodsAuditService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    StandardGoodsService standardGoodsService;

    @Autowired
    StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Autowired
    @Lazy
    GoodsAuditServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;


    @Override
    public Boolean saveGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        GoodsAuditDO goodsAuditDO = PojoUtils.map(request, GoodsAuditDO.class);
        //先判断是否存在原始数据
        QueryWrapper<GoodsAuditDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GoodsAuditDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(GoodsAuditDO::getAuditStatus, GoodsAuditStatusEnum.TO_AUDIT.getCode());
        lambdaQueryWrapper.eq(GoodsAuditDO::getGid, request.getGid());
        GoodsAuditDO goodsAudit = this.getOne(lambdaQueryWrapper);
        if (goodsAudit != null) {
            goodsAuditDO.setId(goodsAudit.getId());
        } else {
            goodsAuditDO.setId(null);
        }
        return this.saveOrUpdate(goodsAuditDO);
    }

    @Override
    public Boolean notPassGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        GoodsAuditDO goodsAuditDO = PojoUtils.map(request, GoodsAuditDO.class);
        //先判断是否存在原始数据
        QueryWrapper<GoodsAuditDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GoodsAuditDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(GoodsAuditDO::getAuditStatus, GoodsAuditStatusEnum.TO_AUDIT.getCode());
        lambdaQueryWrapper.eq(GoodsAuditDO::getGid, request.getGid());
        GoodsAuditDO goodsAudit = this.getOne(lambdaQueryWrapper);
        if (goodsAudit != null) {
            goodsAuditDO.setId(goodsAudit.getId());
            goodsAuditDO.setAuditStatus(GoodsAuditStatusEnum.NOT_PASS_AUDIT.getCode());
            goodsAuditDO.setOpUserId(request.getOpUserId());
            this.updateById(goodsAuditDO);
        }
        return true;
    }
    @Override
    public Boolean passGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        GoodsAuditDO goodsAuditDO = PojoUtils.map(request, GoodsAuditDO.class);
        //先判断是否存在原始数据
        QueryWrapper<GoodsAuditDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GoodsAuditDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(GoodsAuditDO::getAuditStatus, GoodsAuditStatusEnum.TO_AUDIT.getCode());
        lambdaQueryWrapper.eq(GoodsAuditDO::getGid, request.getGid());
        GoodsAuditDO goodsAudit = this.getOne(lambdaQueryWrapper);
        if (goodsAudit != null) {
            goodsAuditDO.setId(goodsAudit.getId());
            goodsAuditDO.setAuditStatus(GoodsAuditStatusEnum.PASS_AUDIT.getCode());
            goodsAuditDO.setOpUserId(request.getOpUserId());
            this.updateById(goodsAuditDO);
        }
        return true;
    }

    @Override
    public GoodsAuditDTO getGoodsAuditByGidAndAuditStatus(Long gid) {
        QueryWrapper<GoodsAuditDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GoodsAuditDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(GoodsAuditDO::getAuditStatus, GoodsAuditStatusEnum.NOT_PASS_AUDIT.getCode());
        lambdaQueryWrapper.eq(GoodsAuditDO::getGid, gid);
        lambdaQueryWrapper.orderByDesc(GoodsAuditDO::getId);
        List<GoodsAuditDO> goodsAuditDOList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(goodsAuditDOList)) {
            return PojoUtils.map(goodsAuditDOList.get(0), GoodsAuditDTO.class);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rejectGoodsAudit(SaveOrUpdateGoodsAuditRequest request) {
        GoodsAuditDO goodsAuditDO = this.getById(request.getId());
        if (goodsAuditDO == null) {
            throw new BusinessException(GoodsErrorCode.GOODS_AUDIT_NOT_EXIST);
        }
        if (!goodsAuditDO.getAuditStatus().equals(GoodsAuditStatusEnum.TO_AUDIT.getCode())) {
            throw new BusinessException(GoodsErrorCode.GOODS_AUDIT_NOT_EXIST);
        }

        UpdateAuditStatusByGoodsIdRequest updateAuditStatusByGoodsIdRequest = new UpdateAuditStatusByGoodsIdRequest();
        updateAuditStatusByGoodsIdRequest.setId(goodsAuditDO.getGid());
        updateAuditStatusByGoodsIdRequest.setAuditStatus(GoodsStatusEnum.REJECT.getCode());
        updateAuditStatusByGoodsIdRequest.setOpUserId(request.getOpUserId());
        goodsService.updateAuditStatusByGoodsId(updateAuditStatusByGoodsIdRequest);

        goodsAuditDO.setId(goodsAuditDO.getId());
        goodsAuditDO.setRejectMessage(request.getRejectMessage());
        goodsAuditDO.setAuditStatus(GoodsAuditStatusEnum.NOT_PASS_AUDIT.getCode());
        goodsAuditDO.setUpdateUser(request.getOpUserId());
        goodsAuditDO.setUpdateTime(new Date());
        return this.saveOrUpdate(goodsAuditDO);
    }

    @Override
    public Page<GoodsAuditDO> queryPageListGoodsAudit(QueryGoodsAuditPageRequest request) {
        Page<GoodsAuditDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<GoodsAuditDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GoodsAuditDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(GoodsAuditDO::getAuditStatus, GoodsAuditStatusEnum.TO_AUDIT.getCode());
        String ename = request.getEname();
        if (StringUtils.isNotEmpty(ename)) {
            lambdaQueryWrapper.like(GoodsAuditDO::getEname, ename);
        }
        String name = request.getName();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(GoodsAuditDO::getName, name);
        }
        String licenseNo = request.getLicenseNo();
        if (StringUtils.isNotEmpty(licenseNo)) {
            lambdaQueryWrapper.like(GoodsAuditDO::getLicenseNo, licenseNo);
        }
        String manufacturer = request.getManufacturer();
        if (StringUtils.isNotEmpty(manufacturer)) {
            lambdaQueryWrapper.like(GoodsAuditDO::getManufacturer, manufacturer);
        }
        Integer source = request.getSource();
        if (source != null && source != 0) {
            lambdaQueryWrapper.eq(GoodsAuditDO::getSource, source);
        }
        lambdaQueryWrapper.orderByDesc(GoodsAuditDO::getId);
        return this.page(page, queryWrapper);
    }

    @Override
    public Page<GoodsAuditDO> queryPageListGoodsAuditRecord(QueryGoodsAuditRecordPageRequest request) {
        Page<GoodsAuditDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<GoodsAuditDO> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GoodsAuditDO> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(GoodsAuditDO::getGid, request.getGoodsId());
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean linkSellSpecifications(SaveSellSpecificationsRequest request) {
        StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsService.getStandardGoodsById(request.getStandardId());
        StandardGoodsSpecificationDO standardGoodsSpecificationDO = standardGoodsSpecificationService.getById(request.getSellSpecificationsId());
        GoodsAuditDO goodsAuditDO = this.getById(request.getId());
        //先判断商品是否重复
        Long gid = goodsService.queryInfoBySpecIdAndEid(request.getSellSpecificationsId(), goodsAuditDO.getEid());
        SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest = new SaveOrUpdateGoodsRequest();
        if (gid > 0) {
            saveOrUpdateGoodsRequest.setAuditStatus(GoodsStatusEnum.REPETITION.getCode());
            saveOrUpdateGoodsRequest.setRepGoodsId(gid);
            GoodsLineBO goodsLineBO=goodsService.getGoodsLineByGoodsIds(Lists.newArrayList(gid)).get(0);
            SaveGoodsLineRequest goodsLineRequest = new SaveGoodsLineRequest();
            goodsLineRequest.setMallFlag(goodsLineBO.getMallStatus());
            goodsLineRequest.setPopFlag(goodsLineBO.getPopStatus());
            saveOrUpdateGoodsRequest.setGoodsLineInfo(goodsLineRequest);
            //获取商品sku
            List<GoodsSkuDTO> goodsSkuList=goodsService.getGoodsSkuByGoodsIdAndStatus(goodsAuditDO.getGid(),null);
            if(CollUtil.isNotEmpty(goodsSkuList)){
                List<SaveOrUpdateGoodsSkuRequest> skuRequestList= Lists.newArrayList();
                for(GoodsSkuDTO goodsSkuDTO:goodsSkuList){
                    if(StrUtil.isNotEmpty(goodsSkuDTO.getInSn())){
                        SaveOrUpdateGoodsSkuRequest skuRequest = PojoUtils.map(goodsSkuDTO,SaveOrUpdateGoodsSkuRequest.class);
                        skuRequestList.add(skuRequest);
                    }
                }
                saveOrUpdateGoodsRequest.setGoodsSkuList(skuRequestList);
                log.info("商品审核匹配sku列表：{}",skuRequestList);
            }
        } else {
            saveOrUpdateGoodsRequest.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
            saveOrUpdateGoodsRequest.setRepGoodsId(0L);
        }
        saveOrUpdateGoodsRequest.setPopEidList(request.getPopEidList());
        saveOrUpdateGoodsRequest.setSellSpecificationsId(request.getSellSpecificationsId());
        saveOrUpdateGoodsRequest.setStandardId(request.getStandardId());
        saveOrUpdateGoodsRequest.setOpUserId(request.getOpUserId());
        saveOrUpdateGoodsRequest.setSellSpecifications(standardGoodsSpecificationDO.getSellSpecifications());
        saveOrUpdateGoodsRequest.setSellUnit(standardGoodsSpecificationDO.getUnit());
        saveOrUpdateGoodsRequest.setGoodsType(standardGoodsByIdInfoDTO.getBaseInfo().getGoodsType());
        saveOrUpdateGoodsRequest.setEid(goodsAuditDO.getEid());
        saveOrUpdateGoodsRequest.setId(goodsAuditDO.getGid());
        Long goodsId = goodsService.editGoods(saveOrUpdateGoodsRequest);
        //mq刷新数据
        refreshGoodsByMq(goodsAuditDO.getEid(), goodsId);
        goodsAuditDO.setAuditStatus(GoodsAuditStatusEnum.PASS_AUDIT.getCode());
        goodsAuditDO.setUpdateUser(request.getOpUserId());
        goodsAuditDO.setUpdateTime(new Date());
        return this.updateById(goodsAuditDO);
    }


    private void refreshGoodsByMq(Long eid, Long gid) {
        //刷新数据
        List<GoodsSkuDTO> goodsSkuDTOList = goodsService.getGoodsSkuByGoodsIds(Arrays.asList(gid));
        List<String> inSnList = goodsSkuDTOList.stream().map(e -> e.getInSn()).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(inSnList)) {
            GoodsRefreshDTO goodsRefreshDTO = new GoodsRefreshDTO();
            goodsRefreshDTO.setEid(eid);
            goodsRefreshDTO.setInSnList(inSnList);
            _this.sendMq(Constants.TOPIC_GOODS_REFRESH, Constants.TAG_GOODS_REFRESH, JSON.toJSONString(goodsRefreshDTO),gid);
        }
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg,Long id) {
        Integer intId=null;
        if(null !=id && 0<id){
            intId=id.intValue();
        }
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg, intId);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg,Integer id) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg,id);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
