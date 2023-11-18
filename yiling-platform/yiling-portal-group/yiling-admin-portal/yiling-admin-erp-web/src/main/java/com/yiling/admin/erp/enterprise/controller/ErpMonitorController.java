package com.yiling.admin.erp.enterprise.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.erp.enterprise.form.QueryErpMonitorPageForm;
import com.yiling.admin.erp.enterprise.form.QueryErpMonitorPurchaseExceptionPageForm;
import com.yiling.admin.erp.enterprise.form.QueryErpMonitorSaleExceptionPageForm;
import com.yiling.admin.erp.enterprise.form.UpdateClientToolForm;
import com.yiling.admin.erp.enterprise.vo.ErpMonitorCountInfoVO;
import com.yiling.admin.erp.enterprise.vo.ErpMonitorCountStatisticsVo;
import com.yiling.admin.erp.enterprise.vo.ErpMonitorDetailVo;
import com.yiling.admin.erp.enterprise.vo.ErpMonitorPageVo;
import com.yiling.admin.erp.enterprise.vo.ErpMonitorPurchaseExceptionVO;
import com.yiling.admin.erp.enterprise.vo.ErpMonitorSaleExceptionVO;
import com.yiling.admin.erp.redis.factory.RedisServiceFactory;
import com.yiling.admin.erp.redis.service.RedisService;
import com.yiling.admin.erp.redis.util.RedisKey;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.api.DictTypeApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.basic.dict.bo.DictTypeBO;
import com.yiling.basic.dict.bo.request.QueryDictTypeRequest;
import com.yiling.dataflow.check.api.FlowPurchaseCheckApi;
import com.yiling.dataflow.check.dto.request.QueryFlowPurchaseCheckTaskPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpDataStatApi;
import com.yiling.open.erp.api.ErpHeartApi;
import com.yiling.open.erp.bo.ErpErpDataStatCountBO;
import com.yiling.open.erp.bo.ErpHeartBeatCountBO;
import com.yiling.open.erp.bo.ErpMonitorCountInfoDetailBO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.dto.request.ErpMonitorQueryRequest;
import com.yiling.open.erp.dto.request.QueryErpDataStatCountRequest;
import com.yiling.open.erp.dto.request.QueryHeartBeatCountRequest;
import com.yiling.open.erp.enums.ErpMonitorCountStatisticsOpenTypeEnum;
import com.yiling.open.monitor.api.MonitorAbnormalDataApi;
import com.yiling.open.monitor.dto.MonitorAbnormalDataDTO;
import com.yiling.open.monitor.dto.request.QueryErpMonitorSaleExceptionPageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * B2B运营后台 ERP监控信息
 *
 * @author: houjie.sun
 * @date: 2022/1/13
 */
@Api(tags = "ERP监控信息接口")
@RestController
@RequestMapping("/erpMonitor")
public class ErpMonitorController {
    @DubboReference(timeout = 10 * 1000)
    ErpClientApi erpClientApi;
    @DubboReference
    ErpHeartApi erpHeartApi;
    @DubboReference
    ErpDataStatApi erpDataStatApi;
    @DubboReference
    DictTypeApi dictTypeApi;
    @DubboReference
    DictDataApi dictDataApi;
    @DubboReference
    MonitorAbnormalDataApi monitorAbnormalDataApi;
    @DubboReference
    FlowPurchaseCheckApi flowPurchaseCheckApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final RedisService redisService = RedisServiceFactory.getRedisService();
    /**
     * erp对接请求次数阈值,数据字典名称
     */
    private static final String DICT_TYPE_ERP_MONITOR_COUNT = "erp_monitor_count";


    @ApiOperation(value = "数量统计", httpMethod = "POST")
    @PostMapping("/countStatistics")
    public Result<ErpMonitorCountStatisticsVo> countStatistics(@CurrentUser CurrentAdminInfo adminInfo) {
        // 统计，超过请求次数关闭对接数量、24小时内无心跳对接数量
        ErpMonitorCountStatisticsVo erpMonitorCountStatistics = PojoUtils.map(erpClientApi.getErpMonitorCountStatistics(), ErpMonitorCountStatisticsVo.class);
        return Result.success(erpMonitorCountStatistics);
    }

    @ApiOperation(value = "开启远程执行", httpMethod = "POST")
    @PostMapping("/updateComand")
    public Result<BoolObject> updateComand(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateClientToolForm form) {
        ErpClientSaveRequest request = new ErpClientSaveRequest();
        request.setSuId(form.getSuId());
        request.setCommandTime(new Date());
        request.setCommandStatus(1);
        Boolean bool = erpClientApi.updateCommandBySuId(request);
        return Result.success(new BoolObject(bool));
    }



    @ApiOperation(value = "ERP对接企业监控信息列表分页", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<ErpMonitorPageVo<ErpMonitorDetailVo>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryErpMonitorPageForm form) {
        ErpMonitorPageVo<ErpMonitorDetailVo> page = new ErpMonitorPageVo();
        ErpMonitorQueryRequest request = PojoUtils.map(form, ErpMonitorQueryRequest.class);
        // 统计当月无销售企业
        if(ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.NO_FLOW_SALE.getCode(), form.getOpenType())){
            Set<String> noFlowSaleEidSet = stringRedisTemplate.opsForSet().members(ErpConstants.erp_flow_sale_Statistics);
            if(CollUtil.isEmpty(noFlowSaleEidSet)){
                return Result.success(page);
            }
            List<Long> eidList = Convert.toList(Long.class, noFlowSaleEidSet);
            request.setRkSuIdList(eidList);
        }

        Page<ErpClientDTO> erpMonitorPage = erpClientApi.getErpMonitorListPage(request);
        page = new ErpMonitorPageVo();
        page.setSize(erpMonitorPage.getSize());
        page.setCurrent(erpMonitorPage.getCurrent());
        page.setTotal(erpMonitorPage.getTotal());
        page.setRecords(PojoUtils.map(erpMonitorPage.getRecords(), ErpMonitorDetailVo.class));
        // 根据数据字典名称，获取定义的erp对接请求次数阈值信息
        buildErpMonitorCountInfo(page);
        // 统计请求数量
        handleRequestAndHeartCount(request, page);
        return Result.success(page);
    }

    @ApiOperation(value = "销售异常信息列表分页", httpMethod = "POST")
    @PostMapping("/querySaleExceptionListPage")
    public Result<Page<ErpMonitorSaleExceptionVO>> querySaleExceptionListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryErpMonitorSaleExceptionPageForm form) {
        if(!ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.ALL_QUERY.getCode(), form.getOpenType()) && !ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.EXCEPTION_FLOW_SALE.getCode(), form.getOpenType())){
            throw new BusinessException(ResultCode.FAILED, "ERP监控查询类型不符合");
        }
        // 查询时间校验
        checkStartAndEndDate(form.getFlowTimeStart(), form.getFlowTimeEnd());
        QueryErpMonitorSaleExceptionPageRequest request = PojoUtils.map(form, QueryErpMonitorSaleExceptionPageRequest.class);
        Page<ErpMonitorSaleExceptionVO> saleExceptionPage = PojoUtils.map(monitorAbnormalDataApi.getSaleExceptionListPage(request), ErpMonitorSaleExceptionVO.class);
        // 原始单据上传时间
        buildParentUploadTime(saleExceptionPage);
        return Result.success(saleExceptionPage);
    }

    private void buildParentUploadTime(Page<ErpMonitorSaleExceptionVO> saleExceptionPage) {
        if(ObjectUtil.isNull(saleExceptionPage) || CollUtil.isEmpty(saleExceptionPage.getRecords())){
            return;
        }
        List<Long> parentIdList = saleExceptionPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getParentId()) && 0 != o.getParentId().intValue()).map(ErpMonitorSaleExceptionVO::getParentId).distinct().collect(Collectors.toList());
        if(CollUtil.isEmpty(parentIdList)){
            return;
        }
        List<MonitorAbnormalDataDTO> monitorAbnormalDataList = monitorAbnormalDataApi.getByIdList(parentIdList);
        if(CollUtil.isEmpty(monitorAbnormalDataList)){
            return;
        }
        Map<Long, MonitorAbnormalDataDTO> monitorAbnormalDataMap = monitorAbnormalDataList.stream().collect(Collectors.toMap(MonitorAbnormalDataDTO::getId, Function.identity()));
        saleExceptionPage.getRecords().forEach(o -> {
            if(ObjectUtil.isNotNull(o.getParentId()) && 0 != o.getParentId().intValue()){
                MonitorAbnormalDataDTO monitorAbnormalDataDTO = monitorAbnormalDataMap.get(o.getParentId());
                o.setParentUploadTime(monitorAbnormalDataDTO.getUploadTime());
            }
        });
    }

    /**
     * 查询日期范围校验，最多30天
     *
     * @param startTime
     * @param endTime
     */
    private void checkStartAndEndDate(Date startTime, Date endTime) {
        if(ObjectUtil.isNull(startTime) || ObjectUtil.isNull(endTime)){
            throw new BusinessException(ResultCode.FAILED, "开始日期、结束日期不能为空");
        }
        DateTime startDate = DateUtil.beginOfDay(startTime);
        DateTime endDate = DateUtil.endOfDay(endTime);
        long offset = DateUtil.betweenDay(startDate, endDate, false) + 1;
        if (offset > 31) {
            throw new BusinessException(ResultCode.FAILED, "开始日期、结束日期，时间范围不能大于31天");
        }
    }

    @ApiOperation(value = "采购异常信息列表分页", httpMethod = "POST")
    @PostMapping("/queryPurchaseExceptionListPage")
    public Result<Page<ErpMonitorPurchaseExceptionVO>> queryPurchaseExceptionListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryErpMonitorPurchaseExceptionPageForm form) {
        if(!ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.ALL_QUERY.getCode(), form.getOpenType()) && !ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.EXCEPTION_FLOW_PURCHASE.getCode(), form.getOpenType())){
            throw new BusinessException(ResultCode.FAILED, "ERP监控查询类型不符合");
        }
        // 查询时间校验
        checkStartAndEndDate(form.getPoTimeStart(), form.getPoTimeEnd());
        QueryFlowPurchaseCheckTaskPageRequest request = PojoUtils.map(form, QueryFlowPurchaseCheckTaskPageRequest.class);
        Page<ErpMonitorPurchaseExceptionVO> purchaseExceptionPage = PojoUtils.map(flowPurchaseCheckApi.getPurchaseExceptionListPage(request), ErpMonitorPurchaseExceptionVO.class);
        return Result.success(purchaseExceptionPage);
    }

    /**
     * 根据数据字典名称，获取定义的erp对接请求次数阈值信息
     *
     * @param page
     */
    private void buildErpMonitorCountInfo(ErpMonitorPageVo<ErpMonitorDetailVo> page) {
        // 获取nacos配置的erp对接请求次数阈值信息
        List<ErpMonitorCountInfoDetailBO> erpMonitorCountInfoList = erpClientApi.getErpMonitorCountInfoDetail();
        if(CollUtil.isEmpty(erpMonitorCountInfoList)){
            page.setMonitorCountInfoList(ListUtil.empty());
            return;
        }
        Map<String, ErpMonitorCountInfoDetailBO> erpMonitorCountInfoMap = erpMonitorCountInfoList.stream().collect(Collectors.toMap(ErpMonitorCountInfoDetailBO::getTaskNo, Function.identity()));

        QueryDictTypeRequest dictTypeRequest = new QueryDictTypeRequest();
        dictTypeRequest.setName(DICT_TYPE_ERP_MONITOR_COUNT);
        Page<DictTypeBO> dictTypePage = dictTypeApi.getDictTypePage(dictTypeRequest);
        if (ObjectUtil.isNull(dictTypePage) || CollUtil.isEmpty(dictTypePage.getRecords())) {
            page.setMonitorCountInfoList(ListUtil.empty());
            return;
        }

        DictTypeBO dictTypeBO = dictTypePage.getRecords().get(0);
        List<DictDataBO> dictDataList = dictDataApi.getEnabledByTypeIdList(dictTypeBO.getId());
        if (CollUtil.isEmpty(dictDataList)) {
            page.setMonitorCountInfoList(ListUtil.empty());
            return;
        }

        List<ErpMonitorCountInfoVO> monitorCountInfoList = new ArrayList<>();
        ErpMonitorCountInfoVO erpMonitorCountInfoVO;
        for (DictDataBO dictData : dictDataList) {
            String taskNo = dictData.getLabel();
            ErpMonitorCountInfoDetailBO erpMonitorCountInfo = erpMonitorCountInfoMap.get(taskNo);

            erpMonitorCountInfoVO = new ErpMonitorCountInfoVO();
            erpMonitorCountInfoVO.setTaskNo(taskNo);
            erpMonitorCountInfoVO.setTaskName(dictData.getDescription());
            // 请求次数取nacos配置的值，没有则取数据字典的
            String value = "0";
            if(ObjectUtil.isNotNull(erpMonitorCountInfo)){
                value = erpMonitorCountInfo.getMonitorCount().toString();
            } else{
                value = dictData.getValue();
            }
            erpMonitorCountInfoVO.setValue(value);
            monitorCountInfoList.add(erpMonitorCountInfoVO);
        }
        page.setMonitorCountInfoList(monitorCountInfoList);
    }

    /**
     * 统计请求、心跳数量
     *
     * @param request
     * @param page
     */
    private void handleRequestAndHeartCount(ErpMonitorQueryRequest request, ErpMonitorPageVo<ErpMonitorDetailVo> page) {
        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return;
        }
        List<ErpMonitorDetailVo> records = page.getRecords();
        List<Long> suIdList = records.stream().map(r -> r.getSuId()).distinct().collect(Collectors.toList());
        // 统计时间跨度，默认统计当天，最大90天
        Date date = new Date();
        Date startTime = request.getStartTime();
        if (ObjectUtil.isNull(startTime)) {
            startTime = DateUtil.beginOfDay(date);
        }
        Date endTime = request.getEndTime();
        if (ObjectUtil.isNull(endTime)) {
            endTime = DateUtil.endOfDay(date);
        }
        long betweenDays = DateUtil.between(startTime, endTime, DateUnit.DAY);
        if (betweenDays > 90) {
            throw new BusinessException(ResultCode.FAILED, "查询时间范围不能大于90天");
        }

        // 统计请求数量
        Map<Integer, ErpErpDataStatCountBO> erpDataStatCountMap = getErpDataStatCountMap(suIdList, startTime, endTime);

        for (ErpMonitorDetailVo detail : records) {
            // 请求数量
            long requestAddCont = 0;
            long requestUpdateCont = 0;
            long requestDeleteCont = 0;
            ErpErpDataStatCountBO erpErpDataStatCountBO = erpDataStatCountMap.get(detail.getSuId());

            if (ObjectUtil.isNotNull(erpErpDataStatCountBO)) {
                requestAddCont = ObjectUtil.isNull(erpErpDataStatCountBO.getAddCount()) ? 0 : erpErpDataStatCountBO.getAddCount();
                requestUpdateCont = ObjectUtil.isNull(erpErpDataStatCountBO.getUpdateCount()) ? 0 : erpErpDataStatCountBO.getUpdateCount();
                requestDeleteCont = ObjectUtil.isNull(erpErpDataStatCountBO.getDeleteCount()) ? 0 : erpErpDataStatCountBO.getDeleteCount();
            }
            detail.setRequestAddCont(requestAddCont);
            detail.setRequestUpdateCont(requestUpdateCont);
            detail.setRequestDeleteCont(requestDeleteCont);
            detail.setRequestTotalCont(requestAddCont + requestUpdateCont + requestDeleteCont);
            String heartTime = redisService.get(RedisKey.generate("heart", detail.getSuId() + ""));
            if (StrUtil.isNotEmpty(heartTime)) {
                detail.setRedisHeartTime(DateUtil.parseDateTime(heartTime));
            }
            if (detail.getCommandStatus() == null || detail.getCommandStatus() == 0) {
                detail.setCommandButtonStatus(0);
                detail.setCommandButtonDesc("打开远程操作");
            } else {
                if (detail.getRedisHeartTime() != null && detail.getRedisHeartTime().getTime() > detail.getCommandTime().getTime()) {
                    detail.setCommandButtonStatus(2);
                    detail.setCommandButtonDesc("远程执行");
                } else {
                    detail.setCommandButtonStatus(1);
                    if (detail.getRedisHeartTime() == null) {
                        detail.setCommandButtonDesc("版本不支持");
                    } else {
                        long miao = 60*5 - (System.currentTimeMillis() - detail.getRedisHeartTime().getTime()) / 1000;
                        detail.setCommandButtonDesc("剩余" + miao + "秒可操作");
                    }
                }
            }
        }
    }

    /**
     * 根据suid、开始时间、结束时间，统计请求数量
     *
     * @param suIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private Map<Integer, ErpErpDataStatCountBO> getErpDataStatCountMap(List<Long> suIdList, Date startTime, Date endTime) {
        Map<Integer, ErpErpDataStatCountBO> erpDataStatCountMap = new HashMap<>();
        if (CollUtil.isEmpty(suIdList)) {
            return erpDataStatCountMap;
        }
        QueryErpDataStatCountRequest dataStatCountRequest = new QueryErpDataStatCountRequest();
        dataStatCountRequest.setSuIdList(suIdList);
        dataStatCountRequest.setStartTime(startTime);
        dataStatCountRequest.setEndTime(endTime);
        List<ErpErpDataStatCountBO> erpDataStatCountList = erpDataStatApi.getErpDataStatCount(dataStatCountRequest);
        if (CollUtil.isNotEmpty(erpDataStatCountList)) {
            erpDataStatCountMap = erpDataStatCountList.stream().collect(Collectors.toMap(d -> d.getSuId(), d -> d, (v1, v2) -> v2));
        }
        return erpDataStatCountMap;
    }

    /**
     * 根据suid、开始时间、结束时间，统计心跳数量
     *
     * @param suIdList
     * @param startTime
     * @param endTime
     * @return
     */
    private Map<Integer, ErpHeartBeatCountBO> getErpHeartCountMap(List<Long> suIdList, Date startTime, Date endTime) {
        Map<Integer, ErpHeartBeatCountBO> erpHeartCountMap = new HashMap<>();
        if (CollUtil.isEmpty(suIdList)) {
            return erpHeartCountMap;
        }
        QueryHeartBeatCountRequest heartBeatCountRequest = new QueryHeartBeatCountRequest();
        heartBeatCountRequest.setSuIdList(suIdList);
        heartBeatCountRequest.setStartTime(startTime);
        heartBeatCountRequest.setEndTime(endTime);
        List<ErpHeartBeatCountBO> erpHeartCountList = erpHeartApi.getErpHeartCount(heartBeatCountRequest);
        if (CollUtil.isNotEmpty(erpHeartCountList)) {
            erpHeartCountMap = erpHeartCountList.stream().collect(Collectors.toMap(h -> h.getSuId(), h -> h, (v1, v2) -> v2));
        }
        return erpHeartCountMap;
    }

}
