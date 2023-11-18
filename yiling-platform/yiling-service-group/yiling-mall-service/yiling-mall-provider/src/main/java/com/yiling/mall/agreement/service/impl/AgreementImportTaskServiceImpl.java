package com.yiling.mall.agreement.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsPatentEnum;
import com.yiling.mall.agreement.dao.AgreementImportTaskMapper;
import com.yiling.mall.agreement.dto.request.ImportAgreementConditionRequest;
import com.yiling.mall.agreement.dto.request.ImportAgreementGoodsRequest;
import com.yiling.mall.agreement.dto.request.ImportTempAgreementRequest;
import com.yiling.mall.agreement.entity.AgreementImportTaskDO;
import com.yiling.mall.agreement.enums.AgreeImportStatusEnum;
import com.yiling.mall.agreement.enums.AgreementErrorCode;
import com.yiling.mall.agreement.service.AgreementImportGoodsService;
import com.yiling.mall.agreement.service.AgreementImportTaskService;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.CloseAgreementRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementCountRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementGoodsRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementRequest;
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.enums.AgreementStatusEnum;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议导入任务表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-18
 */
@Slf4j
@Service
public class AgreementImportTaskServiceImpl extends BaseServiceImpl<AgreementImportTaskMapper, AgreementImportTaskDO> implements AgreementImportTaskService {

    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    AgreementApi agreementApi;
    @DubboReference
    CustomerApi customerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;
    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @Autowired
    AgreementImportGoodsService importGoodsService;

    /**
     * 数据字段渠道类型,typeId
     */
    private final Long CHANNEL_TYPE_ID = 1L;


    @GlobalTransactional
    @Override
    public void generateAgree(String taskCode) {
        //根据任务code查询导入任务
        List<AgreementImportTaskDO> taskList = queryAgreeImportListByTaskCode(taskCode, AgreeImportStatusEnum.UN_EXECUTION);
        if (CollUtil.isEmpty(taskList)) {
            log.error("协议导入列表为空，taskCode={}", taskCode);
            return;
        }
        //丙方map
        Map<Long, List<AgreementImportTaskDO>> thirdMap = taskList.stream().collect(Collectors.groupingBy(AgreementImportTaskDO::getThirdEid));
        //        // 查询在线支付渠道类型
        //        Map<String, String> channelMap = dictTypeApi.getEnabledByTypeIdList(CHANNEL_TYPE_ID).stream().collect(Collectors.toMap(DictDataBO::getValue, DictDataBO::getLabel));

        thirdMap.forEach((thirdId, tasks) -> {
            Map<Long, List<AgreementImportTaskDO>> mainAgreeMap = tasks.stream().collect(Collectors.groupingBy(AgreementImportTaskDO::getEid));

            //主协议
            mainAgreeMap.forEach((eid, mainList) -> {
                //校验eid
                EnterpriseDTO subject = enterpriseApi.getById(eid);

                //创建主协议eid-secondId
                //查询乙方是否为客户
                EnterpriseDTO third = enterpriseApi.getById(thirdId);

                if (ObjectUtil.isNull(subject)){
                    mainList.stream().forEach(e -> {
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("主体不存在");
                    });
                    return;
                }
                if (ObjectUtil.isNull(third)){
                    mainList.stream().forEach(e -> {
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("丙方不存在");
                    });
                    return;
                }
                if (ObjectUtil.equal(third.getChannelId(),0L)){
                    mainList.stream().forEach(e -> {
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("丙方没有维护渠道类型");
                    });
                    return;
                }
                //如果不为以岭客户
                if (!isCustomer(third.getName())) {
                    mainList.stream().forEach(e -> {
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("丙方不是以岭客户");
                    });
                    return;
                }
                AtomicReference<Boolean> hasFail= new AtomicReference<>(false);

                Long mainAgreeId;
                try {
                    mainAgreeId = saveYear(eid, subject.getName(), thirdId, third.getName(), EnterpriseChannelEnum.getByCode(third.getChannelId()).getName(), getAgreeName(eid)+"年度协议", getAgreeName(eid)+"年度协议", DateUtil.beginOfYear(new Date()), DateUtil.endOfYear(new Date()), importGoodsService.queryGoodsListByTaskCode(taskCode, eid));
                } catch (BusinessException e) {
                    mainList.stream().forEach(o -> {
                        o.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        o.setErrMsg(e.getMessage());
                    });
                    return;
                }

                //                Map<Long, List<AgreementImportTaskDO>> collect = mainList.stream().collect(Collectors.groupingBy(AgreementImportTaskDO::getSecondEid));

                mainList.stream().forEach(e -> {
                    //生成附加协议

                    //查询三方弹窗
                    //如果三方没查到关系
                    if (!isThird(thirdId, e.getSecondEid())) {
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("没有查到乙方");
                        return;
                    }
                    EnterpriseDTO second = enterpriseApi.getById(e.getSecondEid());
                    if (ObjectUtil.isNull(second)){
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("乙方不存在");
                        return;
                    }
                    if (ObjectUtil.equal(second.getChannelId(),0L)){
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg("乙方没维护渠道类型");
                        return;
                    }
                    ImportTempAgreementRequest tempAgreementRequest = initTempRequest(e, getAgreeName(eid)+"补充协议.", DateUtil.beginOfYear(new Date()), DateUtil.endOfYear(new Date()), getAgreeName(eid)+"补充协议", mainAgreeId, EnterpriseChannelEnum.getByCode(second.getChannelId()).getName(), second.getId(), second.getName(), importGoodsService.queryGoodsListByTaskCode(taskCode, eid));

                    try {
                        //补充协议存盘
                        saveTemp(tempAgreementRequest);
                    } catch (BusinessException ex) {
                        e.setStatus(AgreeImportStatusEnum.FAIL.getCode());
                        e.setErrMsg(ex.getMessage());
                        if (!hasFail.get()){
                            hasFail.set(true);
                        }
                    }
                    e.setStatus(AgreeImportStatusEnum.SUCCESS.getCode());
                });
                if (hasFail.get()){
                    log.error("协议导入失败，请检查参数={}", JSON.toJSONString(mainList));
                    //全局回滚
                    throw new ServiceException(ResultCode.FAILED);
//                    agreementClose(mainAgreeId);
                }

            });
            boolean isSuccess = updateBatchById(tasks);
            if (!isSuccess){
                log.error("更新导入协议状态失败，参数={}",tasks);
                throw new ServiceException(ResultCode.FAILED);
            }

        });
    }

    @Override
    public List<AgreementImportTaskDO> queryAgreeImportListByTaskCode(String code, AgreeImportStatusEnum statusEnum) {
        if (StrUtil.isBlank(code) || ObjectUtil.isNull(statusEnum)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<AgreementImportTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementImportTaskDO::getTaskCode, code);
        wrapper.eq(AgreementImportTaskDO::getStatus, statusEnum.getCode());
        List<AgreementImportTaskDO> list = list(wrapper);
        return list;
    }

    /**
     * 保存年度协议
     *
     * @param eid
     * @param eName
     * @param secondEid
     * @param secondName
     * @param secondChannelName
     * @param name
     * @param content
     * @param startTime
     * @param endTime
     * @param goodsList
     * @return
     */
    public Long saveYear(Long eid, String eName, Long secondEid, String secondName, String secondChannelName, String name, String content, Date startTime, Date endTime, List<SaveAgreementGoodsRequest> goodsList) {

        //判断商品是否在主体里面
        List<Long> goodsIds = goodsApi.getGoodsIdsByEid(eid);
        for (SaveAgreementGoodsRequest var : goodsList) {
            if (!goodsIds.contains(var.getGoodsId())) {
                throw new BusinessException(AgreementErrorCode.GOODS_NOT_FIND);
            }
        }

        //同一销售分公司（北京以岭、石家庄以岭、以岭健康城）、同一采购企业、同一时间维度、只能存在一份
        QueryAgreementCountRequest request = new QueryAgreementCountRequest();
        request.setEid(eid);
        request.setSecondEid(secondEid);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        Integer count = agreementApi.countAgreementByTimeAndOther(request);
        if (count >= 1) {
            throw new BusinessException(AgreementErrorCode.AGREEMENT_EXIST);
        }

        //添加商品标准库信息
        SaveAgreementRequest agreementRequest = new SaveAgreementRequest();
        agreementRequest.setEid(eid);
        agreementRequest.setEname(eName);
        agreementRequest.setSecondEid(secondEid);
        agreementRequest.setSecondName(secondName);
        agreementRequest.setSecondChannelName(secondChannelName);
        agreementRequest.setName(name);
        agreementRequest.setContent(content);
        agreementRequest.setStartTime(startTime);
        agreementRequest.setEndTime(endTime);
        agreementRequest.setAgreementGoodsList(goodsList);

        if (CollUtil.isNotEmpty(goodsList)) {
            List<Long> goodsIdList = goodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
            Map<Long, StandardGoodsBasicDTO> map = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            goodsList.forEach(e -> {
                StandardGoodsBasicDTO standardGoodsBasicDTO = map.get(e.getGoodsId());
                e.setStandardGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
                e.setStandardLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
            });
        }

        agreementRequest.setCategory(AgreementCategoryEnum.YEAR_AGREEMENT.getCode());
        agreementRequest.setMode(AgreementModeEnum.SECOND_AGREEMENTS.getCode());
        agreementRequest.setStatus(AgreementStatusEnum.OPEN.getCode());
        agreementRequest.setOpUserId(1L);
        return agreementApi.saveForImport(agreementRequest);
    }

    /**
     * 判断是否为客户
     *
     * @return
     */
    public Boolean isCustomer(String name) {

        QueryCustomerPageListRequest request = new QueryCustomerPageListRequest();
        request.setChannelIds(ListUtil.toList(EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode(), EnterpriseChannelEnum.LEVEL_1.getCode(), EnterpriseChannelEnum.LEVEL_2.getCode(), EnterpriseChannelEnum.KA.getCode(), EnterpriseChannelEnum.Z2P1.getCode()));
        request.setEids(Collections.singletonList(Constants.YILING_EID));
        request.setUseLine(EnterpriseCustomerLineEnum.POP.getCode());
        request.setName(name);

        Page<EnterpriseCustomerDTO> pageDTO = customerApi.pageList(request);


        return CollUtil.isNotEmpty(pageDTO.getRecords());
    }

    public Boolean isThird(Long buyerEid, Long sellerEid) {
        QueryPurchaseRelationPageListRequest request = new QueryPurchaseRelationPageListRequest();
        request.setSellerEid(sellerEid);
        request.setBuyerEid(buyerEid);

        Page<EnterpriseDTO> pageList = enterprisePurchaseRelationApi.getSellerEnterprisePageList(request);
        return CollUtil.isNotEmpty(pageList.getRecords());
    }

    public void saveTemp(ImportTempAgreementRequest form) {

        //获取主体协议
        AgreementDTO agreementDetailsInfo = agreementApi.getAgreementDetailsInfo(form.getParentId());


        //验证商品是否在主协议下
        List<Long> goodsIdList = form.getAgreementGoodsList().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getAgreementGoodsAgreementIdByList(form.getParentId());
        List<Long> agreementGoodsIdList = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        for (Long goodsId : goodsIdList) {
            if (!agreementGoodsIdList.contains(goodsId)) {
                log.error("补充协议商品不再主协议内,goodsId={}", goodsId);
                throw new BusinessException(AgreementErrorCode.AGREEMENT_GOODS_NOT_FIND);
            }
        }

        //如果新增补充协议是专利和非专利需要
        if (form.getIsPatent().equals(GoodsPatentEnum.UN_PATENT.getCode()) || form.getIsPatent().equals(GoodsPatentEnum.PATENT.getCode())) {
            List<Long> goodsIds = agreementGoodsDTOList.stream().filter(e -> e.getIsPatent().equals(form.getIsPatent())).map(m -> m.getGoodsId()).collect(Collectors.toList());
            for (Long goodsId : goodsIdList) {
                if (!goodsIds.contains(goodsId)) {
                    log.error("补充协议商品类型不匹配,goodsId={}", goodsId);
                    throw new BusinessException(AgreementErrorCode.AGREEMENT_GOODS_TYPE_INVALID);
                }
            }
        }

        //如果有第三方企业，就把第三方赋值到乙方
        SaveAgreementRequest agreementRequest = PojoUtils.map(form, SaveAgreementRequest.class);

        //添加商品标准库信息
        List<SaveAgreementGoodsRequest> goodsList = agreementRequest.getAgreementGoodsList();
        if (CollUtil.isNotEmpty(goodsList)) {
            List<Long> goodsIds = goodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
            Map<Long, StandardGoodsBasicDTO> map = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            goodsList.forEach(e -> {
                StandardGoodsBasicDTO standardGoodsBasicDTO = map.get(e.getGoodsId());
                e.setStandardGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
                e.setStandardLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
            });
        }

        //判断传入的值为直连
        if (form.getType() != null && form.getType() == 2) {
            agreementRequest.setConditionRule(0);
        }

        agreementRequest.setEid(agreementDetailsInfo.getEid());
        agreementRequest.setEname(agreementDetailsInfo.getEname());

        agreementRequest.setCategory(AgreementCategoryEnum.TEMP_AGREEMENT.getCode());
        //三方协议
        if (form.getThirdEid() != null && form.getThirdEid() != 0) {
            agreementRequest.setMode(AgreementModeEnum.THIRD_AGREEMENTS.getCode());
            agreementRequest.setSecondEid(form.getThirdEid());
            agreementRequest.setSecondName(form.getThirdName());
            agreementRequest.setSecondChannelName(form.getThirdChannelName());
            agreementRequest.setThirdEid(agreementDetailsInfo.getSecondEid());
            agreementRequest.setThirdName(agreementDetailsInfo.getSecondName());
            agreementRequest.setThirdChannelName(agreementDetailsInfo.getSecondChannelName());
        } else {
            //双方协议
            agreementRequest.setMode(AgreementModeEnum.SECOND_AGREEMENTS.getCode());
            agreementRequest.setSecondEid(agreementDetailsInfo.getEid());
            agreementRequest.setSecondName(agreementDetailsInfo.getEname());
            agreementRequest.setSecondChannelName(EnterpriseChannelEnum.INDUSTRY.getName());
            agreementRequest.setThirdEid(agreementDetailsInfo.getSecondEid());
            agreementRequest.setThirdName(agreementDetailsInfo.getSecondName());
            agreementRequest.setThirdChannelName(agreementDetailsInfo.getSecondChannelName());
        }
        agreementRequest.setOpUserId(1L);
        Boolean isSuccess = agreementApi.save(agreementRequest);
        if (!isSuccess) {
            log.error("导入协议时，保存补充协议失败");
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    public ImportTempAgreementRequest initTempRequest(AgreementImportTaskDO request, String content, Date startTime, Date endTime, String name, Long parentId, String thirdChannelName, Long thirdEid, String thirdName, List<SaveAgreementGoodsRequest> goodsList) {
        ImportTempAgreementRequest result = new ImportTempAgreementRequest();
        result.setChildType(1);
        result.setConditionRule(1);
        result.setContent(content);
        result.setEndTime(endTime);
        result.setIsPatent(0);
        result.setMode(2);
        result.setName(name);
        result.setParentId(parentId);
        result.setRebateType(1);
        result.setRebateCycle(1);
        result.setRestitutionType(0);
        result.setRestitutionTypeValues(ListUtil.toList());
        result.setStartTime(startTime);
        result.setThirdChannelName(thirdChannelName);
        result.setThirdEid(thirdEid);
        result.setThirdName(thirdName);
        result.setType(1);
        ImportAgreementConditionRequest conditionRequest = new ImportAgreementConditionRequest();
        conditionRequest.setAmount(BigDecimal.ZERO);
        conditionRequest.setBackAmountType(0);
        conditionRequest.setBackAmountTypeValues(new ArrayList<>());
        conditionRequest.setPayType(0);
        conditionRequest.setPayTypeValues(new ArrayList<>());
        conditionRequest.setPolicyType(1);
        conditionRequest.setPolicyValue(BigDecimal.ZERO);
        conditionRequest.setTotalAmount(BigDecimal.ZERO);

        result.setAgreementConditionList(ListUtil.toList(conditionRequest));
        result.setAgreementGoodsList(PojoUtils.map(goodsList, ImportAgreementGoodsRequest.class));

        return result;
    }

    /**
     * 停用协议
     *
     * @param agreementId
     */
    public void agreementClose(Long agreementId) {
        CloseAgreementRequest request = new CloseAgreementRequest();
        request.setAgreementId(agreementId);
        request.setOpType(2);
        request.setOpUserId(1L);
        Boolean isClose = agreementApi.agreementClose(request);
        if (!isClose){
            log.error("停用协议失败，参数={}",agreementId);
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    private String getAgreeName(Long eid){
        if (ObjectUtil.equal(eid,2L)){
            return "北京以岭";
        }
        if (ObjectUtil.equal(eid,3L)){
            return "石家庄以岭";
        }
        if (ObjectUtil.equal(eid,4L)){
            return "河北大运河";
        }
        return "";
    }


    public void importAgree() {
        //查询乙方是否为客户
        //创建年度协议
        //创建补充协议
        //查询三方弹窗
        //补充协议存盘
        return;
    }

}
