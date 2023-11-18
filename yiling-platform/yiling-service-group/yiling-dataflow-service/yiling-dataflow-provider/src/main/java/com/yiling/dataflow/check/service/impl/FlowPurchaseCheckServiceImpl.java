package com.yiling.dataflow.check.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dao.FlowPurchaseCheckTaskMapper;
import com.yiling.dataflow.check.dto.request.FlowPurchaseSpecificationIdTotalQuantityRequest;
import com.yiling.dataflow.check.dto.request.QueryFlowPurchaseCheckTaskPageRequest;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckJobDO;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckTaskDO;
import com.yiling.dataflow.check.service.FlowPurchaseCheckJobService;
import com.yiling.dataflow.check.service.FlowPurchaseCheckService;
import com.yiling.dataflow.flow.enums.SyncStatus;
import com.yiling.dataflow.order.dao.FlowPurchaseMapper;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
@Slf4j
@Service
public class FlowPurchaseCheckServiceImpl extends BaseServiceImpl<FlowPurchaseCheckTaskMapper, FlowPurchaseCheckTaskDO> implements FlowPurchaseCheckService {

    @Autowired
    private FlowPurchaseCheckJobService flowPurchaseCheckJobService;
    @Autowired
    private FlowPurchaseService flowPurchaseService;
    @Autowired
    private FlowSaleService flowSaleService;
    @Autowired
    private FlowPurchaseMapper flowPurchaseMapper;
    @Autowired
    private FlowPurchaseCheckTaskMapper flowPurchaseCheckTaskMapper;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    public static final int SUB_SIZE = 500;

    @Override
    public List<FlowPurchaseSpecificationIdTotalQuantityBO> getSpecificationIdTotalQuantityByPoTime(FlowPurchaseSpecificationIdTotalQuantityRequest request) {
        return flowPurchaseMapper.getSpecificationIdTotalQuantityByPoTime(request);
    }

    @Override
    public Boolean saveBatchCheck(List<FlowPurchaseCheckTaskDO> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return this.saveBatch(list, 1000);
    }

    @Override
    public void flowPurchaseCheck() {
        log.info("[采购流向是否有对应的销售数据核查task]任务执行开始");
        LambdaQueryWrapper<FlowPurchaseCheckTaskDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowPurchaseCheckTaskDO::getCheckStatus, SyncStatus.UNSYNC.getCode()).last("limit 1");
        FlowPurchaseCheckTaskDO flowPurchaseCheckTask = this.getOne(queryWrapper);
        if (flowPurchaseCheckTask == null) {
            return;
        }

        Long checkJobId = flowPurchaseCheckTask.getCheckJobId();
        if (ObjectUtil.isNull(checkJobId) || 0 == checkJobId.intValue()) {
            log.error("[采购流向是否有对应的销售数据核查task]主任务id不能为空...");
            return;
        }

        FlowPurchaseCheckJobDO flowPurchaseCheckJobDO = flowPurchaseCheckJobService.getById(checkJobId);

        QueryWrapper<FlowPurchaseCheckTaskDO> jobIdQuery = new QueryWrapper<>();
        jobIdQuery.select(" DISTINCT eid ").lambda().eq(FlowPurchaseCheckTaskDO::getCheckJobId,checkJobId).eq(FlowPurchaseCheckTaskDO::getCheckStatus, SyncStatus.UNSYNC.getCode());
        List<FlowPurchaseCheckTaskDO> flowPurchaseCheckTaskDOList=this.list(jobIdQuery);
        List<Long> eidList=flowPurchaseCheckTaskDOList.stream().map(e->e.getEid()).collect(Collectors.toList());
        for(Long eid:eidList) {
            LambdaQueryWrapper<FlowPurchaseCheckTaskDO> taskQueryWrapper = new LambdaQueryWrapper<>();
            taskQueryWrapper.eq(FlowPurchaseCheckTaskDO::getCheckStatus, SyncStatus.UNSYNC.getCode()).eq(FlowPurchaseCheckTaskDO::getCheckJobId, checkJobId).eq(FlowPurchaseCheckTaskDO::getEid, eid);
            List<FlowPurchaseCheckTaskDO> list = this.list(taskQueryWrapper);
            if (CollUtil.isEmpty(list)) {
                continue;
            }

            List<Long> supplierIdList = list.stream().map(e -> e.getSupplierId()).collect(Collectors.toList());
            getFlowPurchaseCheckTaskDO(list, supplierIdList, flowPurchaseCheckJobDO.getStartTime(), flowPurchaseCheckJobDO.getEndTime());

            for (FlowPurchaseCheckTaskDO flowPurchaseCheckTaskDO : list) {
                flowPurchaseCheckTaskDO.setCheckStatus(SyncStatus.SUCCESS.getCode());
                flowPurchaseCheckTaskDO.setOpTime(new Date());
                flowPurchaseCheckTaskDO.setOpUserId(0L);
            }
            this.updateBatchById(list);
        }
    }

    @Override
    public void getFlowPurchaseCheckTaskDO(List<FlowPurchaseCheckTaskDO> list, List<Long> eidList, Date start, Date end) {
        //取流向里面的销售数据
        List<FlowSaleDTO> flowSaleDTOList = getFlowSaleList(eidList, start, end);

        //时间+商业公司+客户公司+规格ID+批号
        Map<String, BigDecimal> map = new HashMap<>();
        for (FlowSaleDTO flowSaleDTO : flowSaleDTOList) {
            BigDecimal number = flowSaleDTO.getSoQuantity();
            if(flowSaleDTO.getSpecificationId()==0){
                continue;
            }
            if (map.containsKey(DateUtil.formatDate(flowSaleDTO.getSoTime()) + "_" + flowSaleDTO.getEid() + "_" + flowSaleDTO.getEnterpriseName() + "_" + flowSaleDTO.getSpecificationId() + "_" + flowSaleDTO.getSoBatchNo())) {
                number = map.getOrDefault(DateUtil.formatDate(flowSaleDTO.getSoTime()) + "_" + flowSaleDTO.getEid() + "_" + flowSaleDTO.getEnterpriseName() + "_" + flowSaleDTO.getSpecificationId() + "_" + flowSaleDTO.getSoBatchNo(), BigDecimal.ZERO).add(number);
            }
            map.put(DateUtil.formatDate(flowSaleDTO.getSoTime()) + "_" + flowSaleDTO.getEid() + "_" + flowSaleDTO.getEnterpriseName() + "_" + flowSaleDTO.getSpecificationId() + "_" + flowSaleDTO.getSoBatchNo(), number);
        }

        for (FlowPurchaseCheckTaskDO flowPurchaseCheckTaskDO : list) {
            // 销售时间，取采购时间向前推15天（包含采购当天）作为销售日期开始时间、采购时间作为销售日期结束时间，共查询15天的销售
            for (int i = 15; i > 0; i--) {
                String startTime = DateUtil.formatDate(DateUtil.offsetDay(flowPurchaseCheckTaskDO.getPoTime(), -(i - 1)));
                BigDecimal soQuantity = map.get(startTime + "_" + flowPurchaseCheckTaskDO.getSupplierId() + "_" + flowPurchaseCheckTaskDO.getEname() + "_" + flowPurchaseCheckTaskDO.getSpecificationId() + "_" + flowPurchaseCheckTaskDO.getPoBatchNo());
                if (soQuantity != null && flowPurchaseCheckTaskDO.getTotalPoQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    //采购的数量够减
                    if (flowPurchaseCheckTaskDO.getTotalPoQuantity().compareTo(soQuantity) >= 0) {
                        flowPurchaseCheckTaskDO.setTotalPoQuantity(flowPurchaseCheckTaskDO.getTotalPoQuantity().subtract(soQuantity));
                        flowPurchaseCheckTaskDO.setTotalSoQuantity(flowPurchaseCheckTaskDO.getTotalSoQuantity().add(soQuantity));
                        map.put(startTime + "_" + flowPurchaseCheckTaskDO.getSupplierId() + "_" + flowPurchaseCheckTaskDO.getEname() + "_" + flowPurchaseCheckTaskDO.getSpecificationId() + "_" + flowPurchaseCheckTaskDO.getPoBatchNo(), BigDecimal.ZERO);
                    } else {
                        BigDecimal shengyu = soQuantity.subtract(flowPurchaseCheckTaskDO.getTotalPoQuantity());
                        flowPurchaseCheckTaskDO.setTotalSoQuantity(flowPurchaseCheckTaskDO.getTotalSoQuantity().add(flowPurchaseCheckTaskDO.getTotalPoQuantity()));
                        flowPurchaseCheckTaskDO.setTotalPoQuantity(BigDecimal.ZERO);
                        map.put(startTime + "_" + flowPurchaseCheckTaskDO.getSupplierId() + "_" + flowPurchaseCheckTaskDO.getEname() + "_" + flowPurchaseCheckTaskDO.getSpecificationId() + "_" + flowPurchaseCheckTaskDO.getPoBatchNo(), shengyu);
                    }
                }
            }

            if (flowPurchaseCheckTaskDO.getTotalPoQuantity().compareTo(BigDecimal.ZERO) == 0) {
                flowPurchaseCheckTaskDO.setSaleFlag(3);
                flowPurchaseCheckTaskDO.setCheckStatus(SyncStatus.SUCCESS.getCode());
            }else if(flowPurchaseCheckTaskDO.getTotalSoQuantity().compareTo(BigDecimal.ZERO) == 0){
                flowPurchaseCheckTaskDO.setSaleFlag(1);
                flowPurchaseCheckTaskDO.setCheckMsg("没有采购销售");
            }else{
                flowPurchaseCheckTaskDO.setSaleFlag(2);
                flowPurchaseCheckTaskDO.setCheckMsg("采购数量>销售数量");
            }
        }
    }

    public List<FlowSaleDTO> getFlowSaleList(List<Long> eids, Date startTime, Date endTime) {
        List<FlowSaleDTO> saleFlowList = new ArrayList<>();
        int dayOffset = 15;
        // 销售时间，取采购时间向前推15天（包含采购当天）作为销售日期开始时间、采购时间作为销售日期结束时间，共查询15天的销售
        DateTime saleStartTime = DateUtil.beginOfDay(DateUtil.offsetDay(startTime, -(dayOffset - 1)));
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setEidList(eids);
        request.setStartTime(saleStartTime);
        request.setEndTime(endTime);
        request.setSpecificationIdFlag(1);
        Page<FlowSaleDTO> pageSale = null;
        int currentSale = 1;
        do {
            request.setCurrent(currentSale);
            request.setSize(500);
            pageSale = PojoUtils.map(flowSaleService.page(request), FlowSaleDTO.class);

            if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                break;
            }

            for (FlowSaleDTO e : pageSale.getRecords()) {
                FlowSaleDTO saleFlow = PojoUtils.map(e, FlowSaleDTO.class);
                saleFlowList.add(saleFlow);
            }
            currentSale = currentSale + 1;
        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));
        return saleFlowList;
    }

    @Override
    public Long getPurchaseExceptionCount(Date startTime, Date endTime) {
        Long purchaseExceptionCount = flowPurchaseCheckTaskMapper.getPurchaseExceptionCount(startTime, endTime);
        if(ObjectUtil.isNotNull(purchaseExceptionCount)){
            return purchaseExceptionCount;
        }
        return 0L;
    }

    @Override
    public Page<FlowPurchaseCheckTaskDO> page(QueryFlowPurchaseCheckTaskPageRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getPoTimeStart(), "参数 poTimeStart 不能为空");
        Assert.notNull(request.getPoTimeEnd(), "参数 poTimeEnd 不能为空");
        Page<FlowPurchaseCheckTaskDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, pageQueryWrapper(request));
    }

    private LambdaQueryWrapper<FlowPurchaseCheckTaskDO> pageQueryWrapper(QueryFlowPurchaseCheckTaskPageRequest request) {
        LambdaQueryWrapper<FlowPurchaseCheckTaskDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        Long eid = request.getEid();
        if (ObjectUtil.isNotNull(eid) && 0 != eid.intValue()) {
            lambdaQueryWrapper.eq(FlowPurchaseCheckTaskDO::getEid, eid);
        }

        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            lambdaQueryWrapper.like(FlowPurchaseCheckTaskDO::getEname, ename);
        }

        Date poTimeStart = request.getPoTimeStart();
        if (ObjectUtil.isNotNull(poTimeStart)) {
            lambdaQueryWrapper.ge(FlowPurchaseCheckTaskDO::getPoTime, DateUtil.beginOfDay(poTimeStart));
        }

        Date poTimeEnd = request.getPoTimeEnd();
        if (ObjectUtil.isNotNull(poTimeEnd)) {
            lambdaQueryWrapper.le(FlowPurchaseCheckTaskDO::getPoTime, DateUtil.endOfDay(poTimeEnd));
        }

        // 任务状态成功的
        lambdaQueryWrapper.eq(FlowPurchaseCheckTaskDO::getCheckStatus, 2);
        // 有无销售：0-无销售，1-有销售-数量不符合(采购数量>销售数量)
        lambdaQueryWrapper.in(FlowPurchaseCheckTaskDO::getSaleFlag, ListUtil.toList(0, 1));
        lambdaQueryWrapper.orderByDesc(FlowPurchaseCheckTaskDO::getId);
        return lambdaQueryWrapper;
    }

}
