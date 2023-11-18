package com.yiling.dataflow.order.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsService;
import com.yiling.dataflow.order.dao.FlowGoodsBatchDetailMapper;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailBackupListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailByGbMonthPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.UpdateCrmGoodsCodeRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDO;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.service.FlowGoodsPriceRelationService;
import com.yiling.dataflow.statistics.bo.GoodsSpecQuantityBO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.api.EsFlowGoodsBatchDetailApi;
import com.yiling.search.flow.dto.EsFlowGoodsBatchDetailDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * ERP库存汇总同步数据 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-14
 */
@Slf4j
@Service
public class FlowGoodsBatchDetailServiceImpl extends BaseServiceImpl<FlowGoodsBatchDetailMapper, FlowGoodsBatchDetailDO> implements FlowGoodsBatchDetailService {

    @Autowired
    private FlowGoodsBatchService         flowGoodsBatchService;
    @Autowired
    private CrmEnterpriseService          crmEnterpriseService;
    @Autowired
    private FlowGoodsPriceRelationService flowGoodsPriceRelationService;
    @Autowired
    private CrmGoodsService               crmGoodsService;
    @DubboReference(timeout = 1000*60*60)
    private EsFlowGoodsBatchDetailApi esFlowGoodsBatchDetailApi;

    @Override
    public Page<FlowGoodsBatchDetailDO> page(QueryFlowGoodsBatchDetailListPageRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public Boolean updateFlowGoodsBatchDetailByIds(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList) {
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = PojoUtils.map(requestList, FlowGoodsBatchDetailDO.class);
        return this.updateBatchById(flowGoodsBatchDetailDOList);
    }

    @Override
    public Boolean updateFlowGoodsBatchDetailById(SaveOrUpdateFlowGoodsBatchDetailRequest request) {
        FlowGoodsBatchDetailDO flowGoodsBatchDetailDO = PojoUtils.map(request, FlowGoodsBatchDetailDO.class);
        return this.updateById(flowGoodsBatchDetailDO);
    }

    @Override
    public Integer deleteFlowGoodsBatchDetailByEidAndDate(DeleteFlowGoodsBatchDetailRequest request) {
        return this.baseMapper.deleteFlowGoodsBatchDetailByEidAndDate(request);
    }

    @Override
    public Integer addFlowGoodsBatchDetailList(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList) {
        return this.saveBatch(PojoUtils.map(requestList, FlowGoodsBatchDetailDO.class)) ? 1 : 0;
    }

    @Override
    public void statisticsFlowGoodsBatch(List<Long> suIdList) {
        Date date = DateUtil.parseDate(DateUtil.today());
        for (Long suId : suIdList) {
            executeGoodsBatchStatistics(suId, date);
        }
    }

    @Override
    public Boolean executeGoodsBatchStatistics(Long eid, Date date) {
        Long start=System.currentTimeMillis();
        LambdaQueryWrapper<FlowGoodsBatchDetailDO> flowGoodsBatchDetailWrapper = new LambdaQueryWrapper<>();
        flowGoodsBatchDetailWrapper.eq(FlowGoodsBatchDetailDO::getEid,eid);
        flowGoodsBatchDetailWrapper.eq(FlowGoodsBatchDetailDO::getGbDetailTime,DateUtil.parseDate(DateUtil.formatDate(date)));
        int i=this.count(flowGoodsBatchDetailWrapper);
        if(i>0){
            log.info("库存已经备份:eid{},count{}", eid, i);
            return false;
        }
        List<FlowGoodsBatchDO> goodsBatchFlowExcelList = new ArrayList<>();
        LambdaQueryWrapper<FlowGoodsBatchDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDO::getEid, eid);
        Integer count = flowGoodsBatchService.count(wrapper);
        log.info("实时库存备份:eid{},count{}", eid, count);
        if (count == 0) {
            return false;
        }
        long current = 1;
        long size = 2000;
        while ((current - 1) * size < count) {
            Page<FlowGoodsBatchDO> page = new Page<>(current, size, count, false);
            Page<FlowGoodsBatchDO> flowGoodsBatchDOPage = flowGoodsBatchService.page(page, wrapper);
            List<FlowGoodsBatchDO> flowGoodsBatchDOList = flowGoodsBatchDOPage.getRecords();
            if (CollUtil.isEmpty(flowGoodsBatchDOList)) {
                break;
            }
            goodsBatchFlowExcelList.addAll(flowGoodsBatchDOList);
            current++;
        }

        if (CollUtil.isNotEmpty(goodsBatchFlowExcelList)) {
            //过滤库存为零的情况
            goodsBatchFlowExcelList = goodsBatchFlowExcelList.stream().filter(e -> e.getGbNumber().longValue() != 0).collect(Collectors.toList());
            //合并批次
            Map<String, FlowGoodsBatchDO> flowGoodsBatchDTOMap = new HashMap<>();
            for (FlowGoodsBatchDO flowGoodsBatchDO : goodsBatchFlowExcelList) {
                String key = this.getKey(flowGoodsBatchDO);
                if (flowGoodsBatchDTOMap.containsKey(key)) {
                    FlowGoodsBatchDO oldFlowGoodsBatchDO = flowGoodsBatchDTOMap.get(key);
                    if(oldFlowGoodsBatchDO.getUpdateTime().getTime()<flowGoodsBatchDO.getUpdateTime().getTime()){
                        oldFlowGoodsBatchDO.setUpdateTime(flowGoodsBatchDO.getUpdateTime());
                    }
                    oldFlowGoodsBatchDO.setGbNumber(flowGoodsBatchDO.getGbNumber().add(oldFlowGoodsBatchDO.getGbNumber()));
                } else {
                    flowGoodsBatchDTOMap.put(key, flowGoodsBatchDO);
                }
            }

            List<SaveOrUpdateFlowGoodsBatchDetailRequest> list = new ArrayList<>();
            for (Map.Entry<String, FlowGoodsBatchDO> flowGoodsBatchDOEntry : flowGoodsBatchDTOMap.entrySet()) {
                SaveOrUpdateFlowGoodsBatchDetailRequest saveOrUpdateFlowGoodsBatchDetailRequest = PojoUtils.map(flowGoodsBatchDOEntry.getValue(), SaveOrUpdateFlowGoodsBatchDetailRequest.class);
                saveOrUpdateFlowGoodsBatchDetailRequest.setGbDetailTime(date);
                String gbDetailMonth = DateUtil.format(date, "yyyy-MM");
                saveOrUpdateFlowGoodsBatchDetailRequest.setGbDetailMonth(gbDetailMonth);
                saveOrUpdateFlowGoodsBatchDetailRequest.setId(null);
                saveOrUpdateFlowGoodsBatchDetailRequest.setCollectTime(flowGoodsBatchDOEntry.getValue().getUpdateTime());
                list.add(saveOrUpdateFlowGoodsBatchDetailRequest);
            }
            addFlowGoodsBatchDetailList(list);
        }
        log.info("执行时间：{}",System.currentTimeMillis()-start);
        return true;
    }

    private String getKey(FlowGoodsBatchDO flowGoodsBatchDO) {
        return flowGoodsBatchDO.getSpecificationId() + "-" + flowGoodsBatchDO.getInSn() + "-" + flowGoodsBatchDO.getGbBatchNo();
    }


    @Override
    public List<GoodsSpecQuantityBO> findGoodsDateQuantityByEidAndDateTime(Long eid, String dateTime, List<Long> specificationIdList) {
        return baseMapper.findGoodsDateQuantityByEidAndDateTime(eid, dateTime, specificationIdList);
    }

    @Override
    public List<FlowGoodsBatchDetailDO> getEndMonthCantMatchGoodsQuantityList(List<Long> eidList, String dateTime) {
        return baseMapper.getEndMonthCantMatchGoodsQuantityList(eidList, dateTime);
    }

    @Override
    public void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request) {
        for (FlushGoodsSpecIdRequest.FlushDataRequest flushData : request.getFlushDataList()) {
            if (flushData.getRecommendSpecificationId() == null || flushData.getRecommendSpecificationId() == 0) {
                continue;
            }
            LambdaUpdateWrapper<FlowGoodsBatchDetailDO> wrapper = new LambdaUpdateWrapper<>();
//            wrapper.eq(FlowGoodsBatchDetailDO::getEid, request.getEid());
            wrapper.eq(FlowGoodsBatchDetailDO::getGbName, flushData.getGoodsName());
            wrapper.eq(FlowGoodsBatchDetailDO::getGbSpecifications, flushData.getSpec());
            wrapper.eq(FlowGoodsBatchDetailDO::getSpecificationId, 0);

            FlowGoodsBatchDetailDO flowGoodsBatchDetailDO = new FlowGoodsBatchDetailDO();
            flowGoodsBatchDetailDO.setSpecificationId(flushData.getRecommendSpecificationId());

            baseMapper.update(flowGoodsBatchDetailDO, wrapper);
        }
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
    public void updateFlowGoodsBatchDetailCrmGoodsSign(List<Long> crmIds) {
        //1、先查询没有匹配的流向数据
        if (CollUtil.isNotEmpty(crmIds)) {
            for (Long crmId : crmIds) {
                LambdaQueryWrapper<FlowGoodsBatchDetailDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FlowGoodsBatchDetailDO::getCrmGoodsCode, 0);
                wrapper.eq(FlowGoodsBatchDetailDO::getCrmEnterpriseId, crmId);
                Integer count = baseMapper.selectCount(wrapper);
                log.info("crm历史库存商品编码标记:eid{},count{}", crmId,count);
                if (count == 0) {
                    continue;
                }
                long current = 1;
                long size = 2000;
                while ((current - 1) * size < count) {
                    Page<FlowGoodsBatchDetailDO> page = new Page<>(1, size, count, false);
                    Page<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOPage = baseMapper.selectPage(page, wrapper);
                    List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = flowGoodsBatchDetailDOPage.getRecords();
                    if (CollUtil.isEmpty(flowGoodsBatchDetailDOList)) {
                        break;
                    }
                    executeFlowGoodsBatchDetailCrmGoodsCode(flowGoodsBatchDetailDOList);
                    current++;
                }
                //todo  调度完成将-1改为0
                updateCrmGoodsCode(crmId);
            }
        }
    }

    @Override
    public Page<FlowGoodsBatchDetailDO> pageBackup(QueryFlowGoodsBatchDetailBackupListPageRequest request) {
        return this.baseMapper.pageBackup(request.getPage(), request);
    }

    @Override
    public int refreshFlowGoodsBatchDetail(UpdateFlowIndexRequest request) {
        Integer count = baseMapper.selectFlowGoodsBatchDetailCount(request);
        log.info("销售流向数据刷新索引:eid{},count{}", request.getEid(), count);
        if (count == 0) {
            return 0;
        }
        long current = 1;
        long size = 2000;
        while ((current - 1) * size < count) {
            Page<FlowGoodsBatchDetailDO> page = new Page<>(current, size, count, false);
            Page<FlowGoodsBatchDetailDO> flowSaleDOPage = baseMapper.selectFlowGoodsBatchDetailPage(page, request);
            List<FlowGoodsBatchDetailDO> flowSaleDOList = flowSaleDOPage.getRecords();
            if (CollUtil.isEmpty(flowSaleDOList)) {
                break;
            }
            current++;
            Long start=System.currentTimeMillis();
            esFlowGoodsBatchDetailApi.updateFlowGoodsBatchDetail(PojoUtils.map(flowSaleDOList, EsFlowGoodsBatchDetailDTO.class));
            log.info("执行时间：{}",System.currentTimeMillis()-start);
        }
        return count;
    }

    @Override
    public int refreshFlowGoodsBatchDetailBackup(UpdateFlowIndexRequest request) {
        log.info("采购流向数据刷新索引:eid{}", request.getEid());
        QueryFlowGoodsBatchDetailBackupListPageRequest queryFlowGoodsBatchDetailBackupListPageRequest=new QueryFlowGoodsBatchDetailBackupListPageRequest();
        queryFlowGoodsBatchDetailBackupListPageRequest.setEidList(Arrays.asList(request.getEid()));
        queryFlowGoodsBatchDetailBackupListPageRequest.setTableName("flow_goods_batch_detail_"+request.getYear());
        Page<FlowGoodsBatchDetailDO> page;
        int current = 1;
        int size = 2000;
        do {
            queryFlowGoodsBatchDetailBackupListPageRequest.setSize(size);
            queryFlowGoodsBatchDetailBackupListPageRequest.setCurrent(current);
            page = this.baseMapper.pageBackup(queryFlowGoodsBatchDetailBackupListPageRequest.getPage(), queryFlowGoodsBatchDetailBackupListPageRequest);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<FlowGoodsBatchDetailDO> records = page.getRecords();
            esFlowGoodsBatchDetailApi.updateFlowGoodsBatchDetail(PojoUtils.map(records, EsFlowGoodsBatchDetailDTO.class));
            if (records.size() < size) {
                break;
            }
            current++;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return 0;
    }

    @Override
    public boolean isHaveDataByEidAndGbDetailTime(QueryFlowGoodsBatchDetailExistRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getEid(), "参数 eid 不能为空");
        Assert.notNull(request.getGbDetailTime(), "参数 gbDetailTime 不能为空");
        LambdaQueryWrapper<FlowGoodsBatchDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDetailDO::getEid, request.getEid());
        wrapper.eq(FlowGoodsBatchDetailDO::getGbDetailTime, request.getGbDetailTime());
        wrapper.last("limit 1");
        FlowGoodsBatchDetailDO one = this.getOne(wrapper);
        if (ObjectUtil.isNotNull(one)) {
            return true;
        }
        return false;
    }

    @Override
    public Page<FlowGoodsBatchDetailDTO> getPageByGbMonth(QueryFlowGoodsBatchDetailByGbMonthPageRequest request) {
        Page<FlowGoodsBatchDetailDTO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(baseMapper.getPageByGbMonth(page, request), FlowGoodsBatchDetailDTO.class);
    }

    @Override
    public void updateCrmGoodsCode(UpdateCrmGoodsCodeRequest request) {
        FlowGoodsBatchDetailDO flowGoodsBatchDetailDO = new FlowGoodsBatchDetailDO();
        flowGoodsBatchDetailDO.setId(request.getId());
        flowGoodsBatchDetailDO.setCrmGoodsCode(request.getCrmGoodsCode());
        flowGoodsBatchDetailDO.setUpdateTime(new Date());
        baseMapper.updateById(flowGoodsBatchDetailDO);
    }

    @Override
    public List<FlowStatisticsBO> getFlowGoodsBatchStatistics(QueryFlowStatisticesRequest request) {
        return baseMapper.getFlowGoodsBatchStatistics(request);
    }

    public void executeFlowGoodsBatchDetailCrmGoodsCode(List<FlowGoodsBatchDetailDO> list) {
        for (FlowGoodsBatchDetailDO flowGoodsBatchDO : list) {
            if (StrUtil.isEmpty(flowGoodsBatchDO.getCrmCode())) {
                CrmEnterpriseDTO crmEnterpriseCodeDO = crmEnterpriseService.getCrmEnterpriseCodeByName(flowGoodsBatchDO.getEname(),true);
                if (crmEnterpriseCodeDO != null) {
                    flowGoodsBatchDO.setCrmCode(crmEnterpriseCodeDO.getCode());
                }
            }
        }

        for (FlowGoodsBatchDetailDO flowGoodsBatchDO : list) {
            SaveOrUpdateFlowGoodsBatchDetailRequest saveOrUpdateFlowSaleRequest = new SaveOrUpdateFlowGoodsBatchDetailRequest();
            saveOrUpdateFlowSaleRequest.setId(flowGoodsBatchDO.getId());
            if (StrUtil.isNotEmpty(flowGoodsBatchDO.getCrmCode())) {
                saveOrUpdateFlowSaleRequest.setCrmCode(flowGoodsBatchDO.getCrmCode());
                FlowGoodsPriceRelationDTO flowGoodsPriceRelationDO = flowGoodsPriceRelationService.getByGoodsNameAndSpecAndEnterpriseCode(flowGoodsBatchDO.getGbName(), flowGoodsBatchDO.getGbSpecifications(), flowGoodsBatchDO.getCrmCode());
                if (flowGoodsPriceRelationDO != null) {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(flowGoodsPriceRelationDO.getGoodsCode());
                } else {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                }
            } else {
                saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
            }

            if (saveOrUpdateFlowSaleRequest.getCrmGoodsCode() == -1) {
                //获取crm_goods
                if (flowGoodsBatchDO.getSpecificationId() != 0) {
                    List<CrmGoodsDTO> crmGoodsDTOList = crmGoodsService.getCrmGoodsBySpecificationId(flowGoodsBatchDO.getSpecificationId());
                    if (CollUtil.isNotEmpty(crmGoodsDTOList)) {
                        if (crmGoodsDTOList.size() == 1) {
                            saveOrUpdateFlowSaleRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                        } else {
                            saveOrUpdateFlowSaleRequest.setCrmGoodsCode(crmGoodsDTOList.get(0).getGoodsCode());
                            saveOrUpdateFlowSaleRequest.setGoodsPossibleError(1);
                        }
                    } else {
                        saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                    }
                } else {
                    saveOrUpdateFlowSaleRequest.setCrmGoodsCode(-1L);
                }
            }
            this.updateFlowGoodsBatchDetailById(saveOrUpdateFlowSaleRequest);
        }
    }

    private void updateCrmGoodsCode(Long crmId) {
        LambdaQueryWrapper<FlowGoodsBatchDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDetailDO::getCrmGoodsCode, -1);
        wrapper.eq(FlowGoodsBatchDetailDO::getCrmEnterpriseId,crmId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return;
        }

        FlowGoodsBatchDetailDO flowSaleDO = new FlowGoodsBatchDetailDO();
        flowSaleDO.setCrmGoodsCode(0L);
        baseMapper.update(flowSaleDO, wrapper);
    }
}
