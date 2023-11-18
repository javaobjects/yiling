package com.yiling.sjms.gb.service;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/3/9
 */
@Slf4j
public class BusinessDepartmentServiceTest extends BaseTest {

    @Autowired
    BusinessDepartmentService businessDepartmentService;

    @Test
    public void getByOrgId() {
        long orgId = 12354L;
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentService.getByOrgId(orgId);
        log.info("esbOrganizationDTO = {}", esbOrganizationDTO);
    }

    @Test
    public void listByOrgIds() {
        Map<Long, EsbOrganizationDTO> map = businessDepartmentService.listByOrgIds(ListUtil.toList(12294L, 12297L));
        log.info("map = {}", map);
    }

    @Test
    public void getByEmpId() {
        String empId = "24154";
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentService.getByEmpId(empId);
        log.info("esbOrganizationDTO = {}", esbOrganizationDTO);
    }
}
