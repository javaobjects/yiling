package com.yiling.dataflow.flow.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.config.FlowSettlementEnterpriseTagConfig;
import com.yiling.dataflow.config.FlowTagConfig;
import com.yiling.dataflow.flow.dao.FlowSaleSummaryDayMapper;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.entity.FlowSaleSummaryDO;
import com.yiling.dataflow.flow.entity.FlowSaleSummaryDayDO;
import com.yiling.dataflow.flow.service.FlowSaleSummaryDayService;
import com.yiling.dataflow.flow.service.FlowSaleSummaryService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
@Service
public class FlowSaleSummaryDayServiceImpl extends BaseServiceImpl<FlowSaleSummaryDayMapper, FlowSaleSummaryDayDO> implements FlowSaleSummaryDayService {

    @Autowired
    private FlowSaleSummaryService flowSaleSummaryService;

    @Autowired
    private FlowTagConfig flowTagConfig;

    @Autowired
    private FlowSettlementEnterpriseTagConfig flowSettlementEnterpriseTagConfig;


    @Override
    public void updateFlowSaleSummaryDayByDateTimeAndEid(QueryFlowSaleSummaryRequest request) {
        if (request.getEid() == null || request.getEid() == 0) {
            return;
        }
        if (request.getStartTime() == null) {
            return;
        }
        if (request.getEndTime() == null) {
            return;
        }
        //先删除数据
        this.baseMapper.deleteFlowSaleSummaryDayByEidAndSoTime(Arrays.asList(request.getEid()), request.getStartTime(), request.getEndTime());
        //2在查询流向数据
        {
            QueryFlowSaleSummaryRequest queryFlowSaleSummaryRequest = new QueryFlowSaleSummaryRequest();
            queryFlowSaleSummaryRequest.setStartTime(request.getStartTime());
            queryFlowSaleSummaryRequest.setEndTime(request.getEndTime());
            queryFlowSaleSummaryRequest.setEid(request.getEid());
            Page<FlowSaleSummaryDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowSaleSummaryRequest.setSize(size);
                queryFlowSaleSummaryRequest.setCurrent(current);
                page = flowSaleSummaryService.pageList(queryFlowSaleSummaryRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                this.inserteFlowSaleSummaryDayByFlowSaleSummaryList(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }

    }

    @Override
    public void updateFlowSaleSummaryDayLingShouByTerminalCustomerType(QueryFlowSaleSummaryRequest request) {
        this.baseMapper.updateFlowSaleSummaryDayByTerminalCustomerType(Arrays.asList(request.getEid()),request.getStartTime(),request.getEndTime(),Arrays.asList(1,3),"商务管理部","当月非锁销量","自然量");
    }

    @Override
    public void updateFlowSaleSummaryDayPifaByTerminalCustomerType(QueryFlowSaleSummaryRequest request) {
        this.baseMapper.updateFlowSaleSummaryDayByTerminalCustomerType(Arrays.asList(request.getEid()),request.getStartTime(),request.getEndTime(),Arrays.asList(2),"渠道内","商业客户数据计入营销中心","其他商业");
    }

    public void inserteFlowSaleSummaryDayByFlowSaleSummaryList(List<FlowSaleSummaryDO> flowSaleSummaryList) {
        List<FlowSaleSummaryDayDO> flowSaleSummaryDayList = new ArrayList<>();
        for (FlowSaleSummaryDO flowSaleSummaryDO : flowSaleSummaryList) {
            FlowSaleSummaryDayDO flowSaleSummaryDayDO = PojoUtils.map(flowSaleSummaryDO, FlowSaleSummaryDayDO.class);
            if(flowSaleSummaryDayDO.getIsLocked().equals("否")){
                this.flowSaleSummaryDayTran(flowSaleSummaryDayDO);
            }
            flowSaleSummaryDayList.add(flowSaleSummaryDayDO);
        }
        //批量插入
        this.saveBatch(flowSaleSummaryDayList);
    }

    public void flowSaleSummaryDayTran(FlowSaleSummaryDayDO flowSaleSummaryDayDO) {
        //不计入产品
        if (flowTagConfig.getBujiruList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            flowSaleSummaryDayDO.setDepartmentLabel("营销公司");
            flowSaleSummaryDayDO.setJudgmentLabel("不计入");
            flowSaleSummaryDayDO.setRemarkLabel("无效产品");
            return;
        }
        //万州产品
        if (flowTagConfig.getWanzhouList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            flowSaleSummaryDayDO.setDepartmentLabel("营销公司");
            flowSaleSummaryDayDO.setJudgmentLabel("不计入");
            flowSaleSummaryDayDO.setRemarkLabel("万州普药");
            return;
        }
        //招商普药
        if (flowTagConfig.getZhaoshangList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            flowSaleSummaryDayDO.setDepartmentLabel("营销公司");
            flowSaleSummaryDayDO.setJudgmentLabel("不计入");
            flowSaleSummaryDayDO.setRemarkLabel("招商普药");
            return;
        }
        //处理库存产品
        if (flowTagConfig.getKucuiList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            flowSaleSummaryDayDO.setDepartmentLabel("营销公司");
            flowSaleSummaryDayDO.setJudgmentLabel("不计入");
            flowSaleSummaryDayDO.setRemarkLabel("处理库存");
            return;
        }
        //双花产品
        if (flowTagConfig.getShuanghuaList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            if (flowSaleSummaryDayDO.getCommercialProvince().contains("上海") || flowSaleSummaryDayDO.getCommercialProvince().contains("江苏")) {
                flowSaleSummaryDayDO.setDepartmentLabel("营销公司");
                flowSaleSummaryDayDO.setJudgmentLabel("不参与计算品种");
                flowSaleSummaryDayDO.setRemarkLabel("江苏双花");
            }
            return;
        }
        //集采产品
        {
            for (String jicai : flowTagConfig.getJicaiList()) {
                if (jicai.contains(flowSaleSummaryDayDO.getCommercialProvince()) && jicai.contains(String.valueOf(flowSaleSummaryDayDO.getCrmGoodsCode()))) {
                    if (flowSaleSummaryDayDO.getTerminalCustomerType() == 1||flowSaleSummaryDayDO.getTerminalCustomerType() == 3) {
                        flowSaleSummaryDayDO.setDepartmentLabel("普药集采");
                        flowSaleSummaryDayDO.setJudgmentLabel("当月非锁销量");
                        flowSaleSummaryDayDO.setRemarkLabel("集采销量");
                        return;
                    }
                    if (flowSaleSummaryDayDO.getTerminalCustomerType() == 2) {
                        flowSaleSummaryDayDO.setDepartmentLabel("渠道内");
                        flowSaleSummaryDayDO.setJudgmentLabel("商业客户数据计入营销中心");
                        flowSaleSummaryDayDO.setRemarkLabel("其他商业(集采)");
                        return;
                    }
                }
            }
        }
        //中药饮片
        if (flowTagConfig.getZhongyaoList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            if (flowSaleSummaryDayDO.getTerminalCustomerType() == 1||flowSaleSummaryDayDO.getTerminalCustomerType() == 3) {
                flowSaleSummaryDayDO.setDepartmentLabel("中药饮片事业部");
                flowSaleSummaryDayDO.setJudgmentLabel("当月非锁销量");
                flowSaleSummaryDayDO.setRemarkLabel("饮片非锁");
                return;
            }
            if (flowSaleSummaryDayDO.getTerminalCustomerType() == 2) {
                flowSaleSummaryDayDO.setDepartmentLabel("渠道内");
                flowSaleSummaryDayDO.setJudgmentLabel("商业客户数据计入营销中心");
                flowSaleSummaryDayDO.setRemarkLabel("其他商业(饮片)");
                return;
            }
        }
        //参灵蓝药品
        if (flowTagConfig.getCanlinglanList().contains(flowSaleSummaryDayDO.getCrmGoodsCode())) {
            flowSaleSummaryDayDO.setDepartmentLabel("营销公司");
            flowSaleSummaryDayDO.setJudgmentLabel("不计入");
            flowSaleSummaryDayDO.setRemarkLabel("参灵蓝");
            return;
        }
        //分销部
        if (flowSaleSummaryDayDO.getBusinessDepartment().contains("分销部")) {
            if (flowSaleSummaryDayDO.getTerminalCustomerType() == 1||flowSaleSummaryDayDO.getTerminalCustomerType() == 3) {
                flowSaleSummaryDayDO.setDepartmentLabel("分销部");
                flowSaleSummaryDayDO.setJudgmentLabel("当月非锁销量");
                flowSaleSummaryDayDO.setRemarkLabel("分销非锁");
                return;
            }
        }
        //数字化
        if (flowSaleSummaryDayDO.getBusinessDepartment().contains("数字化网络部")) {
            if (flowSaleSummaryDayDO.getTerminalCustomerType() == 1||flowSaleSummaryDayDO.getTerminalCustomerType() == 3) {
                flowSaleSummaryDayDO.setDepartmentLabel("数字化网络部");
                flowSaleSummaryDayDO.setJudgmentLabel("当月非锁销量");
                flowSaleSummaryDayDO.setRemarkLabel("数字化非锁");
                return;
            }
        }
    }
}
