package com.yiling.dataflow.wash.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchTransitDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.dataflow.wash.service.FlowGoodsBatchTransitService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@Slf4j
@DubboService
public class FlowInTransitOrderApiImpl implements FlowInTransitOrderApi {

    @Autowired
    private FlowGoodsBatchTransitService flowGoodsBatchTransitService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @DubboReference
    private CrmGoodsGroupApi crmGoodsGroupApi;
    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    SjmsUserDatascopeApi userDatascopeApi;

    @Override
    public Page<FlowGoodsBatchTransitDTO> listPage(QueryFlowGoodsBatchTransitPageRequest request) {
        return PojoUtils.map(flowGoodsBatchTransitService.listPage(request), FlowGoodsBatchTransitDTO.class);
    }

    @Override
    public List<FlowGoodsBatchTransitDTO> listByEnterpriseAndSupplyIdAndGoodsCode(Integer goodsBatchType, List<Long> crmEnterpriseIdList, List<Long> crmSupplyIdList, List<Long> crmGoodsCodeList) {
        return PojoUtils.map(flowGoodsBatchTransitService.listByEnterpriseAndSupplyIdAndGoodsCode(goodsBatchType, crmEnterpriseIdList, crmSupplyIdList, crmGoodsCodeList), FlowGoodsBatchTransitDTO.class);
    }

    @Override
    public Boolean batchSave(List<SaveFlowGoodsBatchTransitRequest> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return flowGoodsBatchTransitService.batchSave(list);
    }

    @Override
    public Boolean batchUpdate(List<UpdateFlowGoodsBatchTransitRequest> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        return flowGoodsBatchTransitService.batchUpdate(list);
    }

    @Override
    public FlowGoodsBatchTransitDTO getById(Long id) {
        return PojoUtils.map(flowGoodsBatchTransitService.getById(id), FlowGoodsBatchTransitDTO.class);
    }

    @Override
    public int deleteById(Long id, Long currentUserId) {
        return flowGoodsBatchTransitService.deleteById(id, currentUserId);
    }

    @Override
    public Map<Long, Long> getCrmEnterpriseRelationShipMap(Map<Long, Long> crmEnterpriseIdGoodsCodeMap, int type, String gbDetailMonth) {
        if (CollUtil.isEmpty(crmEnterpriseIdGoodsCodeMap)) {
            return MapUtil.empty();
        }

        // 机构id 对应的三者关系 map
        Map<Long, Long> crmEnterpriseIdCersIdMap = new HashMap<>();

        // 1、查询清洗日程，获取年月，作为备份表后缀。
        String tableSuffix = "wash_";
        JSONObject jsonObject = checkFlowMonthWashControl(type, gbDetailMonth);
        if (ObjectUtil.isNull(jsonObject)) {
            throw new ServiceException("未查询到进行中、或手动开启的[终端库存上报]清洗日程");
        }
        String year = jsonObject.getStr("year", "");
        String month = jsonObject.getStr("month", "");
        tableSuffix = tableSuffix.concat(year).concat(month);

        for (Map.Entry<Long, Long> entry : crmEnterpriseIdGoodsCodeMap.entrySet()) {
            Long crmEnterpriseId = entry.getKey();
            Long goodsCode = entry.getValue();
            Long crmEnterpriseRelationShipId = null;
            try {
                crmEnterpriseRelationShipId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(goodsCode, crmEnterpriseId,  tableSuffix);
            } catch (Exception e) {
                log.warn("终端库存导入, 查询三者关系备份为空, 商品code：{}, 机构id:{}, 备份表后缀名：{}", goodsCode, crmEnterpriseId, tableSuffix);
            }
            if (ObjectUtil.isNotNull(crmEnterpriseRelationShipId) && 0 != crmEnterpriseRelationShipId) {
                crmEnterpriseIdCersIdMap.put(crmEnterpriseId, crmEnterpriseRelationShipId);
            }
        }
        return crmEnterpriseIdCersIdMap;
    }

    @Override
    public String checkFlowMonthWashControl(String businessName, String gbDetailMonth) {
        String[] split = gbDetailMonth.split("-");
        // 根据所属年月查询日程
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getByYearAndMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        if (ObjectUtil.isNull(flowMonthWashControlDTO) || ObjectUtil.isNull(flowMonthWashControlDTO.getId()) || 0 == flowMonthWashControlDTO.getId().intValue()) {
            return "此所属年月【" + gbDetailMonth + "】对应的 " + businessName + " 月流向清洗日程未创建";
        } else {
            Integer goodsBatchStatus = flowMonthWashControlDTO.getWashStatus();
            if (ObjectUtil.isNull(goodsBatchStatus) || goodsBatchStatus!=2) {
                String year = flowMonthWashControlDTO.getYear().toString();
                String month = flowMonthWashControlDTO.getMonth().toString();
                if (month.length() == 1) {
                    month = "0".concat(month);
                }
                String yearMonth = year.concat("-").concat(month);
                Date startTime = flowMonthWashControlDTO.getGoodsBatchStartTime();
                Date endTime = flowMonthWashControlDTO.getGoodsBatchEndTime();
                String start = DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss");
                String end = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
                throw new BusinessException(ResultCode.FAILED, "["+businessName+"]的月流向清洗日程不是进行中、不是手动开启，【" + yearMonth + "】提交时间为 " + start +" 至 " + end);
            }
        }
        return null;
    }

    @Override
    public FlowMonthWashControlDTO getGoodsBatchTime() {
        // 获取状态开启的日程的在途订单上报
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        if (ObjectUtil.isNotNull(flowMonthWashControlDTO) && ObjectUtil.isNotNull(flowMonthWashControlDTO.getId()) && 0 != flowMonthWashControlDTO.getId().intValue()) {
            return flowMonthWashControlDTO;
        }
        Date date = new Date();
        // 查询当月的日程
        int year = DateUtil.year(date);
        int month = DateUtil.month(date);
        FlowMonthWashControlDTO currentMonth = flowMonthWashControlApi.getByYearAndMonth(year, month);
        if (ObjectUtil.isNotNull(currentMonth) && ObjectUtil.isNotNull(currentMonth.getId()) && 0 != currentMonth.getId().intValue()) {
            return currentMonth;
        }
        // 查询上个月的日程
        DateTime lastMonthDate = DateUtil.lastMonth();
        int yearTwo = DateUtil.year(lastMonthDate);
        int monthTwo = DateUtil.month(lastMonthDate);
        FlowMonthWashControlDTO lastMonth = flowMonthWashControlApi.getByYearAndMonth(yearTwo, monthTwo);
        if (ObjectUtil.isNotNull(lastMonth) && ObjectUtil.isNotNull(lastMonth.getId()) && 0 != lastMonth.getId().intValue()) {
            return lastMonth;
        }
        return null;
    }

    @Override
    public Map<String, List<String>> buildUserDatascope(String currentUserCode, String method) {
        SjmsUserDatascopeBO datascopeBO = userDatascopeApi.getByEmpId(currentUserCode);
        log.debug(method + ", 当前用户:" + currentUserCode + "数据权限为:" + JSONUtil.toJsonStr(datascopeBO));
        if (ObjectUtil.isNull(datascopeBO)) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }
        Map<String, List<String>> map = new HashMap<>();
        List<Long> crmIds;
        List<String> provinceCodes = null;
        switch (OrgDatascopeEnum.getFromCode(datascopeBO.getOrgDatascope())) {
            case ALL:
                map.put("datascopeType", ListUtil.toList("0"));
                break;
            case PORTION:
                SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = datascopeBO.getOrgPartDatascopeBO();
                crmIds = orgPartDatascopeBO.getCrmEids();
                provinceCodes = orgPartDatascopeBO.getProvinceCodes();
                if (null == crmIds && null == provinceCodes) {
                    throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
                }

                map.put("datascopeType", ListUtil.toList("1"));
                if (CollUtil.isNotEmpty(crmIds)) {
                    map.put("crmIds", Convert.toList(String.class, crmIds));
                }
                map.put("provinceCodes", provinceCodes);
                break;
            default:
                throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }
        return map;
    }

    private JSONObject checkFlowMonthWashControl(int type, String gbDetailMonth) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = null;
        if (type == 0) {
            flowMonthWashControlDTO = flowMonthWashControlApi.getWashStatus();
        } else if (type == 1) {
            String[] split = gbDetailMonth.split("-");
            flowMonthWashControlDTO = flowMonthWashControlApi.getByYearAndMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }
        if (ObjectUtil.isNull(flowMonthWashControlDTO) || ObjectUtil.isNull(flowMonthWashControlDTO.getId()) || 0 == flowMonthWashControlDTO.getId().intValue()) {
            log.warn("[在途订单、终端库存上报]的月流向清洗日程已全部关闭", gbDetailMonth);
            return null;
        }
        Integer goodsBatchStatus = flowMonthWashControlDTO.getWashStatus();
        if (ObjectUtil.isNull(goodsBatchStatus) || goodsBatchStatus!=2) {
            log.warn("[在途订单、终端库存上报]的月流向清洗日程不是进行中、不是手动开启, 所属月份:{}", gbDetailMonth);
            return null;
        }

        String year = flowMonthWashControlDTO.getYear().toString();
        String month = flowMonthWashControlDTO.getMonth().toString();
        if (month.length() == 1) {
            month = "0".concat(month);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("year", year);
        jsonObject.set("month", month);
        return jsonObject;
    }
}
