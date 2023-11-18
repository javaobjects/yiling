package com.yiling.dataflow.sale.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetDetailBO;
import com.yiling.dataflow.sale.dao.SaleDepartmentSubTargetDetailMapper;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDetailDO;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailService;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 部门销售指标子项配置详情 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Slf4j
@Service
public class SaleDepartmentSubTargetDetailServiceImpl extends BaseServiceImpl<SaleDepartmentSubTargetDetailMapper, SaleDepartmentSubTargetDetailDO> implements SaleDepartmentSubTargetDetailService {
    @Autowired
    private SaleTargetService saleTargetService;


    @Override
    public Page<SaleDepartmentSubTargetDetailBO> queryPage(QuerySaleDeptSubTargetDetailRequest request) {
        LambdaQueryWrapper<SaleDepartmentSubTargetDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentSubTargetDetailDO::getDepartId,request.getDepartId()).eq(SaleDepartmentSubTargetDetailDO::getSaleTargetId,request.getSaleTargetId());
        if(Objects.nonNull(request.getCategoryId()) && request.getCategoryId()>0){
            wrapper.eq(SaleDepartmentSubTargetDetailDO::getCategoryId,request.getCategoryId());
        }
        if(Objects.nonNull(request.getDepartProvinceId()) && request.getDepartProvinceId()>0){
            wrapper.eq(SaleDepartmentSubTargetDetailDO::getDepartProvinceId,request.getDepartProvinceId());
        }
        if(Objects.nonNull(request.getDepartRegionId()) && request.getDepartRegionId()>0){
            wrapper.eq(SaleDepartmentSubTargetDetailDO::getDepartRegionId,request.getDepartRegionId());
        }
        if(StrUtil.isNotEmpty(request.getCategoryName())){
            wrapper.eq(SaleDepartmentSubTargetDetailDO::getCategoryName,request.getCategoryName());
        }
        if(StrUtil.isNotEmpty(request.getDepartRegionName())){
            wrapper.eq(SaleDepartmentSubTargetDetailDO::getDepartRegionName,request.getDepartRegionName());
        }
        if(StrUtil.isNotEmpty(request.getDepartProvinceName())){
            wrapper.eq(SaleDepartmentSubTargetDetailDO::getDepartProvinceName,request.getDepartProvinceName());
        }
        Page<SaleDepartmentSubTargetDetailDO> page = this.page(request.getPage(), wrapper);
        if(page.getTotal()==0){
            return request.getPage();
        }
        Page<SaleDepartmentSubTargetDetailBO> saleDepartmentSubTargetDetailBOPage = PojoUtils.map(page,SaleDepartmentSubTargetDetailBO.class);
        SaleTargetDO saleTargetDO = saleTargetService.getById(request.getSaleTargetId());

        saleDepartmentSubTargetDetailBOPage.getRecords().forEach(saleDepartmentSubTargetDetailBO -> {
            if(Objects.nonNull(saleTargetDO)){
                saleDepartmentSubTargetDetailBO.setTargetNo(saleTargetDO.getTargetNo()).setTargetYear(saleTargetDO.getTargetYear()).setName(saleTargetDO.getName());
            }
        });
        return saleDepartmentSubTargetDetailBOPage;
    }

}
