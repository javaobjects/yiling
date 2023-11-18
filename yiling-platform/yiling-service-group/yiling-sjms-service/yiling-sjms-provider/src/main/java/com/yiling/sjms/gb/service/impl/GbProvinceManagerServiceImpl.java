package com.yiling.sjms.gb.service.impl;

import java.util.Arrays;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.sjms.gb.dao.GbProvinceManagerMapper;
import com.yiling.sjms.gb.entity.GbProvinceManagerDO;
import com.yiling.sjms.gb.service.GbProvinceManagerService;

import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 省区负责人关系 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-28
 */
@Slf4j
@Service
public class GbProvinceManagerServiceImpl extends BaseServiceImpl<GbProvinceManagerMapper, GbProvinceManagerDO> implements GbProvinceManagerService {

    public static final Long ORG_PID = 12325L;
    public static final String JOB_NAME_PROVINCE_MANAGER = "省区经理";
    public static final String GANQING = "甘青";
    public static final String GANNING = "甘宁";
    public static final String NINGXIA = "宁夏";
    public static final String GANQINGNING = "甘宁青";
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Override
    public SimpleEsbEmployeeInfoBO getByProvinceName(QueryProvinceManagerRequest request) {
        log.info("准备获取省区经理 -> provinceName={}, orgId={}", request.getProvinceName(), request.getOrgId());

        if(Constants.COMMERCE_ORG_ID.equals(request.getOrgId())) {
            //商务部省区查询 甘青、宁夏特殊处理
            if(request.getProvinceName().equals(GANQING) || request.getProvinceName().equals(NINGXIA) ||request.getProvinceName().equals(GANNING)){
                log.info("甘宁青省份名称转换");
                request.setProvinceName(GANQINGNING);
            }
        }
        QueryWrapper<GbProvinceManagerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GbProvinceManagerDO::getProvinceName, request.getProvinceName()).eq(GbProvinceManagerDO::getOrgId,request.getOrgId()).last("limit 1");
        GbProvinceManagerDO entity = this.getOne(queryWrapper);
        if (entity != null) {
            log.info("已从配置表中获取到省区经理信息 -> empId={}, empName={}", entity.getEmpId(), entity.getEmpName());
            return new SimpleEsbEmployeeInfoBO(entity.getEmpId(), entity.getEmpName());
        }
        log.info("未从配置表中获取到省区经理信息，将通过其他逻辑获取");

        EsbOrganizationDTO esbOrganizationDTO = null;
        if(!Constants.COMMERCE_ORG_ID.equals(request.getOrgId())){
            //市场运营部省区查询
            log.info("查询市场运营部下对应省份的组织信息 -> provinceName={}, orgId={}", request.getProvinceName(), request.getOrgId());
            esbOrganizationDTO = esbOrganizationApi.getByPidAndName(request.getOrgId(), request.getProvinceName());
        }else{
            log.info("查询部门下对应省份的组织信息 -> provinceName={}, pidList=3320,9795,9799,12264,12265", request.getProvinceName());
            esbOrganizationDTO = esbOrganizationApi.getByPidListAndName(Arrays.asList(3320L,9795L,9799L,12264L,12265L), request.getProvinceName());
        }

        if (esbOrganizationDTO == null) {
            log.error("未找到应省区的部门信息，请核对。provinceName={}", request.getProvinceName());
            return null;
        }

        log.info("已找到对应省份的部门信息 -> orgId={}, orgName={}", esbOrganizationDTO.getOrgId(), esbOrganizationDTO.getOrgName());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByDeptIdAndJobName(esbOrganizationDTO.getOrgId(), JOB_NAME_PROVINCE_MANAGER);
        if (esbEmployeeDTO == null) {
            log.error("未找到对应省区的省区经理人员信息，请核对。provinceName={}",  request.getProvinceName());
            return null;
        }

        log.info("已获取到省区经理信息 -> empId={}, empName={}", esbEmployeeDTO.getEmpId(), esbEmployeeDTO.getEmpName());
        return new SimpleEsbEmployeeInfoBO(esbEmployeeDTO.getEmpId(), esbEmployeeDTO.getEmpName());
    }
}
