package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.bo.EnterpriseStatisticsBO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.QueryContactorEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.RegistEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseChannelRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseHmcTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateErpStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateManagerMobileRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
public interface EnterpriseService extends BaseService<EnterpriseDO> {

    /**
     * 根据名称获取企业信息
     *
     * @param name 企业名称
     * @return
     */
    EnterpriseDO getByName(String name);

    /**
     * 根据执业许可证号/社会信用统一代码获取企业信息
     *
     * @param licenseNumber 执业许可证号/社会信用统一代码
     * @return
     */
    EnterpriseDO getByLicenseNumber(String licenseNumber);

    /**
     * 根据ERP编码查询企业信息
     *
     * @param erpCode ERP编码
     * @return
     */
    EnterpriseDO getByErpCode(String erpCode);

    /**
     * 获取用户所属企业信息列表
     *
     * @param userId 用户ID
     * @param statusEnum 状态枚举
     * @return
     */
    List<EnterpriseDO> listByUserId(Long userId, EnableStatusEnum statusEnum);

    /**
     * 根据上级企业ID获取下级企业信息列表
     *
     * @param parentId 上级企业ID
     * @return
     */
    List<EnterpriseDO> listByParentId(Long parentId);

    /**
     * 获取下级企业ID列表
     *
     * @param eid 企业ID
     * @return
     */
    List<Long> listSubEids(Long eid);

    /**
     * 获取指定渠道类型的企业ID列表
     *
     * @param enterpriseChannelEnum 企业渠道枚举
     * @return
     */
    List<Long> listEidsByChannel(EnterpriseChannelEnum enterpriseChannelEnum);

    /**
     * 批量获取用户所属企业信息列表
     *
     * @param userIds 用户ID列表
     * @return key：用户ID，value：用户所属企业信息列表
     */
    Map<Long, List<EnterpriseDO>> listByUserIds(List<Long> userIds);

    /**
     * 批量获取商务联系人负责的客户企业信息
     *
     * @param eid 企业ID
     * @param contactUserIds 商务联系人用户ID
     * @return
     */
    Map<Long, List<EnterpriseDO>> listByContactUserIds(Long eid, List<Long> contactUserIds);

    /**
     * 查询企业分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseDO> pageList(QueryEnterprisePageListRequest request);

    /**
     * 查询企业列表
     *
     * @param request
     * @return
     */
    List<EnterpriseDO> queryList(QueryEnterprisePageListRequest request);

    /**
     * 注册企业信息
     *
     * @param request
     * @return 企业ID
     */
    Long regist(RegistEnterpriseRequest request);

    /**
     * 创建企业信息<br/>
     * 1、生成企业信息<br/>
     * 2、生成企业账号及权限信息<br/>
     * 3、生成企业资质信息<br/>
     * 4、生成企业收货地址信息<br/>
     *
     * @param request
     * @return 企业ID
     */
    Long create(CreateEnterpriseRequest request);

    /**
     * 修改企业信息
     *
     * @param request
     * @return
     */
    boolean update(UpdateEnterpriseRequest request);

    /**
     * 修改企业类型
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/15
     **/
    boolean updateType(UpdateEnterpriseTypeRequest request);

    /**
     * 修改企业渠道
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/15
     **/
    boolean updateChannel(UpdateEnterpriseChannelRequest request);

    /**
     * 修改企业HMC业务类型
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/4/15
     **/
    boolean updateHmcType(UpdateEnterpriseHmcTypeRequest request);

    /**
     * 更新企业状态
     *
     * @param requset
     * @return
     */
    boolean updateStatus(UpdateEnterpriseStatusRequest requset);

    /**
     * 导入企业信息
     *
     * @param request
     * @return
     */
    boolean importData(ImportEnterpriseRequest request);

    /**
     * 企业数量统计
     *
     * @return
     */
    EnterpriseStatisticsBO quantityStatistics();

    /**
     * 根据省市区查询企业列表
     * @param request
     * @return
     */
    List<EnterpriseDTO> queryListByArea(QueryEnterpriseListRequest request);


    /**
     * 销售助手我的可客户信息
     * @param request
     * @return
     */
    Page<EnterpriseDO> myCustomerPageList(QueryContactorEnterprisePageListRequest request);

    /**
     * 获取协议主体列表
     *
     * @return
     */
    List<EnterpriseDO> listMainPart();

    /**
     * 更新审核状态
     * @param request
     * @return
     */
    boolean updateAuthStatus(UpdateEnterpriseAuthRequest request);

    /**
     * 根据名字模糊查询接通erp并审核通过的公司
     * @param request
     * @return
     */
    List<EnterpriseDTO> getEnterpriseListByName(QueryEnterpriseByNameRequest request);

    /**
     * 更新ERP对接级别
     *
     * @param request
     * @return
     */
    boolean updateErpStatus(UpdateErpStatusRequest request);

    /**
     * 修改企业管理员手机号
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/8
     **/
    boolean updateManagerMobile(UpdateManagerMobileRequest request);

    /**
     * 企业同步打标识
     *
     * @return
     */
    boolean syncHandlerFlag();
}
