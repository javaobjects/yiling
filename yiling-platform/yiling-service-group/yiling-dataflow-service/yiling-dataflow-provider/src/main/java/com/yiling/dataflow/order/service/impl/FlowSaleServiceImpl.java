package com.yiling.dataflow.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.dataflow.flow.service.FlowEnterpriseGoodsMappingService;
import com.yiling.dataflow.order.bo.FlowCrmGoodsBO;
import com.yiling.dataflow.order.bo.FlowSaleCurrentMonthCountBO;
import com.yiling.dataflow.order.dao.FlowSaleMapper;
import com.yiling.dataflow.order.dto.FlowOrderExportReportDetailDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.FlowSaleMonthCountRequest;
import com.yiling.dataflow.order.dto.request.QueryCrmGoodsRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowOrderExportReportPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleBySoMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistsRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSalePageByEidAndGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateEnterpriseCrmCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowGoodsSpecMappingDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.enums.FlowSaleReportSyncStatusEnum;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.order.service.FlowSettlementEnterpriseTagService;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsRelationEditTaskRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskBusinessTypeEnum;
import com.yiling.dataflow.relation.service.FlowGoodsPriceRelationService;
import com.yiling.dataflow.relation.service.FlowGoodsRelationEditTaskService;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.search.flow.api.EsFlowSaleApi;
import com.yiling.search.flow.dto.EsFlowSaleDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 流向销售明细信息表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Slf4j
@Service
public class FlowSaleServiceImpl extends BaseServiceImpl<FlowSaleMapper, FlowSaleDO> implements FlowSaleService {

    @Autowired
    private FlowSettlementEnterpriseTagService   flowSettlementEnterpriseTagService;
    @Autowired(required = false)
    private RocketMqProducerService              rocketMqProducerService;
    @Autowired
    private FlowGoodsSpecMappingService          flowGoodsSpecMappingService;
    @Autowired
    private FlowGoodsRelationService             flowGoodsRelationService;
    @Autowired
    private FlowGoodsRelationEditTaskService     flowGoodsRelationEditTaskService;
    @Autowired
    private FlowEnterpriseCustomerMappingService flowEnterpriseCustomerMappingService;
    @Autowired
    private FlowEnterpriseGoodsMappingService    flowEnterpriseGoodsMappingService;
    @Autowired
    private CrmEnterpriseService                 crmEnterpriseService;
    @Autowired
    private FlowGoodsPriceRelationService        flowGoodsPriceRelationService;
    @Autowired
    private CrmGoodsService                      crmGoodsService;
    @Autowired
    private FlowPurchaseService                  flowPurchaseService;
    @Autowired
    private FlowGoodsBatchService                flowGoodsBatchService;
    @Autowired
    private FlowGoodsBatchDetailService          flowGoodsBatchDetailService;
    @DubboReference(timeout = 1000 * 60 * 60)
    private EsFlowSaleApi                        esFlowSaleApi;

    @Override
    public Integer deleteFlowSaleBySoIdAndEid(DeleteFlowSaleRequest request) {
        QueryWrapper<FlowSaleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowSaleDO::getSoId, request.getSoId())
                .eq(FlowSaleDO::getEid, request.getEid());

        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setOpUserId(request.getOpUserId());
        return this.batchDeleteWithFill(flowSaleDO, queryWrapper);
    }

    @Override
    public Integer updateDataTagByIdList(List<Long> idList, Integer dataTag) {
        LambdaQueryWrapper<FlowSaleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowSaleDO::getId, idList);

        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setDataTag(dataTag);
        flowSaleDO.setUpdateUser(0L);
        flowSaleDO.setUpdateTime(new Date());
        return baseMapper.update(flowSaleDO, queryWrapper);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        LambdaQueryWrapper<FlowSaleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowSaleDO::getId, idList);

        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setUpdateUser(0L);
        flowSaleDO.setUpdateTime(new Date());

        return this.batchDeleteWithFill(flowSaleDO, queryWrapper);
    }

    @Override
    public List<FlowSaleDO> getFlowSaleDTOBySoIdAndEid(QueryFlowSaleRequest request) {
        QueryWrapper<FlowSaleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowSaleDO::getSoId, request.getSoId())
                .eq(FlowSaleDO::getEid, request.getEid());

        return this.list(queryWrapper);
    }

    @Override
    public Integer updateFlowSaleBySoIdAndEid(SaveOrUpdateFlowSaleRequest request) {
        QueryWrapper<FlowSaleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowSaleDO::getSoId, request.getSoId())
                .eq(FlowSaleDO::getEid, request.getEid());

        FlowSaleDO flowSaleDO = PojoUtils.map(request, FlowSaleDO.class);
        return this.update(flowSaleDO, queryWrapper) ? 1 : 0;
    }

    @Override
    public FlowSaleDO insertFlowSale(SaveOrUpdateFlowSaleRequest request) {
        FlowSaleDO flowSaleDO = PojoUtils.map(request, FlowSaleDO.class);
        this.save(flowSaleDO);
        return flowSaleDO;
    }

    @Override
    public Integer updateFlowSaleByIds(List<SaveOrUpdateFlowSaleRequest> requestList) {
        List<FlowSaleDO> flowSaleDOList = PojoUtils.map(requestList, FlowSaleDO.class);
        return this.updateBatchById(flowSaleDOList) ? 1 : 0;
    }

    @Override
    public Page<FlowSaleDO> page(QueryFlowPurchaseListPageRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public List<FlowOrderExportReportDetailDTO> getOrderFlowReport(QueryFlowOrderExportReportPageRequest request) {

        return this.baseMapper.getOrderFlowReport(request);
    }

    @Override
    public Integer deleteFlowSaleBydEidAndSoTime(DeleteFlowSaleByUnlockRequest request) {
        return this.baseMapper.deleteFlowSaleBydEidAndPoTime(request);
    }

    @Override
    public Boolean updateFlowSaleById(SaveOrUpdateFlowSaleRequest request) {
        FlowSaleDO entity = PojoUtils.map(request, FlowSaleDO.class);
        return this.updateById(entity);
    }

    @Override
    public Page<FlowSaleDO> flowSaleYunCangPage(QueryFlowPurchaseListPageRequest request) {
        // 根据多个企业标签名称查询eidList
        List<Long> eidList = flowSettlementEnterpriseTagService.getFlowSettlementEnterpriseTagNameList();
        if (CollUtil.isEmpty(eidList)) {
            return request.getPage();
        }
        request.setEidList(eidList);
        request.setReportSyncStatus(1);
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public Page<FlowSaleDO> pageByEidAndGoodsInSn(QueryFlowSalePageByEidAndGoodsInSnRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid()) || StrUtil.isBlank(request.getGoodsInSn())) {
            return request.getPage();
        }
        LambdaQueryWrapper<FlowSaleDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(FlowSaleDO::getEid, request.getEid());
        lambdaQueryWrapper.eq(FlowSaleDO::getGoodsInSn, request.getGoodsInSn());
        return this.page(new Page<>(request.getCurrent(), request.getSize()), lambdaQueryWrapper);
    }

    @Override
    public void flowSaleReportSyncByEnterpriseTag() {
        long start = System.currentTimeMillis();
        log.info("[销售流向根据商业标签同步到返利]任务执行开始...");
        // 查询符合标签的企业
        List<Long> eidList = flowSettlementEnterpriseTagService.getFlowSettlementEnterpriseTagNameList();
        if (CollUtil.isEmpty(eidList)) {
            return;
        }

        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setReportSyncStatus(FlowSaleReportSyncStatusEnum.TODO.getCode());
        for (Long eid : eidList) {
            request.setEidList(Arrays.asList(eid));
            Page<FlowSaleDO> page;
            int current = 1;
            int size = 500;
            List<FlowSaleDO> handlerList = new ArrayList<>();

            do {
                request.setSize(size);
                request.setCurrent(current);
                page = this.baseMapper.page(request.getPage(), request);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                List<FlowSaleDO> records = page.getRecords();
                handlerList.addAll(records);

                if (records.size() < size) {
                    break;
                }
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

            // 保存以岭品关系修改任务
            if (CollUtil.isEmpty(handlerList)) {
                continue;
            }
            List<Long> flowSaleUpdateIdList = handlerList.stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn()) && ObjectUtil.isNotNull(o.getReportSyncStatus()) && 0 == o.getReportSyncStatus().intValue()).map(FlowSaleDO::getId).distinct().collect(Collectors.toList());
            List<String> goodsInSnList = handlerList.stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).map(FlowSaleDO::getGoodsInSn).distinct().collect(Collectors.toList());
            List<FlowGoodsRelationDO> flowGoodsRelationList = flowGoodsRelationService.getByEidAndGoodsInSn(eid, goodsInSnList);
            if (CollUtil.isEmpty(flowGoodsRelationList)) {
                continue;
            }
            Map<String, List<FlowSaleDO>> flowSaleMap = handlerList.stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).collect(Collectors.groupingBy(FlowSaleDO::getGoodsInSn));
            Map<String, FlowGoodsRelationDO> flowGoodsRelationMap = flowGoodsRelationList.stream().collect(Collectors.toMap(FlowGoodsRelationDO::getGoodsInSn, Function.identity(), (k1, k2) -> k1));
            List<SaveFlowGoodsRelationEditTaskRequest> taskRequestList = new ArrayList<>();
            Date date = new Date();
            for (String goodsInSn : flowGoodsRelationMap.keySet()) {
                FlowGoodsRelationDO flowGoodsRelationDO = flowGoodsRelationMap.get(goodsInSn);
                List<FlowSaleDO> flowSaleDOS = flowSaleMap.get(goodsInSn);
                if (CollUtil.isNotEmpty(flowSaleDOS)) {
                    List<Long> flowSaleIdList = flowSaleDOS.stream().map(FlowSaleDO::getId).collect(Collectors.toList());
                    SaveFlowGoodsRelationEditTaskRequest flowGoodsRelationEditTaskRequest = new SaveFlowGoodsRelationEditTaskRequest();
                    flowGoodsRelationEditTaskRequest.setBusinessType(FlowGoodsRelationEditTaskBusinessTypeEnum.FLOW_SALE_ENTERPRISETAG.getCode());
                    flowGoodsRelationEditTaskRequest.setFlowGoodsRelationId(flowGoodsRelationDO.getId());
                    flowGoodsRelationEditTaskRequest.setEid(eid);
                    flowGoodsRelationEditTaskRequest.setFlowSaleIds(String.join(",", Convert.toList(String.class, flowSaleIdList)));
                    flowGoodsRelationEditTaskRequest.setOpUserId(0L);
                    flowGoodsRelationEditTaskRequest.setOpTime(date);
                    taskRequestList.add(flowGoodsRelationEditTaskRequest);
                }
            }
            if (CollUtil.isEmpty(taskRequestList)) {
                continue;
            }
            List<Long> taskIds = flowGoodsRelationEditTaskService.saveByRequestList(taskRequestList);
            if (CollUtil.isNotEmpty(taskIds)) {
                // 发送返利报表消息，以岭品关系修改任务表主键
                JSONObject dataFlowJson = new JSONObject();
                dataFlowJson.put("taskIds", taskIds);
                String json = JSONUtil.toJsonStr(dataFlowJson);
                SendResult sendResult = rocketMqProducerService.sendSync(Constants.TOPIC_FLOW_GOODS_RELATION_EDIT_TASK_SEND, taskIds.toString(), DateUtil.formatDate(new Date()), json);
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    log.error("FlowSaleServiceImpl.flowSaleReportSyncByEnterpriseTag, 根据符合的企业标签同步流向销售到返利报表, 发送以岭品关系修改任务消息失败, json:{}", json);
                }
                // 销售更新同步返利状态
                if (CollUtil.isNotEmpty(flowSaleUpdateIdList)) {
                    this.updateReportSyncStatusByIdList(flowSaleUpdateIdList);
                }
            }
        }
        log.info("[销售流向根据商业标签同步到返利]任务执行结束, 耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public Boolean updateReportSyncStatusByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return false;
        }
        List<FlowSaleDO> updateList = new ArrayList<>();
        for (Long id : idList) {
            FlowSaleDO flowSale = new FlowSaleDO();
            flowSale.setId(id);
            flowSale.setReportSyncStatus(FlowSaleReportSyncStatusEnum.DONE.getCode());
            updateList.add(flowSale);
        }
        return this.updateBatchById(updateList, 1000);
    }

    @Override
    @Async("asyncExecutor")
    public void syncFlowSaleSpec() {
        log.info("每天同步销售流向 商品规格id任务开始执行...");
        //  若存在specificationId为-1的情况，将-1改为0
        updateSpecificationId();

        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getSpecificationId, 0);

        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }
        long current = 1;
        long size = 2000;
        while ((current - 1) * size < count) {
            Page<FlowSaleDO> page = new Page<>(1, size, count, false);
            Page<FlowSaleDO> flowSaleDOPage = baseMapper.selectPage(page, wrapper);
            List<FlowSaleDO> flowSaleDOList = flowSaleDOPage.getRecords();
            if (CollUtil.isEmpty(flowSaleDOList)) {
                break;
            }
            for (FlowSaleDO flowSaleDO : flowSaleDOList) {
                // 修改specificationId
                String goodsName = flowSaleDO.getGoodsName();
                String spec = flowSaleDO.getSoSpecifications();
                String manufacturer = flowSaleDO.getSoManufacturer();
                if (StringUtils.isEmpty(goodsName) || StringUtils.isEmpty(spec) || StringUtils.isEmpty(manufacturer)) {
                    flowSaleDO.setSpecificationId(-1L);
                } else {
                    FlowGoodsSpecMappingDO flowGoodsSpecMappingDO = flowGoodsSpecMappingService.findByGoodsNameAndSpec(goodsName, spec, manufacturer);
                    if (flowGoodsSpecMappingDO == null) {
                        flowSaleDO.setSpecificationId(-1L);
                    } else {
                        flowSaleDO.setSpecificationId(flowGoodsSpecMappingDO.getSpecificationId());
                    }
                }
                this.updateById(flowSaleDO);
            }
            current++;
        }
        //  调度完成将-1改为0
        updateSpecificationId();
        log.info("每天同步销售流向 商品规格id任务执行完毕！");
    }

    @Override
    public List<FlowSaleDO> getCantMatchGoodsQuantityList(List<Long> eidList, String monthTime) {
        return baseMapper.getCantMatchGoodsQuantityList(eidList, monthTime);
    }

    @Override
    public void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request) {
        for (FlushGoodsSpecIdRequest.FlushDataRequest flushData : request.getFlushDataList()) {
            if (flushData.getRecommendSpecificationId() == null || flushData.getRecommendSpecificationId() == 0) {
                continue;
            }
            LambdaUpdateWrapper<FlowSaleDO> wrapper = new LambdaUpdateWrapper<>();
            //            wrapper.eq(FlowSaleDO::getEid, request.getEid());
            wrapper.eq(FlowSaleDO::getGoodsName, flushData.getGoodsName());
            wrapper.eq(FlowSaleDO::getSoSpecifications, flushData.getSpec());
            wrapper.eq(FlowSaleDO::getSpecificationId, 0);

            FlowSaleDO flowSaleDO = new FlowSaleDO();
            flowSaleDO.setSpecificationId(flushData.getRecommendSpecificationId());

            baseMapper.update(flowSaleDO, wrapper);
        }
    }

    @Override
    public Integer getFlowSaleExistsCount(QueryFlowSaleExistsRequest request) {
        return this.baseMapper.getFlowSaleExistsCount(request);
    }

    @Override
    public BigDecimal getFlowSaleExistsQuantity(QueryFlowSaleExistsRequest request) {
        return this.baseMapper.getFlowSaleExistsQuantity(request);
    }

    @Override
    public boolean isUsedSpecificationId(Long specificationId) {
        Integer count = baseMapper.getSpecificationIdCount(specificationId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<FlowSaleCurrentMonthCountBO> getMonthCount(FlowSaleMonthCountRequest request) {
        Assert.notNull(request, "参数 eidList 不能为空");
        Assert.notEmpty(request.getEidList(), "参数 eidList 不能为空");
        Assert.notNull(request.getStartTime(), "参数 startTime 不能为空");
        Assert.notNull(request.getEndTime(), "参数 endTime 不能为空");
        return this.baseMapper.getMonthCount(request);
    }

    @Async("asyncExecutor")
    @Override
    public void updateFlowSaleCrmSign(List<Long> crmIds) {
        this.updateFlowSaleCrmInnerSign(crmIds);
        this.updateFlowSaleCrmGoodsSign(crmIds);
//        flowPurchaseService.updateFlowPurchaseCrmGoodsSign(crmIds);
//        flowGoodsBatchService.updateFlowGoodsBatchCrmGoodsSign(crmIds);
//        flowGoodsBatchDetailService.updateFlowGoodsBatchDetailCrmGoodsSign(crmIds);
    }

    private void updateSpecificationId() {
        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getSpecificationId, -1);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        LambdaUpdateWrapper<FlowSaleDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FlowSaleDO::getSpecificationId, -1);

        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setSpecificationId(0L);
        baseMapper.update(flowSaleDO, updateWrapper);
    }

    private void updateEnterpriseCrmCode(Long crmId) {
        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getEnterpriseCrmCode, "-1");
        wrapper.eq(FlowSaleDO::getCrmEnterpriseId, crmId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setEnterpriseCrmCode("");
        baseMapper.update(flowSaleDO, wrapper);
    }

    private void updateCrmGoodsCode(Long crmId) {
        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getCrmGoodsCode, -1);
        wrapper.eq(FlowSaleDO::getCrmEnterpriseId, crmId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setCrmGoodsCode(0L);
        baseMapper.update(flowSaleDO, wrapper);
    }

    private LambdaQueryWrapper<FlowSaleDO> getSalePageQueryWrapper(QueryFlowPurchaseListPageRequest request) {
        LambdaQueryWrapper<FlowSaleDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        //Long eid = request.getEid();
        //if (eid != null && eid != 0) {
        //    lambdaQueryWrapper.eq(FlowSaleDO::getEid, eid);
        //}
        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            lambdaQueryWrapper.like(FlowSaleDO::getEname, ename);
        }
        String enterpriseInnerCode = request.getEnterpriseInnerCode();
        if (StrUtil.isNotBlank(enterpriseInnerCode)) {
            lambdaQueryWrapper.like(FlowSaleDO::getEnterpriseInnerCode, enterpriseInnerCode);
        }
        String enterpriseName = request.getEnterpriseName();
        if (StrUtil.isNotBlank(enterpriseName)) {
            lambdaQueryWrapper.like(FlowSaleDO::getEnterpriseName, enterpriseName);
        }
        String goodsName = request.getGoodsName();
        if (StrUtil.isNotBlank(goodsName)) {
            lambdaQueryWrapper.like(FlowSaleDO::getGoodsName, goodsName);
        }
        String soLicense = request.getLicense();
        if (StrUtil.isNotBlank(soLicense)) {
            lambdaQueryWrapper.like(FlowSaleDO::getSoLicense, soLicense);
        }
        Date soTimeStart = request.getStartTime();
        if (ObjectUtil.isNotNull(soTimeStart)) {
            lambdaQueryWrapper.ge(FlowSaleDO::getSoTime, DateUtil.beginOfDay(soTimeStart));
        }
        Date soTimeEnd = request.getEndTime();
        if (ObjectUtil.isNotNull(soTimeEnd)) {
            lambdaQueryWrapper.le(FlowSaleDO::getSoTime, DateUtil.endOfDay(soTimeEnd));
        }
        List<String> sourceList = request.getSourceList();
        if (CollUtil.isNotEmpty(sourceList)) {
            lambdaQueryWrapper.in(FlowSaleDO::getSoSource, sourceList);
        }
        String manufacturer = request.getManufacturer();
        if (StrUtil.isNotBlank(manufacturer)) {
            lambdaQueryWrapper.like(FlowSaleDO::getSoManufacturer, manufacturer);
        }
        //String provinceCode = request.getProvinceCode();
        //if (StrUtil.isNotBlank(provinceCode)) {
        //    lambdaQueryWrapper.like(FlowSaleDO::getProvinceCode, provinceCode);
        //}
        //String cityCode = request.getCityCode();
        //if (StrUtil.isNotBlank(cityCode)) {
        //    lambdaQueryWrapper.like(FlowSaleDO::getCityCode, cityCode);
        //}
        //String regionCode = request.getRegionCode();
        //if (StrUtil.isNotBlank(regionCode)) {
        //    lambdaQueryWrapper.like(FlowSaleDO::getRegionCode, regionCode);
        //}
        List<Long> eidList = request.getEidList();
        if (CollUtil.isNotEmpty(eidList) && eidList.get(0) != -99L) {
            lambdaQueryWrapper.in(FlowSaleDO::getEid, eidList);
        }
        return lambdaQueryWrapper;
    }

    @Override
    public void updateFlowSaleCrmInnerSign(List<Long> crmIds) {
        if (CollUtil.isNotEmpty(crmIds)) {
            for (Long crmId : crmIds) {
                CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(crmId);
                if(crmEnterpriseDO==null){
                    continue;
                }
                LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FlowSaleDO::getEnterpriseCrmCode, "");
                wrapper.eq(FlowSaleDO::getCrmEnterpriseId, crmId);
                Integer count = baseMapper.selectCount(wrapper);
                log.info("crm销售单客户编码标记:eid{},count{}", crmId, count);
                if (count == 0) {
                    continue;
                }
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowSaleDO> page = new Page<>(1, size, count, false);
                    Page<FlowSaleDO> flowSaleDOPage = baseMapper.selectPage(page, wrapper);
                    List<FlowSaleDO> flowSaleDOList = flowSaleDOPage.getRecords();
                    if (CollUtil.isEmpty(flowSaleDOList)) {
                        break;
                    }
                    executeFlowSaleCrmInnerCode(flowSaleDOList, crmEnterpriseDO);
                    current++;
                }
                //  调度完成将-1改为''
                updateEnterpriseCrmCode(crmId);
            }
        }
    }

    @Override
    public void updateFlowSaleCrmGoodsSign(List<Long> crmIds) {
        if (CollUtil.isNotEmpty(crmIds)) {
            for (Long crmId : crmIds) {
                CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(crmId);
                if(crmEnterpriseDO==null){
                    continue;
                }
                LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FlowSaleDO::getCrmGoodsCode, 0);
                wrapper.eq(FlowSaleDO::getCrmEnterpriseId, crmId);
                Integer count = baseMapper.selectCount(wrapper);
                log.info("crm销售单商品编码标记:eid{},count{}", crmId, count);
                if (count == 0) {
                    continue;
                }
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowSaleDO> page = new Page<>(1, size, count, false);
                    Page<FlowSaleDO> flowSaleDOPage = baseMapper.selectPage(page, wrapper);
                    List<FlowSaleDO> flowSaleDOList = flowSaleDOPage.getRecords();
                    if (CollUtil.isEmpty(flowSaleDOList)) {
                        break;
                    }
                    executeFlowSaleCrmGoodsCode(flowSaleDOList, crmEnterpriseDO);
                    current++;
                }
                //  调度完成将-1改为0
                updateCrmGoodsCode(crmId);
            }
        }
    }

    @Override
    public List<FlowSaleDO> getByIdsContainsDeleteFlag(List<Long> ids) {
        return this.baseMapper.getByIdsContainsDeleteFlag(ids);
    }

    @Override
    public List<FlowSaleDO> getByEidAndSoNo(Long eid, String soNo) {
        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getEid, eid);
        wrapper.eq(FlowSaleDO::getSoNo, soNo);
        wrapper.select(FlowSaleDO::getEid, FlowSaleDO::getEname, FlowSaleDO::getSpecificationId, FlowSaleDO::getSoBatchNo, FlowSaleDO::getSoTime, FlowSaleDO::getEnterpriseName, FlowSaleDO::getSoQuantity);

        List<FlowSaleDO> flowSaleDOList = baseMapper.selectList(wrapper);
        return flowSaleDOList;
    }

    @Override
    public Date getMaxSoTimeByEid(Long eid) {
        return this.baseMapper.getMaxSoTimeByEid(eid);
    }

    @Override
    public int refreshFlowSale(UpdateFlowIndexRequest request) {
        Integer count = baseMapper.selectFlowSaleCount(request);
        log.info("销售流向数据刷新索引:eid{},count{}", request.getEid(), count);
        if (count == 0) {
            return 0;
        }
        long current = 1;
        long size = 2000;
        while ((current - 1) * size < count) {
            Page<FlowSaleDO> page = new Page<>(current, size, count, false);
            Page<FlowSaleDO> flowSaleDOPage = baseMapper.selectFlowSalePage(page, request);
            List<FlowSaleDO> flowSaleDOList = flowSaleDOPage.getRecords();
            if (CollUtil.isEmpty(flowSaleDOList)) {
                break;
            }
            current++;
            Long start = System.currentTimeMillis();
            esFlowSaleApi.updateFlowSale(PojoUtils.map(flowSaleDOList, EsFlowSaleDTO.class));
            log.info("执行时间：{}", System.currentTimeMillis() - start);
        }
        return count;
    }

    @Override
    public int refreshFlowSaleBakup(UpdateFlowIndexRequest request) {
        log.info("销售流向数据刷新索引:eid{}", request.getEid());
        QueryFlowPurchaseBackupListPageRequest queryFlowPurchaseBackupListPageRequest = new QueryFlowPurchaseBackupListPageRequest();
        queryFlowPurchaseBackupListPageRequest.setEidList(Arrays.asList(request.getEid()));
        queryFlowPurchaseBackupListPageRequest.setTableName("flow_sale_" + request.getYear());
        Page<FlowSaleDO> page;
        int current = 1;
        int size = 2000;
        do {
            queryFlowPurchaseBackupListPageRequest.setSize(size);
            queryFlowPurchaseBackupListPageRequest.setCurrent(current);
            page = this.baseMapper.pageBackup(queryFlowPurchaseBackupListPageRequest.getPage(), queryFlowPurchaseBackupListPageRequest);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<FlowSaleDO> records = page.getRecords();
            esFlowSaleApi.updateFlowSale(PojoUtils.map(records, EsFlowSaleDTO.class));
            if (records.size() < size) {
                break;
            }
            current++;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return 0;
    }

    @Override
    public List<FlowCrmGoodsBO> getUnmappedCrmGoods(QueryCrmGoodsRequest request) {
        return this.baseMapper.getUnmappedCrmGoods(request);
    }

    @Override
    public Integer getCountByEidAndSoTime(Long eid, Date startTime, Date endTime) {
        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getEid, eid);
        wrapper.ge(FlowSaleDO::getSoTime, startTime);
        wrapper.le(FlowSaleDO::getSoTime, endTime);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public boolean isHaveDataByEidAndSoTime(QueryFlowSaleExistRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getEid(), "参数 request.getEid() 不能为空");
        Assert.notNull(request.getSoTimeStart(), "参数 soTimeStart 不能为空");
        Assert.notNull(request.getSoTimeEnd(), "参数 soTimeEnd 不能为空");
        LambdaQueryWrapper<FlowSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleDO::getEid, request.getEid());
        wrapper.ge(FlowSaleDO::getSoTime, request.getSoTimeStart());
        wrapper.le(FlowSaleDO::getSoTime, request.getSoTimeEnd());
        wrapper.last("limit 1");
        FlowSaleDO one = this.getOne(wrapper);
        if (ObjectUtil.isNotNull(one)) {
            return true;
        }
        return false;
    }

    @Override
    public Page<FlowSaleDTO> getPageBySoMonth(QueryFlowSaleBySoMonthPageRequest request) {
        Page<FlowSaleDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(baseMapper.getPageBySoMonth(page, request), FlowSaleDTO.class);
    }

    @Override
    public void updateCrmGoodsCode(UpdateCrmGoodsCodeRequest request) {
        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setId(request.getId());
        flowSaleDO.setCrmGoodsCode(request.getCrmGoodsCode());
        flowSaleDO.setUpdateTime(new Date());
        baseMapper.updateById(flowSaleDO);
    }

    @Override
    public void updateEnterpriseCrmCodeCode(UpdateEnterpriseCrmCodeRequest request) {
        FlowSaleDO flowSaleDO = new FlowSaleDO();
        flowSaleDO.setId(request.getId());
        flowSaleDO.setEnterpriseCrmCode(request.getEnterpriseCrmCode());
        flowSaleDO.setUpdateTime(new Date());
        baseMapper.updateById(flowSaleDO);
    }

    @Override
    public List<FlowStatisticsBO> getFlowSaleStatistics(QueryFlowStatisticesRequest request) {
        return baseMapper.getFlowSaleStatistics(request);
    }


    @Override
    public Page<FlowSaleDO> pageBackup(QueryFlowPurchaseBackupListPageRequest request) {
        return baseMapper.pageBackup(request.getPage(), request);
    }

    public void executeFlowSaleCrmGoodsCode(List<FlowSaleDO> list, CrmEnterpriseDO crmEnterpriseDO) {
        for (FlowSaleDO flowSaleDO : list) {
            if (StrUtil.isEmpty(flowSaleDO.getGoodsName()) || StrUtil.isEmpty(flowSaleDO.getSoSpecifications())) {
                continue;
            }
            SaveOrUpdateFlowSaleRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowSaleRequest();
            saveOrUpdateFlowSaleRequest.setId(flowSaleDO.getId());
            if (flowSaleDO.getCrmEnterpriseId() != null && flowSaleDO.getCrmEnterpriseId() != 0) {
                saveOrUpdateFlowSaleRequest.setCrmCode(flowSaleDO.getCrmCode());
                FlowEnterpriseGoodsMappingDTO flowEnterpriseGoodsMappingDTO = flowEnterpriseGoodsMappingService.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(flowSaleDO.getGoodsName(), flowSaleDO.getSoSpecifications(), flowSaleDO.getCrmEnterpriseId());
                if (flowEnterpriseGoodsMappingDTO != null) {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(flowEnterpriseGoodsMappingDTO.getCrmGoodsCode());
                } else {
                    SaveFlowEnterpriseGoodsMappingRequest saveFlowEnterpriseGoodsMappingRequest = new SaveFlowEnterpriseGoodsMappingRequest();
                    saveFlowEnterpriseGoodsMappingRequest.setCrmEnterpriseId(flowSaleDO.getCrmEnterpriseId());
                    saveFlowEnterpriseGoodsMappingRequest.setEnterpriseName(crmEnterpriseDO.getName());
                    saveFlowEnterpriseGoodsMappingRequest.setFlowGoodsName(flowSaleDO.getGoodsName());
                    saveFlowEnterpriseGoodsMappingRequest.setFlowSpecification(flowSaleDO.getSoSpecifications());
                    saveFlowEnterpriseGoodsMappingRequest.setFlowManufacturer(flowSaleDO.getSoManufacturer());
                    saveFlowEnterpriseGoodsMappingRequest.setFlowUnit(flowSaleDO.getSoUnit());
                    saveFlowEnterpriseGoodsMappingRequest.setFlowGoodsInSn(flowSaleDO.getGoodsInSn());
                    saveFlowEnterpriseGoodsMappingRequest.setLastUploadTime(new Date());
                    saveFlowEnterpriseGoodsMappingRequest.setOpTime(new Date());
                    flowEnterpriseGoodsMappingService.saveOrUpdate(saveFlowEnterpriseGoodsMappingRequest);
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                }
            } else {
                saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
            }

            if (saveOrUpdateFlowSaleRequest.getCrmGoodsCode() == -1) {
                //获取crm_goods
                if (flowSaleDO.getSpecificationId() != 0) {
                    List<CrmGoodsDTO> crmGoodsDTOList = crmGoodsService.getCrmGoodsBySpecificationId(flowSaleDO.getSpecificationId());
                    if (CollUtil.isNotEmpty(crmGoodsDTOList)) {
                        if (crmGoodsDTOList.size() == 1) {
                            saveOrUpdateFlowSaleRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                        } else {
                            saveOrUpdateFlowSaleRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                            saveOrUpdateFlowSaleRequest.setGoodsPossibleError(1);
                        }
                    }
                }
            }
            this.updateFlowSaleById(saveOrUpdateFlowSaleRequest);
        }
    }

    public void executeFlowSaleCrmInnerCode(List<FlowSaleDO> list, CrmEnterpriseDO crmEnterpriseDO) {
        //3、通过flow_customer_mapping映射出类型名称和归属部门
        for (FlowSaleDO flowSaleDO : list) {
            if (StrUtil.isEmpty(flowSaleDO.getEnterpriseName())) {
                continue;
            }

            SaveOrUpdateFlowSaleRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowSaleRequest();
            saveOrUpdateFlowSaleRequest.setId(flowSaleDO.getId());
            if (flowSaleDO.getCrmEnterpriseId() != 0) {
                String enterpriseCrmCode = mathEnterpriseCrmCode(flowSaleDO.getEnterpriseName());
                if(StrUtil.isEmpty(enterpriseCrmCode)) {
                    saveOrUpdateFlowSaleRequest.setCrmCode(flowSaleDO.getCrmCode());
                    FlowEnterpriseCustomerMappingDTO flowCustomerMappingDTO = flowEnterpriseCustomerMappingService.findByCustomerNameAndCrmEnterpriseId(flowSaleDO.getEnterpriseName(), flowSaleDO.getCrmEnterpriseId());
                    if (flowCustomerMappingDTO != null) {
                        if (flowCustomerMappingDTO.getCrmOrgId() != null && flowCustomerMappingDTO.getCrmOrgId() != 0) {
                            saveOrUpdateFlowSaleRequest.setEnterpriseCrmCode(String.valueOf(flowCustomerMappingDTO.getCrmOrgId()));
                        } else {
                            saveOrUpdateFlowSaleRequest.setEnterpriseCrmCode("");
                        }
                    } else {
                        SaveFlowEnterpriseCustomerMappingRequest saveFlowEnterpriseCustomerMappingRequest = new SaveFlowEnterpriseCustomerMappingRequest();
                        saveFlowEnterpriseCustomerMappingRequest.setFlowCustomerName(flowSaleDO.getEnterpriseName());
                        saveFlowEnterpriseCustomerMappingRequest.setCrmEnterpriseId(flowSaleDO.getCrmEnterpriseId());
                        saveFlowEnterpriseCustomerMappingRequest.setEnterpriseName(crmEnterpriseDO.getName());
                        saveFlowEnterpriseCustomerMappingRequest.setProvince(crmEnterpriseDO.getProvinceName());
                        saveFlowEnterpriseCustomerMappingRequest.setProvinceCode(crmEnterpriseDO.getProvinceCode());
                        saveFlowEnterpriseCustomerMappingRequest.setLastUploadTime(new Date());
                        saveFlowEnterpriseCustomerMappingRequest.setOpTime(new Date());
                        flowEnterpriseCustomerMappingService.saveOrUpdate(saveFlowEnterpriseCustomerMappingRequest);
                        saveOrUpdateFlowSaleRequest.setEnterpriseCrmCode("-1");
                    }
                }else{
                    saveOrUpdateFlowSaleRequest.setEnterpriseCrmCode(enterpriseCrmCode);
                }
            } else {
                saveOrUpdateFlowSaleRequest.setEnterpriseCrmCode("-1");
            }
            this.updateFlowSaleById(saveOrUpdateFlowSaleRequest);
        }
    }

    public String mathEnterpriseCrmCode(String enterpriseName) {
        List<CrmEnterpriseDTO> crmEnterpriseDTOList = crmEnterpriseService.getCrmEnterpriseCodeByNameList(Arrays.asList(enterpriseName));
        if (CollUtil.isNotEmpty(crmEnterpriseDTOList)) {
            crmEnterpriseDTOList = crmEnterpriseDTOList.stream().sorted(Comparator.comparing(CrmEnterpriseDTO::getBusinessCode)).collect(Collectors.toList());
            return String.valueOf(crmEnterpriseDTOList.get(0).getId());
        }
        return "";
    }

}
