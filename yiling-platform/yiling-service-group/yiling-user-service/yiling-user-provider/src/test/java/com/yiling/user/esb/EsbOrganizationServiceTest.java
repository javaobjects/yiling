package com.yiling.user.esb;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.BaseTest;
import com.yiling.user.esb.bo.EsbOrgInfoTreeBO;
import com.yiling.user.esb.entity.EsbOrganizationDO;
import com.yiling.user.esb.service.EsbJobService;
import com.yiling.user.esb.service.EsbOrganizationService;
import com.yiling.user.esb.service.impl.EsbBusinessOrganizationServiceImpl;

import cn.hutool.core.collection.ListUtil;

/**
 * ESB部门架构 测试类
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
public class EsbOrganizationServiceTest extends BaseTest {

    @Autowired
    private EsbOrganizationService esbOrganizationService;
    @Autowired
    private EsbBusinessOrganizationServiceImpl esbBusinessOrganizationService;
    @Autowired
    private EsbJobService esbJobService;

    @Test
    public void listTree() {
        List<EsbOrgInfoTreeBO> treeBOList = esbOrganizationService.listTree();
        System.out.println(JSONObject.toJSONString(treeBOList));
    }

    @Test
    public void getAllParentOrg() {
        List<EsbOrganizationDO> allParentOrgList = ListUtil.toList();
        Map<Long, EsbOrganizationDO> orgMap = esbOrganizationService.list().stream().collect(Collectors.toMap(EsbOrganizationDO::getOrgId, Function.identity()));

        allParentOrgList = esbBusinessOrganizationService.getAllParentOrg(13798L, orgMap, allParentOrgList);
        System.out.println(JSONObject.toJSONString(allParentOrgList));
    }

    @Test
    public void getAllChildOrg() {
        LambdaQueryWrapper<EsbOrganizationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName, EsbOrganizationDO::getOrgPid);
        List<EsbOrganizationDO> organizationDOList = esbOrganizationService.list(wrapper);

        List<EsbOrganizationDO> childList = ListUtil.toList();
        List<EsbOrganizationDO> allChildOrg = esbBusinessOrganizationService.getAllChildOrg(13798L, organizationDOList, childList);
        System.out.println(JSONObject.toJSONString(allChildOrg));

    }

    @Test
    public void getJobByDeptListId() {
        Map<Long, List<Long>> map = esbJobService.getJobByDeptListId(ListUtil.toList(12001L, 5480L));
        System.out.println(JSONObject.toJSONString(map));
    }

}
