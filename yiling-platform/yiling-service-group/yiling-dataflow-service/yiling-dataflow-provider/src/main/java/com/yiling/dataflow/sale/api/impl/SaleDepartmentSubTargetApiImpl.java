package com.yiling.dataflow.sale.api.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetApi;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveBathSaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentTargetDO;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.dataflow.sale.enums.DeptTargetErrorCode;
import com.yiling.dataflow.sale.enums.SaleDepartmentSubTargetErrorCode;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetService;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@DubboService
@Slf4j
public class SaleDepartmentSubTargetApiImpl implements SaleDepartmentSubTargetApi {
    @Resource
    private SaleDepartmentSubTargetService saleDepartmentSubTargetService;

    @Resource
    private SaleDepartmentTargetService saleDepartmentTargetService;

    @Resource
    private SaleTargetService saleTargetService;

    @Override
    public List<SaleDepartmentSubTargetDTO> listByParam(QuerySaleDepartmentSubTargetRequest request) {
        return saleDepartmentSubTargetService.listByParam(request);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean saveBatch(SaveBathSaleDepartmentSubTargetRequest request) {
        //验证数据是否存在
        SaleTargetDO saleTargetDO = saleTargetService.getById(request.getSaleTargetId());
        if (ObjectUtil.isNull(saleTargetDO)){
            log.error("部门指标不存在，指标id={}",request.getSaleTargetId());
            throw new BusinessException(DeptTargetErrorCode.TARGET_NOT_FIND);
        }
        //查询部门列表
        SaleDepartmentTargetDTO dept = saleDepartmentTargetService.queryListByTargetId(request.getSaleTargetId(),request.getDepartId());
        if (ObjectUtil.isNull(dept)){
            log.error("部门不存在，指标id={}",request.getSaleTargetId());
            throw new BusinessException(DeptTargetErrorCode.DEPT_NOT_FIND);
        }
        if(CrmSaleDepartmentTargetConfigStatusEnum.UN_SPLIT!=CrmSaleDepartmentTargetConfigStatusEnum.getFromCode(dept.getConfigStatus())){
            throw new BusinessException(DeptTargetErrorCode.DEPT_CONFIG_STATUS);
        }
        UpdateConfigStatusRequest updateConfigStatusRequest=new UpdateConfigStatusRequest();
        PojoUtils.map(request,updateConfigStatusRequest);
        updateConfigStatusRequest.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.WAIT_SPLIT);
        //更新指标部门配置状态为已配置状态
        saleDepartmentTargetService.updateConfigStatus(updateConfigStatusRequest);
        List<SaleDepartmentSubTargetDO> list = new ArrayList<>();
        list.addAll( PojoUtils.map(request.getProvinceList(), SaleDepartmentSubTargetDO.class));
        list.addAll( PojoUtils.map(request.getMonthList(), SaleDepartmentSubTargetDO.class));
        list.addAll( PojoUtils.map(request.getGoodsList(), SaleDepartmentSubTargetDO.class));
        list.addAll( PojoUtils.map(request.getAreaList(), SaleDepartmentSubTargetDO.class));
        return saleDepartmentSubTargetService.saveOrUpdateBatch(list);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean updateBatch(SaveBathSaleDepartmentSubTargetRequest request) {
        //验证数据是否存在
        SaleTargetDO saleTargetDO = saleTargetService.getById(request.getSaleTargetId());
        if (ObjectUtil.isNull(saleTargetDO)){
            log.error("部门指标不存在，指标id={}",request.getSaleTargetId());
            throw new BusinessException(DeptTargetErrorCode.TARGET_NOT_FIND);
        }
        //查询部门列表
        SaleDepartmentTargetDTO dept = saleDepartmentTargetService.queryListByTargetId(request.getSaleTargetId(),request.getDepartId());
        if (ObjectUtil.isNull(dept)){
            log.error("部门不存在，指标id={}",request.getSaleTargetId());
            throw new BusinessException(DeptTargetErrorCode.DEPT_NOT_FIND);
        }
        if(CrmSaleDepartmentTargetConfigStatusEnum.COPE_SPLIT==CrmSaleDepartmentTargetConfigStatusEnum.getFromCode(dept.getConfigStatus())){
            throw new BusinessException(DeptTargetErrorCode.DEPT_CONFIG_STATUS);
        }
        //先移除在处理在批量新增
        log.info("逻辑删除部门指标配置详情和分解模版的状态为等待:{},{}",request.getSaleTargetId(),request.getDepartId());
        saleDepartmentTargetService.deleteOldData(PojoUtils.map(dept, SaleDepartmentTargetDO.class));
        UpdateConfigStatusRequest updateConfigStatusRequest=new UpdateConfigStatusRequest();
        PojoUtils.map(request,updateConfigStatusRequest);
        updateConfigStatusRequest.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.WAIT_SPLIT);
        //更新指标部门配置状态为已配置状态
        log.info("更新部门的状态为等待:{},{}",request.getSaleTargetId(),request.getDepartId());
        saleDepartmentTargetService.updateConfigStatus(updateConfigStatusRequest);
        //移除sub
        log.info("逻辑删除部门指标配置的状态为等待:{},{}",request.getSaleTargetId(),request.getDepartId());
        int delCount= saleDepartmentSubTargetService.removeBySaleTargetAndDepartId(request);
        // 需要提供移除接口
        List<SaleDepartmentSubTargetDO> list = new ArrayList<>();
        list.addAll( PojoUtils.map(request.getProvinceList(), SaleDepartmentSubTargetDO.class));
        list.addAll( PojoUtils.map(request.getMonthList(), SaleDepartmentSubTargetDO.class));
        list.addAll( PojoUtils.map(request.getGoodsList(), SaleDepartmentSubTargetDO.class));
        list.addAll( PojoUtils.map(request.getAreaList(), SaleDepartmentSubTargetDO.class));
        return saleDepartmentSubTargetService.saveBatch(list);
    }
}
