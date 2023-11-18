package com.yiling.goods.medicine.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.service.InventorySubscriptionService;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsOverSoldEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.service.GoodsService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.medicine.dao.GoodsSkuMapper;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsSkuRequest;
import com.yiling.goods.medicine.entity.GoodsSkuDO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.service.GoodsSkuService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品sku 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-19
 */
@Service
@Slf4j
public class GoodsSkuServiceImpl extends BaseServiceImpl<GoodsSkuMapper, GoodsSkuDO> implements GoodsSkuService {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private InventorySubscriptionService inventorySubscriptionService;

    /**
     * 通过goodsid、产品线、内码、包装数量是否有重复，没有重复就新增
     *
     * @param request
     * @return
     */
    @Override
    public Long saveOrUpdateByNumber(SaveOrUpdateGoodsSkuRequest request) {
        GoodsSkuDO goodsSkuDO = null;
        if (request.getId() != null && request.getId() > 0) {
            goodsSkuDO = this.getById(request.getId());
            if (StrUtil.isNotEmpty(request.getInSn())) {
                QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(GoodsSkuDO::getEid, request.getEid())
                        .eq(GoodsSkuDO::getInSn, request.getInSn())
                        .eq(GoodsSkuDO::getGoodsLine, goodsSkuDO.getGoodsLine())
                        .exists("select 1 from goods t where t.audit_status=4 and t.id=goods_id")
                        .ne(GoodsSkuDO::getId, goodsSkuDO.getId());
                GoodsSkuDO existGoodsSkuDO = this.getOne(wrapper);
                if (existGoodsSkuDO != null) {
                    if(GoodsSkuStatusEnum.DISABLE.getCode().equals(existGoodsSkuDO.getStatus())){
                        throw new BusinessException(GoodsErrorCode.GOODS_INSN_REPETITION,"供应商商品内码已经存在,并且已停用");
                    }else {
                        throw new BusinessException(GoodsErrorCode.GOODS_INSN_REPETITION);
                    }
                }
            }
        } else {
            if (StrUtil.isNotEmpty(request.getInSn())) {
                QueryWrapper<GoodsSkuDO> wrapperOne = new QueryWrapper<>();
                wrapperOne.lambda().eq(GoodsSkuDO::getGoodsId, request.getGoodsId())
                        .eq(GoodsSkuDO::getInSn, request.getInSn())
                        .eq(GoodsSkuDO::getGoodsLine, request.getGoodsLine());
                goodsSkuDO = this.getOne(wrapperOne);
            }
        }
        GoodsDTO goodsDTO = goodsService.queryInfo(request.getGoodsId());
        if (goodsSkuDO != null) {
            GoodsSkuDO oldGoodsSkuDO = PojoUtils.map(request, GoodsSkuDO.class);
            oldGoodsSkuDO.setId(goodsSkuDO.getId());
            Long inventoryId = goodsSkuDO.getInventoryId();
            SaveInventoryRequest saveInventoryRequest = new SaveInventoryRequest();
            saveInventoryRequest.setOpUserId(request.getOpUserId());
            saveInventoryRequest.setGid(request.getGoodsId());
            saveInventoryRequest.setInSn(request.getInSn());
            saveInventoryRequest.setQty(request.getQty());
            saveInventoryRequest.setOverSoldType(Objects.isNull(goodsDTO)? GoodsOverSoldEnum.NO_OVER_SOLD.getType():goodsDTO.getOverSoldType());
            saveInventoryRequest.setBatchNumber(request.getBatchNumber());
            saveInventoryRequest.setExpiryDate(request.getExpiryDate());
            saveInventoryRequest.setId(inventoryId);
            //当修改内码和原始内码不一致时候 先查询修改的内码是否已生成库存数据，若存在则将sku绑定该库存，否则把其他sku和该sku绑定相同库存id的内码数据一起修改
            if(null!=request.getInSn() && !request.getInSn().equals(goodsSkuDO.getInSn())){
                log.info("skuId:{},原内码：{}，修改内码：{}",goodsSkuDO.getId(),goodsSkuDO.getInSn(),request.getInSn());
                InventoryDTO existInventory = inventoryService.getByGidAndInSn(request.getGoodsId(), request.getInSn());
                if(null!=existInventory){
                    log.info("skuId:{}库存换绑，修改的内码存在库存：{}，原库存id:{}",goodsSkuDO.getId(), JSONUtil.toJsonStr(existInventory),inventoryId);
                    //sku库存id换绑
                    saveInventoryRequest.setId(existInventory.getId());
                    oldGoodsSkuDO.setInventoryId(existInventory.getId());
                    inventoryService.save(saveInventoryRequest);
                } else {
                    inventoryService.save(saveInventoryRequest);
                    QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(GoodsSkuDO::getInventoryId,inventoryId);
                    wrapper.lambda().ne(GoodsSkuDO::getId,goodsSkuDO.getId());
                    List<GoodsSkuDO> list = this.list(wrapper);
                    if(CollectionUtil.isNotEmpty(list)){
                        log.info("被动刷新内码skuIds：{}",JSONUtil.toJsonStr(list));
                        GoodsSkuDO refreshSku=new GoodsSkuDO();
                        refreshSku.setInSn(request.getInSn());
                        refreshSku.setOpUserId(request.getOpUserId());
                        QueryWrapper<GoodsSkuDO> refreshWrapper = new QueryWrapper<>();
                        refreshWrapper.lambda().in(GoodsSkuDO::getId,list.stream().map(GoodsSkuDO::getId).collect(Collectors.toList()));
                        this.update(refreshSku,refreshWrapper);
                    }
                }
            }else {
                inventoryService.save(saveInventoryRequest);
            }
            this.updateById(oldGoodsSkuDO);
            return goodsSkuDO.getId();
        }

        //初始化库存
        if (StrUtil.isNotEmpty(request.getInSn())) {
            QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(GoodsSkuDO::getEid, request.getEid())
                    .eq(GoodsSkuDO::getInSn, request.getInSn())
                    .exists("select 1 from goods t where t.audit_status=4 and t.id=goods_id")
                    .eq(GoodsSkuDO::getGoodsLine, request.getGoodsLine());

            GoodsSkuDO oldGoodsSkuDO = this.getOne(wrapper);
            if (oldGoodsSkuDO != null) {
                throw new BusinessException(GoodsErrorCode.GOODS_INSN_REPETITION);
            }
        }
        SaveInventoryRequest saveInventoryRequest = new SaveInventoryRequest();
        saveInventoryRequest.setOpUserId(request.getOpUserId());
        saveInventoryRequest.setGid(request.getGoodsId());
        saveInventoryRequest.setInSn(request.getInSn());
        saveInventoryRequest.setQty(request.getQty());
        saveInventoryRequest.setOverSoldType(Objects.isNull(goodsDTO)? GoodsOverSoldEnum.NO_OVER_SOLD.getType():goodsDTO.getOverSoldType());
        saveInventoryRequest.setBatchNumber(request.getBatchNumber());
        saveInventoryRequest.setExpiryDate(request.getExpiryDate());
        Long inventoryId = inventoryService.save(saveInventoryRequest);

        goodsSkuDO = PojoUtils.map(request, GoodsSkuDO.class);
        goodsSkuDO.setInventoryId(inventoryId);

        this.save(goodsSkuDO);
        return goodsSkuDO.getId();
    }

    @Override
    public Boolean deleteGoodsSku(List<Long> skuIds, Long operUser) {
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GoodsSkuDO::getId, skuIds);

        GoodsSkuDO goodsSkuDO = new GoodsSkuDO();
        goodsSkuDO.setOpUserId(operUser);
        return this.batchDeleteWithFill(goodsSkuDO, wrapper) > 0;
    }

    @Override
    public List<Long> getB2bInventoryByGoodsId(Long goodsId) {
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getGoodsId, goodsId).eq(GoodsSkuDO::getGoodsLine, GoodsLineEnum.B2B.getCode());
        return this.list(wrapper).stream().map(e -> e.getInventoryId()).collect(Collectors.toList());
    }
    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsId(Long goodsId){
        return this.getGoodsSkuByGoodsIdAndStatus(goodsId,ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode()));
    }
    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIdAndStatus(Long goodsId,List<Integer> statusList) {
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getGoodsId, goodsId);
        if(CollectionUtil.isNotEmpty(statusList)){
            wrapper.lambda().in(GoodsSkuDO::getStatus,statusList);
        }
        List<GoodsSkuDO> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        List<Long> ids = list.stream().map(e -> e.getInventoryId()).collect(Collectors.toList());
        List<InventoryDTO> inventoryDTOList = inventoryService.getInventoryByIds(ids);
        Map<Long, InventoryDTO> inventoryDTOMap = inventoryDTOList.stream().collect(Collectors.toMap(InventoryDTO::getId, Function.identity()));
        List<GoodsSkuDTO> goodsSkuDTOList = PojoUtils.map(list, GoodsSkuDTO.class);
        goodsSkuDTOList.forEach(e -> {
            InventoryDTO inventoryDTO = inventoryDTOMap.get(e.getInventoryId());
            e.setInSn(inventoryDTO.getInSn());
            e.setQty(inventoryDTO.getQty());
            e.setFrozenQty(inventoryDTO.getFrozenQty());
            e.setOverSoldType(inventoryDTO.getOverSoldType());
//            e.setBatchNumber(inventoryDTO.getBatchNumber());
//            e.setExpiryDate(inventoryDTO.getExpiryDate());
        });
        this.updateSkuRealQty(goodsSkuDTOList);
        return goodsSkuDTOList;
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIds(List<Long> goodsIds) {
        return this.getGoodsSkuByGoodsIdsAndStatus(goodsIds,ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIdsAndStatus(List<Long> goodsIds,List<Integer> statusList){
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GoodsSkuDO::getGoodsId, goodsIds);
        if(CollectionUtil.isNotEmpty(statusList)){
            wrapper.lambda().in(GoodsSkuDO::getStatus, statusList);
        }
        List<GoodsSkuDO> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        List<Long> ids = list.stream().map(e -> e.getInventoryId()).collect(Collectors.toList());
        List<InventoryDTO> inventoryDTOList = inventoryService.getInventoryByIds(ids);
        Map<Long, InventoryDTO> inventoryDTOMap = inventoryDTOList.stream().collect(Collectors.toMap(InventoryDTO::getId, Function.identity()));
        List<GoodsSkuDTO> goodsSkuDTOList = PojoUtils.map(list, GoodsSkuDTO.class);
        goodsSkuDTOList.forEach(e -> {
            InventoryDTO inventoryDTO = inventoryDTOMap.get(e.getInventoryId());
            e.setInSn(inventoryDTO.getInSn());
            e.setQty(inventoryDTO.getQty());
            e.setFrozenQty(inventoryDTO.getFrozenQty());
            e.setOverSoldType(inventoryDTO.getOverSoldType());
//            e.setBatchNumber(inventoryDTO.getBatchNumber());
//            e.setExpiryDate(inventoryDTO.getExpiryDate());
        });
        this.updateSkuRealQty(goodsSkuDTOList);
        return goodsSkuDTOList;
    }
    @Override
    public List<GoodsSkuDTO> getGoodsSkuListByEidAndInSnAndGoodsLine(Long eid, String inSn, Integer goodsLine) {
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getEid, eid)
                .eq(GoodsSkuDO::getInSn, inSn)
                .exists("select 1 from goods t where t.audit_status!=7 and t.id=goods_id");
        if (goodsLine != null && goodsLine != 0) {
            wrapper.lambda().eq(GoodsSkuDO::getGoodsLine, goodsLine);
        }

        List<GoodsSkuDO> goodsSkuDOList = this.list(wrapper);
        return PojoUtils.map(goodsSkuDOList, GoodsSkuDTO.class);
    }

    @Override
    public Boolean updateStatusByEidAndInSn(Long eid, String inSn, Integer status,Long updater) {
        if(StringUtils.isBlank(inSn)|| eid==null||eid==0L){
            return false;
        }
        GoodsSkuDO skuDO = new GoodsSkuDO();
        skuDO.setStatus(status);
        skuDO.setUpdateUser(updater);
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getEid, eid)
                .eq(GoodsSkuDO::getInSn, inSn)
                .exists("select 1 from goods t where t.audit_status!=7 and t.del_flag=0 and t.id=goods_id");
        this.baseMapper.update(skuDO,wrapper);
        return true;
    }

    @Override
    public Boolean updateByGoodsId(SaveOrUpdateGoodsSkuRequest request) {
        if(request.getGoodsId() == null || request.getGoodsId()==0){
            return false;
        }
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getGoodsId, request.getGoodsId());
        List<GoodsSkuDO> doList = this.list(wrapper);
        if(CollectionUtil.isNotEmpty(doList)){
            doList.forEach(skuDO->{
                skuDO.setEid(request.getEid());
                skuDO.setRemark(request.getRemark());
                skuDO.setStatus(request.getStatus());
            });
            this.updateBatchById(doList);
        }
        return true;
    }

    private void updateSkuRealQty(List<GoodsSkuDTO> skuList){
        if(CollectionUtil.isEmpty(skuList)){
            return;
        }
        List<Long> inventoryIds = skuList.stream().map(GoodsSkuDTO::getInventoryId).collect(Collectors.toList());
        List<InventorySubscriptionDTO> selfSubscriptionList = inventorySubscriptionService.getSelfSubscriptionByInventoryIds(inventoryIds);
        Map<Long, InventorySubscriptionDTO> subscriptionDTOMap = selfSubscriptionList.stream().collect(Collectors.toMap(InventorySubscriptionDTO::getInventoryId, Function.identity(), (e1, e2) -> e1));
        skuList.forEach(sku->{
            InventorySubscriptionDTO selfSubscription = subscriptionDTOMap.get(sku.getInventoryId());
            if(null!=selfSubscription){
                sku.setRealQty(selfSubscription.getQty());
            }else {
                sku.setRealQty(0L);
            }
        });
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByIds(List<Long> skuIds) {
        List<GoodsSkuDTO> goodsSkuDTOList = PojoUtils.map(listByIds(skuIds), GoodsSkuDTO.class);
        List<Long> inventroyIds = goodsSkuDTOList.stream().map(e -> e.getInventoryId()).collect(Collectors.toList());
        List<InventoryDTO> inventoryDTOList = inventoryService.getInventoryByIds(inventroyIds);
        Map<Long, InventoryDTO> inventoryDTOMap = inventoryDTOList.stream().collect(Collectors.toMap(InventoryDTO::getId, Function.identity()));
        goodsSkuDTOList.forEach(e -> {
            InventoryDTO inventoryDTO = inventoryDTOMap.get(e.getInventoryId());
            e.setInSn(inventoryDTO.getInSn());
            e.setQty(inventoryDTO.getQty());
            e.setFrozenQty(inventoryDTO.getFrozenQty());
            e.setOverSoldType(inventoryDTO.getOverSoldType());
//            e.setBatchNumber(inventoryDTO.getBatchNumber());
//            e.setExpiryDate(inventoryDTO.getExpiryDate());
        });
        return goodsSkuDTOList;
    }

    @Override
    public GoodsSkuDTO getGoodsSkuById(Long skuId) {
        GoodsSkuDTO skuDTO = PojoUtils.map(this.getById(skuId), GoodsSkuDTO.class);
        if(null!=skuDTO){
            InventoryDO inventoryDO = inventoryService.getById(skuDTO.getInventoryId());
            skuDTO.setInSn(inventoryDO.getInSn());
            skuDTO.setQty(inventoryDO.getQty());
            skuDTO.setFrozenQty(inventoryDO.getFrozenQty());
            skuDTO.setOverSoldType(inventoryDO.getOverSoldType());
//            skuDTO.setBatchNumber(inventoryDO.getBatchNumber());
//            skuDTO.setExpiryDate(inventoryDO.getExpiryDate());
        }
        return skuDTO;
    }
    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGidAndGoodsLine(Long goodsId, GoodsLineEnum lineEnum) {
        if(null==goodsId || null == lineEnum){
            return ListUtil.empty();
        }
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getGoodsId, goodsId);
        wrapper.lambda().eq(GoodsSkuDO::getGoodsLine,lineEnum.getCode());
        return PojoUtils.map(this.list(wrapper),GoodsSkuDTO.class);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGidAndInventoryId(Long goodsId, Long inventoryId) {
        if(null==goodsId || null == inventoryId){
            return ListUtil.empty();
        }
        QueryWrapper<GoodsSkuDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsSkuDO::getGoodsId, goodsId);
        wrapper.lambda().eq(GoodsSkuDO::getInventoryId,inventoryId);
        wrapper.lambda().in(GoodsSkuDO::getStatus,ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
        return PojoUtils.map(this.list(wrapper),GoodsSkuDTO.class);
    }
}
