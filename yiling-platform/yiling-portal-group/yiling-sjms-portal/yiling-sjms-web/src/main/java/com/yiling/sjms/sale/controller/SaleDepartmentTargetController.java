package com.yiling.sjms.sale.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.no.api.NoApi;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetApi;
import com.yiling.dataflow.sale.api.SaleDepartmentTargetApi;
import com.yiling.dataflow.sale.api.SaleTargetApi;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.SaleTargetDTO;
import com.yiling.dataflow.sale.dto.request.GenerateCrmConfigMouldRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetCheckRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetPageListRequest;
import com.yiling.dataflow.sale.dto.request.RemoveSaleTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveSaleTargetRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentSubTargetTypeEnum;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.dataflow.sale.enums.DeptTargetErrorCode;
import com.yiling.dataflow.sale.enums.SaleDepartmentSubTargetErrorCode;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.sale.enums.SaleTargetNoEnum;
import com.yiling.sjms.sale.form.GenerateMouldForm;
import com.yiling.sjms.sale.form.QuerySaleTargetCheckForm;
import com.yiling.sjms.sale.form.SaleTargetCurCalForm;
import com.yiling.sjms.sale.form.SaleTargetDepartmentCurCalForm;
import com.yiling.sjms.sale.form.SaleTargetDepartmentLastCalForm;
import com.yiling.sjms.sale.form.SaleTargetForm;
import com.yiling.sjms.sale.form.SaleTargetLastCalForm;
import com.yiling.sjms.sale.form.SaveSaleTargetForm;
import com.yiling.sjms.sale.vo.QuerySaleDepartmentTargetVO;
import com.yiling.sjms.sale.vo.QuerySaleTargetVO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/sale/depart")
@Api(tags = "销售指标管理")
public class SaleDepartmentTargetController extends BaseController {
    @DubboReference
    private SaleTargetApi saleTargetApi;
    @DubboReference
    private SaleDepartmentTargetApi saleDepartmentTargetApi;
    @DubboReference
    private SaleDepartmentSubTargetApi saleDepartmentSubTargetApi;

    @DubboReference
    private NoApi noApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private EsbBusinessOrganizationApi esbBusinessOrganizationApi;

    private final static   BigDecimal NUM100=new BigDecimal("100.00");
    @ApiOperation(value = "销售指标管理列表")
    @PostMapping("/list")
    public Result<Page<QuerySaleTargetVO>> queryPageList(@RequestBody QuerySaleTargetPageListRequest form) {
        QuerySaleTargetPageListRequest request = new QuerySaleTargetPageListRequest();
        PojoUtils.map(form, request);
        Page<SaleTargetDTO> pageDto = saleTargetApi.queryPageList(request);
        if (CollUtil.isEmpty(pageDto.getRecords())) {
            return Result.success(new Page<>());
        }
        Page<QuerySaleTargetVO> page = PojoUtils.map(pageDto, QuerySaleTargetVO.class);
        page.getRecords().forEach(m -> {
            List<SaleDepartmentTargetDTO> saleDepartmentTargets = saleDepartmentTargetApi.listBySaleTargetId(m.getId());
            m.setDepartmentTargets(PojoUtils.map(saleDepartmentTargets, QuerySaleDepartmentTargetVO.class));
        });
        return Result.success(page);
    }

    @ApiOperation(value = "添加销售指标")
    @PostMapping("/add")
    public Result<Long> add(@RequestBody @Validated SaveSaleTargetForm form, @CurrentUser CurrentSjmsUserInfo userInfo) {
        if (BigDecimal.ZERO.compareTo(form.getSaleAmount()) >=0) {
            return Result.failed(101, "部门销售目标合计值不能为0");
        }
        QuerySaleTargetCheckRequest requestCheck = new QuerySaleTargetCheckRequest();
        PojoUtils.map(form, requestCheck);
        int count = saleTargetApi.countByName(requestCheck);
        if(count>0){
            return Result.failed(101, "指标名称重复");
        }
        LongSummaryStatistics curSum = form.getDepartmentTargets().stream().mapToLong(m -> m.getCurrentTarget().longValue()).summaryStatistics();
        log.info("保存提交的销售目标:{},部门合计销售目标:{}", form.getSaleAmount().longValue(), curSum.getSum());
        if (curSum.getSum() != form.getSaleAmount().longValue()) {
            return Result.failed(101, "部门销售目标合计值与销售目标值不一致，请调整部门销售目标");
        }
        String code = noApi.gen(SaleTargetNoEnum.sale_target_no);
        log.info("指标编号:{}", code);
        SaveSaleTargetRequest request = new SaveSaleTargetRequest();
        PojoUtils.map(form,request);
        request.setTargetNo(code);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long saleTargetId = saleTargetApi.save(request);
        return Result.success(saleTargetId);
    }

    @ApiOperation(value = "修改销售指标")
    @PostMapping("/edit")
    public Result<String> edit(@RequestBody SaleTargetForm form) {
        String code = noApi.gen(SaleTargetNoEnum.sale_target_no);
        return Result.success(code);
    }

    @ApiOperation(value = "查询销售指标")
    @GetMapping("/query")
    public Result<QuerySaleTargetVO> query(@RequestParam(value = "id", required = false) Long id) {
        QuerySaleTargetVO vo = new QuerySaleTargetVO();
        //初始化页面数据
        if (ObjectUtil.isEmpty(id)) {
            //TODO:获取业务部门为事业部的数据
            List<SimpleEsbBzOrgBO> bzOrgListByTagType = esbBusinessOrganizationApi.getBzOrgListByTagType(EsbBusinessOrganizationTagTypeEnum.BUSINESS_TAG);
            List<QuerySaleDepartmentTargetVO> departmentTargets = new ArrayList<>();
            bzOrgListByTagType.stream().forEach(m->{
                departmentTargets.add(new QuerySaleDepartmentTargetVO().setDepartId(m.getOrgId()).setDepartName(m.getOrgName()));
            });
            vo.setDepartmentTargets(departmentTargets);
            return Result.success(vo);
        }
        //根据ID查询目标值
        SaleTargetDTO saleTarget = saleTargetApi.getById(id);
        List<SaleDepartmentTargetDTO> saleDepartmentTargets = saleDepartmentTargetApi.listBySaleTargetId(id);
        PojoUtils.map(saleTarget, vo);
        vo.setDepartmentTargets(PojoUtils.map(saleDepartmentTargets, QuerySaleDepartmentTargetVO.class));
        return Result.success(vo);
    }

    @ApiOperation(value = "删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@RequestParam(value = "id", required = true) Long id,@CurrentUser CurrentSjmsUserInfo userInfo) {
        RemoveSaleTargetRequest request = new RemoveSaleTargetRequest();
        request.setId(id);
        request.setOpUserId(userInfo.getCurrentUserId());
        boolean count = saleTargetApi.removeById(request);
        return Result.success();
    }

    @ApiOperation(value = "上一年度金额预计算")
    @PostMapping("/last/cal")
    public Result<SaleTargetCalVO> lastCalculation(@RequestBody @Valid SaleTargetLastCalForm form) {
        if (form.getSaleTarget() == null||BigDecimal.ZERO.compareTo(form.getSaleTarget())>=0) {
            throw new  BusinessException(SaleDepartmentSubTargetErrorCode.DATA_EMPTY_ERR,"销售目标金额输入错误");
        }
        LongSummaryStatistics lastTargetLongSummary = form.getList().stream().mapToLong(m -> m.getLastTarget().longValue()).summaryStatistics();
//        if(lastTargetLongSummary.getSum()==0L){
//            throw new  BusinessException(SaleDepartmentSubTargetErrorCode.DATA_EMPTY_ERR,"上一年部门业绩合计不能为0");
//        }
        List<SaleTargetDepartmentCalVO> voList = new ArrayList<>();
        log.info("上一年度金额预计算:{}", form);

        //计算2022年目标和占比，2023年根据销售目标计算2023年部门目标
        lastRadioCal(form.getList(),new BigDecimal(lastTargetLongSummary.getSum()==0L?1:lastTargetLongSummary.getSum()),form.getSaleTarget(),voList);
        //计算2023年增长和占比
        lastAndCurRadioCal(voList);
        //调价
        adjustment(voList);
        log.info("{}",voList);
        return Result.success(buildCalResult(voList));
    }
    @ApiOperation(value = "金额预计算")
    @PostMapping("/cur/cal")
    public Result<SaleTargetCalVO> curCalculation(@RequestBody @Valid SaleTargetCurCalForm form) {
        log.info("Form:{}", form);
        List<SaleTargetDepartmentCurCalForm> listData = form.getList();
        LongSummaryStatistics currentTargetLongSummary = listData.stream().mapToLong(m -> m.getCurrentTarget().longValue()).summaryStatistics();
        //上一年的目标目标总额
        List<SaleTargetDepartmentCalVO> voList = new ArrayList<>();
        //初始化2022年和计算2023年列
        curRadioCal(listData,new BigDecimal(currentTargetLongSummary.getSum()==0L?1:currentTargetLongSummary.getSum()),voList);
        lastAndCurRadioCal(voList);
        //调价
        adjustment(voList);
        log.info("{}",voList);
        //设置合计金额
        return Result.success(buildCalResult(voList));
    }

    @ApiOperation(value = "检查指标名称是否重复")
    @PostMapping("/check/name")
    public Result<Boolean> check(@RequestBody QuerySaleTargetCheckForm form) {
        QuerySaleTargetCheckRequest request = new QuerySaleTargetCheckRequest();
        PojoUtils.map(form, request);
        int count = saleTargetApi.countByName(request);
        return Result.success(count == 0 ? true : false);
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal("100.00").compareTo(new BigDecimal("101.00")));
        SaleTargetCurCalForm form = new SaleTargetCurCalForm();
        List<SaleTargetDepartmentCurCalForm> list2 = new ArrayList<>();
        List<BigDecimal> lastBig = Arrays.asList(
                new BigDecimal("3003500000"),
                new BigDecimal("4700000000")
//                new BigDecimal("2143600000"),
//                new BigDecimal("16214300000"),
//                new BigDecimal("1602500000"),
//                new BigDecimal("700000000"),
//                new BigDecimal("46000000"),
//                new BigDecimal("700000000"),
//                new BigDecimal("100000000"),
//                new BigDecimal("3600000000"),
//                new BigDecimal("1000000000"),
//                new BigDecimal("8350000000")
        );
        List<BigDecimal> curBig = Arrays.asList(
                new BigDecimal("30035"),
                new BigDecimal("47000")
//                new BigDecimal("21436"),
//                new BigDecimal("162143"),
//                new BigDecimal("16025"),
//                new BigDecimal("7000"),
//                new BigDecimal("460"),
//                new BigDecimal("7000"),
//                new BigDecimal("1000"),
//                new BigDecimal("36000"),
//                new BigDecimal("10000"),
//                new BigDecimal("83500")
        );
        for (int i = 0; i < 2; i++) {
            SaleTargetDepartmentCurCalForm subForm = new SaleTargetDepartmentCurCalForm();
            subForm.setId(Long.valueOf(i));
            subForm.setLastTarget(lastBig.get(i));
            subForm.setCurrentTarget(curBig.get(i));
            list2.add(subForm);
        }
        form.setList(list2);
        System.out.println(JSON.toJSON(form));
    }


//    @ApiOperation(value = "生成模板")
//    @PostMapping("/generateMould")
//    @Deprecated
//    public Result<Boolean> generateMould(@Valid @RequestBody GenerateMouldForm form, @CurrentUser CurrentSjmsUserInfo userInfo) {
//        SaleDepartmentTargetDTO targetDTO = saleDepartmentTargetApi.listBySaleTargetIdAndDepartId(form.getSaleTargetId(), form.getDepartId());
//        if (ObjectUtil.isNull(targetDTO)) {
//            return Result.failed("部门目标不存在");
//        }
//        if (ObjectUtil.notEqual(targetDTO.getConfigStatus(), CrmSaleDepartmentTargetConfigStatusEnum.WAIT_SPLIT.getCode())) {
//            return Result.failed("部门目标，请稍后再来");
//        }
//        if (ObjectUtil.equal(targetDTO.getConfigStatus(), CrmSaleDepartmentTargetConfigStatusEnum.COPE_SPLIT.getCode())) {
//            return Result.failed("部门目标正在配置，请稍后再来");
//        }
//        QuerySaleDepartmentSubTargetRequest checkRequest=new QuerySaleDepartmentSubTargetRequest();
//        checkRequest.setSaleTargetId(targetDTO.getSaleTargetId());
//        checkRequest.setDepartId(targetDTO.getDepartId());
//
//        Map<Integer, List<SaleDepartmentSubTargetDTO>> subMap = saleDepartmentSubTargetApi.listByParam(checkRequest).stream().collect(Collectors.groupingBy(SaleDepartmentSubTargetDTO::getType));
//        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode())){
//            return Result.failed("省区不存在");
//        }
//        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.MONTH.getCode())){
//            return Result.failed("月份不存在");
//        }
//        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.GOODS.getCode())){
//            return Result.failed("品种不存在，请稍后再来");
//        }
//        //月度值map
//        List<SaleDepartmentSubTargetDTO> monthList = subMap.get(CrmSaleDepartmentSubTargetTypeEnum.MONTH.getCode());
//        List<Long> distinctMonth = monthList.stream().map(SaleDepartmentSubTargetDTO::getRelId).distinct().collect(Collectors.toList());
//        if (ObjectUtil.notEqual(distinctMonth.size(),monthList.size())){
//            return Result.failed("指标月度值存在重复");
//        }
//
//        UpdateConfigStatusRequest request = PojoUtils.map(form, UpdateConfigStatusRequest.class);
//        request.setOpUserId(userInfo.getCurrentUserId());
//        request.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.COPE_SPLIT);
//        Boolean isSuccess = saleDepartmentTargetApi.updateConfigStatus(request);
//        GenerateCrmConfigMouldRequest msgRequest = PojoUtils.map(form, GenerateCrmConfigMouldRequest.class);
//        if (!isSuccess) {
//            log.error("更新部门指标的配置状态失败，参数={}", request);
//            throw new ServiceException(ResultCode.FAILED);
//        }
//        //发送消息
//        sendMq(Constants.TOPIC_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG, Constants.TAG_GENERATE_CRM_SALE_DEPARTMENT_TARGET_CONFIG, JSON.toJSONString(msgRequest));
//
//        return Result.success(isSuccess);
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param topic
//     * @param topicTag
//     * @param msg
//     * @return
//     */
//    public boolean sendMq(String topic, String topicTag, String msg) {
//        MqMessageBO mqMessageBO = sendPrepare(topic, topicTag, msg);
//
//        mqMessageSendApi.send(mqMessageBO);
//
//        return true;
//    }
//
//    /**
//     * 消息持久化
//     *
//     * @param topic
//     * @param topicTag
//     * @param msg
//     * @return
//     */
//    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {
//
//        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
//        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
//
//        return mqMessageBO;
//    }
    //计算2022年和本年度目标
    private void lastRadioCal(List<SaleTargetDepartmentLastCalForm> listData,BigDecimal lastTargetSumTotal,BigDecimal saleTarget,List<SaleTargetDepartmentCalVO> voList){
        for (int i = 0; i < listData.size(); i++) {
            SaleTargetDepartmentLastCalForm m = listData.get(i);
            SaleTargetDepartmentCalVO vo = new SaleTargetDepartmentCalVO();
            vo.setId(m.getId());
            vo.setDepartId(m.getDepartId());
            vo.setDepartName(m.getDepartName());
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
     private void curRadioCal(List<SaleTargetDepartmentCurCalForm> listData,BigDecimal curTargetSum,List<SaleTargetDepartmentCalVO> voList){
         for (int i = 0; i < listData.size(); i++) {
             SaleTargetDepartmentCurCalForm m = listData.get(i);
             SaleTargetDepartmentCalVO vo = new SaleTargetDepartmentCalVO();
             vo.setId(m.getId());
             vo.setDepartId(m.getDepartId());
             vo.setDepartName(m.getDepartName());
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
    private void lastAndCurRadioCal(List<SaleTargetDepartmentCalVO> voList){
        LongSummaryStatistics currentTargetLongSummary = voList.stream().mapToLong(m -> m.getCurrentTarget().longValue()).summaryStatistics();
        BigDecimal bigCurSum=new BigDecimal(currentTargetLongSummary.getSum()==0L?1:currentTargetLongSummary.getSum());
        for (int i = 0; i < voList.size(); i++) {
            SaleTargetDepartmentCalVO vo = voList.get(i);
            //本年度销售占比
            BigDecimal currentRedio = vo.getCurrentTarget().divide(bigCurSum, 4, BigDecimal.ROUND_HALF_UP);
            vo.setCurrentTargetRatio(currentRedio.multiply(NUM100).setScale(2, BigDecimal.ROUND_HALF_UP));
            //增长（元）
            vo.setCurrentIncrease(vo.getCurrentTarget().subtract(vo.getLastTarget()).setScale(0,BigDecimal.ROUND_HALF_UP));
        }
    }
    public SaleTargetDepartmentCalVO buildZero(SaleTargetDepartmentCalVO vo){
        vo.setLastTarget(BigDecimal.ZERO);
        vo.setLastTargetRatio(BigDecimal.ZERO);
        vo.setCurrentTarget(BigDecimal.ZERO);
        vo.setCurrentTargetRatio(BigDecimal.ZERO);
        vo.setCurrentIncrease(BigDecimal.ZERO);
        return vo;
    }
    private  SaleTargetCalVO buildCalResult(List<SaleTargetDepartmentCalVO> voList){
        SaleTargetCalVO calVO = new SaleTargetCalVO();
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
    private void adjustment(List<SaleTargetDepartmentCalVO> voList){
        BigDecimal sumTmpLastRadio= new BigDecimal(voList.stream().mapToDouble(m -> m.getLastTargetRatio().doubleValue()).summaryStatistics().getSum());
        if(NUM100.compareTo(sumTmpLastRadio.setScale(2,BigDecimal.ROUND_HALF_UP))!=0&& BigDecimal.ZERO.compareTo(sumTmpLastRadio)!=0){
            BigDecimal subValue = NUM100.subtract(sumTmpLastRadio);
            SaleTargetDepartmentCalVO tmpVo = voList.stream().reduce((first, second) -> second).orElse(new SaleTargetDepartmentCalVO());
            tmpVo.setLastTargetRatio(tmpVo.getLastTargetRatio().add(subValue).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        BigDecimal sumTmpCurRadio= new BigDecimal(voList.stream().mapToDouble(m -> m.getCurrentTargetRatio().doubleValue()).summaryStatistics().getSum());
        if(NUM100.compareTo(sumTmpCurRadio.setScale(2,BigDecimal.ROUND_HALF_UP))!=0&& BigDecimal.ZERO.compareTo(sumTmpCurRadio)!=0){
            BigDecimal subValue = NUM100.subtract(sumTmpCurRadio);
            SaleTargetDepartmentCalVO tmpVo = voList.stream().reduce((first, second) -> second).orElse(new SaleTargetDepartmentCalVO());
            tmpVo.setCurrentTargetRatio(tmpVo.getCurrentTargetRatio().add(subValue).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
    }
}
