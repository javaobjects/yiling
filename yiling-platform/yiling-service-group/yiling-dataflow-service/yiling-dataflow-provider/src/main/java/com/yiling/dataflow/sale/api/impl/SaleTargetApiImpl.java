package com.yiling.dataflow.sale.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.sale.api.SaleTargetApi;
import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import com.yiling.dataflow.sale.dto.SaleTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetCheckRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetPageListRequest;
import com.yiling.dataflow.sale.dto.request.RemoveSaleTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveSaleTargetRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDetailDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetResolveDetailDO;
import com.yiling.dataflow.sale.entity.SaleDepartmentTargetDO;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.dataflow.sale.enums.DeptTargetErrorCode;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetResolveDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetService;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售指标
 */
@Slf4j
@DubboService
public class SaleTargetApiImpl implements SaleTargetApi {
    @Resource
    private SaleTargetService saleTargetService;
    @Resource
    private  SaleDepartmentTargetService  saleDepartmentTargetService;
    @Resource
    private SaleDepartmentSubTargetService saleDepartmentSubTargetService;
    @Resource
    private SaleDepartmentSubTargetDetailService  saleDepartmentSubTargetDetailService;
    @Resource
    private SaleDepartmentSubTargetResolveDetailService saleDepartmentSubTargetResolveDetailService;
    @Override
    public SaleTargetDTO getById(Long id) {
        Assert.notNull(id, "id 不能为null");
        return PojoUtils.map(saleTargetService.getById(id), SaleTargetDTO.class);
    }

    @Override
    public Page<SaleTargetDTO> queryPageList(QuerySaleTargetPageListRequest request) {
        return saleTargetService.queryPageList(request);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public boolean removeById(RemoveSaleTargetRequest request) {
        log.info("删除销售预测配置:{}",request);
        SaleTargetDO saleTargetDO = saleTargetService.getById(request.getId());
        if (ObjectUtil.isNull(saleTargetDO)){
            log.error("部门指标不存在，指标id={}",request.getId());
            throw new BusinessException(DeptTargetErrorCode.TARGET_NOT_FIND);
        }
        //更新配置数据查询更新字表逻辑删除
        List<SaleDepartmentTargetDTO> saleDepartmentTargetDTOS=saleDepartmentTargetService.listBySaleTargetId(request.getId());
        saleDepartmentTargetDTOS.stream().forEach(m->{
            saleDepartmentTargetService.deleteOldData(PojoUtils.map(m,SaleDepartmentTargetDO.class));
            saleDepartmentTargetService.removeById(m.getId());
        });
        UpdateWrapper<SaleTargetDO> updateWrapper=new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(SaleTargetDO::getDelFlag,1)
                .set(SaleTargetDO::getUpdateUser,request.getOpUserId())
                .set(SaleTargetDO::getUpdateTime,request.getOpTime())
                .eq(SaleTargetDO::getId,request.getId());
        boolean update = saleTargetService.update(updateWrapper);
        return update;
    }

    @Override
    public int countByName(QuerySaleTargetCheckRequest request) {
        return saleTargetService.count(new QueryWrapper<SaleTargetDO>().lambda().eq(SaleTargetDO::getName,request.getName()));
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public Long save(SaveSaleTargetRequest request) {
        log.info("销售指标保存请求,{}",request);
        SaleTargetDO saleTargetDO=new SaleTargetDO();
        PojoUtils.map(request,saleTargetDO);
        saleTargetDO.setCreateTime(request.getOpTime());
        saleTargetDO.setCreateUser(request.getOpUserId());
        saleTargetService.save(saleTargetDO);
        log.info("销售指标保存请求,{}",saleTargetDO);
        if(CollUtil.isEmpty(request.getDepartmentTargets())){
            log.error("request:{}",request);
            throw new BusinessException(ResultCode.FAILED);
        }
        List<SaleDepartmentTargetDO> list=PojoUtils.map(request.getDepartmentTargets(),SaleDepartmentTargetDO.class);
        list.stream().forEach(m->{
            m.setSaleTargetId(saleTargetDO.getId());
            m.setCreateTime(request.getOpTime());
            m.setCreateUser(request.getOpUserId());
            m.setUpdateTime(request.getOpTime());
            m.setUpdateUser(request.getOpUserId());
            m.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.UN_SPLIT.getCode());
        });
        log.info("销售指标保存请求:{}",list);
        saleDepartmentTargetService.saveBatch(list);
        return saleTargetDO.getId();
    }
}
