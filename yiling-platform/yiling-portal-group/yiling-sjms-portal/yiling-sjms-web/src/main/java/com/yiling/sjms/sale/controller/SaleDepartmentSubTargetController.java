package com.yiling.sjms.sale.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetApi;
import com.yiling.dataflow.sale.api.SaleDepartmentTargetApi;
import com.yiling.dataflow.sale.api.SaleTargetApi;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.SaleTargetDTO;
import com.yiling.dataflow.sale.dto.request.GenerateCrmConfigMouldRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveBathSaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentMonthDataEnum;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentSubTargetTypeEnum;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.dataflow.sale.enums.SaleDepartmentSubTargetErrorCode;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.sale.form.GenerateMouldForm;
import com.yiling.sjms.sale.form.QuerySaleDepartmentSubTargetForm;
import com.yiling.sjms.sale.form.SaleDepartmentSubTargetCurCalForm;
import com.yiling.sjms.sale.form.SaleDepartmentSubTargetLastCalForm;
import com.yiling.sjms.sale.form.SaleDepartmentTargetCurCalForm;
import com.yiling.sjms.sale.form.SaleDepartmentTargetLastCalForm;
import com.yiling.sjms.sale.form.SaleTargetCurCalForm;
import com.yiling.sjms.sale.form.SaleTargetDepartmentCurCalForm;
import com.yiling.sjms.sale.form.SaleTargetDepartmentLastCalForm;
import com.yiling.sjms.sale.form.SaleTargetLastCalForm;
import com.yiling.sjms.sale.form.SaveBatchSaleDepartmentSubTargetForm;
import com.yiling.sjms.sale.vo.QuerySaleDepartmentSubTargetVO;
import com.yiling.sjms.sale.vo.QuerySaleDepartmentTargetConfigVO;
import com.yiling.sjms.sale.vo.QuerySaleDepartmentTargetVO;
import com.yiling.sjms.sale.vo.SaleDepartmentSubTargetCalVO;
import com.yiling.sjms.sale.vo.SaleDepartmentTargetCalVO;
import com.yiling.sjms.sale.vo.SaleTargetCalVO;
import com.yiling.sjms.sale.vo.SaleTargetDepartmentCalVO;
import com.yiling.user.esb.api.EsbBusinessOrganizationApi;
import com.yiling.user.esb.bo.SimpleEsbBzOrgBO;
import com.yiling.user.esb.enums.EsbBusinessOrganizationTagTypeEnum;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 销售指标基本信息
 */
@Slf4j
@RestController
@RequestMapping("/sale/depart/config")
@Api(tags = "销售指标部门配置")
public class SaleDepartmentSubTargetController extends BaseController {
    @DubboReference
    private SaleDepartmentSubTargetApi saleDepartmentSubTargetApi;
    @DubboReference
    private SaleDepartmentTargetApi saleDepartmentTargetApi;
    @DubboReference
    private EsbBusinessOrganizationApi esbBusinessOrganizationApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;
    @DubboReference
    private SaleTargetApi saleTargetApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    private final static   BigDecimal NUM100=new BigDecimal("100.00");
    @ApiOperation(value = "销售指标部门指标参数配置列表")
    @PostMapping("/list")
    public Result<QuerySaleDepartmentTargetConfigVO> list(@RequestBody @Valid QuerySaleDepartmentSubTargetForm form) {
        QuerySaleDepartmentSubTargetRequest request = new QuerySaleDepartmentSubTargetRequest();
        QuerySaleDepartmentTargetConfigVO vo = new QuerySaleDepartmentTargetConfigVO();
        PojoUtils.map(form, request);
        //检查是否有数据，初始化页面数据
        List<SaleDepartmentSubTargetDTO> departmentSubTargetDTOS = saleDepartmentSubTargetApi.listByParam(request);
        //初始化数据
        if (CollUtil.isEmpty(departmentSubTargetDTOS)) {
            departmentSubTargetDTOS = getInitSaleDePartSubObj(request);
        }
        //获取指标年份
        SaleTargetDTO saleTarget = saleTargetApi.getById(form.getSaleTargetId());
        //VO
        SaleDepartmentTargetDTO saleDepartmentTargetDTO = saleDepartmentTargetApi.listBySaleTargetIdAndDepartId(request.getSaleTargetId(), request.getDepartId());
        //PojoUtils.map(saleDepartmentTargetDTO, vo);
        vo.setTargetYear(saleTarget.getTargetYear());
        vo.setName(saleTarget.getName());
        vo.setTargetNo(saleTarget.getTargetNo());
        vo.setSaleTarget(saleDepartmentTargetDTO.getCurrentTarget());
        vo.setLastTargetSum(new BigDecimal(departmentSubTargetDTOS.stream().filter(m->m.getLastTarget()!=null).mapToDouble(m -> m.getLastTarget().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        vo.setLastTargetRatioSum(new BigDecimal(departmentSubTargetDTOS.stream().filter(m->m.getLastTargetRatio()!=null).mapToDouble(m -> m.getLastTargetRatio().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        vo.setCurrentTargetSum(new BigDecimal(departmentSubTargetDTOS.stream().filter(m->m.getCurrentTarget()!=null).mapToDouble(m -> m.getCurrentTarget().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        vo.setCurrentTargetRatioSum(new BigDecimal(departmentSubTargetDTOS.stream().filter(m->m.getCurrentTargetRatio()!=null).mapToDouble(m -> m.getCurrentTargetRatio().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        vo.setCurrentIncreaseSum(new BigDecimal(departmentSubTargetDTOS.stream().filter(m->m.getCurrentIncrease()!=null).mapToDouble(m -> m.getCurrentIncrease().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        vo.setList(PojoUtils.map(departmentSubTargetDTOS, QuerySaleDepartmentSubTargetVO.class));
        return Result.success(vo);
    }

    @ApiOperation(value = "添加销售部门指标")
    @PostMapping("/add")
    public Result<Long> add(@RequestBody @Validated SaveBatchSaleDepartmentSubTargetForm form, @CurrentUser CurrentSjmsUserInfo userInfo) {
        //验证金额
        boolean flag = checkSubmitAmount(form);
        SaveBathSaleDepartmentSubTargetRequest request = new SaveBathSaleDepartmentSubTargetRequest();
        PojoUtils.map(form, request);
        defaultAreaList(request);
        request.setOpUserId(userInfo.getCurrentUserId());
        //检查

     //   SaleDepartmentTargetDTO targetDTO = saleDepartmentTargetApi.listBySaleTargetIdAndDepartId(form.getSaleTargetId(), form.getDepartId());
        //添加
            //批量保存数据
            saleDepartmentSubTargetApi.saveBatch(request);

        //生成模版，更改状态
        GenerateMouldForm generateMouldForm=new GenerateMouldForm();
        PojoUtils.map(form,generateMouldForm);
        generateMould(generateMouldForm,userInfo);
        return Result.success();
    }
    @ApiOperation(value = "修改销售部门指标")
    @PostMapping("/edit")
    public Result<Long> edit(@RequestBody SaveBatchSaleDepartmentSubTargetForm form, @CurrentUser CurrentSjmsUserInfo userInfo) {
        //验证金额
        boolean flag = checkSubmitAmount(form);
        SaleDepartmentTargetDTO targetDTO = saleDepartmentTargetApi.listBySaleTargetIdAndDepartId(form.getSaleTargetId(), form.getDepartId());
        //添加
        if ((ObjectUtil.equal(targetDTO.getConfigStatus(), CrmSaleDepartmentTargetConfigStatusEnum.COPE_SPLIT.getCode())||
                ( ObjectUtil.equal(targetDTO.getConfigStatus(), CrmSaleDepartmentTargetConfigStatusEnum.WAIT_SPLIT.getCode()))) ) {
            //批量保存数据
            throw new BusinessException(SaleDepartmentSubTargetErrorCode.CONFIG_STATUS_ERR);
        }

        SaveBathSaleDepartmentSubTargetRequest request = new SaveBathSaleDepartmentSubTargetRequest();
        PojoUtils.map(form, request);
        defaultAreaList(request);
        saleDepartmentSubTargetApi.updateBatch(request);
        //生成模版，更改状态
        GenerateMouldForm generateMouldForm=new GenerateMouldForm();
        PojoUtils.map(form,generateMouldForm);
        generateMould(generateMouldForm,userInfo);
        return Result.success();
    }
    @ApiOperation(value = "上一年度金额预计算")
    @PostMapping("/last/cal")
    public Result<SaleDepartmentTargetCalVO> lastCalculation(@RequestBody @Valid SaleDepartmentTargetLastCalForm form) {
        if (form.getSaleTarget() == null||BigDecimal.ZERO.compareTo(form.getSaleTarget())>=0) {
            throw new  BusinessException(SaleDepartmentSubTargetErrorCode.DATA_EMPTY_ERR,"销售目标金额输入错误");
        }
        log.info("上一年度金额预计算:{}", form);
        LongSummaryStatistics lastTargetLongSummary = form.getList().stream().mapToLong(m -> m.getLastTarget().longValue()).summaryStatistics();
        // 100的big
        List<SaleDepartmentSubTargetCalVO> voList = new ArrayList<>();
        lastRadioCal(form.getList(),new BigDecimal(lastTargetLongSummary.getSum()==0?1:lastTargetLongSummary.getSum()),form.getSaleTarget(),voList);
        lastAndCurRadioCal(voList);
        adjustment(voList);
        log.info("{}",voList);
        return Result.success(buildCalResult(voList));
    }

    @ApiOperation(value = "金额预计算")
    @PostMapping("/cur/cal")
    public Result<SaleDepartmentTargetCalVO> curCalculation(@RequestBody @Valid SaleDepartmentTargetCurCalForm form) {
        log.info("Form:{}", form);
        List<SaleDepartmentSubTargetCalVO> voList = new ArrayList<>();
        curRadioCal(form.getList(),voList);
        lastAndCurRadioCal(voList);
        adjustment(voList);
        log.info("{}",voList);
        return Result.success(buildCalResult(voList));
    }

    /**
     * 生成指标配置模版详情
     * @param form
     * @param userInfo
     * @return
     */
    public Result<Boolean> generateMould(GenerateMouldForm form,  CurrentSjmsUserInfo userInfo) {
        SaleDepartmentTargetDTO targetDTO = saleDepartmentTargetApi.listBySaleTargetIdAndDepartId(form.getSaleTargetId(), form.getDepartId());
        if (ObjectUtil.isNull(targetDTO)) {
            return Result.failed("部门目标不存在");
        }
        if (ObjectUtil.notEqual(targetDTO.getConfigStatus(), CrmSaleDepartmentTargetConfigStatusEnum.WAIT_SPLIT.getCode())) {
            return Result.failed("部门目标，请稍后再来");
        }
        if (ObjectUtil.equal(targetDTO.getConfigStatus(), CrmSaleDepartmentTargetConfigStatusEnum.COPE_SPLIT.getCode())) {
            return Result.failed("部门目标正在配置，请稍后再来");
        }
        QuerySaleDepartmentSubTargetRequest checkRequest=new QuerySaleDepartmentSubTargetRequest();
        checkRequest.setSaleTargetId(targetDTO.getSaleTargetId());
        checkRequest.setDepartId(targetDTO.getDepartId());

        List<SaleDepartmentSubTargetDTO> targetSubList = saleDepartmentSubTargetApi.listByParam(checkRequest);
        //排除目标为0的数据
        List<SaleDepartmentSubTargetDTO> filterTargetSubList = targetSubList.stream().filter(e -> ObjectUtil.notEqual(e.getCurrentTarget(), BigDecimal.ZERO)).collect(Collectors.toList());
        Map<Integer, List<SaleDepartmentSubTargetDTO>> subMap = filterTargetSubList.stream().collect(Collectors.groupingBy(SaleDepartmentSubTargetDTO::getType));

        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode())){
            return Result.failed("省区不存在");
        }
        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.MONTH.getCode())){
            return Result.failed("月份不存在");
        }
        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.GOODS.getCode())){
            return Result.failed("品种不存在，请稍后再来");
        }
        //月度值map
        List<SaleDepartmentSubTargetDTO> monthList = subMap.get(CrmSaleDepartmentSubTargetTypeEnum.MONTH.getCode());
        List<Long> distinctMonth = monthList.stream().map(SaleDepartmentSubTargetDTO::getRelId).distinct().collect(Collectors.toList());
        if (ObjectUtil.notEqual(distinctMonth.size(),monthList.size())){
            return Result.failed("指标月度值存在重复");
        }

        UpdateConfigStatusRequest request = PojoUtils.map(form, UpdateConfigStatusRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.COPE_SPLIT);
        Boolean isSuccess = saleDepartmentTargetApi.updateConfigStatus(request);
        GenerateCrmConfigMouldRequest msgRequest = PojoUtils.map(form, GenerateCrmConfigMouldRequest.class);
        if (!isSuccess) {
            log.error("更新部门指标的配置状态失败，参数={}", request);
            throw new ServiceException(ResultCode.FAILED);
        }
        //发送消息
        sendMq(Constants.TOPIC_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG, Constants.TAG_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG, JSON.toJSONString(msgRequest));

        return Result.success(isSuccess);
    }
    private List<SaleDepartmentSubTargetDTO> getInitSaleDePartSubObj(QuerySaleDepartmentSubTargetRequest request) {
        if (CrmSaleDepartmentSubTargetTypeEnum.MONTH ==( CrmSaleDepartmentSubTargetTypeEnum.getFromCode(request.getType()))) {
            return Arrays.stream(CrmSaleDepartmentMonthDataEnum.values()).map(monthDataEnum -> {
                return new SaleDepartmentSubTargetDTO().setType(request.getType())
                        .setDepartId(request.getDepartId()).setSaleTargetId(request.getSaleTargetId()).setRelId(monthDataEnum.getCode()).setRelName(monthDataEnum.getDesc());
            }).collect(Collectors.toList());
        }
        if (CrmSaleDepartmentSubTargetTypeEnum.GOODS ==( CrmSaleDepartmentSubTargetTypeEnum.getFromCode(request.getType()))) {
            List<SaleDepartmentSubTargetDTO> categorys = new ArrayList<>();
            //参数非必填项
            List<CrmGoodsCategoryDTO> finalStageCategory = crmGoodsCategoryApi.getFinalStageCategory(null);
            finalStageCategory.stream().forEach(m -> {
                categorys.add(new SaleDepartmentSubTargetDTO().setType(request.getType()).setDepartId(request.getDepartId()).setSaleTargetId(request.getSaleTargetId()).setRelId(m.getId()).setRelName(m.getName()));
            });
            return categorys;
        }
        if (CrmSaleDepartmentSubTargetTypeEnum.PROVINCE ==( CrmSaleDepartmentSubTargetTypeEnum.getFromCode(request.getType()))
                || CrmSaleDepartmentSubTargetTypeEnum.AREA ==( CrmSaleDepartmentSubTargetTypeEnum.getFromCode(request.getType()))) {
            //type转化 1省区：业务部门标签2类型 ,4区办:区办标签3
            List<SimpleEsbBzOrgBO> bzOrgListByTagType = esbBusinessOrganizationApi.getBzOrgListByOrgId(request.getDepartId(),EsbBusinessOrganizationTagTypeEnum.getByCode(1 == request.getType() ? 2 : 4 == request.getType() ? 3 : 0));
            List<SaleDepartmentSubTargetDTO> departmentTargets = new ArrayList<>();
            bzOrgListByTagType.stream().forEach(m -> {
                departmentTargets.add(new SaleDepartmentSubTargetDTO().setType(request.getType()).setDepartId(request.getDepartId()).setSaleTargetId(request.getSaleTargetId()).setRelId(m.getOrgId()).setRelName(m.getOrgName()));
            });
            return departmentTargets;
        }
        return ListUtil.empty();
    }

    /**
     * 验证提交金额是否正确
     *
     * @param form
     * @return
     */
    private boolean checkSubmitAmount(SaveBatchSaleDepartmentSubTargetForm form) {
        if (form.getSaleTarget() == null||BigDecimal.ZERO.compareTo(form.getSaleTarget())>=0) {
            throw new  BusinessException(SaleDepartmentSubTargetErrorCode.DATA_EMPTY_ERR,"销售目标金额输入错误");
        }
        if (CollUtil.isEmpty(form.getProvinceList()) || CollUtil.isEmpty(form.getMonthList()) || CollUtil.isEmpty(form.getGoodsList())) {
            throw new  BusinessException(SaleDepartmentSubTargetErrorCode.DATA_EMPTY_ERR,"数据不能为空");
        }
        //验证2022年金额是
        //验证月份
        LongSummaryStatistics curTargetSummary = form.getMonthList().stream().mapToLong(m -> m.getCurrentTarget().longValue()).summaryStatistics();
        log.info("销售目标金额和统计总金额:{}",form.getSaleTarget(),curTargetSummary.getSum());
        if(form.getSaleTarget().longValue()!=curTargetSummary.getSum()){
            throw new BusinessException(SaleDepartmentSubTargetErrorCode.AMOUNT_ERR,"销售目标金额和本年度销售目标金额比匹配");
        }
        return true;
    }
    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = sendPrepare(topic, topicTag, msg);

        mqMessageSendApi.send(mqMessageBO);

        return true;
    }
    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {

        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }

    public static void main(String[] args) {
        System.out.println(BigDecimal.ZERO.compareTo(new BigDecimal("-60")));
    }
    //计算2022年和本年度目标
    private void lastRadioCal(List<SaleDepartmentSubTargetLastCalForm> listData,BigDecimal lastTargetSumTotal,BigDecimal saleTarget,List<SaleDepartmentSubTargetCalVO> voList){
        for (int i = 0; i < listData.size(); i++) {
            SaleDepartmentSubTargetLastCalForm m = listData.get(i);
            SaleDepartmentSubTargetCalVO vo = new SaleDepartmentSubTargetCalVO();
            vo.setId(m.getId());
            vo.setSaleTargetId(m.getSaleTargetId());
            vo.setDepartId(m.getDepartId());
            vo.setType(m.getType());
            vo.setRelId(m.getRelId());
            vo.setRelName(m.getRelName());
            buildZero(vo);
            BigDecimal lastRedio = m.getLastTarget().divide(lastTargetSumTotal, 4, BigDecimal.ROUND_HALF_UP);
            vo.setLastTargetRatio(lastRedio.multiply(NUM100).setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setLastTarget(m.getLastTarget());
            // 本年度销售目标=输入的销售目标*上一年的销售占比*100
            BigDecimal currentTarget = saleTarget.multiply(vo.getLastTargetRatio()).divide(NUM100, 0, BigDecimal.ROUND_HALF_UP);
            vo.setCurrentTarget(currentTarget);
            voList.add(vo);
            log.info("第" + i + "行 lastTarget=" + m.getLastTarget().toString() + ",lastRadio=" + m.getLastTargetRatio() + ",curTarget=" + vo.getCurrentTargetRatio()
                    + ",curRedio=" + vo.getCurrentTargetRatio() + ",curIncrease=" + m.getCurrentIncrease());
        }
    }
    /**
     * 计算2023年输入值计算
     */
    private void curRadioCal(List<SaleDepartmentSubTargetCurCalForm> listData,List<SaleDepartmentSubTargetCalVO> voList){
        for (int i = 0; i < listData.size(); i++) {
            SaleDepartmentSubTargetCurCalForm m = listData.get(i);
            SaleDepartmentSubTargetCalVO vo = new SaleDepartmentSubTargetCalVO();
            vo.setId(m.getId());
            vo.setSaleTargetId(m.getSaleTargetId());
            vo.setDepartId(m.getDepartId());
            vo.setType(m.getType());
            vo.setRelId(m.getRelId());
            vo.setRelName(m.getRelName());
            buildZero(vo);
            vo.setLastTarget(m.getLastTarget().setScale(0,BigDecimal.ROUND_HALF_UP));
            vo.setLastTargetRatio(m.getLastTargetRatio().setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setCurrentTarget(m.getCurrentTarget().setScale(0,BigDecimal.ROUND_HALF_UP));
            voList.add(vo);
        }
    }

    /**
     *
     * @param voList
     */
    private void lastAndCurRadioCal(List<SaleDepartmentSubTargetCalVO> voList){
        LongSummaryStatistics currentTargetLongSummary = voList.stream().mapToLong(m -> m.getCurrentTarget().longValue()).summaryStatistics();
        BigDecimal bigCurSum=new BigDecimal(currentTargetLongSummary.getSum()==0L?1:currentTargetLongSummary.getSum());
        for (int i = 0; i < voList.size(); i++) {
            SaleDepartmentSubTargetCalVO vo = voList.get(i);
            //本年度销售占比
            BigDecimal currentRedio = vo.getCurrentTarget().divide(bigCurSum, 4, BigDecimal.ROUND_HALF_UP);
            vo.setCurrentTargetRatio(currentRedio.multiply(NUM100).setScale(2, BigDecimal.ROUND_HALF_UP));
            //增长（元）
            vo.setCurrentIncrease(vo.getCurrentTarget().subtract(vo.getLastTarget()).setScale(0,BigDecimal.ROUND_HALF_UP));
        }
    }
    public SaleDepartmentSubTargetCalVO buildZero(SaleDepartmentSubTargetCalVO vo){
        vo.setLastTarget(BigDecimal.ZERO);
        vo.setLastTargetRatio(BigDecimal.ZERO);
        vo.setCurrentTarget(BigDecimal.ZERO);
        vo.setCurrentTargetRatio(BigDecimal.ZERO);
        vo.setCurrentIncrease(BigDecimal.ZERO);
        return vo;
    }
    private  SaleDepartmentTargetCalVO buildCalResult( List<SaleDepartmentSubTargetCalVO> voList){
        //设置合计金额
        SaleDepartmentTargetCalVO calVO = new SaleDepartmentTargetCalVO();
        calVO.setList(voList);
        calVO.setLastTargetSum(new BigDecimal(voList.stream().mapToDouble(m -> m.getLastTarget().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        calVO.setLastTargetRatioSum(new BigDecimal(voList.stream().mapToDouble(m -> m.getLastTargetRatio().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        calVO.setCurrentTargetSum(new BigDecimal(voList.stream().mapToDouble(m -> m.getCurrentTarget().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        calVO.setCurrentTargetRatioSum(new BigDecimal(voList.stream().mapToDouble(m -> m.getCurrentTargetRatio().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        calVO.setCurrentIncreaseSum(new BigDecimal(voList.stream().mapToDouble(m -> m.getCurrentIncrease().doubleValue()).summaryStatistics().getSum()).setScale(0,BigDecimal.ROUND_HALF_UP));
        return calVO;
    }
    /**
     * 调价
     * @param voList
     */
    private void adjustment( List<SaleDepartmentSubTargetCalVO> voList){
        BigDecimal sumTmpLastRadio= new BigDecimal(voList.stream().mapToDouble(m -> m.getLastTargetRatio().doubleValue()).summaryStatistics().getSum());
        if(NUM100.compareTo(sumTmpLastRadio.setScale(2,BigDecimal.ROUND_HALF_UP))!=0&& BigDecimal.ZERO.compareTo(sumTmpLastRadio)!=0){
            BigDecimal subValue = NUM100.subtract(sumTmpLastRadio);
            SaleDepartmentSubTargetCalVO tmpVo = voList.stream().reduce((first, second) -> second).orElse(new SaleDepartmentSubTargetCalVO());
            tmpVo.setLastTargetRatio(tmpVo.getLastTargetRatio().add(subValue).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        BigDecimal sumTmpCurRadio= new BigDecimal(voList.stream().mapToDouble(m -> m.getCurrentTargetRatio().doubleValue()).summaryStatistics().getSum());
        if(NUM100.compareTo(sumTmpCurRadio.setScale(2,BigDecimal.ROUND_HALF_UP))!=0&& BigDecimal.ZERO.compareTo(sumTmpCurRadio)!=0){
            BigDecimal subValue = NUM100.subtract(sumTmpCurRadio);
            SaleDepartmentSubTargetCalVO tmpVo = voList.stream().reduce((first, second) -> second).orElse(new SaleDepartmentSubTargetCalVO());
            tmpVo.setCurrentTargetRatio(tmpVo.getCurrentTargetRatio().add(subValue).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
    }
    private void defaultAreaList(SaveBathSaleDepartmentSubTargetRequest request){
        QuerySaleDepartmentSubTargetRequest areaRequest=new QuerySaleDepartmentSubTargetRequest();
        areaRequest.setDepartId(request.getDepartId());
        areaRequest.setSaleTargetId(request.getSaleTargetId());
        areaRequest.setType(CrmSaleDepartmentSubTargetTypeEnum.AREA.getCode());
        List areaList=getInitSaleDePartSubObj(areaRequest);
        request.setAreaList(areaList);
    }
}
