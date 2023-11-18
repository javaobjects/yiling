package com.yiling.settlement.yee.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.yee.config.YeePayConfig;
import com.yiling.settlement.yee.constant.YeePaySettleConstants;
import com.yiling.settlement.yee.dao.YeeSettleSyncRecordMapper;
import com.yiling.settlement.yee.dto.YeeSettleSyncRecordDTO;
import com.yiling.settlement.yee.dto.request.QueryYeeSettleListByPageRequest;
import com.yiling.settlement.yee.entity.YeeSettleSyncRecordDO;
import com.yiling.settlement.yee.enums.YeeSettleErrCode;
import com.yiling.settlement.yee.enums.YeeSettleStatusEnum;
import com.yiling.settlement.yee.service.YeeSettleSyncRecordService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 易宝结算记录同步表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-04
 */
@Slf4j
@Service
public class YeeSettleSyncRecordServiceImpl extends BaseServiceImpl<YeeSettleSyncRecordMapper, YeeSettleSyncRecordDO> implements YeeSettleSyncRecordService {

    @Autowired
    private YeePayConfig yeePayConfig;

    @Override
    public void initData() {

        Date beginTime = DateUtil.parseDate("2022-09-01 00:00:00");
        DateTime endTime = DateUtil.offsetDay(beginTime, 1);

        do {
            Map<String, String> response = queryYeeSettleRecord(yeePayConfig.getMemberMerchantNo(), beginTime, endTime);

            if (MapUtil.isNotEmpty(response)) {
                //处理结果
                handleResponse(response);
            }
            endTime = DateUtil.offsetDay(endTime, 1);
            beginTime = DateUtil.offsetDay(beginTime, 1);
        } while (DateUtil.compare(endTime, DateUtil.date()) < 0);
    }

    @Override
    public void syncMemberSettleForToday() {
        Map<String, String> response = queryYeeSettleRecord(yeePayConfig.getMemberMerchantNo(), DateUtil.beginOfDay(DateUtil.yesterday()), new Date());

        if (MapUtil.isNotEmpty(response)) {
            //处理结果
            handleResponse(response);
        }
    }

    @Override
    public Map<String, String> queryYeeSettleRecord(String merchantNo, Date beginTime, Date endTime) {
        Map<String, String> result;

        YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
        request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
        request.addParam("merchantNo", merchantNo);
        request.addParam("settleRequestBeginTime", DateUtil.format(beginTime, DatePattern.NORM_DATETIME_PATTERN));
        request.addParam("settleRequestEndTime", DateUtil.format(endTime, DatePattern.NORM_DATETIME_PATTERN));

        YopResponse yopResponse;
        try {
            yopResponse = YopRsaClient.get("/rest/v1.0/settle/records/query", request);
        } catch (Exception exception) {
            log.error("查询易宝子账号回款结算记录失败，参数：{}，原因：{}", JSON.toJSON(request), exception);
            throw new BusinessException(YeeSettleErrCode.YEE_QUERY_SETTLE_FAIL);
        }
        if ("FAILURE".equals(yopResponse.getState())) {
            log.error("查询易宝子账号回款结算记录失败，参数：{}，原因：{}", JSON.toJSON(request), yopResponse.getError().getMessage());
            throw new BusinessException(YeeSettleErrCode.YEE_QUERY_SETTLE_FAIL);
        }

        result = parseResponse(yopResponse);
        String code = result.get("code");
        String message = result.get("message");
        //结算单不存在
        if (ObjectUtil.equal(YeePaySettleConstants.SETTLE_IS_EMPTY, code)) {
            return MapUtil.newHashMap();
        }
        //接口报错
        if (ObjectUtil.notEqual(YeePaySettleConstants.SUCCESS, code)) {
            log.error("查询易宝子账号回款结算记录失败，参数：{}，原因：{}", JSON.toJSON(request), message);
            throw new BusinessException(YeeSettleErrCode.YEE_QUERY_SETTLE_FAIL);
        }
        result.put("parentMerchantNo", yeePayConfig.getMerchantNo());
        result.put("merchantNo", merchantNo);
        return result;
    }

    @Override
    public Page<YeeSettleSyncRecordDTO> queryListByPage(QueryYeeSettleListByPageRequest request) {
        if (ObjectUtil.isNotNull(request.getCreateTimeBegin())){
            request.setCreateTimeBegin(DateUtil.beginOfDay(request.getCreateTimeBegin()));
        }
        if (ObjectUtil.isNotNull(request.getCreateTimeEnd())){
            request.setCreateTimeEnd(DateUtil.endOfDay(request.getCreateTimeEnd()));
        }
        LambdaQueryWrapper<YeeSettleSyncRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(request.getSummaryNo()), YeeSettleSyncRecordDO::getSummaryNo, request.getSummaryNo());
        wrapper.ge(ObjectUtil.isNotNull(request.getCreateTimeBegin()), YeeSettleSyncRecordDO::getCreateTime, request.getCreateTimeBegin());
        wrapper.le(ObjectUtil.isNotNull(request.getCreateTimeEnd()), YeeSettleSyncRecordDO::getCreateTime, request.getCreateTimeEnd());
        Page<YeeSettleSyncRecordDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, YeeSettleSyncRecordDTO.class);
    }

    /**
     * 处理结果
     *
     * @param response
     */
    private void handleResponse(Map<String, String> response) {
        JSONArray array = JSONUtil.parseArray(response.get("settleRecordQueryDtos"));
        Iterator<Object> iterator = array.iterator();
        List<YeeSettleSyncRecordDO> varList = ListUtil.toList();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            YeeSettleSyncRecordDO var = JSONUtil.toBean(JSONUtil.toJsonStr(next), YeeSettleSyncRecordDO.class);
            var.setId(null);
            var.setParentMerchantNo(response.get("parentMerchantNo"));
            var.setMerchantNo(response.get("merchantNo"));
            var.setSyncTime(DateUtil.date());
            var.setOpTime(DateUtil.parseDateTime(JSONUtil.parseObj(next).getStr("createTime")));
            var.setStatus(YeeSettleStatusEnum.getByYeeName(JSONUtil.parseObj(next).getStr("status")).getCode());
            varList.add(var);
        }
        //存盘
        saveOrUpdateSettle(varList);
    }

    /**
     * 保存或更新结算单
     *
     * @param varList
     */
    private void saveOrUpdateSettle(List<YeeSettleSyncRecordDO> varList) {
        if (CollUtil.isEmpty(varList)) {
            return;
        }
        List<String> summaryNoList = varList.stream().map(YeeSettleSyncRecordDO::getSummaryNo).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<YeeSettleSyncRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(YeeSettleSyncRecordDO::getSummaryNo, summaryNoList);
        Map<String, Long> oldSettleMap = list(wrapper).stream().collect(Collectors.toMap(YeeSettleSyncRecordDO::getSummaryNo, YeeSettleSyncRecordDO::getId));
        varList.stream().forEach(e -> {
            Long id = oldSettleMap.get(e.getSummaryNo());
            if (ObjectUtil.isNotNull(id) && ObjectUtil.notEqual(id, 0L)) {
                e.setId(id);
            }
        });
        boolean isSuccess = saveOrUpdateBatch(varList);
        if (!isSuccess) {
            log.error("更新或保存易宝子账户结算记录失败，参数={}", varList);
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    /**
     * 将获取到的response转换成json格式
     *
     * @param response
     * @return
     */
    public static Map<String, String> parseResponse(YopResponse response) {

        Map<String, String> jsonMap;
        jsonMap = JSON.parseObject(response.getStringResult(), new TypeReference<TreeMap<String, String>>() {
        });
        if (ObjectUtil.isNull(jsonMap)) {
            log.error("向易宝发起请求返回的response解析后为空,参数={}，异常信息={}", JSON.toJSONString(response), JSON.toJSON(response.getError()));
            throw new BusinessException(YeeSettleErrCode.YEE_RESPONSE_IS_NULL);
        }
        return jsonMap;
    }

}
