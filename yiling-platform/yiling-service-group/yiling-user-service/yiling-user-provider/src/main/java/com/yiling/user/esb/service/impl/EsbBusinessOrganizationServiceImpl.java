package com.yiling.user.esb.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.bo.EsbBusinessOrgTreeBO;
import com.yiling.user.esb.bo.SimpleEsbBzOrgBO;
import com.yiling.user.esb.dto.request.DeleteBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.SaveBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.UpdateTargetStatusRequest;
import com.yiling.user.esb.entity.EsbBusinessOrganizationDO;
import com.yiling.user.esb.dao.EsbBusinessOrganizationMapper;
import com.yiling.user.esb.entity.EsbOrganizationDO;
import com.yiling.user.esb.enums.EsbBusinessOrganizationTagTypeEnum;
import com.yiling.user.esb.service.EsbBusinessOrganizationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.esb.service.EsbOrganizationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * esb业务架构 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-12
 */
@Slf4j
@Service
public class EsbBusinessOrganizationServiceImpl extends BaseServiceImpl<EsbBusinessOrganizationMapper, EsbBusinessOrganizationDO> implements EsbBusinessOrganizationService {

    @Autowired
    EsbOrganizationService esbOrganizationService;
    @Lazy
    @Autowired
    EsbBusinessOrganizationServiceImpl _this;

    @Override
    public boolean saveBusinessOrg(SaveBusinessOrganizationRequest request) {
        long time = System.currentTimeMillis();
        // 获取已经存在的打标数据加入到将要处理的集合中
        List<EsbBusinessOrganizationDO> businessOrganizationDOList = this.list();
        if (CollUtil.isNotEmpty(businessOrganizationDOList)) {
            List<SaveBusinessOrganizationRequest.MarkingOrg> markingOrgList = PojoUtils.map(businessOrganizationDOList, SaveBusinessOrganizationRequest.MarkingOrg.class);
            request.getMarkingOrgList().addAll(markingOrgList);
        }
        // 校验部门架构打标的结构是否符合要求
        this.checkTag(request);

        // 生成业务架构
        _this.genBusinessOrg(request);
        log.info("生成业务架构耗时={}毫秒", System.currentTimeMillis() - time);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setTargetStatus(UpdateTargetStatusRequest request) {
        // 获取当前勾选节点的所有下级
        List<EsbBusinessOrganizationDO> list = this.list();
        List<EsbBusinessOrganizationDO> childList = ListUtil.toList();
        childList = this.getAllChildBzOrg(request.getId(), list, childList);

        EsbBusinessOrganizationDO organizationDO = PojoUtils.map(request, EsbBusinessOrganizationDO.class);
        this.updateById(organizationDO);

        childList.forEach(esbBusinessOrganizationDO -> {
            EsbBusinessOrganizationDO businessOrganizationDO = new EsbBusinessOrganizationDO();
            businessOrganizationDO.setId(esbBusinessOrganizationDO.getId());
            businessOrganizationDO.setTargetStatus(request.getTargetStatus());
            businessOrganizationDO.setOpUserId(request.getOpUserId());
            this.updateById(businessOrganizationDO);
        });

        return true;
    }

    @Override
    public List<EsbBusinessOrgTreeBO> queryBzOrgTree() {
        List<EsbBusinessOrganizationDO> list = this.list();
        if (CollUtil.isEmpty(list)) {
            return ListUtil.toList();
        }

        List<Long> orgIdList = list.stream().map(EsbBusinessOrganizationDO::getOrgId).distinct().collect(Collectors.toList());
        Map<Long, String> orgNameMap = esbOrganizationService.listByOrgIds(orgIdList).stream().collect(Collectors.toMap(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName));

        List<EsbBusinessOrgTreeBO> organizationBOList = PojoUtils.map(list, EsbBusinessOrgTreeBO.class);
        organizationBOList.forEach(esbBusinessOrgTreeBO -> esbBusinessOrgTreeBO.setOrgName(orgNameMap.get(esbBusinessOrgTreeBO.getOrgId())));

        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("id");
        //默认为parentId可以不设置
        config.setParentIdKey("bzPid");
        config.setNameKey("orgName");
        //排序字段
        config.setWeightKey("orgId");

        List<Tree<Long>> treeNodes = TreeUtil.build(organizationBOList, 0L, config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getBzPid());
            tree.setName(treeNode.getOrgName());
            // 扩展属性
            tree.putExtra("targetStatus", treeNode.getTargetStatus());
            tree.putExtra("tagType", treeNode.getTagType());
            tree.putExtra("orgId", treeNode.getOrgId());
            tree.putExtra("orgPid", treeNode.getOrgPid());
        });

        return PojoUtils.map(treeNodes, EsbBusinessOrgTreeBO.class);
    }

    @Override
    public List<SimpleEsbBzOrgBO> getBzOrgListByTagType(EsbBusinessOrganizationTagTypeEnum tagTypeEnum) {
        LambdaQueryWrapper<EsbBusinessOrganizationDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(tagTypeEnum)) {
            wrapper.eq(EsbBusinessOrganizationDO::getTagType, tagTypeEnum.getCode());
        }
        List<EsbBusinessOrganizationDO> list = this.list(wrapper);

        List<Long> orgIdList = list.stream().map(EsbBusinessOrganizationDO::getOrgId).distinct().collect(Collectors.toList());
        Map<Long, String> orgNameMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(orgIdList)) {
            orgNameMap = esbOrganizationService.listByOrgIds(orgIdList).stream().collect(Collectors.toMap(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName));
        }

        List<SimpleEsbBzOrgBO> simpleEsbBzOrgBOList = PojoUtils.map(this.list(wrapper), SimpleEsbBzOrgBO.class);
        Map<Long, String> finalOrgNameMap = orgNameMap;
        simpleEsbBzOrgBOList.forEach(simpleEsbBzOrgBO -> simpleEsbBzOrgBO.setOrgName(finalOrgNameMap.get(simpleEsbBzOrgBO.getOrgId())));
        return simpleEsbBzOrgBOList;
    }

    @Override
    public List<SimpleEsbBzOrgBO> getBzOrgListByOrgId(Long orgId, EsbBusinessOrganizationTagTypeEnum tagTypeEnum) {
        LambdaQueryWrapper<EsbBusinessOrganizationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EsbBusinessOrganizationDO::getOrgId, orgId);
        wrapper.eq(EsbBusinessOrganizationDO::getTagType, EsbBusinessOrganizationTagTypeEnum.BUSINESS_TAG.getCode());
        wrapper.last("limit 1");
        EsbBusinessOrganizationDO businessOrganizationDO = this.getOne(wrapper);
        if (Objects.isNull(businessOrganizationDO)) {
            return ListUtil.toList();
        }

        List<EsbBusinessOrganizationDO> businessOrganizationDOList = ListUtil.toList();
        if (tagTypeEnum == EsbBusinessOrganizationTagTypeEnum.PROVINCE_TAG) {
            wrapper.clear();
            wrapper.eq(EsbBusinessOrganizationDO::getBzPid, businessOrganizationDO.getId());
            businessOrganizationDOList = this.list(wrapper);

        } else if (tagTypeEnum == EsbBusinessOrganizationTagTypeEnum.REGION_TAG) {
            // 获取当前节点的所有下级
            List<EsbBusinessOrganizationDO> list = this.list();
            List<EsbBusinessOrganizationDO> childList = ListUtil.toList();
            childList = this.getAllChildBzOrg(businessOrganizationDO.getId(), list, childList);

            businessOrganizationDOList = childList.stream().filter(esbBusinessOrganizationDO -> esbBusinessOrganizationDO.getTagType().equals(tagTypeEnum.getCode())).collect(Collectors.toList());
        }

        if (CollUtil.isEmpty(businessOrganizationDOList)) {
            return ListUtil.toList();
        }

        List<Long> orgIdList = businessOrganizationDOList.stream().map(EsbBusinessOrganizationDO::getOrgId).distinct().collect(Collectors.toList());
        Map<Long, String> orgNameMap = esbOrganizationService.listByOrgIds(orgIdList).stream().collect(Collectors.toMap(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName));

        List<SimpleEsbBzOrgBO> simpleEsbBzOrgBOList = PojoUtils.map(businessOrganizationDOList, SimpleEsbBzOrgBO.class);
        simpleEsbBzOrgBOList.forEach(simpleEsbBzOrgBO -> simpleEsbBzOrgBO.setOrgName(orgNameMap.get(simpleEsbBzOrgBO.getOrgId())));
        return simpleEsbBzOrgBOList;

    }

    @Override
    public boolean deleteTag(DeleteBusinessOrganizationRequest request) {
        // 校验该标签是否存在子节点，存在则无法删除
        LambdaQueryWrapper<EsbBusinessOrganizationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EsbBusinessOrganizationDO::getOrgId, request.getOrgId());
        wrapper.last("limit 1");
        EsbBusinessOrganizationDO organizationDO = this.getOne(wrapper);
        if (Objects.isNull(organizationDO)) {
            return false;
        }

        wrapper.clear();
        wrapper.eq(EsbBusinessOrganizationDO::getBzPid, organizationDO.getId());
        List<EsbBusinessOrganizationDO> list = this.list(wrapper);

        if (CollUtil.isNotEmpty(list)) {
            throw new BusinessException(ResultCode.FAILED, "当前打标节点存在子节点，无法删除");
        }

        EsbBusinessOrganizationDO businessOrganizationDO = new EsbBusinessOrganizationDO();
        businessOrganizationDO.setId(organizationDO.getId());
        businessOrganizationDO.setOpUserId(request.getOpUserId());
        return this.deleteByIdWithFill(businessOrganizationDO) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void genBusinessOrg(SaveBusinessOrganizationRequest request) {
        List<SaveBusinessOrganizationRequest.MarkingOrg> markingOrgList = request.getMarkingOrgList();
        Map<Integer, List<SaveBusinessOrganizationRequest.MarkingOrg>> map = markingOrgList.stream().collect(Collectors.groupingBy(SaveBusinessOrganizationRequest.MarkingOrg::getTagType));

        // 省区打标数据
        List<SaveBusinessOrganizationRequest.MarkingOrg> orgList = map.get(EsbBusinessOrganizationTagTypeEnum.PROVINCE_TAG.getCode());
        List<Long> provinceList = ListUtil.toList();
        if (CollUtil.isNotEmpty(orgList)) {
            provinceList = orgList.stream().map(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId).collect(Collectors.toList());
        }
        // 区办打标数据
        List<SaveBusinessOrganizationRequest.MarkingOrg> regionOrgList = map.get(EsbBusinessOrganizationTagTypeEnum.REGION_TAG.getCode());
        List<Long> regionList = ListUtil.toList();
        if (CollUtil.isNotEmpty(regionOrgList)) {
            regionList = regionOrgList.stream().map(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId).collect(Collectors.toList());
        }

        // 查询所有部门架构数据
        LambdaQueryWrapper<EsbOrganizationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgName, EsbOrganizationDO::getOrgPid);
        wrapper.eq(EsbOrganizationDO::getState, 0);
        wrapper.eq(EsbOrganizationDO::getStatus,0);
        List<EsbOrganizationDO> organizationDOList = esbOrganizationService.list(wrapper);
        Map<Long, Long> orgPidMap = organizationDOList.stream().collect(Collectors.toMap(EsbOrganizationDO::getOrgId, EsbOrganizationDO::getOrgPid));

        // 获取已经存在的打标数据
        List<EsbBusinessOrganizationDO> businessOrganizationDOList = this.list();
        Map<Long, Integer> tagStatusMap = businessOrganizationDOList.stream().collect(Collectors.toMap(EsbBusinessOrganizationDO::getOrgId, EsbBusinessOrganizationDO::getTargetStatus));

        // 清除旧的业务架构数据
        LambdaQueryWrapper<EsbBusinessOrganizationDO> clearWrapper = new LambdaQueryWrapper<>();
        EsbBusinessOrganizationDO organization = new EsbBusinessOrganizationDO();
        organization.setOpUserId(request.getOpUserId());
        this.batchDeleteWithFill(organization, clearWrapper);

        // 所有事业部
        List<Long> businessList = markingOrgList.stream().filter(markingOrg -> markingOrg.getTagType().equals(EsbBusinessOrganizationTagTypeEnum.BUSINESS_TAG.getCode())).map(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId).collect(Collectors.toList());

        List<Long> finalProvinceList = provinceList;
        List<Long> finalRegionList = regionList;
        businessList.forEach(orgId -> {

            EsbBusinessOrgTreeBO businessOrgTreeBO = new EsbBusinessOrgTreeBO();
            businessOrgTreeBO.setOrgId(orgId);
            businessOrgTreeBO.setBzPid(0L);
            businessOrgTreeBO.setTagType(EsbBusinessOrganizationTagTypeEnum.BUSINESS_TAG.getCode());
            businessOrgTreeBO.setTargetStatus(tagStatusMap.getOrDefault(orgId, 0));
            businessOrgTreeBO.setOrgPid(orgPidMap.getOrDefault(orgId, 0L));
            // 保存事业部节点
            EsbBusinessOrganizationDO esbBusinessOrganizationDO = PojoUtils.map(businessOrgTreeBO, EsbBusinessOrganizationDO.class);
            esbBusinessOrganizationDO.setOpUserId(request.getOpUserId());
            this.save(esbBusinessOrganizationDO);

            // 获取当前事业部所有的下级节点
            List<EsbOrganizationDO> childList = ListUtil.toList();
            childList = getAllChildOrg(orgId, organizationDOList, childList);

            List<EsbBusinessOrgTreeBO> provinceTagList = ListUtil.toList();
            childList.forEach(esbOrganizationDO -> {
                if (finalProvinceList.contains(esbOrganizationDO.getOrgId())) {
                    // 构建省区打标
                    EsbBusinessOrgTreeBO provinceOrgTreeBO = new EsbBusinessOrgTreeBO();
                    provinceOrgTreeBO.setOrgId(esbOrganizationDO.getOrgId());
                    provinceOrgTreeBO.setBzPid(esbBusinessOrganizationDO.getId());
                    provinceOrgTreeBO.setTagType(EsbBusinessOrganizationTagTypeEnum.PROVINCE_TAG.getCode());
                    provinceOrgTreeBO.setTargetStatus(tagStatusMap.getOrDefault(esbOrganizationDO.getOrgId(), 0));
                    provinceOrgTreeBO.setOrgPid(orgPidMap.getOrDefault(esbOrganizationDO.getOrgId(), 0L));
                    provinceTagList.add(provinceOrgTreeBO);
                }
            });

            if (CollUtil.isNotEmpty(provinceTagList)) {
                List<EsbBusinessOrganizationDO> provinceOrganizationDOList = PojoUtils.map(provinceTagList, EsbBusinessOrganizationDO.class);
                provinceOrganizationDOList.forEach(organizationDO -> {
                    organizationDO.setOpUserId(request.getOpUserId());
                    this.save(organizationDO);
                });

                List<EsbBusinessOrgTreeBO> regionTagList = ListUtil.toList();
                provinceOrganizationDOList.forEach(esbOrganizationDO -> {

                    List<EsbOrganizationDO> provinceChildList = ListUtil.toList();
                    provinceChildList = getAllChildOrg(esbOrganizationDO.getOrgId(), organizationDOList, provinceChildList);

                    provinceChildList.forEach(organizationDO -> {
                        if (finalRegionList.contains(organizationDO.getOrgId())) {
                            // 构建区办打标
                            EsbBusinessOrgTreeBO provinceOrgTreeBO = new EsbBusinessOrgTreeBO();
                            provinceOrgTreeBO.setOrgId(organizationDO.getOrgId());
                            provinceOrgTreeBO.setBzPid(esbOrganizationDO.getId());
                            provinceOrgTreeBO.setTagType(EsbBusinessOrganizationTagTypeEnum.REGION_TAG.getCode());
                            provinceOrgTreeBO.setTargetStatus(tagStatusMap.getOrDefault(esbOrganizationDO.getOrgId(), 0));
                            provinceOrgTreeBO.setOrgPid(orgPidMap.getOrDefault(organizationDO.getOrgId(), 0L));
                            regionTagList.add(provinceOrgTreeBO);
                        }
                    });

                });

                if (CollUtil.isNotEmpty(regionTagList)) {
                    List<EsbBusinessOrganizationDO> regionOrganizationDOList = PojoUtils.map(regionTagList, EsbBusinessOrganizationDO.class);
                    regionOrganizationDOList.forEach(organizationDO -> organizationDO.setOpUserId(request.getOpUserId()));
                    this.saveBatch(regionOrganizationDOList);
                }
                log.info("事业部ID={} 部门ID={} -> 省区={} -> 区办={}", esbBusinessOrganizationDO.getId(), esbBusinessOrganizationDO.getOrgId(), JSONObject.toJSONString(provinceTagList), JSONObject.toJSONString(regionTagList));
            }

        });

    }

    /**
     * 校验部门架构打标的结构是否符合要求
     *
     * @param request
     */
    public void checkTag(SaveBusinessOrganizationRequest request) {
        // 全部打标数据
        List<SaveBusinessOrganizationRequest.MarkingOrg> markingOrgList = request.getMarkingOrgList();
        // 全部打标数据map
        Map<Long, Integer> tagTypeMap = markingOrgList.stream().collect(Collectors.toMap(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId, SaveBusinessOrganizationRequest.MarkingOrg::getTagType));
        // 获取全部部门架构数据
        LambdaQueryWrapper<EsbOrganizationDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(EsbOrganizationDO::getStatus, 0);
        queryWrapper.eq(EsbOrganizationDO::getState, 0);
        List<EsbOrganizationDO> esbOrganizationDOList = esbOrganizationService.list(queryWrapper);
        Map<Long, EsbOrganizationDO> orgMap = esbOrganizationDOList.stream().collect(Collectors.toMap(EsbOrganizationDO::getOrgId, Function.identity()));

        // 所有事业部
        List<Long> businessList = markingOrgList.stream().filter(markingOrg -> markingOrg.getTagType().equals(EsbBusinessOrganizationTagTypeEnum.BUSINESS_TAG.getCode())).map(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId).collect(Collectors.toList());

        businessList.forEach(orgId -> {
            // 获取当前部门的所有父级部门
            List<EsbOrganizationDO> allParentOrgList = ListUtil.toList();
            allParentOrgList = this.getAllParentOrg(orgId, orgMap, allParentOrgList);

            // 事务部打标校验：校验勾选的节点的上级都满足没有打标的
            allParentOrgList.forEach(esbOrganizationDO -> {
                Integer tagType = tagTypeMap.getOrDefault(esbOrganizationDO.getOrgId(), 0);
                if (tagType != 0) {
                    throw new BusinessException(ResultCode.FAILED, "事业部标签层级有误");
                }
            });

        });

        // 所有业务省区
        List<Long> provinceList = markingOrgList.stream().filter(markingOrg -> markingOrg.getTagType().equals(EsbBusinessOrganizationTagTypeEnum.PROVINCE_TAG.getCode())).map(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId).collect(Collectors.toList());

        provinceList.forEach(orgId -> {
            // 获取当前部门的所有父级部门
            List<EsbOrganizationDO> allParentOrgList = ListUtil.toList();
            allParentOrgList = this.getAllParentOrg(orgId, orgMap, allParentOrgList);
            // 业务省区打标校验：勾选的节点上级都满足只存在“事业部”标签
            List<Integer> typeList = ListUtil.toList();
            allParentOrgList.forEach(esbOrganizationDO -> {
                Integer tagType = tagTypeMap.getOrDefault(esbOrganizationDO.getOrgId(), 0);
                if (tagType != 0) {
                    typeList.add(tagType);
                }
            });
            if (CollUtil.isEmpty(typeList) || typeList.size() != 1 ||
                    typeList.contains(EsbBusinessOrganizationTagTypeEnum.PROVINCE_TAG.getCode()) || typeList.contains(EsbBusinessOrganizationTagTypeEnum.REGION_TAG.getCode())) {
                throw new BusinessException(ResultCode.FAILED, "业务省区标签层级有误");
            }
        });

        // 所有区办
        List<Long> regionList = markingOrgList.stream().filter(markingOrg -> markingOrg.getTagType().equals(EsbBusinessOrganizationTagTypeEnum.REGION_TAG.getCode())).map(SaveBusinessOrganizationRequest.MarkingOrg::getOrgId).collect(Collectors.toList());

        regionList.forEach(orgId -> {
            // 获取当前部门的所有父级部门
            List<EsbOrganizationDO> allParentOrgList = ListUtil.toList();
            allParentOrgList = this.getAllParentOrg(orgId, orgMap, allParentOrgList);
            // 区办打标校验：勾选的节点最近的上级有打标“业务省区”标签
            List<Integer> typeList = ListUtil.toList();
            allParentOrgList.forEach(esbOrganizationDO -> {
                Integer tagType = tagTypeMap.getOrDefault(esbOrganizationDO.getOrgId(), 0);
                if (tagType != 0) {
                    typeList.add(tagType);
                }
            });
            if (CollUtil.isEmpty(typeList) || typeList.size() != 2) {
                throw new BusinessException(ResultCode.FAILED, "区办标签层级有误");
            }
        });

    }

    /**
     * 获取所有的父级部门架构
     *
     * @param orgId 部门ID
     * @param parentList 空集合用于接收返回的数据
     * @return
     */
    public List<EsbOrganizationDO> getAllParentOrg(Long orgId, Map<Long, EsbOrganizationDO> orgMap, List<EsbOrganizationDO> parentList) {
        EsbOrganizationDO organizationDO = orgMap.get(orgId);

        EsbOrganizationDO parentOrg = orgMap.get(organizationDO.getOrgPid());
        if (Objects.nonNull(parentOrg)) {
            parentList.add(parentOrg);
            this.getAllParentOrg(parentOrg.getOrgId(), orgMap, parentList);
        }

        return parentList;
    }

    /**
     * 获取所有的子级部门架构
     *
     * @param orgId 部门ID
     * @param organizationDOList 所有的部门架构数据
     * @param childList 空集合用于接收返回的数据
     * @return
     */
    public List<EsbOrganizationDO> getAllChildOrg(Long orgId, List<EsbOrganizationDO> organizationDOList, List<EsbOrganizationDO> childList) {

        for(EsbOrganizationDO esbOrganizationDO : organizationDOList){
            if(orgId.equals(esbOrganizationDO.getOrgPid())){
                //添加子级节点
                childList.add(esbOrganizationDO);
                getAllChildOrg(esbOrganizationDO.getOrgId(), organizationDOList, childList);
            }
        }

        return childList;
    }

    /**
     * 获取当前节点的所有的子级业务架构
     *
     * @param id 业务架构表ID
     * @param allBzOrgDOList 所有的业务架构数据
     * @param childList 空集合用于接收返回的数据
     * @return
     */
    public List<EsbBusinessOrganizationDO> getAllChildBzOrg(Long id, List<EsbBusinessOrganizationDO> allBzOrgDOList, List<EsbBusinessOrganizationDO> childList) {

        for(EsbBusinessOrganizationDO businessOrganizationDO : allBzOrgDOList){
            if(id.equals(businessOrganizationDO.getBzPid())){
                //添加子级节点
                childList.add(businessOrganizationDO);
                getAllChildBzOrg(businessOrganizationDO.getId(), allBzOrgDOList, childList);
            }
        }

        return childList;
    }

}
