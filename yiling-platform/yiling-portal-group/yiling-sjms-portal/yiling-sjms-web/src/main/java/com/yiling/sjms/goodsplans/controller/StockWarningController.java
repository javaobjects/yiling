package com.yiling.sjms.goodsplans.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockWarnApi;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.StockWarnIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.goodsplans.form.StockWarnPageForm;
import com.yiling.sjms.goodsplans.vo.StockWarnIconVo;
import com.yiling.sjms.goodsplans.vo.StockWarnVo;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 库存预警
 */
@Slf4j
@RestController
@RequestMapping("/sw")
@Api(tags = "库存预警")
public class StockWarningController extends BaseController {
    @DubboReference(timeout = 60000)
    private FlowAnalyseStockWarnApi flowAnalyseStockWarnApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;
    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;
    @ApiOperation(value = "库存预警列表")
    @PostMapping("/list")
    public Result<Page<StockWarnVo>> getStockWarnPage(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid StockWarnPageForm form){
        SjmsUserDatascopeBO sjmsUserDataScope = getSjmsUserDataScope(userInfo);
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(sjmsUserDataScope.getOrgDatascope())){
            return Result.success(new Page<StockWarnVo>());
        }
        StockWarnPageRequest request = new StockWarnPageRequest();
        PojoUtils.map(form,request);
        //预处理Eid
       // request.setEidsList(getSupplierEidList(form,sjmsUserDataScope));
        request.setCrmIdList(getSupplierEidList(form,sjmsUserDataScope));
        Page<StockWarnDTO> stockWarnPage = flowAnalyseStockWarnApi.getStockWarnPage(request);
        Page<StockWarnVo> stockWarnVoPage = PojoUtils.map(stockWarnPage, StockWarnVo.class);
        return Result.success(stockWarnVoPage);
    }

    @ApiOperation(value = "库存预警库存可销天数")
    @PostMapping("/sale/icon/list")
    public Result<Page<StockWarnIconVo>> getStockWarnIconPage(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid StockWarnPageForm form){
        SjmsUserDatascopeBO sjmsUserDataScope = getSjmsUserDataScope(userInfo);
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(sjmsUserDataScope.getOrgDatascope())){
            return Result.success(new Page<StockWarnIconVo>());
        }
        StockWarnIconRequest request = new StockWarnIconRequest();
        PojoUtils.map(form,request);
        request.setCrmIdList(getSupplierEidList(form,sjmsUserDataScope));
        Page<StockWarnIconDTO> stockWarnPage = flowAnalyseStockWarnApi.getSaleDaysIconWarn(request);
        Page<StockWarnIconVo> stockWarnVoPage = PojoUtils.map(stockWarnPage, StockWarnIconVo.class);
        return Result.success(stockWarnVoPage);
    }

    /**
     * 处理查询条件EId 如果没有根据经销商等级和经销商体系,默认返回的情况是-1。防止查询的数据是全部
     * @param form
     * @return
     */
    private List<Long> getSupplierEidList(StockWarnPageForm form,SjmsUserDatascopeBO sjmsUserDataScope){
        List<Long> list=new ArrayList<>();
        // 根据查询条件返回 返回机构编码的集合;当返回空时或null，代表没有可匹配的eid集合，增加默认的不存在eid-1；
        List<Long> listByLevelAndGroupEidList= ListUtil.empty();
        if (Objects.nonNull(form.getSupplierLevel()) ||Objects.nonNull(form.getBusinessSystem())){
            listByLevelAndGroupEidList = crmSupplierApi.listByLevelAndGroup(form.getSupplierLevel(), form.getBusinessSystem());
            listByLevelAndGroupEidList.add(-1L);
        }
        log.info("listByLevelAndGroupEidList->{},eid->{}",listByLevelAndGroupEidList,form.getCrmEnterpriseId());
        if(Objects.nonNull(form.getCrmEnterpriseId())&& form.getCrmEnterpriseId().intValue()>0&&CollUtil.isEmpty(listByLevelAndGroupEidList)||(CollUtil.isNotEmpty(listByLevelAndGroupEidList)&&listByLevelAndGroupEidList.contains(form.getCrmEnterpriseId()))){
            list.add(form.getCrmEnterpriseId());
        }
        if(Objects.nonNull(form.getCrmEnterpriseId())&& form.getCrmEnterpriseId().intValue()>0&&(CollUtil.isNotEmpty(listByLevelAndGroupEidList)&&!listByLevelAndGroupEidList.contains(form.getCrmEnterpriseId()))){
            list.add(-1L);
        }
        if(Objects.isNull(form.getCrmEnterpriseId())&&CollUtil.isNotEmpty(listByLevelAndGroupEidList)){
            list.addAll(listByLevelAndGroupEidList);
        }
        //permit  当不等于空的时候有权限控制
        // 2中逻辑控制 1中时获取权限2个list的交集，1中时返回权限列表的list;
        if(OrgDatascopeEnum.PORTION==OrgDatascopeEnum.getFromCode(sjmsUserDataScope.getOrgDatascope())){
            QueryDataScopeRequest request=new QueryDataScopeRequest();
            request.setSjmsUserDatascopeBO(sjmsUserDataScope);
            List<CrmEnterpriseDTO>  permitCrmEnterList= crmEnterpriseApi.getCrmEnterpriseListByDataScope(request);
            List<Long> eIdsPermitList= Optional.ofNullable(permitCrmEnterList.stream().filter(m->m.getId()>0).map(CrmEnterpriseDTO::getId).collect(Collectors.toList())).orElse(Lists.newArrayList());
            //什么时间获取交集
            //当 查询条件大于>的时候获取交集
            if(!list.isEmpty()){
                List<Long> intersection=eIdsPermitList.stream().filter(item->list.contains(item)).collect(Collectors.toList());
                if(Objects.nonNull(form.getBusinessSystem())||Objects.nonNull(form.getSupplierLevel())){
                    intersection.add(-1L);
                }
                if(intersection.isEmpty()){
                    intersection.add(-1L);
                }
                return  intersection;
            }
            return eIdsPermitList;
        }
        return list;
    }
    private SjmsUserDatascopeBO  getSjmsUserDataScope( CurrentSjmsUserInfo userInfo){
        // 获取权限
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("销售预测权限获取:empId={},longs={}",userInfo.getCurrentUserCode(),byEmpId);
        return byEmpId;
    }
}
