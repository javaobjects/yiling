package com.yiling.dataflow.flow.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.flow.api.FlowSaleMatchApi;
import com.yiling.dataflow.flow.dto.FlowSaleMatchResultDTO;
import com.yiling.dataflow.flow.dto.request.FlowSaleMatchRequest;
import com.yiling.dataflow.flow.entity.FlowCrmSaleDO;
import com.yiling.dataflow.flow.service.FlowCrmSaleService;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/30
 */
@DubboService
@Slf4j
public class FlowSaleMatchApiImpl implements FlowSaleMatchApi {

    @Autowired
    private FlowSaleService flowSaleService;

    @Autowired
    private FlowCrmSaleService flowCrmSaleService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @Override
    public FlowSaleMatchResultDTO match(FlowSaleMatchRequest request) {
        FlowSaleMatchResultDTO result = new FlowSaleMatchResultDTO();

        Long eid = request.getEid();
        String soNo = request.getSoNo();

        List<FlowSaleDO> flowSaleDOList = flowSaleService.getByEidAndSoNo(eid, soNo);

        if (CollUtil.isEmpty(flowSaleDOList)) {
            return result;
        }

        // 获取以岭商业公司eid
        List<Long> eids = enterpriseApi.listSubEids(Constants.YILING_EID);

        List<FlowSaleMatchResultDTO.ErpGoodsInfo> erpList = new ArrayList<>();
        // 查询商品数据信息
        for (FlowSaleDO flowSaleDO : flowSaleDOList) {
            if (flowSaleDO.getSpecificationId() == null || flowSaleDO.getSpecificationId() == 0) {
                // 存在specificationId=0时，则匹配失败，直接返回空集合
                return result;
            }
            FlowSaleMatchResultDTO.ErpGoodsInfo erpGoodsInfo = new FlowSaleMatchResultDTO.ErpGoodsInfo();
            erpGoodsInfo.setEid(flowSaleDO.getEid());
            erpGoodsInfo.setEname(flowSaleDO.getEname());

            Long specId = flowSaleDO.getSpecificationId();
            List<GoodsDTO> goodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(Collections.singletonList(specId), eids);
            if (CollUtil.isEmpty(goodsDTOList)) {
                erpGoodsInfo.setYlGoodsId(0L);
                //  查询以岭标准名称和规格
                List<StandardGoodsSpecificationDTO> goodsSpecList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(Collections.singletonList(specId));
                if (CollUtil.isNotEmpty(goodsSpecList)) {
                    erpGoodsInfo.setYlGoodsName(goodsSpecList.get(0).getName());
                    erpGoodsInfo.setYlGoodsSpec(goodsSpecList.get(0).getSellSpecifications());
                }
            } else {
                GoodsDTO goodsDTO = goodsDTOList.get(0);
                erpGoodsInfo.setYlGoodsId(goodsDTO.getId());
                erpGoodsInfo.setYlGoodsName(goodsDTO.getName());
                erpGoodsInfo.setYlGoodsSpec(goodsDTO.getSellSpecifications());
            }
            erpGoodsInfo.setSellSpecId(specId);

            erpGoodsInfo.setCrmGoodsCode(String.valueOf(flowSaleDO.getCrmGoodsCode()));
            // 时间去掉时分秒
            erpGoodsInfo.setSoTime(DateUtil.parse(DateUtil.format(flowSaleDO.getSoTime(), "yyyy-MM-dd")));
            erpGoodsInfo.setSoBatchNo(flowSaleDO.getSoBatchNo());
            erpGoodsInfo.setEnterpriseName(flowSaleDO.getEnterpriseName());
            erpGoodsInfo.setSoQuantity(flowSaleDO.getSoQuantity());
            erpList.add(erpGoodsInfo);
        }
        result.setErpList(erpList);

        //  匹配CRM数据
        // erpList根据匹配条件分组
        Map<String, List<FlowSaleMatchResultDTO.ErpGoodsInfo>> erpListMap = erpList.stream()
                .collect(Collectors.groupingBy(e -> e.getEname() + "," + e.getEnterpriseName() + "," + e.getSoTime() + "," + e.getCrmGoodsCode() + "," + e.getSoQuantity() + "," + e.getSoBatchNo()));
        for (Map.Entry<String, List<FlowSaleMatchResultDTO.ErpGoodsInfo>> entry : erpListMap.entrySet()) {
            List<FlowSaleMatchResultDTO.ErpGoodsInfo> list = entry.getValue();
            FlowSaleMatchResultDTO.ErpGoodsInfo e = list.get(0);
            List<FlowCrmSaleDO> crmList = flowCrmSaleService.getList(e.getEname(), e.getEnterpriseName(), e.getSoTime(), e.getCrmGoodsCode(), e.getSoQuantity(), e.getSoBatchNo());
            if (CollUtil.isEmpty(crmList)) {
                return result;
            }
            if (crmList.size() != list.size()) {
                return result;
            }
        }
        result.setCrmFlag(true);
        return result;
    }
}
