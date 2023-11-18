package com.yiling.user.esb.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.bo.EsbOrgInfoTreeBO;
import com.yiling.user.esb.bo.EsbOrganizationBO;
import com.yiling.user.esb.bo.SimpleEsbOrgInfoBO;
import com.yiling.user.esb.dao.EsbOrganizationMapper;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbOrganizationRequest;
import com.yiling.user.esb.entity.EsbBusinessOrganizationDO;
import com.yiling.user.esb.entity.EsbOrganizationDO;
import com.yiling.user.esb.service.EsbBusinessOrganizationService;
import com.yiling.user.esb.service.EsbOrganizationService;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * esb组织架构 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-24
 */
@Service
public class EsbOrganizationServiceImpl extends BaseServiceImpl<EsbOrganizationMapper, EsbOrganizationDO> implements EsbOrganizationService {

    @Autowired
    EsbBusinessOrganizationService esbBusinessOrganizationService;

    @Override
    public EsbOrganizationDO getByOrgId(Long orgId) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbOrganizationDO::getOrgId, orgId)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EsbOrganizationDO> listByOrgIds(List<Long> orgIds) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(EsbOrganizationDO::getOrgId, orgIds)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .orderByAsc(EsbOrganizationDO::getOrgId);
        return this.list(queryWrapper);
    }

    @Override
    public EsbOrganizationDO getByPidAndName(Long orgPid, String orgName) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbOrganizationDO::getOrgPid, orgPid)
                .eq(EsbOrganizationDO::getOrgName, orgName)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public EsbOrganizationDO getByPid(Long orgPid) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbOrganizationDO::getOrgId, orgPid)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public EsbOrganizationDO getByPidListAndName(List<Long> orgPidList, String orgName) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(EsbOrganizationDO::getOrgPid, orgPidList)
                .eq(EsbOrganizationDO::getOrgName, orgName)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EsbOrganizationDO> listByPid(Long orgPid) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbOrganizationDO::getOrgPid, orgPid)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .orderByAsc(EsbOrganizationDO::getOrgId);
        return this.list(queryWrapper);
    }

    @Override
    public List<SimpleEsbOrgInfoBO> listAll(boolean setParentOrgInfoFlag) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName, EsbOrganizationDO::getOrgPid)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .orderByAsc(EsbOrganizationDO::getOrgId);

        List<EsbOrganizationDO> doList = this.list(queryWrapper);
        List<SimpleEsbOrgInfoBO> boList = PojoUtils.map(doList, SimpleEsbOrgInfoBO.class);
        if (!setParentOrgInfoFlag) {
            return boList;
        }

        Map<Long, SimpleEsbOrgInfoBO> map = boList.stream().collect(Collectors.toMap(SimpleEsbOrgInfoBO::getOrgId, Function.identity()));
        // 设置上级部门信息
        boList.forEach(e -> e.setParentOrgInfo(map.get(e.getOrgPid())));
        return boList;
    }

    @Override
    public Boolean saveOrUpdate(SaveOrUpdateEsbOrganizationRequest request) {
        Assert.notNull(request, "参数request不能为空");

        Long orgId = request.getOrgId();
        EsbOrganizationDO entity = this.getByOrgId(orgId);
        if (entity == null) {
            entity = PojoUtils.map(request, EsbOrganizationDO.class);
            return this.save(entity);
        } else {
            PojoUtils.map(request, entity);
            return this.updateById(entity);
        }
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<EsbOrganizationDO> findByOrgName(String orgName, String tableSuffix) {
        if(StrUtil.isBlank(orgName)){
            return ListUtil.empty();
        }
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(EsbOrganizationDO::getOrgName,orgName)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .orderByAsc(EsbOrganizationDO::getOrgId);
        return this.list(queryWrapper);
    }

    @Override
    public List<EsbOrgInfoTreeBO> listTree() {
        LambdaQueryWrapper<EsbOrganizationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName, EsbOrganizationDO::getOrgPid, EsbOrganizationDO::getState);
        List<EsbOrganizationDO> list = this.list(wrapper);

        // 获取业务架构标签类型
        List<EsbBusinessOrganizationDO> businessOrganizationDOList = esbBusinessOrganizationService.list();
        Map<Long, Integer> tagTypeMap = businessOrganizationDOList.stream().collect(Collectors.toMap(EsbBusinessOrganizationDO::getOrgId, EsbBusinessOrganizationDO::getTagType));

        List<EsbOrganizationBO> organizationBOList = PojoUtils.map(list, EsbOrganizationBO.class);
        organizationBOList.forEach(esbOrganizationBO -> esbOrganizationBO.setTagType(tagTypeMap.get(esbOrganizationBO.getOrgId())));

        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("orgId");
        //默认为parentId可以不设置
        config.setParentIdKey("orgPid");
        config.setNameKey("orgName");
        //排序字段
        config.setWeightKey("orgId");

        List<Tree<Long>> treeNodes = TreeUtil.build(organizationBOList, 0L, config, (treeNode, tree) -> {
            tree.setId(treeNode.getOrgId());
            tree.setParentId(treeNode.getOrgPid());
            tree.setName(treeNode.getOrgName());
            // 扩展属性
            tree.putExtra("tagType", treeNode.getTagType());
            tree.putExtra("state", treeNode.getState());
            tree.putExtra("disabled", treeNode.getState() != 0);
        });

        return PojoUtils.map(treeNodes, EsbOrgInfoTreeBO.class);
    }


    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<EsbOrganizationDO> listSuffixByOrgIds(List<Long> orgIds,String tableSuffix) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(EsbOrganizationDO::getOrgId, orgIds)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .orderByAsc(EsbOrganizationDO::getOrgId);
        return this.list(queryWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<SimpleEsbOrgInfoBO> listSuffixAll(boolean setParentOrgInfoFlag, String tableSuffix) {
        QueryWrapper<EsbOrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName, EsbOrganizationDO::getOrgPid)
                .eq(EsbOrganizationDO::getState, ORG_STATE_NORMAL)
                .eq(EsbOrganizationDO::getStatus, ORG_STATUS_NORMAL)
                .orderByAsc(EsbOrganizationDO::getOrgId);

        List<EsbOrganizationDO> doList = this.list(queryWrapper);
        List<SimpleEsbOrgInfoBO> boList = PojoUtils.map(doList, SimpleEsbOrgInfoBO.class);
        if (!setParentOrgInfoFlag) {
            return boList;
        }

        Map<Long, SimpleEsbOrgInfoBO> map = boList.stream().collect(Collectors.toMap(SimpleEsbOrgInfoBO::getOrgId, Function.identity()));
        // 设置上级部门信息
        boList.forEach(e -> e.setParentOrgInfo(map.get(e.getOrgPid())));
        return boList;
    }
}
