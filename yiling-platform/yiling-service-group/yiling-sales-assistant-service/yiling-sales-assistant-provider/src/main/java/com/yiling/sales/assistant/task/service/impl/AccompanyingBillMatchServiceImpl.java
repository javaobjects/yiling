package com.yiling.sales.assistant.task.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.flow.api.FlowSaleMatchApi;
import com.yiling.dataflow.flow.dto.FlowSaleMatchResultDTO;
import com.yiling.dataflow.flow.dto.request.FlowSaleMatchRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.sales.assistant.task.dao.AccompanyingBillMatchMapper;
import com.yiling.sales.assistant.task.dto.AccompanyingBillMatchDTO;
import com.yiling.sales.assistant.task.dto.AccompanyingBillMatchDetailDTO;
import com.yiling.sales.assistant.task.dto.request.QueryMatchBillPageRequest;
import com.yiling.sales.assistant.task.entity.AccompanyingBillDO;
import com.yiling.sales.assistant.task.entity.AccompanyingBillMatchDO;
import com.yiling.sales.assistant.task.entity.MatchDetailDO;
import com.yiling.sales.assistant.task.enums.AccompanyBillAuditStatusEnum;
import com.yiling.sales.assistant.task.enums.AccompanyBillMatchStatusEnum;
import com.yiling.sales.assistant.task.enums.FlowMatchResultEnum;
import com.yiling.sales.assistant.task.service.AccompanyingBillMatchService;
import com.yiling.sales.assistant.task.service.AccompanyingBillService;
import com.yiling.sales.assistant.task.service.MatchDetailService;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 随货同行单匹配流向 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
@Slf4j
@Service
public class AccompanyingBillMatchServiceImpl extends BaseServiceImpl<AccompanyingBillMatchMapper, AccompanyingBillMatchDO> implements AccompanyingBillMatchService {

    @Autowired
    private AccompanyingBillService accompanyingBillService;

    @Autowired
    private MatchDetailService matchDetailService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private FlowSaleMatchApi flowSaleMatchApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private GoodsApi goodsApi;

    @Override
    public Page<AccompanyingBillMatchDTO> queryPage(QueryMatchBillPageRequest request) {

        LambdaQueryWrapper<AccompanyingBillMatchDO> wrapper = Wrappers.lambdaQuery();
        //根据单据编号查询
        if(StrUtil.isNotEmpty(request.getDocCode())){
            LambdaQueryWrapper<AccompanyingBillDO> awrapper = Wrappers.lambdaQuery();
            awrapper.like(StrUtil.isNotEmpty(request.getDocCode()),AccompanyingBillDO::getDocCode,request.getDocCode());
            List<AccompanyingBillDO> accompanyingBillDOS = accompanyingBillService.list(awrapper);
            if(CollUtil.isEmpty(accompanyingBillDOS)){
                return request.getPage();
            }
            List<Long> accompanyingBillIds = accompanyingBillDOS.stream().map(AccompanyingBillDO::getId).collect(Collectors.toList());
            wrapper.in(AccompanyingBillMatchDO::getAccompanyingBillId,accompanyingBillIds);
        }
        wrapper.like(StrUtil.isNotEmpty(request.getRecvEname()),AccompanyingBillMatchDO::getErpRecvName,request.getRecvEname());
        wrapper.ge(Objects.nonNull(request.getErpMatchStartTime()),AccompanyingBillMatchDO::getErpMatchTime,request.getErpMatchStartTime());
        if(Objects.nonNull(request.getErpMatchEndTime())){
            wrapper.le(AccompanyingBillMatchDO::getErpMatchTime, DateUtil.endOfDay(request.getErpMatchEndTime()));
        }
        wrapper.ge(Objects.nonNull(request.getCrmMatchStartTime()),AccompanyingBillMatchDO::getCrmMatchTime,request.getCrmMatchStartTime());
        if(Objects.nonNull(request.getCrmMatchEndTime())){
            wrapper.le(AccompanyingBillMatchDO::getCrmMatchTime, DateUtil.endOfDay(request.getCrmMatchEndTime()));
        }
        wrapper.eq(Objects.nonNull(request.getResult()),AccompanyingBillMatchDO::getResult,request.getResult());
        wrapper.orderByDesc(AccompanyingBillMatchDO::getUpdateTime);
        Page<AccompanyingBillMatchDO> billMatchDOPage = this.page(request.getPage(), wrapper);
        Page<AccompanyingBillMatchDTO> result = PojoUtils.map(billMatchDOPage,AccompanyingBillMatchDTO.class);

        if(billMatchDOPage.getTotal()==0){
            return result;
        }
        List<AccompanyingBillMatchDTO> records = result.getRecords();
        List<Long> billIdList = records.stream().map(AccompanyingBillMatchDTO::getAccompanyingBillId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<AccompanyingBillDO> accbillWrapper = Wrappers.lambdaQuery();
        accbillWrapper.in(AccompanyingBillDO::getId,billIdList);
        List<AccompanyingBillDO> accBillMatchDOS = accompanyingBillService.list(accbillWrapper);
        Map<Long, AccompanyingBillDO> billDOMap = accBillMatchDOS.stream().collect(Collectors.toMap(AccompanyingBillDO::getId, v -> v, (v1, v2) -> v1));

        List<Long> recvEidList = records.stream().map(AccompanyingBillMatchDTO::getCrmRecvEid).distinct().collect(Collectors.toList());
/*
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(recvEidList);
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, v -> v, (v1, v2) -> v1));
*/

        records.forEach(accompanyingBillMatchDTO -> {
            AccompanyingBillDO accompanyingBillDO = billDOMap.get(accompanyingBillMatchDTO.getAccompanyingBillId());
            accompanyingBillMatchDTO.setDocCode(accompanyingBillDO.getDocCode());
          /*  EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(accompanyingBillMatchDTO.getCrmRecvEid());*/
            accompanyingBillMatchDTO.setRecvEname(accompanyingBillMatchDTO.getErpRecvName());
        });
        return result;
    }

    @Override
    public AccompanyingBillMatchDTO getDetail(Long id) {
        AccompanyingBillMatchDO accompanyingBillMatchDO = this.getById(id);
        AccompanyingBillMatchDTO accompanyingBillMatchDTO = new AccompanyingBillMatchDTO();
        PojoUtils.map(accompanyingBillMatchDO,accompanyingBillMatchDTO);
        if(Objects.isNull(accompanyingBillMatchDTO)){
            return accompanyingBillMatchDTO;
        }
        //商品信息
        LambdaQueryWrapper<MatchDetailDO> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MatchDetailDO::getAccompanyingBillId,accompanyingBillMatchDO.getAccompanyingBillId());
        List<MatchDetailDO> matchDetailDOList = matchDetailService.list(lambdaQueryWrapper);
        //
        AccompanyingBillDO accompanyingBillDO = accompanyingBillService.getById(accompanyingBillMatchDO.getAccompanyingBillId());
        accompanyingBillMatchDTO.setDocCode(accompanyingBillDO.getDocCode());
        List<AccompanyingBillMatchDetailDTO> erpGoodsList = PojoUtils.map(matchDetailDOList,AccompanyingBillMatchDetailDTO.class);
         accompanyingBillMatchDTO.setErpGoodsList(erpGoodsList);
         if(AccompanyBillMatchStatusEnum.EQUAL.getCode().equals(accompanyingBillMatchDTO.getResult())){
             accompanyingBillMatchDTO.setCrmGoodsList(erpGoodsList);
         }
/*        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(accompanyingBillMatchDTO.getCrmRecvEid());
        accompanyingBillMatchDTO.setRecvEname(enterpriseDTO.getName());
        EnterpriseDTO distributorEnterpriseDTO = enterpriseApi.getById(accompanyingBillMatchDTO.getErpDistributorEid());*/
        accompanyingBillMatchDTO.setDistributorEname(accompanyingBillMatchDO.getErpDistributorName()).setRecvEname(accompanyingBillMatchDO.getErpRecvName()).setCrmDeliveryTime(accompanyingBillMatchDO.getErpDeliveryTime());
        return accompanyingBillMatchDTO;
    }

    @Override
    public void billFlowMatchTimer() {
        //查询上个月提交的随货同行单
        DateTime dateTime = DateUtil.offset(new Date(), DateField.MONTH, -1);
        DateTime beginOfMonth = DateUtil.beginOfMonth(dateTime);
        DateTime endOfMonth = DateUtil.endOfMonth(dateTime);
        LambdaQueryWrapper<AccompanyingBillMatchDO> wrapper = Wrappers.lambdaQuery();
        wrapper.between(AccompanyingBillMatchDO::getUploadTime,beginOfMonth,endOfMonth).eq(AccompanyingBillMatchDO::getResult, FlowMatchResultEnum.TO_MATCH.getCode());
        List<AccompanyingBillMatchDO> accompanyingBillMatchDOS = this.list(wrapper);
        if(CollUtil.isEmpty(accompanyingBillMatchDOS)){
            log.info("上月无待匹配随货同行单");
            return;
        }
        List<Long> billIds = accompanyingBillMatchDOS.stream().map(AccompanyingBillMatchDO::getAccompanyingBillId).collect(Collectors.toList());
        LambdaQueryWrapper<AccompanyingBillDO> billWrapper = Wrappers.lambdaQuery();
        billWrapper.in(AccompanyingBillDO::getId,billIds);
        List<AccompanyingBillDO> accompanyingBillDOS = accompanyingBillService.list(billWrapper);
        Map<Long, AccompanyingBillMatchDO> billMatchDOMap = accompanyingBillMatchDOS.stream().collect(Collectors.toMap(AccompanyingBillMatchDO::getAccompanyingBillId, Function.identity()));
        accompanyingBillDOS.forEach(accompanyingBillDO -> {
            FlowSaleMatchRequest request = new FlowSaleMatchRequest();
            request.setEid(accompanyingBillDO.getDistributorEid()).setSoNo(accompanyingBillDO.getDocCode());
            FlowSaleMatchResultDTO match = flowSaleMatchApi.match(request);
            AccompanyingBillMatchDO accompanyingBillMatchDO = billMatchDOMap.get(accompanyingBillDO.getId());
            accompanyingBillMatchDO.setCrmMatchTime(new Date()).setErpMatchTime(new Date());
            if(match.getErpList().size()==0){
                accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.FAIL_TWO.getStatus()).setRejectionReason("流向不存在，请确认上传随货同行单及单据是否正确");
                accompanyingBillMatchDO.setErpResult(0).setCrmResult(0);
            }
            List<MatchDetailDO> matchDetailDOList = Lists.newArrayList();
            if(match.getErpList().size()>0){
                FlowSaleMatchResultDTO.ErpGoodsInfo erpGoodsInfo = match.getErpList().get(0);
                accompanyingBillMatchDO.setErpResult(1).setErpDistributorEid(erpGoodsInfo.getEid()).setErpDistributorName(erpGoodsInfo.getEname()).setErpRecvName(erpGoodsInfo.getEnterpriseName()).setErpDeliveryTime(erpGoodsInfo.getSoTime());
                if(match.getCrmFlag()){
                    accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.TWO_PASS_AUDIT.getStatus());
                    accompanyingBillMatchDO.setCrmResult(1);
                }else{
                    accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.FAIL_THREE.getStatus()).setRejectionReason("流向不存在，请确认上传随货同行单及单据是否正确");;
                    accompanyingBillMatchDO.setCrmResult(0);
                }
                List<Long> specIdList = match.getErpList().stream().map(FlowSaleMatchResultDTO.ErpGoodsInfo::getSellSpecId).collect(Collectors.toList());
                List<GoodsDTO> goodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(specIdList, Collections.singletonList(accompanyingBillDO.getDistributorEid()));

                match.getErpList().forEach(goods->{
                   MatchDetailDO matchDetailDO = new MatchDetailDO();
                    matchDetailDOList.add(matchDetailDO);
                    matchDetailDO.setAccompanyingBillId(accompanyingBillDO.getId()).setBatchNo(goods.getSoBatchNo()).setOutDate(goods.getSoTime()).setQuantity(goods.getSoQuantity().longValue()).setSellSpecificationsId(goods.getSellSpecId()).setYlGoodsId(goods.getYlGoodsId())
                            .setYlGoodsName(goods.getYlGoodsName()).setYlGoodsSpecifications(goods.getYlGoodsSpec());
                    if(CollUtil.isEmpty(goodsDTOList)){
                        matchDetailDO.setGoodsId(0L);
                    }else{
                        GoodsDTO goodsBase = goodsDTOList.stream().filter(goodsDTO -> goodsDTO.getSellSpecificationsId().equals(goods.getSellSpecId())).findFirst().orElse(null);
                        if(Objects.nonNull(goodsBase)){
                            matchDetailDO.setGoodsId(goodsBase.getId());
                        }else{
                            matchDetailDO.setGoodsId(0L);
                        }
                    }
                });
            }
            SpringUtil.getBean(AccompanyingBillMatchServiceImpl.class).updateMatchResult(accompanyingBillMatchDO,matchDetailDOList,accompanyingBillDO);
            if(accompanyingBillMatchDO.getResult().equals(FlowMatchResultEnum.ERP_CRM_EQ.getCode())){
                MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_SA_BILL_MATCH, Constants.TAG_SA_BILL_MATCH, accompanyingBillDO.getId().toString());
                mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
                mqMessageSendApi.send(mqMessageTwoBO);
            }
        });
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateMatchResult(AccompanyingBillMatchDO accompanyingBillMatchDO, List<MatchDetailDO> matchDetailDOList,AccompanyingBillDO accompanyingBillDO){
        if(accompanyingBillMatchDO.getCrmResult()==1 && accompanyingBillMatchDO.getErpResult()==1){
            accompanyingBillMatchDO.setResult(FlowMatchResultEnum.ERP_CRM_EQ.getCode());
        }
        if(accompanyingBillMatchDO.getCrmResult()==0 && accompanyingBillMatchDO.getErpResult()==1){
            accompanyingBillMatchDO.setResult(FlowMatchResultEnum.ERP_CRM_NQ.getCode());
        }
        if(accompanyingBillMatchDO.getErpResult()==0){
            accompanyingBillMatchDO.setResult(FlowMatchResultEnum.NONE.getCode());
        }
        this.updateById(accompanyingBillMatchDO);
        matchDetailService.saveBatch(matchDetailDOList);
        accompanyingBillService.updateById(accompanyingBillDO);
    }
}
