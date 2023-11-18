package com.yiling.open.erp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.SjmsFlowMonitorNoDataSyncEnterpriseBO;
import com.yiling.open.erp.dao.ErpClientMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientParentQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.dto.request.ErpMonitorQueryRequest;
import com.yiling.open.erp.dto.request.QueryClientFlowEnterpriseRequest;
import com.yiling.open.erp.dto.request.UpdateClientStatusRequest;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.dto.request.UpdateMonitorStatusRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.enums.ClientRunningStatusEnum;
import com.yiling.open.erp.enums.ClientStatusEnum;
import com.yiling.open.erp.enums.ErpEnterpriseSyncStatus;
import com.yiling.open.erp.enums.ErpFlowLevelEnum;
import com.yiling.open.erp.enums.ErpMonitorCountStatisticsOpenTypeEnum;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.util.RandomStringUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 客户端抽取工具实例表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2020-08-12
 */
@Service
@CacheConfig(cacheNames = "open:erpClient")
public class ErpClientServiceImpl extends BaseServiceImpl<ErpClientMapper, ErpClientDO> implements ErpClientService {

    @Autowired
    private ErpClientMapper erpClientMapper;

    //    @DubboReference
    //    private EnterpriseApi enterpriseApi;

    @Override
    @Cacheable(key = "#suId+'+'+#suDeptNo+'+getErpClientBySuIdAndSuDeptNo'")
    public ErpClientDTO getErpClientBySuIdAndSuDeptNo(Long suId, String suDeptNo) {
        QueryWrapper<ErpClientDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpClientDO::getSuId, suId).eq(ErpClientDO::getSuDeptNo, suDeptNo).eq(ErpClientDO::getSyncStatus, 1);
        return PojoUtils.map(this.getOne(queryWrapper),ErpClientDTO.class);
    }

    @Override
    @Cacheable(key = "#key+'+selectByClientKey'")
    public ErpClientDTO selectByClientKey(String key) {
        return PojoUtils.map(erpClientMapper.selectByClientKey(key),ErpClientDTO.class);
    }

    @Override
    @Cacheable(key = "#suId+'+selectBySuId'")
    public List<ErpClientDTO> selectBySuId(Long suId) {
        QueryWrapper<ErpClientDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpClientDO::getSuId, suId);
        return PojoUtils.map(this.list(queryWrapper),ErpClientDTO.class);
    }

    @Override
    @Cacheable(key = "#rkSuId+'+selectByRkSuId'")
    public ErpClientDTO selectByRkSuId(Long rkSuId) {
        QueryWrapper<ErpClientDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpClientDO::getRkSuId, rkSuId);
        return PojoUtils.map(this.getOne(queryWrapper),ErpClientDTO.class);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateCommandBySuId(ErpClientSaveRequest erpClientSaveRequest) {
        QueryWrapper<ErpClientDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpClientDO::getSuId, erpClientSaveRequest.getSuId());

        ErpClientDO erpClientDO = new ErpClientDO();
        erpClientDO.setCommandTime(erpClientSaveRequest.getCommandTime());
        erpClientDO.setCommandStatus(erpClientSaveRequest.getCommandStatus());
        erpClientDO.setCommand(erpClientSaveRequest.getCommand());
        erpClientDO.setUpdateUser(erpClientSaveRequest.getOpUserId());
        erpClientDO.setUpdateTime(new Date());
        return this.update(erpClientDO, queryWrapper);
    }


    @Override
    @CacheEvict(allEntries = true)
    public Long saveOrUpdateErpClient(ErpClientSaveRequest erpClientSaveRequest) {
        QueryWrapper<ErpClientDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpClientDO::getRkSuId, erpClientSaveRequest.getRkSuId());
        ErpClientDO erpClient = this.getOne(queryWrapper);
        Long opUserId = erpClientSaveRequest.getOpUserId();
        Date date = new Date();

        if (erpClient == null) {
            erpClient = saveErpClient(erpClientSaveRequest, opUserId, date);
        } else {
            updateErpClient(erpClientSaveRequest, queryWrapper, erpClient, opUserId, date);
        }
        return erpClient.getId();
    }

    private void updateErpClient(ErpClientSaveRequest erpClientSaveRequest, QueryWrapper<ErpClientDO> queryWrapper, ErpClientDO erpClient, Long opUserId, Date date) {
        ErpClientDO erpClientUpdate = new ErpClientDO();
        erpClientUpdate.setId(erpClient.getId());
        erpClientUpdate.setSuDeptNo(erpClientSaveRequest.getSuDeptNo());
        erpClientUpdate.setDepth(erpClientSaveRequest.getDepth());
        erpClientUpdate.setFlowLevel(erpClientSaveRequest.getFlowLevel());
        erpClientUpdate.setInstallEmployee(erpClientSaveRequest.getInstallEmployee());
        erpClientUpdate.setCommand(erpClientSaveRequest.getCommand());
        erpClientUpdate.setSyncStatus(erpClientSaveRequest.getSyncStatus());
        erpClientUpdate.setClientStatus(erpClientSaveRequest.getClientStatus());
        erpClientUpdate.setErpBrand(erpClientSaveRequest.getErpBrand());
        erpClientUpdate.setBusinessEmployee(erpClientSaveRequest.getBusinessEmployee());
        erpClientUpdate.setFlowMode(erpClientSaveRequest.getFlowMode());
        erpClientUpdate.setRemark(erpClientSaveRequest.getRemark());
        erpClientUpdate.setUpdateTime(date);
        erpClientUpdate.setUpdateUser(opUserId);
        // 对接时间
        Date depthTime = handlerDepthTime(erpClientSaveRequest, date, erpClient);
        if (ObjectUtil.isNotNull(depthTime)) {
            erpClientUpdate.setDepthTime(depthTime);
        }
        // 同步状态开启时间
        if (ObjectUtil.equal(ErpEnterpriseSyncStatus.SYNCING.getCode(), erpClientSaveRequest.getSyncStatus())) {
            erpClientUpdate.setSyncStatusTime(date);
        }
        // crm企业id
        Long crmEnterpriseId = 0L;
        if (ObjectUtil.isNotNull(erpClientSaveRequest.getCrmEnterpriseId()) && 0 != erpClientSaveRequest.getCrmEnterpriseId()) {
            crmEnterpriseId = erpClientSaveRequest.getCrmEnterpriseId();
        }
        erpClientUpdate.setCrmEnterpriseId(crmEnterpriseId);
        // crm所属省份编码
        String crmProvinceCode = "";
        if (StrUtil.isNotBlank(erpClientSaveRequest.getCrmProvinceCode())) {
            crmProvinceCode = erpClientSaveRequest.getCrmProvinceCode();
        }
        erpClientUpdate.setCrmProvinceCode(crmProvinceCode);
        this.update(erpClientUpdate, queryWrapper);
    }

    private ErpClientDO saveErpClient(ErpClientSaveRequest erpClientSaveRequest, Long opUserId, Date date) {
        ErpClientDO erpClient = PojoUtils.map(erpClientSaveRequest, ErpClientDO.class);
        // 无父类公司的保存时生成key、密匙
        if (ObjectUtil.equal(erpClient.getSuId(), erpClient.getRkSuId())) {
            String key = getKeyOrSecret(1);
            erpClient.setClientKey(key);
            String secret = getKeyOrSecret(2);
            erpClient.setClientSecret(secret);
        }
        erpClient.setClientStatus(erpClientSaveRequest.getClientStatus());
        // 对接时间
        if (ObjectUtil.equal(ErpEnterpriseSyncStatus.SYNCING.getCode(), erpClientSaveRequest.getSyncStatus())) {
            erpClient.setDepthTime(date);
            // 同步状态开启时间
            erpClient.setSyncStatusTime(date);
        }
        erpClient.setId(null);
        erpClient.setCreateTime(date);
        erpClient.setCreateUser(opUserId);
        erpClient.setUpdateTime(date);
        erpClient.setUpdateUser(opUserId);
        this.save(erpClient);
        return erpClient;
    }

    /**
     * 第一次开启同步状态时保存对接时间
     *
     * @param erpClientSaveRequest
     * @param date
     * @param erpClient
     */
    private Date handlerDepthTime(ErpClientSaveRequest erpClientSaveRequest, Date date, ErpClientDO erpClient) {
        if (ObjectUtil.equal(ErpEnterpriseSyncStatus.SYNCING.getCode(), erpClientSaveRequest.getSyncStatus())) {
            Date depthTime = erpClient.getDepthTime();
            if (ObjectUtil.isNotNull(depthTime) && ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(depthTime, "yyyy-MM-dd HH:mm:ss"))) {
                return date;
            }
        }
        return null;
    }

    /**
     * 生成 key 或 secret
     *
     * @param type 1-key 2-secret
     * @return
     */
    private String getKeyOrSecret(int type) {
        String result = RandomStringUtil.randomStr(32);

        ErpClientDTO erpClientDb;
        if (type == 1) {
            // 校验key是否重复
            erpClientDb = this.selectByClientKey(result);
            if (ObjectUtil.isNull(erpClientDb)) {
                return result;
            } else {
                this.getKeyOrSecret(type);
            }
        } else {
            // 校验secret是否重复
            erpClientDb = this.selectByClientSecret(result);
            if (ObjectUtil.isNull(erpClientDb)) {
                return result;
            } else {
                this.getKeyOrSecret(type);
            }
        }
        return null;
    }

    @Override
    public Page<ErpClientDO> page(ErpClientQueryRequest request) {
        Page<ErpClientDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, getErpClientPageQueryWrapper(request));
    }

    private LambdaQueryWrapper<ErpClientDO> getErpClientPageQueryWrapper(ErpClientQueryRequest request) {
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        Long suId = request.getSuId();
        if (suId != null && suId != 0) {
            lambdaQueryWrapper.eq(ErpClientDO::getSuId, suId);
        }

        Long rkSuId = request.getRkSuId();
        if (rkSuId != null && rkSuId != 0) {
            lambdaQueryWrapper.eq(ErpClientDO::getRkSuId, rkSuId);
        }

        Integer biStatus = request.getBiStatus();
        if (biStatus != null && biStatus != 0) {
            lambdaQueryWrapper.eq(ErpClientDO::getBiStatus, biStatus);
        }

        String clientName = request.getClientName();
        if (StrUtil.isNotBlank(clientName)) {
            lambdaQueryWrapper.like(ErpClientDO::getClientName, clientName);
        }

        Integer clientStatus = request.getClientStatus();
        if (clientStatus != null && clientStatus.intValue() != -1) {
            lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, clientStatus);
        }

        Integer depth = request.getDepth();
        if (depth != null && depth.intValue() != -1) {
            lambdaQueryWrapper.eq(ErpClientDO::getDepth, depth);
        }

        Integer syncStatus = request.getSyncStatus();
        if (syncStatus != null && syncStatus.intValue() != -1) {
            lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, syncStatus);
        }

        Integer flow = request.getFlowLevel();
        if (flow != null && flow.intValue() != -1) {
            lambdaQueryWrapper.eq(ErpClientDO::getFlowLevel, flow);
        }

        Integer flowStatus = request.getFlowStatus();
        if (flowStatus != null && flowStatus.intValue() == 1) {
            lambdaQueryWrapper.in(ErpClientDO::getFlowLevel, Arrays.asList(1, 2));
        }

        String installEmployee = request.getInstallEmployee();
        if (StrUtil.isNotBlank(installEmployee)) {
            lambdaQueryWrapper.like(ErpClientDO::getInstallEmployee, installEmployee);
        }

        Integer monitorStatus = request.getMonitorStatus();
        if (monitorStatus != null && monitorStatus.intValue() != -1) {
            lambdaQueryWrapper.eq(ErpClientDO::getMonitorStatus, monitorStatus);
        }

        Integer flowMode = request.getFlowMode();
        if (ObjectUtil.isNotNull(flowMode) && 0 != flowMode.intValue()) {
            lambdaQueryWrapper.eq(ErpClientDO::getFlowMode, flowMode);
        }

        Date heartBeatTimStart = request.getHeartBeatTimStart();
        if (ObjectUtil.isNotNull(heartBeatTimStart)) {
            lambdaQueryWrapper.gt(ErpClientDO::getHeartBeatTime, heartBeatTimStart);
        }

        Date heartBeatTimEnd = request.getHeartBeatTimEnd();
        if (ObjectUtil.isNotNull(heartBeatTimEnd)) {
            lambdaQueryWrapper.lt(ErpClientDO::getHeartBeatTime, heartBeatTimEnd);
        }

        Integer dataInitStatus = request.getDataInitStatus();
        if (ObjectUtil.isNotNull(dataInitStatus)) {
            lambdaQueryWrapper.eq(ErpClientDO::getDataInitStatus, dataInitStatus);
        }

        Long crmEnterpriseId = request.getCrmEnterpriseId();
        if (ObjectUtil.isNotNull(crmEnterpriseId)) {
            lambdaQueryWrapper.eq(ErpClientDO::getCrmEnterpriseId, crmEnterpriseId);
        }

        lambdaQueryWrapper.isNotNull(ErpClientDO::getDepthTime);
        lambdaQueryWrapper.orderByDesc(ErpClientDO::getId);
        lambdaQueryWrapper.orderByDesc(ErpClientDO::getCreateTime);
        return lambdaQueryWrapper;
    }

    @Override
    public List<ErpClientDO> selectByRkSuIdList(List<Long> rkSuIdList) {
        if (CollUtil.isEmpty(rkSuIdList)) {
            return ListUtil.empty();
        }
        QueryWrapper<ErpClientDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ErpClientDO::getRkSuId, rkSuIdList);
        return this.list(queryWrapper);
    }

    @Override
    @Cacheable(key = "#secret+'+selectByClientSecret'")
    public ErpClientDTO selectByClientSecret(String secret) {
        return PojoUtils.map(erpClientMapper.selectByClientSecret(secret),ErpClientDTO.class);
    }

    @Override
    public Page<ErpClientDO> parentPage(ErpClientParentQueryRequest request) {
        Page<ErpClientDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.baseMapper.getParentPage(page, request);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Boolean updateMonitorStatus(UpdateMonitorStatusRequest request) {
        LambdaQueryWrapper<ErpClientDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ErpClientDO::getSuId, request.getSuId());
        ErpClientDO entity = new ErpClientDO();
        entity.setMonitorStatus(request.getMonitorStatus());
        entity.setUpdateUser(request.getOpUserId());
        entity.setUpdateTime(new Date());
        return this.update(entity, queryWrapper);
    }

    @Override
    public Boolean updateClientStatus(UpdateClientStatusRequest request) {
        LambdaQueryWrapper<ErpClientDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ErpClientDO::getSuId, request.getSuId());
        ErpClientDO entity = new ErpClientDO();
        entity.setClientStatus(request.getClientStatus());
        entity.setOpUserId(request.getOpUserId());
        entity.setUpdateTime(new Date());
        return this.update(entity, queryWrapper);
    }

    @Override
    public Boolean updateHeartBeatTimeBySuid(UpdateHeartBeatTimeRequest request) {
        LambdaQueryWrapper<ErpClientDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ErpClientDO::getSuId, request.getSuId());
        Date date = new Date();
        ErpClientDO entity = new ErpClientDO();
        entity.setHeartBeatTime(date);
        entity.setVersions(request.getVersions());
        entity.setOpUserId(request.getOpUserId());
        entity.setUpdateTime(date);
        return this.update(entity, queryWrapper);
    }

    @Override
    public Page<ErpClientDO> getErpMonitorListPage(ErpMonitorQueryRequest request) {
        Page<ErpClientDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, getErpMonitorPageQueryWrapper(request));
    }

    @Override
    public Page<ErpClientDO> flowEnterprisePage(QueryClientFlowEnterpriseRequest request) {
        LambdaQueryWrapper<ErpClientDO> queryWrapper = new LambdaQueryWrapper();
        Long eid = request.getEid();
        if (ObjectUtil.isNotNull(eid) && eid.intValue() != 0) {
            queryWrapper.eq(ErpClientDO::getRkSuId, eid);
        }
        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            queryWrapper.like(ErpClientDO::getClientName, ename);
        }
        queryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
        queryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
        List<Integer> levelList = new ArrayList<>();
        levelList.add(ErpFlowLevelEnum.YILING.getCode());
        levelList.add(ErpFlowLevelEnum.ALL.getCode());
        queryWrapper.in(ErpClientDO::getFlowLevel, levelList);
        queryWrapper.orderByAsc(ErpClientDO::getRkSuId);
        Page<ErpClientDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ErpClientDO> getFlowEnterpriseListByName(String name) {
        Assert.notNull(name, "name不能为空");
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(ErpClientDO::getClientName, name);
        lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
        lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
        List<Integer> flowLevelList = new ArrayList<>();
        flowLevelList.add(1);
        flowLevelList.add(2);
        lambdaQueryWrapper.in(ErpClientDO::getFlowLevel, flowLevelList);
        lambdaQueryWrapper.last("limit 10");
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<ErpClientDO> getAllFlowEnterpriseList(ErpClientQueryRequest request) {
        Long rkSuId = 0L;
        String clientName = "";
        String installEmployee = "";
        if (ObjectUtil.isNotNull(request)) {
            rkSuId = request.getRkSuId();
            clientName = request.getClientName();
            installEmployee = request.getInstallEmployee();
        }
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
        lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
        List<Integer> flowLevelList = new ArrayList<>();
        flowLevelList.add(1);
        flowLevelList.add(2);
        lambdaQueryWrapper.in(ErpClientDO::getFlowLevel, flowLevelList);
        if (ObjectUtil.isNotNull(rkSuId) && 0 != rkSuId.intValue()) {
            lambdaQueryWrapper.eq(ErpClientDO::getRkSuId, rkSuId);
        }
        if (StrUtil.isNotBlank(clientName)) {
            lambdaQueryWrapper.like(ErpClientDO::getClientName, clientName);
        }
        if (StrUtil.isNotBlank(installEmployee)) {
            lambdaQueryWrapper.like(ErpClientDO::getInstallEmployee, installEmployee);
        }
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public boolean updateDdataInitStatusByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "idList不能为空");
        List<ErpClientDO> list = new ArrayList<>();
        Date date = new Date();
        idList.forEach(o -> {
            ErpClientDO erpClientDO = new ErpClientDO();
            erpClientDO.setId(o);
            erpClientDO.setDataInitStatus(1);
            erpClientDO.setUpdateTime(date);
            erpClientDO.setUpdateUser(0L);
            list.add(erpClientDO);
        });
        return this.updateBatchById(list);
    }

    @Override
    public boolean updateCollectAndFlowDateBatch(List<ErpClientDO> list) {
        Assert.notEmpty(list, "list不能为空");
        return this.updateBatchById(list, 1000);
    }

    @Override
    public Integer getDeployInterfaceCountByRkSuIdList(List<String> licenseNumberList) {
        return this.baseMapper.deployInterfaceCount(licenseNumberList);

    }

    @Override
    public Integer getSyncStatusOffCountByRkSuIdList(List<String> licenseNumberList) {
        return this.baseMapper.getSyncStatusOffCountByRkSuIdList(licenseNumberList);
    }

    @Override
    public Integer getClientStatusOffCountByRkSuIdList(List<String> licenseNumberList) {
        return this.baseMapper.getClientStatusOffCountByRkSuIdList(licenseNumberList);
    }

    @Override
    public Integer getRunningCountByRkSuIdList(List<String> licenseNumberList) {
        return this.baseMapper.getRunningCountByRkSuIdList(licenseNumberList);
    }

    @Override
    public Integer getRunningCountByRkSuIdListForPage(ErpClientQuerySjmsRequest request) {
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = getsjmsErpClientQueryWrapper(request);

        Integer runningStatus = request.getRunningStatus();
        // 运行状态：全部
        if (ObjectUtil.isNull(runningStatus) || 0 == runningStatus.intValue()) {
            // 2-运行中， 已开启同步、已激活
            lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
            lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
        } else if (ObjectUtil.isNotNull(runningStatus) && 0 != runningStatus.intValue()) {
            // 运行状态：1-未运行 2-运行中
            if (ClientRunningStatusEnum.OFF.getCode() == runningStatus.intValue() ) {
                // 1-未运行，未开启同步 或 未激活
                return 0;
            } else if (ClientRunningStatusEnum.ON.getCode() == runningStatus.intValue() ) {
                // 2-运行中， 已开启同步、已激活
                lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
                lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
            }
        }

        // 流向级别：仅统计开启流向对接（1-以岭流向、2-全品流向）
        Integer flowLevel = request.getFlowLevel();
        if (ObjectUtil.isNull(flowLevel) || -1 == flowLevel.intValue()) {
            // 流向级别：-1 全部
            lambdaQueryWrapper.in(ErpClientDO::getFlowLevel, ListUtil.toList(ErpFlowLevelEnum.YILING.getCode(), ErpFlowLevelEnum.ALL.getCode()));
        } else if (ObjectUtil.isNotNull(flowLevel) && ErpFlowLevelEnum.NO.getCode() == flowLevel.intValue()) {
            // 流向级别：0-未对接
            return 0;
        } else if (ObjectUtil.isNotNull(flowLevel) && ErpFlowLevelEnum.YILING.getCode() == flowLevel.intValue()) {
            // 流向级别：1-以岭流向
            lambdaQueryWrapper.eq(ErpClientDO::getFlowLevel, ErpFlowLevelEnum.YILING.getCode());
        } else if (ObjectUtil.isNotNull(flowLevel) && ErpFlowLevelEnum.ALL.getCode() == flowLevel.intValue()) {
            // 流向级别：2-全品流向
            lambdaQueryWrapper.eq(ErpClientDO::getFlowLevel, ErpFlowLevelEnum.ALL.getCode());
        }

        return this.count(lambdaQueryWrapper);
    }

    @Override
    public Integer getNoDatCountByRkSuIdListAndFlowDate(List<String> licenseNumberList, Date lastestFlowDateEnd) {
        if (null == licenseNumberList) {
            return 0;
        }
        Assert.notNull(lastestFlowDateEnd, "参数 lastestFlowDateEnd 不能为空");
        return this.baseMapper.getNoDatCountByRkSuIdListAndFlowDate(licenseNumberList, lastestFlowDateEnd);
    }

    @Override
    public Integer getNoDatCountByRkSuIdListAndFlowDateByCrmIds(SjmsUserDatascopeBO datascopeBO, Date lastestCollectDateEnd) {
        Assert.notNull(lastestCollectDateEnd, "参数 lastestCollectDateEnd 不能为空");
        return this.baseMapper.getNoDatCountByRkSuIdListAndFlowDateByCrmIds(datascopeBO, lastestCollectDateEnd);
    }

    @Override
    public Integer getNoDatCountByRkSuIdListAndFlowDateForPage(ErpClientQuerySjmsRequest request) {
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = getsjmsErpClientQueryWrapper(request);

        // 运行状态，仅统计运行中的：已开启同步、已激活
        Integer runningStatus = request.getRunningStatus();
        // 运行状态：全部
        if (ObjectUtil.isNull(runningStatus) || 0 == runningStatus.intValue()) {
            // 2-运行中， 已开启同步、已激活
            lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
            lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
        } else if (ObjectUtil.isNotNull(runningStatus) && 0 != runningStatus.intValue()) {
            // 运行状态：1-未运行 2-运行中
            if (ClientRunningStatusEnum.OFF.getCode() == runningStatus.intValue() ) {
                // 1-未运行，未开启同步 或 未激活
                return 0;
            } else if (ClientRunningStatusEnum.ON.getCode() == runningStatus.intValue() ) {
                // 2-运行中， 已开启同步、已激活
                lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
                lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
            }
        }

        // 流向级别：仅统计开启流向对接（1-以岭流向、2-全品流向）
        Integer flowLevel = request.getFlowLevel();
        if (ObjectUtil.isNull(flowLevel) || -1 == flowLevel.intValue()) {
            // 流向级别：-1 全部
            lambdaQueryWrapper.in(ErpClientDO::getFlowLevel, ListUtil.toList(ErpFlowLevelEnum.YILING.getCode(), ErpFlowLevelEnum.ALL.getCode()));
        } else if (ObjectUtil.isNotNull(flowLevel) && ErpFlowLevelEnum.NO.getCode() == flowLevel.intValue()) {
            // 流向级别：0-未对接
            return 0;
        } else if (ObjectUtil.isNotNull(flowLevel) && ErpFlowLevelEnum.YILING.getCode() == flowLevel.intValue()) {
            // 流向级别：1-以岭流向
            lambdaQueryWrapper.eq(ErpClientDO::getFlowLevel, ErpFlowLevelEnum.YILING.getCode());
        } else if (ObjectUtil.isNotNull(flowLevel) && ErpFlowLevelEnum.ALL.getCode() == flowLevel.intValue()) {
            // 流向级别：2-全品流向
            lambdaQueryWrapper.eq(ErpClientDO::getFlowLevel, ErpFlowLevelEnum.ALL.getCode());
        }

        // 流向业务日期
        Date lastestFlowDateStart = request.getLastestFlowDateStart();
        if (ObjectUtil.isNotNull(lastestFlowDateStart)) {
            lambdaQueryWrapper.ge(ErpClientDO::getLastestFlowDate, DateUtil.beginOfDay(lastestFlowDateStart));
        }
        Date lastestFlowDateEnd = request.getLastestFlowDateEnd();
        if (ObjectUtil.isNotNull(lastestFlowDateEnd)) {
            lambdaQueryWrapper.le(ErpClientDO::getLastestFlowDate, DateUtil.endOfDay(lastestFlowDateEnd));
        }

        return this.count(lambdaQueryWrapper);
    }

    @Override
    public Integer getNoDataSyncCountByRkSuIdListAndLastestFlowDate(List<String> licenseNumberList, Date lastestFlowDateEnd) {
        if (null == licenseNumberList) {
            return 0;
        }
        Assert.notNull(lastestFlowDateEnd, "参数 lastestFlowDateEnd 不能为空");
        return this.baseMapper.getNoDataSyncCountByRkSuIdListAndLastestFlowDate(licenseNumberList, lastestFlowDateEnd);
    }

    @Override
    public List<SjmsFlowMonitorNoDataSyncEnterpriseBO> getNoDataSyncEnterpriseListByRkSuIdListAndEndDate(List<String> licenseNumberList, Date yesterday) {
        if (null == licenseNumberList) {
            return ListUtil.empty();
        }
        Assert.notNull(yesterday, "参数 yesterday 不能为空");
        List<SjmsFlowMonitorNoDataSyncEnterpriseBO> list = this.baseMapper.getNoDataSyncEnterpriseListByRkSuIdListAndEndDate(licenseNumberList, yesterday);
        if (CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public Page<ErpClientDO> sjmsPage(ErpClientQuerySjmsRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Page<ErpClientDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, getsjmsErpClientPageQueryWrapper(request));
    }

    @Override
    public Map<Long, String> getInstallEmployeeByEidList(List<Long> eidList) {
        if (CollUtil.isEmpty(eidList)) {
            return MapUtil.empty();
        }
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(ErpClientDO::getRkSuId, eidList);
        List<ErpClientDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }
        Map<Long, String> map = list.stream().collect(Collectors.toMap(o -> o.getRkSuId(), o -> o.getInstallEmployee(), (k1, k2) -> k1));
        return map;
    }

    @Override
    public Map<Long, String> getInstallEmployeeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        if (CollUtil.isEmpty(crmEnterpriseIdList)) {
            return MapUtil.empty();
        }
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(ErpClientDO::getCrmEnterpriseId, crmEnterpriseIdList);
        List<ErpClientDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }
        Map<Long, String> map = list.stream().collect(Collectors.toMap(o -> o.getCrmEnterpriseId(), o -> o.getInstallEmployee(), (k1, k2) -> k1));
        return map;
    }

    @Override
    public List<ErpClientDO> getByLicenseNumberList(List<String> licenseNumberList) {
        Assert.notEmpty(licenseNumberList, "参数 licenseNumberList 不能为空");
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(ErpClientDO::getLicenseNumber, licenseNumberList);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<ErpClientDO> getByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        Assert.notEmpty(crmEnterpriseIdList, "参数 crmEnterpriseIdList 不能为空");
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(ErpClientDO::getCrmEnterpriseId, crmEnterpriseIdList);
        List<ErpClientDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<ErpClientDO> getWithDatascopeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        if (null == crmEnterpriseIdList) {
            return ListUtil.empty();
        }
        List<ErpClientDO> list;
        if (crmEnterpriseIdList.size() == 0) {
            list = this.list();
        } else {
            LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.in(ErpClientDO::getCrmEnterpriseId, crmEnterpriseIdList);
            list = this.list(lambdaQueryWrapper);
        }
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public ErpClientDO getByCrmEnterpriseId(Long crmEnterpriseId) {
        Assert.notNull(crmEnterpriseId, "参数 crmEnterpriseId 不能为空");
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ErpClientDO::getCrmEnterpriseId, crmEnterpriseId);
        return this.getOne(lambdaQueryWrapper);
    }

    private LambdaQueryWrapper<ErpClientDO> getErpMonitorPageQueryWrapper(ErpMonitorQueryRequest request) {
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        Long suId = request.getSuId();
        if (suId != null && suId != 0) {
            lambdaQueryWrapper.eq(ErpClientDO::getSuId, suId);
        }

        Long rkSuId = request.getRkSuId();
        if (rkSuId != null && rkSuId != 0) {
            lambdaQueryWrapper.eq(ErpClientDO::getRkSuId, rkSuId);
        }

        String clientName = request.getClientName();
        if (StrUtil.isNotBlank(clientName)) {
            lambdaQueryWrapper.like(ErpClientDO::getClientName, clientName);
        }

        String installEmployee = request.getInstallEmployee();
        if (StrUtil.isNotBlank(installEmployee)) {
            lambdaQueryWrapper.like(ErpClientDO::getInstallEmployee, installEmployee);
        }

        Integer openType = request.getOpenType();
        if (ObjectUtil.isNotNull(openType) && openType.intValue() != 0) {
            if (ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.ERP_CLIENT_STATUS.getCode(), openType)) {
                // 终端激活状态-未激活、监控状态-开启
                lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, 0);
                lambdaQueryWrapper.eq(ErpClientDO::getMonitorStatus, 1);
            }
            if (ObjectUtil.equal(ErpMonitorCountStatisticsOpenTypeEnum.ERP_HART_BEAT.getCode(), openType)) {
                // 终端激活状态-激活、监控状态-开启
                lambdaQueryWrapper.eq(ErpClientDO::getMonitorStatus, 1);
                // 最后心跳时间 > 24小时（24小时前）
                Date date = new Date();
                DateTime dateTime = DateUtil.offsetDay(date, -1);
                lambdaQueryWrapper.lt(ErpClientDO::getHeartBeatTime, dateTime);
            }

        }

        Integer flowMode = request.getFlowMode();
        if (ObjectUtil.isNotNull(flowMode) && 0 != flowMode.intValue()) {
            lambdaQueryWrapper.eq(ErpClientDO::getFlowMode, flowMode);
        }

        List<Long> rkSuIdList = request.getRkSuIdList();
        if (CollUtil.isNotEmpty(rkSuIdList)) {
            lambdaQueryWrapper.in(ErpClientDO::getRkSuId, rkSuIdList);
        }

        lambdaQueryWrapper.isNotNull(ErpClientDO::getDepthTime);
        lambdaQueryWrapper.orderByDesc(ErpClientDO::getId);
        lambdaQueryWrapper.orderByDesc(ErpClientDO::getCreateTime);
        return lambdaQueryWrapper;
    }

    private LambdaQueryWrapper<ErpClientDO> getsjmsErpClientPageQueryWrapper(ErpClientQuerySjmsRequest request) {
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = getsjmsErpClientQueryWrapper(request);

        // 运行状态：1-未运行 2-运行中
        Integer runningStatus = request.getRunningStatus();
        if (ObjectUtil.isNotNull(runningStatus) && 0 != runningStatus.intValue()) {
            if (ClientRunningStatusEnum.OFF.getCode() == runningStatus.intValue() ) {
                // 1-未运行，未开启同步 或 未激活
                lambdaQueryWrapper.and((wrapper) -> {
                    wrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.UNSYNC.getCode())
                            .or()
                            .eq(ErpClientDO::getClientStatus, ClientStatusEnum.OUT.getCode());
                });
            } else if (ClientRunningStatusEnum.ON.getCode() == runningStatus.intValue() ) {
                // 2-运行中， 已开启同步、已激活
                lambdaQueryWrapper.eq(ErpClientDO::getSyncStatus, ErpEnterpriseSyncStatus.SYNCING.getCode());
                lambdaQueryWrapper.eq(ErpClientDO::getClientStatus, ClientStatusEnum.IN.getCode());
            }
        }

        // 流向级别：0-未对接 1-以岭流向 2-全品流向，-1 全部
        Integer flowLevel = request.getFlowLevel();
        if (ObjectUtil.isNotNull(flowLevel) && flowLevel.intValue() != -1) {
            lambdaQueryWrapper.eq(ErpClientDO::getFlowLevel, flowLevel);
        }

        lambdaQueryWrapper.orderByDesc(ErpClientDO::getDepthTime);
        return lambdaQueryWrapper;
    }

    private LambdaQueryWrapper<ErpClientDO> getsjmsErpClientQueryWrapper(ErpClientQuerySjmsRequest request) {
        LambdaQueryWrapper<ErpClientDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        String licenseNumber = request.getLicenseNumber();
        List<String> licenseNumberList = request.getLicenseNumberList();
        if (StrUtil.isNotBlank(licenseNumber)) {
            lambdaQueryWrapper.eq(ErpClientDO::getLicenseNumber, licenseNumber);
        } else if (CollUtil.isNotEmpty(licenseNumberList)) {
            lambdaQueryWrapper.in(ErpClientDO::getLicenseNumber, licenseNumberList);
        }

        // 流向收集方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口， 0-全部
        Integer flowMode = request.getFlowMode();
        if (ObjectUtil.isNotNull(flowMode) && 0 != flowMode.intValue()) {
            lambdaQueryWrapper.eq(ErpClientDO::getFlowMode, flowMode);
        }

        // 对接时间
        Date depthTimeStart = request.getDepthTimeStart();
        if (ObjectUtil.isNotNull(depthTimeStart)) {
            lambdaQueryWrapper.ge(ErpClientDO::getDepthTime, DateUtil.beginOfDay(depthTimeStart));
        }

        Date depthTimeEnd = request.getDepthTimeEnd();
        if (ObjectUtil.isNotNull(depthTimeEnd)) {
            lambdaQueryWrapper.le(ErpClientDO::getDepthTime, DateUtil.endOfDay(depthTimeEnd));
        }

        // 上次收集时间
        Date lastestCollectDateStart = request.getLastestCollectDateStart();
        if (ObjectUtil.isNotNull(lastestCollectDateStart)) {
            lambdaQueryWrapper.ge(ErpClientDO::getLastestCollectDate, DateUtil.beginOfDay(lastestCollectDateStart));
        }

        Date lastestCollectDateEnd = request.getLastestCollectDateEnd();
        if (ObjectUtil.isNotNull(lastestCollectDateEnd)) {
            lambdaQueryWrapper.le(ErpClientDO::getLastestCollectDate, DateUtil.endOfDay(lastestCollectDateEnd));
        }

        return lambdaQueryWrapper;
    }

    //
    //    @Override
    //    public ErpClientDto get(Long id) {
    //        return ConvertUtil.map(this.getById(id), ErpClientDto.class);
    //    }
    //
    //    @Override
    //    public Boolean saveOrUpdate(ErpClientSaveRequest request) {
    //        ErpClient erpClient = ConvertUtil.map(request, ErpClient.class);
    //        if (erpClient.getId() != null && erpClient.getId() != 0) {
    //            //判断是否erp对接,第一次修改为准
    //            ErpClient oldErpClient = getById(erpClient.getId());
    //            if (erpClient.getDepth() != null && erpClient.getDepth() != 0) {
    //                if (oldErpClient.getDepthTime() == null) {
    //                    erpClient.setDepthTime(new Date());
    //                }
    //                if (oldErpClient.getRkSuId() != null) {
    //                    enterpriseApi.updateEnterpriseErpDockLevel(oldErpClient.getRkSuId(), request.getDepth());
    //                    String ename=enterpriseApi.findEnameById(oldErpClient.getRkSuId()).getData();
    //                    erpClient.setClientName(ename);
    //                }
    //            }
    //
    //            erpClient.setUpdateUser(request.getOpUserId());
    //            erpClient.setUpdateTime(new Date());
    //        } else {
    //            erpClient.setCreateUser(request.getOpUserId());
    //            erpClient.setCreateTime(new Date());
    //            erpClient.setUpdateUser(request.getOpUserId());
    //            erpClient.setUpdateTime(new Date());
    //        }
    //        return this.saveOrUpdate(erpClient);
    //    }
    //
    //    @Override
    //    public List<Integer> findErpClientByDepthTimeNotNull() {
    //        QueryWrapper<ErpClient> queryWrapper = new QueryWrapper();
    //        LambdaQueryWrapper<ErpClient> lambdaQueryWrapper = queryWrapper.lambda();
    //
    //        lambdaQueryWrapper.isNotNull(ErpClient::getDepthTime);
    //        List<ErpClient> erpClientList=this.list(queryWrapper);
    //        List<Integer> suIdList=erpClientList.stream().map(e->e.getRkSuId()).collect(Collectors.toList());
    //        return suIdList;
    //    }
    //
    //    public ErpClient tranErpClient(ErpClientSaveRequest erpClientSaveRequest) {
    //        if (erpClientSaveRequest == null) {
    //            return null;
    //        }
    //        ErpClient erpClient = new ErpClient();
    //        erpClient.setSuId(erpClientSaveRequest.getSuId());
    //        erpClient.setClientName(erpClientSaveRequest.getClientName());
    //        erpClient.setClientStatus(erpClientSaveRequest.getClientStatus());
    //        erpClient.setRkSuId(erpClientSaveRequest.getSuId());
    //        return erpClient;
    //    }

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
