package com.yiling.dataflow.sale.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.sale.bo.SaleDepartmentTargetBO;
import com.yiling.dataflow.sale.dao.SaleDepartmentTargetMapper;
import com.yiling.dataflow.sale.dto.DeptTargetSplitDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetResolveDetailDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.GenerateCrmConfigMouldRequest;
import com.yiling.dataflow.sale.dto.request.QueryRelationShipByPoCodeRequest;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentTargetPageRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDetailDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDetailLogDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetResolveDetailDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetResolveDetailLogDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentTargetDO;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentSubTargetTypeEnum;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.dataflow.sale.enums.DeptTargetErrorCode;
import com.yiling.dataflow.sale.enums.ResolveToastEnum;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailLogService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetResolveDetailLogService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetResolveDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetService;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbJobApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 销售指标部门配置 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Slf4j
@Service
public class SaleDepartmentTargetServiceImpl extends BaseServiceImpl<SaleDepartmentTargetMapper, SaleDepartmentTargetDO> implements SaleDepartmentTargetService {

    @Autowired
    private SaleTargetService saleTargetService;
    @Autowired
    private SaleDepartmentSubTargetResolveDetailService saleDepartmentSubTargetResolveDetailService;
    @Autowired
    SaleDepartmentSubTargetDetailLogService detailLogService;
    @Autowired
    SaleDepartmentSubTargetResolveDetailLogService resolveDetailLogService;
    @Autowired
    SaleDepartmentSubTargetResolveDetailService resolveDetailService;
    @Autowired
    SaleDepartmentSubTargetService targetSubService;
    @Autowired
    SaleDepartmentSubTargetDetailService departmentSubTargetDetailService;
    @Autowired
    CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @DubboReference(timeout = 1000 * 60 * 5)
    EsbJobApi esbJobApi;
    @DubboReference(timeout = 1000 * 60 * 5)
    CrmGoodsInfoApi crmGoodsInfoApi;
    @DubboReference(timeout = 1000 * 60 * 5)
    CrmGoodsCategoryApi crmGoodsCategoryApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @Override
    public Page<SaleDepartmentTargetBO> querySaleDepartmentTargetPage(QuerySaleDepartmentTargetPageRequest request) {
        LambdaQueryWrapper<SaleDepartmentTargetDO> wrapper = Wrappers.lambdaQuery();
        if(StrUtil.isNotEmpty(request.getTargetNo()) || StrUtil.isNotEmpty(request.getName())){
            LambdaQueryWrapper<SaleTargetDO> saleTargetDOLambdaQueryWrapper = Wrappers.lambdaQuery();
            saleTargetDOLambdaQueryWrapper.eq(StrUtil.isNotEmpty(request.getTargetNo()),SaleTargetDO::getTargetNo,request.getTargetNo());
            saleTargetDOLambdaQueryWrapper.eq(StrUtil.isNotEmpty(request.getName()),SaleTargetDO::getName,request.getName()).select(SaleTargetDO::getId);
            List<Object> objectList = saleTargetService.listObjs(saleTargetDOLambdaQueryWrapper);
            if(CollUtil.isNotEmpty(objectList)){
                List<Long> saleTargetIdList = PojoUtils.map(objectList,Long.class);
                wrapper.in(SaleDepartmentTargetDO::getSaleTargetId,saleTargetIdList);
            }else{
                return request.getPage();
            }
        }
        if(Objects.nonNull(request.getDepartId()) && request.getDepartId()>0){
            wrapper.eq(SaleDepartmentTargetDO::getDepartId,request.getDepartId());
        }
        if(StrUtil.isNotEmpty(request.getDepartName())){
            wrapper.eq(SaleDepartmentTargetDO::getDepartName,request.getDepartName());
        }
        if(Objects.nonNull(request.getConfigStatus()) && request.getConfigStatus()>0){
            wrapper.eq(SaleDepartmentTargetDO::getConfigStatus,request.getConfigStatus());
        }
        wrapper.gt(SaleDepartmentTargetDO::getCurrentTarget,0);
        wrapper.gt(SaleDepartmentTargetDO::getCurrentTargetRatio,0);
        wrapper.orderByDesc(SaleDepartmentTargetDO::getConfigTime);
        Page<SaleDepartmentTargetDO> page = this.page(request.getPage(), wrapper);
        if(page.getTotal()==0){
            return request.getPage();
        }
        //查询指标名称
        List<Long> targetIdList = page.getRecords().stream().map(SaleDepartmentTargetDO::getSaleTargetId).collect(Collectors.toList());
        List<SaleTargetDO> saleTargetDOS = saleTargetService.listByIds(targetIdList);
        Map<Long, SaleTargetDO> nameMap = saleTargetDOS.stream().collect(Collectors.toMap(SaleTargetDO::getId, Function.identity()));
        List<SaleDepartmentTargetBO> saleDepartmentTargetBOS = Lists.newArrayList();
        page.getRecords().forEach(saleDepartmentTargetDO -> {
            SaleDepartmentTargetBO saleDepartmentTargetBO = new SaleDepartmentTargetBO();
            saleDepartmentTargetBOS.add(saleDepartmentTargetBO);
            PojoUtils.map(saleDepartmentTargetDO,saleDepartmentTargetBO);
            saleDepartmentTargetBO.setName(nameMap.get(saleDepartmentTargetDO.getSaleTargetId()).getName());
            saleDepartmentTargetBO.setTargetNo(nameMap.get(saleDepartmentTargetDO.getSaleTargetId()).getTargetNo());
            saleDepartmentTargetBO.setTargetYear(nameMap.get(saleDepartmentTargetDO.getSaleTargetId()).getTargetYear());
           /* List<Integer> countList = this.countResolve(saleDepartmentTargetBO.getDepartId(),saleDepartmentTargetBO.getSaleTargetId());
            saleDepartmentTargetBO.setGoal(countList.size());
            List<Integer> collect = countList.stream().filter(c -> c.equals(2)).collect(Collectors.toList());*/
           // saleDepartmentTargetBO.setResolved(collect.size());
            saleDepartmentTargetBO.setResolveToast(ResolveToastEnum.UN_RESOLVE.getCode());
            if(saleDepartmentTargetDO.getGoal()>0 && saleDepartmentTargetDO.getResolved().equals(saleDepartmentTargetDO.getGoal()) && saleDepartmentTargetDO.getCurrentTarget().compareTo(saleDepartmentTargetDO.getRealTarget())==0){
                saleDepartmentTargetBO.setResolveToast(ResolveToastEnum.SAME.getCode());
            }
            if(saleDepartmentTargetDO.getGoal()>0 && saleDepartmentTargetDO.getResolved().equals(saleDepartmentTargetDO.getGoal()) && saleDepartmentTargetDO.getCurrentTarget().compareTo(saleDepartmentTargetDO.getRealTarget())!=0){
                saleDepartmentTargetBO.setResolveToast(ResolveToastEnum.NOT_SAME.getCode());
            }
        });
        Page<SaleDepartmentTargetBO> saleDepartmentTargetBOPage = new Page(request.getCurrent(),request.getSize());
        saleDepartmentTargetBOPage.setTotal(page.getTotal()).setRecords(saleDepartmentTargetBOS);
        return saleDepartmentTargetBOPage;
    }

    @Override
    public SaleDepartmentTargetDTO queryListByTargetId(Long targetId, Long deptId) {
        if (ObjectUtil.isNull(targetId) && ObjectUtil.isNull(deptId)) {
            return null;
        }
        if (ObjectUtil.equal(targetId, 0L) && ObjectUtil.equal(deptId, 0L)) {
            return null;
        }
        LambdaQueryWrapper<SaleDepartmentTargetDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(targetId) && ObjectUtil.notEqual(targetId, 0L), SaleDepartmentTargetDO::getSaleTargetId, targetId);
        wrapper.eq(ObjectUtil.isNotNull(deptId) && ObjectUtil.notEqual(deptId, 0L), SaleDepartmentTargetDO::getDepartId, deptId);
        List<SaleDepartmentTargetDO> list = list(wrapper);

        SaleDepartmentTargetDO obj = list.stream().findAny().get();
        return PojoUtils.map(obj, SaleDepartmentTargetDTO.class);
    }

    @Override
    public Boolean updateConfigStatus(UpdateConfigStatusRequest request) {
        if (ObjectUtil.isNull(request.getSaleTargetId()) || ObjectUtil.equal(request.getSaleTargetId(), 0L)) {
            return Boolean.FALSE;
        }
        if (ObjectUtil.isNull(request.getDepartId()) || ObjectUtil.equal(request.getDepartId(), 0L)) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<SaleDepartmentTargetDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentTargetDO::getSaleTargetId, request.getSaleTargetId());
        wrapper.eq(SaleDepartmentTargetDO::getDepartId, request.getDepartId());
        SaleDepartmentTargetDO var=new SaleDepartmentTargetDO();
        var.setConfigStatus(request.getConfigStatus().getCode());
        var.setConfigTime(new Date());
        var.setGoal(request.getGoal());
        var.setResolved(0);
        var.setRealTarget(BigDecimal.ZERO);
        var.setTemplateUrl(request.getTemplateUrl());
        var.setOpUserId(request.getOpUserId());
        boolean isSuccess = update(var, wrapper);
        if (!isSuccess){
            log.error("更新部门拆解状态失败，参数={}",request.getSaleTargetId());
            throw new ServiceException(ResultCode.FAILED);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateMould(Long targetId,Long deptId) {
        SaleTargetDO saleTargetDO = saleTargetService.getById(targetId);
        if (ObjectUtil.isNull(saleTargetDO)){
            log.error("部门指标不存在，指标id={}",targetId);
            throw new BusinessException(DeptTargetErrorCode.TARGET_NOT_FIND);
        }
        //查询部门列表
        SaleDepartmentTargetDTO dept = queryListByTargetId(targetId,deptId);
        if (ObjectUtil.isNull(dept)){
            log.error("部门不存在，指标id={}",targetId);
            throw new BusinessException(DeptTargetErrorCode.DEPT_NOT_FIND);
        }
        List<SaleDepartmentSubTargetDTO> targetSubList = targetSubService.queryListByTargetIdAndDeptId(targetId, deptId);
        //排除目标为0的数据
        List<SaleDepartmentSubTargetDTO> filterTargetSubList = targetSubList.stream().filter(e -> ObjectUtil.notEqual(e.getCurrentTarget(), BigDecimal.ZERO)).collect(Collectors.toList());
        Map<Integer, List<SaleDepartmentSubTargetDTO>> subMap = filterTargetSubList.stream().collect(Collectors.groupingBy(SaleDepartmentSubTargetDTO::getType));
        Map<Integer, List<SaleDepartmentSubTargetDTO>> originalTargetMap = targetSubList.stream().collect(Collectors.groupingBy(SaleDepartmentSubTargetDTO::getType));
        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode())){
            log.error("省区不存在，指标id={}",targetId);
            throw new BusinessException(DeptTargetErrorCode.PROVINCE_NOT_FIND);
        }
        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.MONTH.getCode())){
            log.error("月份不存在，指标id={}",targetId);
            throw new BusinessException(DeptTargetErrorCode.MONTH_NOT_FIND);
        }
        if (!subMap.containsKey(CrmSaleDepartmentSubTargetTypeEnum.GOODS.getCode())){
            log.error("品种不存在，指标id={}",targetId);
            throw new BusinessException(DeptTargetErrorCode.GOODS_NOT_FIND);
        }
        //月度值map
        List<SaleDepartmentSubTargetDTO> monthList = subMap.get(CrmSaleDepartmentSubTargetTypeEnum.MONTH.getCode());
        List<Long> distinctMonth = monthList.stream().map(SaleDepartmentSubTargetDTO::getRelId).distinct().collect(Collectors.toList());
        if (ObjectUtil.notEqual(distinctMonth.size(),monthList.size())){
            log.error("指标月度值存在重复，参数={}",monthList);
            throw new BusinessException(DeptTargetErrorCode.MONTH_ERR);
        }
        Map<Long, BigDecimal> monthMap = monthList.stream().collect(Collectors.toMap(SaleDepartmentSubTargetDTO::getRelId, SaleDepartmentSubTargetDTO::getCurrentTargetRatio));
        //生成笛卡尔积
        HashMap<Integer, List<SaleDepartmentSubTargetDTO>> cartesianMap = MapUtil.newHashMap();
        cartesianMap.put(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode(),subMap.get(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode()));
        cartesianMap.put(CrmSaleDepartmentSubTargetTypeEnum.GOODS.getCode(),subMap.get(CrmSaleDepartmentSubTargetTypeEnum.GOODS.getCode()));
        //产品要求配置详情去办不参与笛卡尔积
//        cartesianMap.put(CrmSaleDepartmentSubTargetTypeEnum.AREA.getCode(),subMap.get(CrmSaleDepartmentSubTargetTypeEnum.AREA.getCode()));

        List<List<DeptTargetSplitDTO>> tempCartesian = cartesianProduct(getCommonData(dept, cartesianMap));
        //生成子配置项
        List<SaleDepartmentSubTargetDetailDO> subTargetDetailDOS = generateSubTargetDetail(tempCartesian, monthMap,targetId,dept.getCurrentTarget());
        //部门+省区 deptIdList 用于查找岗位
        ArrayList<Long> deptIdList = ListUtil.toList(deptId);
        List<SaleDepartmentSubTargetDTO> provinceList = subMap.get(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode());
        //区办产品要求不过滤为0的目标数据
        List<SaleDepartmentSubTargetDTO> areaList = originalTargetMap.getOrDefault(CrmSaleDepartmentSubTargetTypeEnum.AREA.getCode(),ListUtil.toList());
        deptIdList.addAll(provinceList.stream().map(SaleDepartmentSubTargetDTO::getRelId).distinct().collect(Collectors.toList()));
        deptIdList.addAll(areaList.stream().map(SaleDepartmentSubTargetDTO::getRelId).distinct().collect(Collectors.toList()));
        //生成分解模板
        List<SaleDepartmentSubTargetResolveDetailDO> resolveDetailDOList = generateResolve(deptIdList,dept);

        //删除老数据并且备份数据
        deleteOldData(PojoUtils.map(dept,SaleDepartmentTargetDO.class));
        //保存数据
        boolean isSuccess = departmentSubTargetDetailService.saveBatch(subTargetDetailDOS);
        if (!isSuccess){
            log.error("保存子配置项失败，参数={}",subTargetDetailDOS);
            throw new ServiceException(ResultCode.FAILED);
        }
        if (CollUtil.isNotEmpty(resolveDetailDOList)){
            isSuccess=resolveDetailService.saveBatch(resolveDetailDOList);
            if (!isSuccess){
                log.error("保存分解模板失败，参数={}",resolveDetailDOList);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        UpdateConfigStatusRequest request =new UpdateConfigStatusRequest();
        request.setSaleTargetId(targetId);
        request.setDepartId(deptId);
        request.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.COMPLETE_SPLIT);
        request.setGoal(resolveDetailDOList.size());
        QueryResolveDetailPageRequest resolveDetailPageRequest=new QueryResolveDetailPageRequest();
        resolveDetailPageRequest.setDepartId(deptId);
        resolveDetailPageRequest.setSaleTargetId(targetId);
       /* String genTemplate = resolveDetailService.genTemplate(resolveDetailPageRequest);
        request.setTemplateUrlnTemplate);*/
       //发送mq生成分解模板
        GenerateCrmConfigMouldRequest generateRequest = new GenerateCrmConfigMouldRequest();
        generateRequest.setDepartId(deptId).setSaleTargetId(targetId);
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_GEN_SALE_TARGET_TEMPLATE, Constants.TAG_GEN_SALE_TARGET_TEMPLATE, JSON.toJSONString(generateRequest));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
        //更新拆解状态
        isSuccess = updateConfigStatus(request);
        if (!isSuccess){
            log.error("更新部门指标的配置状态失败，参数={}",request);
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    @Override
    public List<SaleDepartmentTargetDTO> listBySaleTargetId(Long saleTargetId) {
        LambdaQueryWrapper<SaleDepartmentTargetDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentTargetDO::getSaleTargetId, saleTargetId);
        return  PojoUtils.map(list(wrapper),SaleDepartmentTargetDTO.class);
    }

    /**
     * 统计分解数
     * @param departId
     * @param saleTargetId
     * @return
     */
    private List<Integer> countResolve(Long departId,Long saleTargetId){
        LambdaQueryWrapper<SaleDepartmentSubTargetResolveDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartId,departId).eq(SaleDepartmentSubTargetResolveDetailDO::getSaleTargetId,saleTargetId).select(SaleDepartmentSubTargetResolveDetailDO::getResolveStatus);
        List<Object> objects = saleDepartmentSubTargetResolveDetailService.listObjs(wrapper);
        if(CollUtil.isEmpty(objects)){
            return Lists.newArrayList();
        }
        return PojoUtils.map(objects, Integer.class);
    }

    /**
     * 删除老数据并备份
     *
     * @param departmentTargetDO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOldData(SaleDepartmentTargetDO departmentTargetDO ){
        //查询子配置
        LambdaQueryWrapper<SaleDepartmentSubTargetDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentSubTargetDetailDO::getSaleTargetId,departmentTargetDO.getSaleTargetId());
        wrapper.eq(SaleDepartmentSubTargetDetailDO::getDepartId,departmentTargetDO.getDepartId());
        List<SaleDepartmentSubTargetDetailDO> list = departmentSubTargetDetailService.list(wrapper);
        if (CollUtil.isNotEmpty(list)){
            List<SaleDepartmentSubTargetDetailLogDO> logDOList = PojoUtils.map(list, SaleDepartmentSubTargetDetailLogDO.class);
            boolean isSuccess = detailLogService.saveBatch(logDOList);
            if (!isSuccess){
                log.error("插入子配置日志失败，参数={}",logDOList);
                throw new ServiceException(ResultCode.FAILED);
            }
            SaleDepartmentSubTargetDetailDO var=new SaleDepartmentSubTargetDetailDO();
            int rows=departmentSubTargetDetailService.batchDeleteWithFill(var,wrapper);
            if (rows==0){
                log.error("删除老的子配置失败，参数={}",departmentTargetDO.getSaleTargetId());
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        //查询分解模板
        List<SaleDepartmentSubTargetResolveDetailDTO> resolveDetailDTOList = resolveDetailService.queryListByTargetId(departmentTargetDO.getSaleTargetId(),departmentTargetDO.getDepartId());
        if (CollUtil.isNotEmpty(resolveDetailDTOList)){
            List<SaleDepartmentSubTargetResolveDetailLogDO> logDOList = PojoUtils.map(resolveDetailDTOList, SaleDepartmentSubTargetResolveDetailLogDO.class);
            boolean isSuccess = resolveDetailLogService.saveBatch(logDOList);
            if (!isSuccess){
                log.error("插入子配置模板日志失败，参数={}",logDOList);
                throw new ServiceException(ResultCode.FAILED);
            }
            LambdaQueryWrapper<SaleDepartmentSubTargetResolveDetailDO> resolveWrapper = Wrappers.lambdaQuery();
            resolveWrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getSaleTargetId,departmentTargetDO.getSaleTargetId());
            resolveWrapper.eq(SaleDepartmentSubTargetResolveDetailDO::getDepartId,departmentTargetDO.getDepartId());
            SaleDepartmentSubTargetResolveDetailDO var = new SaleDepartmentSubTargetResolveDetailDO();
            int rows=resolveDetailService.batchDeleteWithFill(var,resolveWrapper);
            if (rows==0){
                log.error("删除老的子配置模板失败，参数={}",departmentTargetDO.getSaleTargetId());
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        //删除模板并置空字段
        String templateUrl = departmentTargetDO.getTemplateUrl();
        departmentTargetDO.setTemplateUrl("");
        departmentTargetDO.setOpUserId(1L);

        boolean isSuccess = updateById(departmentTargetDO);
        if (!isSuccess){
            log.error("删除指标模板失败，参数={}",departmentTargetDO);
            throw new ServiceException(ResultCode.FAILED);
        }
    }

    @Override
    public void updateTaskStatus(Long targetId, Long deptId, String errMsg) {
        SaleDepartmentTargetDTO dept = queryListByTargetId(targetId,deptId);
        SaleDepartmentTargetDO targetDO = PojoUtils.map(dept, SaleDepartmentTargetDO.class);
        targetDO.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.FAIL.getCode());
        targetDO.setGenerateTempErrMsg(errMsg);
        updateById(targetDO);
    }

    /**
     * 生成分解模板
     *
     * @param deptIdList
     * @param departmentTargetDTO
     * @return
     */
    private List<SaleDepartmentSubTargetResolveDetailDO> generateResolve(List<Long> deptIdList,SaleDepartmentTargetDTO departmentTargetDTO){
        List<SaleDepartmentSubTargetResolveDetailDO> result=new ArrayList<>(deptIdList.size()*5);
        //查询岗位
        List<Long> jobIdList=ListUtil.toList();
        Map<Long, List<Long>> jobByDeptMap = esbJobApi.getJobByDeptListId(deptIdList);
        jobByDeptMap.forEach((k,v)->{
            jobIdList.addAll(v);
        });
        String tableSuffix = BackupUtil.generateTableSuffix(DateUtil.year(new Date()), DateUtil.lastMonth().monthBaseOne());
        //根据岗位查询三者关系
        List<CrmEnterpriseRelationShipDO> relationList=queryRelationList(jobIdList,tableSuffix);

        Map<Long ,List<CrmGoodsInfoDTO>> goodsInfoDTOMap = crmGoodsInfoApi.findByGroupIds(relationList.stream().map(CrmEnterpriseRelationShipDO::getProductGroupId).filter(e->ObjectUtil.notEqual(e,0L)).distinct().collect(Collectors.toList()));

        relationList.forEach(e->{
            //忽略掉三者关系产品组为0的三者关系
            if (ObjectUtil.equal(e.getProductGroupId(),0L)){
                return;
            }
            List<CrmGoodsInfoDTO> goodsList = goodsInfoDTOMap.getOrDefault(e.getProductGroupId(),ListUtil.toList());
            goodsList.forEach(g->{
                SaleDepartmentSubTargetResolveDetailDO var = initResolveDetail(departmentTargetDTO, e, g);
                result.add(var);
            });
        });
        //补全相关信息
//        Map<Long, String> jobNameMap = esbJobApi.getJobDeptNameByJobDeptIdList(deptIdList);

        List<Long> cateIdList = result.stream().map(SaleDepartmentSubTargetResolveDetailDO::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, String> cateMap = crmGoodsCategoryApi.findByIds(cateIdList).stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));
        result.stream().forEach(e->{
            String cate = cateMap.getOrDefault(e.getCategoryId(),"");
            e.setCategoryName(cate);
//            e.setPostName(jobNameMap.get(e.getPostCode()));
        });
        return result;
    }

    /**
     *
     * 初始化分解模板
     *
     * @param departmentTargetDTO
     * @param relationShip
     * @param goodsInfo
     * @return
     */
    private SaleDepartmentSubTargetResolveDetailDO initResolveDetail(SaleDepartmentTargetDTO departmentTargetDTO, CrmEnterpriseRelationShipDO relationShip, CrmGoodsInfoDTO goodsInfo){
        SaleDepartmentSubTargetResolveDetailDO var=new SaleDepartmentSubTargetResolveDetailDO();
        var.setSaleTargetId(departmentTargetDTO.getSaleTargetId());
        var.setDepartId(departmentTargetDTO.getDepartId());

        var.setDepartName(relationShip.getBusinessDepartment());
        var.setDepartProvinceName(relationShip.getBusinessArea());
        var.setDepartRegionName(relationShip.getBusinessArea());
        var.setProductGroupId(relationShip.getProductGroupId());
        var.setProductGroupName(relationShip.getProductGroup());
        var.setSuperiorSupervisorCode(relationShip.getSuperiorSupervisorCode());
        var.setSuperiorSupervisorName(relationShip.getSuperiorSupervisorName());
        var.setRepresentativeCode(relationShip.getRepresentativeCode());
        var.setRepresentativeName(relationShip.getRepresentativeName());
        var.setCustomerCode(relationShip.getCustomerCode());
        var.setCustomerName(relationShip.getCustomerName());
        var.setSupplyChainRole(relationShip.getSupplyChainRoleType());
        var.setPostCode(relationShip.getPostCode());
        var.setPostName(relationShip.getPostName());
        var.setProvince(relationShip.getProvince());
        var.setCity(relationShip.getCity());
        var.setDistrictCounty(relationShip.getDistrictCounty());

        var.setCategoryId(goodsInfo.getCategoryId());
//        var.setCategoryName(goodsInfo);
        var.setGoodsId(goodsInfo.getGoodsCode());
        var.setGoodsName(goodsInfo.getGoodsName());

        return var;
    }

    /**
     *
     * 初始化分解模板
     *
     * @param detailDO
     * @return
     */
    private SaleDepartmentSubTargetResolveDetailDO initResolveDetail(SaleDepartmentSubTargetDetailDO detailDO){
        SaleDepartmentSubTargetResolveDetailDO var=new SaleDepartmentSubTargetResolveDetailDO();
        var.setSaleTargetId(detailDO.getSaleTargetId());
        var.setDepartId(detailDO.getDepartId());
        var.setDepartName(detailDO.getDepartName());
        var.setDepartProvinceId(detailDO.getDepartProvinceId());
        var.setDepartProvinceName(detailDO.getDepartProvinceName());
        var.setDepartRegionId(detailDO.getDepartRegionId());
        var.setDepartRegionName(detailDO.getDepartRegionName());
        var.setTargetJan(detailDO.getTargetJan());
        var.setTargetFeb(detailDO.getTargetFeb());
        var.setTargetMar(detailDO.getTargetMar());
        var.setTargetApr(detailDO.getTargetApr());
        var.setTargetMay(detailDO.getTargetMay());
        var.setTargetJun(detailDO.getTargetJun());
        var.setTargetJul(detailDO.getTargetJul());
        var.setTargetAug(detailDO.getTargetAug());
        var.setTargetSep(detailDO.getTargetSep());
        var.setTargetOct(detailDO.getTargetOct());
        var.setTargetNov(detailDO.getTargetNov());
        var.setTargetDec(detailDO.getTargetDec());
        return var;
    }

    private String getDeptKey(String dept,String province,String area){
        String key=dept+ StrUtil.DASHED+province;
        if (StrUtil.isNotBlank(area)){
            key=key+StrUtil.DASHED+area;
        }
        return key;
    }

    /**
     * 生成配置详情
     *
     * @param tempCartesian
     * @param monthMap
     * @param targetId
     * @param saleAmount 年度总目标
     * @return
     */
    private List<SaleDepartmentSubTargetDetailDO> generateSubTargetDetail(List<List<DeptTargetSplitDTO>> tempCartesian, Map<Long, BigDecimal> monthMap,Long targetId,BigDecimal saleAmount){
        List<SaleDepartmentSubTargetDetailDO> result=new ArrayList<>(tempCartesian.size());
        tempCartesian.forEach(e->{
            SaleDepartmentSubTargetDetailDO var=new SaleDepartmentSubTargetDetailDO();
            var.setSaleTargetId(targetId);

            AtomicReference<BigDecimal> provinceRatio= new AtomicReference<>(BigDecimal.ZERO);
            AtomicReference<BigDecimal> goodsRatio= new AtomicReference<>(BigDecimal.ZERO);

            e.forEach(s->{
                if (ObjectUtil.equal(0,s.getType())){
                    var.setDepartId(s.getDepartId());
                    var.setDepartName(s.getDepartName());
                }
                if (ObjectUtil.equal(CrmSaleDepartmentSubTargetTypeEnum.PROVINCE.getCode(),s.getType())){
                    var.setDepartProvinceId(s.getRelId());
                    var.setDepartProvinceName(s.getRelName());
                    provinceRatio.set(s.getCurrentTargetRatio());
                }
                if (ObjectUtil.equal(CrmSaleDepartmentSubTargetTypeEnum.GOODS.getCode(),s.getType())){
                    var.setCategoryId(s.getRelId());
                    var.setCategoryName(s.getRelName());
                    goodsRatio.set(s.getCurrentTargetRatio());
                }
                if (ObjectUtil.equal(CrmSaleDepartmentSubTargetTypeEnum.AREA.getCode(),s.getType())){
                    var.setDepartRegionId(s.getRelId());
                    var.setDepartRegionName(s.getRelName());
                }
            });

            var.setTargetJan(setScale(saleAmount.multiply(divideHundred(monthMap.get(1L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetFeb(setScale(saleAmount.multiply(divideHundred(monthMap.get(2L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetMar(setScale(saleAmount.multiply(divideHundred(monthMap.get(3L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetApr(setScale(saleAmount.multiply(divideHundred(monthMap.get(4L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetMay(setScale(saleAmount.multiply(divideHundred(monthMap.get(5L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetJun(setScale(saleAmount.multiply(divideHundred(monthMap.get(6L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetJul(setScale(saleAmount.multiply(divideHundred(monthMap.get(7L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetAug(setScale(saleAmount.multiply(divideHundred(monthMap.get(8L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetSep(setScale(saleAmount.multiply(divideHundred(monthMap.get(9L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetOct(setScale(saleAmount.multiply(divideHundred(monthMap.get(10L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetNov(setScale(saleAmount.multiply(divideHundred(monthMap.get(11L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            var.setTargetDec(setScale(saleAmount.multiply(divideHundred(monthMap.get(12L))).multiply(divideHundred(provinceRatio.get())).multiply(divideHundred(goodsRatio.get()))));
            result.add(var);
        });

        return result;
    }

    private BigDecimal divideHundred(BigDecimal b){
        if (ObjectUtil.isNull(b)){
            return BigDecimal.ZERO;
        }
        return b.divide(BigDecimal.TEN).divide(BigDecimal.TEN);
    }

    private BigDecimal setScale(BigDecimal b){
        return b.setScale(0,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 处理对象属性
     *
     * @param dept
     * @param subMap
     * @return
     */
    private List<List<DeptTargetSplitDTO>> getCommonData(SaleDepartmentTargetDTO dept,Map<Integer, List<SaleDepartmentSubTargetDTO>> subMap){
        List<List<DeptTargetSplitDTO>> result= ListUtil.toList();

        DeptTargetSplitDTO deptTargetSplitDTO=new DeptTargetSplitDTO();
        deptTargetSplitDTO.setDepartId(dept.getDepartId());
        deptTargetSplitDTO.setDepartName(dept.getDepartName());
        deptTargetSplitDTO.setType(0);
        result.add(ListUtil.toList(deptTargetSplitDTO));
        subMap.forEach((k,v)->{
            List<DeptTargetSplitDTO> var = PojoUtils.map(v, DeptTargetSplitDTO.class);
            result.add(var);
        });

        return result;
    }

    /**
     * 生成笛卡尔积
     *
     * @param lists
     * @param <T>
     * @return
     */
    private <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        List<List<T>> resultLists = new ArrayList<List<T>>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<T>());
            return resultLists;
        } else {
            List<T> firstList = lists.get(0);
            List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (T condition : firstList) {
                for (List<T> remainingList : remainingLists) {
                    ArrayList<T> resultList = new ArrayList<T>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }

    /**
     * 分页查询三者关系
     *
     * @param postCode
     * @param tableSuffix
     * @return
     */
    private List<CrmEnterpriseRelationShipDO> queryRelationList(List<Long> postCode, String tableSuffix){

        List<CrmEnterpriseRelationShipDO> result=ListUtil.toList();

        Page<CrmEnterpriseRelationShipDO> orderPage;
        QueryRelationShipByPoCodeRequest queryRequest = new QueryRelationShipByPoCodeRequest();
        queryRequest.setPostCodeList(postCode);

        int current = 1;
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(100);
            //分页查询符合结算条件的订单
            orderPage = crmEnterpriseRelationShipService.pageByPostCodeList(queryRequest,tableSuffix);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            result.addAll(orderPage.getRecords());
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));

        return result;
    }



}
