package com.yiling.open.erp.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.basic.tianyancha.api.TycEnterpriseApi;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;
import com.yiling.basic.tianyancha.enums.TycErrorCode;
import com.yiling.basic.tianyancha.enums.TycProvinceEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpCustomerMapper;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;
import com.yiling.open.erp.entity.ErpCustomerDO;
import com.yiling.open.erp.enums.CustomerErrorEnum;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.handler.ErpCustomerHandler;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpCustomerService;
import com.yiling.open.erp.util.OpenStringUtils;
import com.yiling.open.erp.util.RegexMatcheUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseCustomerGroupTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseCustomerSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * erp客户同步
 *
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@Service(value = "erpCustomerService")
public class ErpCustomerServiceImpl extends ErpEntityServiceImpl implements ErpCustomerService {

    @Autowired
    private ErpCustomerMapper erpCustomerMapper;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private CustomerApi customerApi;

    @DubboReference
    private CustomerGroupApi customerGroupApi;

    @DubboReference
    private LocationApi locationApi;

    @DubboReference
    private TycEnterpriseApi tycEnterpriseApi;

    @DubboReference
    private OrderApi orderApi;

    @Autowired
    private ErpCustomerHandler erpCustomerHandler;

    @Autowired
    private ErpClientService erpClientService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    protected RedisDistributedLock redisDistributedLock;

    private static final String REGION_REGEX = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
    private static final String REDIS_KEY    = "location_RegionFullView";

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpCustomerDO erpCustomerDO = (ErpCustomerDO) baseErpEntity;
        //1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpCustomerDO.getSuId(), erpCustomerDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        return synErpCustomer(PojoUtils.map(erpCustomerDO, ErpCustomerDTO.class), erpClient);
    }

    @Override
    public void syncCustomer() {
        List<ErpCustomerDO> goodsList = erpCustomerMapper.syncCustomer();
        for (ErpCustomerDO erpCustomerDO : goodsList) {
            int i = erpCustomerMapper.updateSyncStatusByStatusAndId(erpCustomerDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(),
                    "job处理");
            if (i > 0) {
                erpCustomerHandler.onlineData(erpCustomerDO);
            }
        }
    }

    @Override
    public Boolean relationCustomer(SaveEnterpriseCustomerRequest request) {
        ErpCustomerDTO erpCustomerDTO = request.getErpCustomer();
        Boolean check = checkErpCustomerByStatus(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode());
        if (!check) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "该erp客户非异常状态无法手动绑定");
        }
        //1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpCustomerDTO.getSuId(), erpCustomerDTO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        String innerCode = erpCustomerDTO.getInnerCode();
        if (StringUtils.isBlank(innerCode)) {
            // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), 3, "客户内码为空");
            return false;
        }
        long start = System.currentTimeMillis();
        String lockName = erpCustomerHandler.getCustomerLockName(erpClient.getSuId().longValue(), innerCode);
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (org.apache.commons.lang.StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            if (null == request.getCustomerEid() || 0 == request.getCustomerEid()) {
                return false;
            }
            EnterpriseDTO enterpriseDtoCustomer = enterpriseApi.getById(request.getCustomerEid());
            if (ObjectUtil.isNull(enterpriseDtoCustomer)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "绑定企业不存在");
            }
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.SYNCING.getCode(),
                    "手动同步中");
            return saveOrUpdateEnterprise(erpClient.getRkSuId().longValue(), enterpriseDtoCustomer, erpCustomerDTO);
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "ERP客户信息同步出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("ERP数据同步耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    @Override
    public Boolean maintain(SaveEnterpriseCustomerRequest request) {
        ErpCustomerDTO erpCustomerDTO = request.getErpCustomer();
        Boolean check = checkErpCustomerByStatus(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode());
        if (!check) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "该erp客户非异常状态无法手动维护");
        }
        String innerCode = erpCustomerDTO.getInnerCode();
        if (StringUtils.isBlank(innerCode)) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "客户内码为空");
        }
        //1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpCustomerDTO.getSuId(), erpCustomerDTO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "未开启同步规则");
        }
        long start = System.currentTimeMillis();
        String lockName = erpCustomerHandler.getCustomerLockName(erpClient.getSuId().longValue(), innerCode);
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (org.apache.commons.lang.StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.SYNCING.getCode(),
                    "手动维护中");
            EnterpriseDTO enterpriseDtoCustomer;
            long eid = Long.parseLong(erpClient.getRkSuId().toString());
            //同步前置条件效验
            Boolean checkErpCustomer = checkErpCustomer(erpCustomerDTO, eid);
            if (!checkErpCustomer) {
                return false;
            }
            String licenseNoMsg = "";
            /**
             * 根据运营需求，手动维护时，唯一代码不做正则效验，只判空
             */
            if (StrUtil.isBlank(erpCustomerDTO.getLicenseNo())) {
                licenseNoMsg = "唯一代码为空";
            }
            /**
             * 无执业许可证号/社会信用统一代码走名字效验erp客户匹配流程
             */
            if (StrUtil.isNotBlank(licenseNoMsg)) {
                // 企业名称校验
                if (StringUtils.isBlank(erpCustomerDTO.getName())) {
                    erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.NAME_ERROR.getDesc());
                    return false;
                }
                enterpriseDtoCustomer = enterpriseApi.getByName(erpCustomerDTO.getName());
                if (null != enterpriseDtoCustomer) {
                    return saveOrUpdateEnterprise(eid, enterpriseDtoCustomer, erpCustomerDTO);
                } else {
                    erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), licenseNoMsg);
                    //名字找不到企业并且唯一代码不正确绑定失败
                    return false;
                }
            }
            /**
             * 唯一代码效验erp客户匹配流程
             */
            //唯一代码查询企业，此处唯一代码只会是执业许可证号或者社会信用统一代码
            enterpriseDtoCustomer = enterpriseApi.getByLicenseNumber(erpCustomerDTO.getLicenseNo());
            Boolean syncFlag = saveOrUpdateEnterprise(eid, enterpriseDtoCustomer, erpCustomerDTO);
            if (syncFlag) {
                erpCustomerMapper.updateById(PojoUtils.map(erpCustomerDTO, ErpCustomerDO.class));
            }
            return syncFlag;
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "ERP客户信息数据维护出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("ERP数据维护耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    @Override
    public Boolean relationCustomerByTyc(SaveEnterpriseCustomerRequest request) {
        if (ObjectUtil.isNull(request.getTycEnterprise()) || ObjectUtil.isNull(request.getErpCustomer())) {
            return false;
        }
        ErpCustomerDTO erpCustomerDTO = request.getErpCustomer();
        TycEnterpriseInfoDTO tycEnterprise = request.getTycEnterprise();
        String innerCode = erpCustomerDTO.getInnerCode();
        if (StringUtils.isBlank(innerCode)) {
            // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), 3, "客户内码为空");
            return false;
        }
        erpCustomerDTO.setName(tycEnterprise.getName());
        erpCustomerDTO.setAddress(tycEnterprise.getRegLocation());
        erpCustomerDTO.setProvince(tycEnterprise.getBase());
        erpCustomerDTO.setRegion(tycEnterprise.getDistrict());
        erpCustomerDTO.setCity(tycEnterprise.getCity());
        erpCustomerDTO.setLicenseNo(tycEnterprise.getCreditCode());
        log.info("天眼查更新后ErpCustomer:{}", erpCustomerDTO);
        ErpCustomerDO erpCustomerDO = PojoUtils.map(erpCustomerDTO, ErpCustomerDO.class);
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpCustomerDO.getSuId(), erpCustomerDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        long start = System.currentTimeMillis();
        String lockName = erpCustomerHandler.getCustomerLockName(erpClient.getSuId().longValue(), innerCode);
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (org.apache.commons.lang.StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.SYNCING.getCode(),
                    "天眼查同步中");
            long eid = Long.parseLong(erpClient.getRkSuId().toString());
            //同步前置条件效验
            Boolean checkErpCustomer = checkErpCustomer(erpCustomerDTO, eid);
            if (!checkErpCustomer) {
                return false;
            }
            boolean bindFlag = saveOrUpdateEnterprise(erpClient.getRkSuId().longValue(), null, erpCustomerDTO);
            if (bindFlag) {
                erpCustomerMapper.updateById(erpCustomerDO);
            }
            return bindFlag;
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "ERP客户信息同步天眼查出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("ERP数据同步耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    @Override
    public Page<ErpCustomerDTO> QueryErpCustomerPageBySyncStatus(ErpCustomerQueryRequest request) {
        //erp客户表数据量较大，导致分页越大速度越慢，优化为只查id字段分页
        Page<Long> page = erpCustomerMapper.getPageListErpCustomerId(request.getPage(), request);
        Page<ErpCustomerDTO> resultPage = PojoUtils.map(page, ErpCustomerDTO.class);

        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            List<ErpCustomerDO> doList = erpCustomerMapper.selectBatchIds(page.getRecords());
            doList.sort(Comparator.comparing(ErpCustomerDO::getId).reversed());
            resultPage.setRecords(PojoUtils.map(doList, ErpCustomerDTO.class));
        }
        return resultPage;
    }

    @Override
    public List<ErpCustomerDTO> getErpCustomerListBySyncStatus(ErpCustomerQueryRequest request) {
        //erp客户表数据量较大，导致分页越大速度越慢，优化为只查id字段分页
        List<Long> idList = erpCustomerMapper.getErpCustomerIdList(request);
        if (CollectionUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        List<ErpCustomerDO> doList = erpCustomerMapper.selectBatchIds(idList);
        doList.sort(Comparator.comparing(ErpCustomerDO::getId).reversed());
        return PojoUtils.map(doList, ErpCustomerDTO.class);
    }

    @Override
    public ErpCustomerDTO findById(Long id) {
        Assert.notNull(id);
        ErpCustomerDO erpCustomerDO = erpCustomerMapper.selectById(id);
        ErpCustomerDTO erpCustomerDTO = PojoUtils.map(erpCustomerDO, ErpCustomerDTO.class);
        if (null != erpCustomerDTO) {
            enterpriseAddressCheck(erpCustomerDTO);
        }
        return erpCustomerDTO;
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpCustomerMapper.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

    /**
     * 客户同步
     *
     * @param erpCustomerDTO
     * @param erpClient
     * @return
     */
    public boolean synErpCustomer(ErpCustomerDTO erpCustomerDTO, ErpClientDTO erpClient) {
        EnterpriseDTO enterpriseDtoCustomer;
        boolean tycFlag = false;
        try {
            long eid = Long.parseLong(erpClient.getRkSuId().toString());

            // 操作类型，1新增，2修改，3删除
            if (ObjectUtil.equal(3, erpCustomerDTO.getOperType())) {
                Long id = erpCustomerDTO.getId();
                ErpCustomerDO erpCustomerDO=erpCustomerMapper.selectById(id);
                String innerCode = erpCustomerDO.getInnerCode();
                if (StringUtils.isBlank(innerCode)) {
                    // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                    erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), 3, "客户内码为空");
                    return false;
                }
                //通过内码查询采购关系
                EnterpriseCustomerDTO enterpriseCustomerDTO = customerApi.listByEidAndCustomerErpCode(erpClient.getRkSuId(), innerCode);
                if (enterpriseCustomerDTO != null) {
                    //调用删除接口
                    customerApi.deleteEnterpriseCustomer(enterpriseCustomerDTO.getId(), 0L);
                }
                // 客户同步不支持删除
                erpCustomerMapper.updateSyncStatusByStatusAndId(erpCustomerDTO.getId(), SyncStatus.SUCCESS.getCode(), SyncStatus.SYNCING.getCode(),
                        "同步成功，客户信息同步时不做删除");
                return true;
            }

            //同步前置条件效验
            Boolean checkErpCustomer = checkErpCustomer(erpCustomerDTO, eid);
            if (!checkErpCustomer) {
                return false;
            }
            // 执业许可证号/社会信用统一代码 校验
            String licenseNoMsg = licenseNoCheck(erpCustomerDTO);
            /**
             * erp客户唯一代码是否为执业许可证号/社会信用统一代码
             * 非执业许可证号/社会信用统一代码走名字效验erp客户匹配流程
             */
            if (StrUtil.isNotBlank(licenseNoMsg)) {
                // 企业名称校验
                if (StringUtils.isBlank(erpCustomerDTO.getName())) {
                    erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.NAME_ERROR.getDesc());
                    return false;
                }
                enterpriseDtoCustomer = enterpriseApi.getByName(erpCustomerDTO.getName());
                if (ObjectUtil.isNull(enterpriseDtoCustomer)) {
                    //如果名字查不到 则通过天眼查更新erp客户信息
                    tycFlag = updateErpCustomerFromTyc(erpCustomerDTO, erpCustomerDTO.getName(), null);
                    if (!tycFlag) {
                        erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), licenseNoMsg);
                        return false;
                    }
                }
                //如果通过名字 平台可以查到企业或者天眼查可以查到企业 则走匹配流程
                return saveOrUpdateEnterprise(eid, enterpriseDtoCustomer, erpCustomerDTO);
            }

            /**
             * 唯一代码效验erp客户匹配流程
             */
            //唯一代码查询企业，此处唯一代码只会是执业许可证号或者社会信用统一代码
            enterpriseDtoCustomer = enterpriseApi.getByLicenseNumber(erpCustomerDTO.getLicenseNo());
            //如果唯一代码是社会信用代码并且平台查不到 则通过天眼查更新erp客户信息
            if (ObjectUtil.isNull(enterpriseDtoCustomer) && CreditCodeUtil.isCreditCode(erpCustomerDTO.getLicenseNo())) {
                updateErpCustomerFromTyc(erpCustomerDTO, null, erpCustomerDTO.getLicenseNo());
            }
            return saveOrUpdateEnterprise(eid, enterpriseDtoCustomer, erpCustomerDTO);
        } catch (Exception e) {
            log.error("[客户信息]同步出现错误", e);
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.SYSTEM_ERROR.getDesc());
        }
        return false;
    }

    /**
     * 检查客户状态是否正确
     *
     * @param id
     * @param status
     * @return
     */
    private Boolean checkErpCustomerByStatus(Long id, Integer status) {
        QueryWrapper<ErpCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpCustomerDO::getId, id).eq(ErpCustomerDO::getSyncStatus, status);
        ErpCustomerDO erpCustomerDO = this.erpCustomerMapper.selectOne(queryWrapper);
        return null != erpCustomerDO;
    }

    /**
     * 同步前前置条件效验
     *
     * @param erpCustomerDTO
     * @param eid
     * @return
     */
    private Boolean checkErpCustomer(ErpCustomerDTO erpCustomerDTO, Long eid) {
        String innerCode = erpCustomerDTO.getInnerCode();
        if (StringUtils.isBlank(innerCode)) {
            // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), 3, "客户内码为空");
            return false;
        }
        EnterpriseDTO enterpriseDto = enterpriseApi.getById(eid);
        if (ObjectUtil.isNull(enterpriseDto)) {
            log.error("商业信息不存在，eid:{}", eid);
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.NOT_FOUND_ENTERPRISE.getDesc());
            return false;
        }
        // 企业类型
        Integer enterpriseType = getEnterpriseType(erpCustomerDTO.getCustomerType(), erpCustomerDTO.getName());
        if (enterpriseType == 0) {
            erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.CUSTOMER_TYPE_ERROR.getDesc());
            return false;
        } else {
            erpCustomerDTO.setEnterpriseType(enterpriseType);
        }
        return true;
    }

    /**
     * 天眼查信息（名字，社会信用代码，省市区，地址）同步至客户信息
     *
     * @param erpCustomerDTO
     * @param name
     * @param creditCode
     * @return
     */
    private boolean updateErpCustomerFromTyc(ErpCustomerDTO erpCustomerDTO, String name, String creditCode) {
        TycEnterpriseQueryRequest request = new TycEnterpriseQueryRequest();
        if (StringUtils.isNotBlank(name)) {
            request.setKeyword(name);
        } else if (StringUtils.isNotBlank(creditCode)) {
            request.setKeyword(creditCode);
        } else {
            log.error("天眼查查询名字和社会信用代码均为空，openid为：" + erpCustomerDTO.getId());
            return false;
        }
        TycResultDTO<TycEnterpriseInfoDTO> tycResultDTO = tycEnterpriseApi.findEnterpriseByKeyword(request);
        //天眼查功能是否关闭
        if (TycErrorCode.CLOSE.getCode().equals(tycResultDTO.getError_code())) {
            return false;
        }
        TycEnterpriseInfoDTO tycEnterpriseDto = tycResultDTO.getResult();
        if (ObjectUtil.isNotNull(tycEnterpriseDto)) {
            if (StringUtils.isBlank(tycEnterpriseDto.getCreditCode())) {
                return false;
            }
            erpCustomerDTO.setName(tycEnterpriseDto.getName());
            erpCustomerDTO.setLicenseNo(tycEnterpriseDto.getCreditCode());
            TycProvinceEnum tycProvinceEnum = TycProvinceEnum.getByCode(tycEnterpriseDto.getBase());
            String province = null == tycProvinceEnum ? tycEnterpriseDto.getBase() : tycProvinceEnum.getProvinceName();
            String city = tycEnterpriseDto.getCity();
            String region = tycEnterpriseDto.getDistrict();
            String address = tycEnterpriseDto.getRegLocation();
            ErpCustomerDTO checkAddressDTO = new ErpCustomerDTO();
            checkAddressDTO.setProvince(province);
            checkAddressDTO.setCity(city);
            checkAddressDTO.setRegion(region);
            checkAddressDTO.setAddress(address);
            //检查天眼查地址是否能匹配本地库地址
            String errorMsg = enterpriseAddressCheck(checkAddressDTO);
            if (StringUtils.isBlank(errorMsg)) {
                erpCustomerDTO.setProvince(province);
                erpCustomerDTO.setCity(city);
                erpCustomerDTO.setRegion(region);
                erpCustomerDTO.setAddress(address);
            }
            return true;
        }
        if (!TycErrorCode.NO_DATA.getCode().equals(tycResultDTO.getError_code())) {
            log.error("天眼查获取企业失败，天眼查响应：{}", tycResultDTO);
        }
        return false;
    }

    private Boolean saveOrUpdateEnterprise(Long eid, EnterpriseDTO enterpriseDtoCustomer, ErpCustomerDTO erpCustomerDTO) {
        if (ObjectUtil.isNull(enterpriseDtoCustomer)) {
            if (EnterpriseTypeEnum.INDUSTRY.getCode().equals(erpCustomerDTO.getEnterpriseType()) || EnterpriseTypeEnum.BUSINESS.getCode().equals(erpCustomerDTO.getEnterpriseType())) {
                erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.CUSTOMER_TYPE_NOT_MATCH.getDesc());
                return false;
            }
            // 新增校验
            String errorMsg = enterpriseAddressCheck(erpCustomerDTO);
            if (StringUtils.isNotBlank(errorMsg)) {
                erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), errorMsg);
                return false;
            }
            if (StringUtils.isBlank(erpCustomerDTO.getName())) {
                erpCustomerMapper.updateSyncStatusAndMsg(erpCustomerDTO.getId(), SyncStatus.FAIL.getCode(), CustomerErrorEnum.NAME_ERROR.getDesc());
                return false;
            }
            enterpriseDtoCustomer = insertEnterprise(erpCustomerDTO, erpCustomerDTO.getEnterpriseType());
        }
        // 企业客户信息
        EnterpriseCustomerDTO enterpriseCustomerDto = customerApi.get(eid, enterpriseDtoCustomer.getId());
        // 企业客户分组
        Long customerGroupId = getCustomerGroupId(erpCustomerDTO, eid);

        if (ObjectUtil.isNotNull(enterpriseCustomerDto)) {
            // 更新企业客户信息，仅更新字段：客户名称、客户编码、客户分组ID
            updateEnterpriseCustomer(erpCustomerDTO, enterpriseCustomerDto, customerGroupId);
        } else {
            insertEnterpriseCustomer(eid, enterpriseDtoCustomer.getId(), erpCustomerDTO.getName(), erpCustomerDTO.getSn(), erpCustomerDTO.getInnerCode(), customerGroupId);
        }
        erpCustomerMapper.updateSyncStatusByStatusAndId(erpCustomerDTO.getId(), SyncStatus.SUCCESS.getCode(), SyncStatus.SYNCING.getCode(),
                "同步成功");
        return true;
    }


    private String licenseNoCheck(ErpCustomerDTO erpCustomerDTO) {
        String name = OpenStringUtils.clearAllSpace(erpCustomerDTO.getName());
        String licenseNo = OpenStringUtils.clearAllSpace(erpCustomerDTO.getLicenseNo());
        erpCustomerDTO.setLicenseNo(licenseNo);
        erpCustomerDTO.setName(name);
        if (StringUtils.isBlank(erpCustomerDTO.getLicenseNo())) {
            return CustomerErrorEnum.LICENSE_NO_ERROR.getDesc();
        }
        //社会唯一信用代码效验
        boolean lengthResult = CreditCodeUtil.isCreditCode(erpCustomerDTO.getLicenseNo());
        //医疗机构执业许可证效验
        boolean matchFlag = RegexMatcheUtils.matchPracticeLicenseOfMedicalInstitution(erpCustomerDTO.getLicenseNo());
        if (!lengthResult && !matchFlag) {
            return CustomerErrorEnum.LICENSE_NO_NOT_MATCH.getDesc();
        }
        return null;
    }

    private Long getCustomerGroupId(ErpCustomerDTO erpCustomerDTO, long eid) {
        Long customerGroupId = null;
        if (StringUtils.isNotBlank(erpCustomerDTO.getGroupName())) {
            // 查询企业客户分组
            EnterpriseCustomerGroupDTO customerGroup = customerGroupApi.getByEidAndName(eid, erpCustomerDTO.getGroupName());
            if (ObjectUtil.isNotNull(customerGroup)) {
                customerGroupId = customerGroup.getId();
            } else {
                // 新增企业客户分组
                AddCustomerGroupRequest customerGroupRequest = new AddCustomerGroupRequest();
                customerGroupRequest.setEid(eid);
                customerGroupRequest.setName(erpCustomerDTO.getGroupName());
                customerGroupRequest.setType(EnterpriseCustomerGroupTypeEnum.ERP_SYNC.getCode());
                customerGroupId = customerGroupApi.add(customerGroupRequest);
            }
        }
        return customerGroupId;
    }


    private void updateEnterpriseCustomer(ErpCustomerDTO erpCustomerDTO, EnterpriseCustomerDTO enterpriseCustomerDto, Long customerGroupId) {
        UpdateCustomerRequest customerRequest = new UpdateCustomerRequest();
        customerRequest.setId(enterpriseCustomerDto.getId());
        boolean updateCustomerFlag = false;
        if (StringUtils.isNotBlank(erpCustomerDTO.getInnerCode())) {
            customerRequest.setCustomerCode(erpCustomerDTO.getSn());
            customerRequest.setCustomerErpCode(erpCustomerDTO.getInnerCode());
            updateCustomerFlag = true;
        }
        if (StringUtils.isNotBlank(erpCustomerDTO.getName())) {
            customerRequest.setCustomerName(erpCustomerDTO.getName());
            updateCustomerFlag = true;
        }
        if (ObjectUtil.isNotNull(customerGroupId)) {
            customerRequest.setCustomerGroupId(customerGroupId);
            updateCustomerFlag = true;
        }

        if (updateCustomerFlag) {
            customerApi.syncUpdateById(customerRequest);
            orderApi.updateCustomerErpCode(enterpriseCustomerDto.getCustomerEid(), enterpriseCustomerDto.getEid(), customerRequest.getCustomerErpCode());
        }
    }

    private void insertEnterpriseCustomer(long eid, Long customerEid, String customerName, String customerCode, String customerErpCode, Long customerGroupId) {
        AddCustomerRequest request = new AddCustomerRequest();
        request.setEid(eid);
        request.setCustomerEid(customerEid);
        request.setCustomerName(customerName);
        request.setCustomerCode(customerCode);
        request.setCustomerErpCode(customerErpCode);
        request.setCustomerGroupId(customerGroupId);
        request.setSource(EnterpriseCustomerSourceEnum.ERP_SYNC.getCode());
        request.setAddPurchaseRelationFlag(false);
        customerApi.add(request);
    }

    private EnterpriseDTO insertEnterprise(ErpCustomerDTO erpCustomerDTO, Integer enterpriseType) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getByLicenseNumber(erpCustomerDTO.getLicenseNo());
        if (null != enterpriseDTO) {
            return enterpriseDTO;
        }
        CreateEnterpriseRequest request = new CreateEnterpriseRequest();
        request.setName(erpCustomerDTO.getName());
        request.setContactor(erpCustomerDTO.getContact());
        request.setContactorPhone(erpCustomerDTO.getPhone());
        request.setLicenseNumber(erpCustomerDTO.getLicenseNo());
        request.setProvinceCode(erpCustomerDTO.getProvinceCode());
        request.setProvinceName(erpCustomerDTO.getProvince());
        request.setCityCode(erpCustomerDTO.getCityCode());
        request.setCityName(erpCustomerDTO.getCity());
        request.setRegionCode(erpCustomerDTO.getRegionCode());
        request.setRegionName(erpCustomerDTO.getRegion());
        request.setAddress(erpCustomerDTO.getAddress());
        request.setType(enterpriseType);
        request.setSource(EnterpriseSourceEnum.ERP_SYNC.getCode());
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setAuthUser(0L);
        request.setAuthTime(new Date());
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        request.setOpUserId(0L);
        // 终端类型自动开通B2B
        if (EnterpriseTypeEnum.getByCode(enterpriseType).isTerminal()) {
            request.setPlatformEnumList(ListUtil.toList(PlatformEnum.B2B));
        }
        enterpriseApi.create(request);
        return enterpriseApi.getByLicenseNumber(erpCustomerDTO.getLicenseNo());
    }

    /**
     * 终端类型转换为对应企业类型
     * <p>
     * 终端类型 -> 企业类型
     * 批发企业=商业
     * 零售=单体药房
     * 零售连锁=连锁总店
     * 医疗机构=诊所/医院
     *
     * @param customerType ERP终端类型
     * @return 企业类型
     */
    public Integer getEnterpriseType(String customerType, String name) {
        // ERP终端类型为[生产企业]的数据不同步
        if (customerType.contains("生产企业")) {
            return 0;
        }
        // 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
        if (customerType.contains("工业")) {
            // 1
            return EnterpriseTypeEnum.INDUSTRY.getCode();
        } else if (customerType.contains("商业") || customerType.contains("批发")) {
            // 2
            return EnterpriseTypeEnum.BUSINESS.getCode();
        } else if (customerType.contains("连锁总店") || customerType.contains("连锁公司") || customerType.contains("连锁药店") || customerType.contains("零售连锁")) {
            // 3
            return EnterpriseTypeEnum.CHAIN_BASE.getCode();
        } else if (customerType.contains("连锁直营") || customerType.contains("连锁门店")) {
            // 4
            return EnterpriseTypeEnum.CHAIN_DIRECT.getCode();
        } else if (customerType.contains("连锁加盟")) {
            // 5
            return EnterpriseTypeEnum.CHAIN_JOIN.getCode();
        } else if (customerType.contains("单体药店") || customerType.contains("单体药房") || customerType.contains("DTP药店") || customerType.contains("零售")) {
            // 6
            return EnterpriseTypeEnum.PHARMACY.getCode();
        } else if (customerType.contains("医院")) {
            // 7
            return EnterpriseTypeEnum.HOSPITAL.getCode();
        } else if (customerType.contains("诊所") || customerType.contains("社区卫生") || customerType.contains("门诊") || customerType.contains("卫生室")) {
            // 8
            return EnterpriseTypeEnum.CLINIC.getCode();
        } else if (customerType.contains("医疗机构")) {
            if (name.contains("医院")) {
                // 7
                return EnterpriseTypeEnum.HOSPITAL.getCode();
            } else {
                // 8
                return EnterpriseTypeEnum.CLINIC.getCode();
            }
        }
        return 0;
    }

    private String enterpriseAddressCheck(ErpCustomerDTO erpCustomerDTO) {
        // 地址
        if (StringUtils.isBlank(erpCustomerDTO.getAddress())) {
            return CustomerErrorEnum.ADDRESS_ERROR.getDesc();
        }

        // 省市区不对的，根据地址重新获取省市区
        if (isRebuildRegion(erpCustomerDTO)) {

            RegionFullViewDTO provinceCityTown = findProvinceCityTown(erpCustomerDTO.getProvince(), erpCustomerDTO.getCity(), erpCustomerDTO.getRegion(), erpCustomerDTO.getAddress());
            if (ObjectUtil.isNull(provinceCityTown)) {
                return CustomerErrorEnum.ADDRESS_ERROR.getDesc();
            }
            if (StrUtil.isBlank(provinceCityTown.getProvinceName())) {
                return CustomerErrorEnum.ADDRESS_ERROR.getDesc();
            }
            erpCustomerDTO.setProvince(provinceCityTown.getProvinceName());
            erpCustomerDTO.setProvinceCode(provinceCityTown.getProvinceCode());
            if (StrUtil.isBlank(provinceCityTown.getCityName())) {
                return CustomerErrorEnum.ADDRESS_ERROR.getDesc();
            }
            erpCustomerDTO.setCity(provinceCityTown.getCityName());
            erpCustomerDTO.setCityCode(provinceCityTown.getCityCode());
            if (StrUtil.isBlank(provinceCityTown.getRegionName())) {
                return CustomerErrorEnum.ADDRESS_ERROR.getDesc();
            }
            erpCustomerDTO.setRegion(provinceCityTown.getRegionName());
            erpCustomerDTO.setRegionCode(provinceCityTown.getRegionCode());
        } else {
            String[] locationNameArray = locationApi.getNamesByCodes(erpCustomerDTO.getProvinceCode(), erpCustomerDTO.getCityCode(), erpCustomerDTO.getRegionCode());
            if (ArrayUtils.isNotEmpty(locationNameArray)) {
                erpCustomerDTO.setProvince(locationNameArray[0]);
                erpCustomerDTO.setCity(locationNameArray[1]);
                erpCustomerDTO.setRegion(locationNameArray[2]);
            }
        }
        return null;
    }

    /**
     * 是否需要重新设置省市区
     * false-否，true-是
     *
     * @param erpCustomerDTO
     * @return
     */
    public boolean isRebuildRegion(ErpCustomerDTO erpCustomerDTO) {
        // 1.省市区，三个字段有空的
        if (StringUtils.isBlank(erpCustomerDTO.getProvince()) || StringUtils.isBlank(erpCustomerDTO.getCity())
                || StringUtils.isBlank(erpCustomerDTO.getRegion())) {
            return true;
        }
        // 根据省市区名称获取省市区编码
        String[] locationArray = locationApi.getCodesByNames(erpCustomerDTO.getProvince(), erpCustomerDTO.getCity(), erpCustomerDTO.getRegion());
        if (ArrayUtils.isEmpty(locationArray)) {
            return true;
        }
        String provinceCode = locationArray[0];
        String cityCode = locationArray[1];
        String regionCode = locationArray[2];
        erpCustomerDTO.setProvinceCode(provinceCode);
        erpCustomerDTO.setCityCode(cityCode);
        erpCustomerDTO.setRegionCode(regionCode);

        return StringUtils.isBlank(provinceCode) || StringUtils.isBlank(cityCode) || StringUtils.isBlank(regionCode);
    }

    public RegionFullViewDTO findProvinceCityTown(String province, String city, String town, String address) {
        List<RegionFullViewDTO> regionFullViewDTOList = null;
        String key = REDIS_KEY + SecureUtil.md5(REDIS_KEY);
        String regionJson = stringRedisTemplate.opsForValue().get(key);
        long time = 60 * 60 * 12;
        if (StringUtils.isEmpty(regionJson)) {
            regionFullViewDTOList = locationApi.getAllProvinceCityRegionList();
            stringRedisTemplate.opsForValue().set(key, JSONArray.toJSONString(regionFullViewDTOList), time);
        } else {
            regionFullViewDTOList = JSONArray.parseArray(regionJson, RegionFullViewDTO.class);
        }

        if (CollUtil.isEmpty(regionFullViewDTOList)) {
            return null;
        }
        //首先判断towm是否为空
        if (StrUtil.isBlank(town)) {
            Map<String, String> addressInfo = getAddressInfo(address);
            if (addressInfo != null && addressInfo.get("county") != null) {
                town = addressInfo.get("county");
            }
        }
        //如果没有找到直接通过第3级匹配地址
        if (StrUtil.isEmpty(town)) {
            for (RegionFullViewDTO regionFullView : regionFullViewDTOList) {
                if (address.contains(regionFullView.getRegionName())) {
                    RegionFullViewDTO regionFullViewDTO = findProvinceCityTownByTown(province, city, regionFullView.getRegionName(), address, regionFullViewDTOList);
                    if (regionFullViewDTO != null) {
                        return regionFullViewDTO;
                    }
                }
            }
        } else {
            return findProvinceCityTownByTown(province, city, town, address, regionFullViewDTOList);
        }
        return null;
    }

    private RegionFullViewDTO findProvinceCityTownByTown(String province, String city, String town, String address, List<RegionFullViewDTO> regionFullViewDTOList) {
        //在通过address匹配第3级
        List<RegionFullViewDTO> fullViewDTOList = new ArrayList<>();
        if (town != null) {//从town信息获取第3级
            for (RegionFullViewDTO regionFullView : regionFullViewDTOList) {
                if (regionFullView.getRegionName().contains(town)) {
                    fullViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(fullViewDTOList)) {
            for (RegionFullViewDTO regionFullView : regionFullViewDTOList) {
                if (address.contains(regionFullView.getRegionName())) {
                    fullViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(fullViewDTOList)) {
            return null;
        }

        if (fullViewDTOList.size() == 1) {//如果只有一条匹配
            return fullViewDTOList.get(0);
        }

        List<RegionFullViewDTO> cityViewDTOList = new ArrayList<>();
        //如果已经有确定的市
        if (StringUtils.isNotEmpty(city)) {
            for (RegionFullViewDTO regionFullView : fullViewDTOList) {
                if (regionFullView.getCityName().contains(city)) {//匹配第2个数据
                    cityViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(cityViewDTOList)) {
            for (RegionFullViewDTO regionFullView : fullViewDTOList) {
                if (address.contains(regionFullView.getCityName())) {//匹配第2个数据
                    cityViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(cityViewDTOList)) {
            return null;
        }

        if (cityViewDTOList.size() == 1) {//如果只有一条匹配
            return cityViewDTOList.get(0);
        }

        List<RegionFullViewDTO> provinceViewDTOList = new ArrayList<>();
        //如果已经有确定的省
        if (StringUtils.isNotEmpty(province)) {
            for (RegionFullViewDTO regionFullView : fullViewDTOList) {
                if (regionFullView.getProvinceName().contains(province)) {//匹配第2个数据
                    provinceViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(provinceViewDTOList)) {
            for (RegionFullViewDTO regionFullView : cityViewDTOList) {
                if (address.contains(regionFullView.getProvinceName())) {//匹配第2个数据
                    provinceViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(provinceViewDTOList)) {
            return null;
        }

        if (provinceViewDTOList.size() == 1) {//如果只有一条匹配
            return cityViewDTOList.get(0);
        }
        return null;
    }


    public static Map<String, String> getAddressInfo(String address) {
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = null, city = null, county = null, town = null, village = null;
        Map<String, String> row = null;
        while (m.find()) {
            row = new LinkedHashMap<String, String>();
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
        }
        return row;
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpCustomerMapper;
    }

    //    public static void main(String[] args) {
    //        Map<String, String> map = getAddressInfo("河北省廊坊市霸州市城区办北杨庄桥东");
    //        map.entrySet().stream().forEach(item -> {
    //            System.out.println(item.getKey() + ":" + item.getValue());
    //        });
    //    }
}
