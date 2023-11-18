package com.yiling.sjms.flow.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseConnectMonitorApi;
import com.yiling.dataflow.flow.bo.FlowEnterpriseConnectStatisticBO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseConnectMonitorDTO;
import com.yiling.dataflow.flow.dto.request.QueryEnterpriseConnectMonitorPageRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.sjms.flow.form.QueryEnterpriseConnectMonitorPageForm;
import com.yiling.sjms.flow.vo.FlowEnterpriseConnectMonitorVO;
import com.yiling.sjms.flow.vo.FlowEnterpriseConnectStatisticVO;
import com.yiling.sjms.flow.vo.FlowMonitorStatisticsCountVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorController
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@RestController
@RequestMapping("/flowEnterpriseConnectMonitor")
@Api(tags = "流向直连接口监控")
@Slf4j
public class FlowEnterpriseConnectMonitorController extends BaseController {

    @DubboReference
    private FlowEnterpriseConnectMonitorApi flowEnterpriseConnectMonitorApi;

    @DubboReference
    private LocationApi locationApi;

    @DubboReference
    SjmsUserDatascopeApi userDatascopeApi;

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    ErpClientApi erpClientApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @ApiOperation(value = "经销商直连接口首页数据列表", httpMethod = "GET")
    @GetMapping("/homeList")
    public Result<List<FlowEnterpriseConnectMonitorVO>> homeList(@CurrentUser CurrentSjmsUserInfo userInfo){
        QueryEnterpriseConnectMonitorPageRequest request = new QueryEnterpriseConnectMonitorPageRequest();
        request.setCurrent(1);
        request.setSize(10);
        Page<FlowEnterpriseConnectMonitorDTO> page = flowEnterpriseConnectMonitorApi.page(request);
        return Result.success(PojoUtils.map(page.getRecords(),FlowEnterpriseConnectMonitorVO.class));
    }

    @ApiOperation(value = "经销商直连接口状态数据分页", httpMethod = "POST")
    @PostMapping("/pageList")
    public Result<Page<FlowEnterpriseConnectMonitorVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody QueryEnterpriseConnectMonitorPageForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("直连接口监控权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if(null==userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            return Result.success(new Page<>());
        }
        QueryEnterpriseConnectMonitorPageRequest request = PojoUtils.map(form, QueryEnterpriseConnectMonitorPageRequest.class);
        if(null != request.getStartDockingTime()){
            if(null == request.getEndDockingTime()){
                return Result.failed("对接结束时间为空");
            }
            request.setStartDockingTime(DateUtil.beginOfDay(request.getStartDockingTime()));
            request.setEndDockingTime(DateUtil.endOfDay(request.getEndDockingTime()));
            if(request.getStartDockingTime().getTime()>request.getEndDockingTime().getTime()){
                return Result.failed("对接结束时间不能大于开始时间");
            }
        }else {
            if(null != request.getEndDockingTime()){
                return Result.failed("对接开始时间为空");
            }
        }
        if(null != request.getStartCollectionTime()){
            if(null == request.getEndCollectionTime()){
                return Result.failed("收集结束时间为空");
            }
            request.setStartCollectionTime(DateUtil.beginOfDay(request.getStartCollectionTime()));
            request.setEndCollectionTime(DateUtil.endOfDay(request.getEndCollectionTime()));
            if(request.getStartCollectionTime().getTime()>request.getEndCollectionTime().getTime()){
                return Result.failed("收集结束时间不能大于开始时间");
            }
        } else {
            if(null != request.getEndCollectionTime()){
                return Result.failed("收集开始时间为空");
            }
        }
        if(null != request.getStartFlowDayCount()){
            if(null == request.getEndFlowDayCount()){
                return Result.failed("过去30天回传流向的天数条件有误");
            }
        }else {
            if(null != request.getEndFlowDayCount()){
                return Result.failed("过去30天回传流向的天数条件有误");
            }
        }
        request.setUserDatascopeBO(userDatascopeBO);
        Page<FlowEnterpriseConnectMonitorDTO> page = flowEnterpriseConnectMonitorApi.page(request);
        return Result.success(PojoUtils.map(page, FlowEnterpriseConnectMonitorVO.class));
    }

    @ApiOperation(value = "根据省份统计直连接口状态", httpMethod = "GET")
    @GetMapping("/statisticByProvince")
    public Result<List<FlowEnterpriseConnectStatisticVO>> statisticByProvince(@RequestParam(name = "supplierLevel",required = false)Integer supplierLevel){
        List<FlowEnterpriseConnectStatisticBO> statisticBOList = flowEnterpriseConnectMonitorApi.countConnectionStatusByProvince(supplierLevel);
        Map<String, FlowEnterpriseConnectStatisticBO> statisticBOMap = statisticBOList.stream().collect(Collectors.toMap(FlowEnterpriseConnectStatisticBO::getProvinceCode, Function.identity(), (e1, e2) -> e1));
        //查询所有省
        List<LocationDTO> locationList = locationApi.listByParentCode("");
        List<FlowEnterpriseConnectStatisticVO> voList = locationList.stream().map(location -> {
            FlowEnterpriseConnectStatisticVO statisticVO;
            FlowEnterpriseConnectStatisticBO statisticBO = statisticBOMap.get(location.getCode());
            if (null != statisticBO) {
                statisticVO = PojoUtils.map(statisticBO, FlowEnterpriseConnectStatisticVO.class);
                if(null != statisticBO.getEnterpriseCount() && statisticBO.getEnterpriseCount() !=0){
                    BigDecimal invalidRatio = NumberUtil.div(statisticBO.getInvalidCount(), statisticBO.getEnterpriseCount(), 4);
                    statisticVO.setInvalidRatio(invalidRatio.scaleByPowerOfTen(2));
                }else {
                    statisticVO.setInvalidRatio(BigDecimal.ZERO);
                }
            } else {
                statisticVO = new FlowEnterpriseConnectStatisticVO();
                statisticVO.setProvinceCode(location.getCode());
                statisticVO.setProvinceName(location.getName());
                statisticVO.setEnterpriseCount(0);
                statisticVO.setInvalidCount(0);
                statisticVO.setValidCount(0);
                statisticVO.setInvalidRatio(BigDecimal.ZERO);
            }
            return statisticVO;
        }).sorted(Comparator.comparing(FlowEnterpriseConnectStatisticVO::getInvalidRatio).reversed()).collect(Collectors.toList());
        return Result.success(voList);
    }

    @ApiOperation(value = "首页-流向接口监控-统计")
    @PostMapping("/statisticsCount")
    public Result<FlowMonitorStatisticsCountVO> getCount(@CurrentUser CurrentSjmsUserInfo userInfo) {
        FlowMonitorStatisticsCountVO vo = new FlowMonitorStatisticsCountVO();

        // 数据权限
        String currentUserCode = userInfo.getCurrentUserCode();
        SjmsUserDatascopeBO datascopeBO = userDatascopeApi.getByEmpId(currentUserCode);
        log.debug("首页-流向接口监控-统计, 当前用户:" + currentUserCode + "数据权限为:" + JSONUtil.toJsonStr(datascopeBO));
        if (ObjectUtil.isNull(datascopeBO) || ObjectUtil.equal(OrgDatascopeEnum.NONE.getCode(), datascopeBO.getOrgDatascope())) {
            return Result.success(vo);
        }
        if (ObjectUtil.equal(OrgDatascopeEnum.PORTION.getCode(), datascopeBO.getOrgDatascope())) {
            SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = datascopeBO.getOrgPartDatascopeBO();
            List<Long> crmIds = orgPartDatascopeBO.getCrmEids();
            List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
            if (CollUtil.isEmpty(crmIds) && CollUtil.isEmpty(provinceCodes)) {
                return Result.success(vo);
            }
        }

        // 根据crm_enterprise_id查询crm获取企业信息、类型为经销商的
        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getDistributorEnterpriseByIds(datascopeBO);
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(vo);
        }

        // 用户负责的客户数量，流向直连接口监控中的企业数量
//        Integer customerCount = flowEnterpriseConnectMonitorApi.countMonitorEnterprise(crmEnterpriseIdList, null, null);
        Integer customerCount = crmEnterpriseList.size();

        // 根据社会统一信用代码统计
        vo = PojoUtils.map(erpClientApi.getSjmsFlowMonitorStatisticsCountByCrmIds(datascopeBO), FlowMonitorStatisticsCountVO.class);
        vo.setCustomerCount(customerCount);
        // 有效直连接口数量和 占总客户数量的比例
        BigDecimal deployAndCustomerCountRatio = excuteDeployAndCustomerCountRatio(vo.getRunningCount(), customerCount);
        vo.setDeployAndCustomerCountRatio(deployAndCustomerCountRatio);
        return Result.success(vo);
    }

    private BigDecimal excuteDeployAndCustomerCountRatio(Integer runningCount, Integer customerCount) {
        BigDecimal deployAndCustomerCountRatio = BigDecimal.ZERO;
        if (customerCount > 0) {
            // 有效直连接口数量
            BigDecimal deployInterfaceCountDecimal = new BigDecimal(runningCount.toString());
            BigDecimal customerCountDecimal = new BigDecimal(customerCount.toString());
            BigDecimal oneHundred = new BigDecimal("100");
            // 有效直连接口数量占总客户数量百分比
            deployAndCustomerCountRatio = oneHundred.multiply(deployInterfaceCountDecimal).divide(customerCountDecimal, 2, BigDecimal.ROUND_HALF_UP);
        }
        return deployAndCustomerCountRatio;
    }
}
