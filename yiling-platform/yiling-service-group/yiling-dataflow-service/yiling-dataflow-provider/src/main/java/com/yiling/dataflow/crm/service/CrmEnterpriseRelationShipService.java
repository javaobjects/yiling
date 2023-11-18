package com.yiling.dataflow.crm.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.sale.dto.request.QueryRelationShipByPoCodeRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-16
 */
public interface CrmEnterpriseRelationShipService extends BaseService<CrmEnterpriseRelationShipDO> {

    boolean isBreathingDepartmentByNameCode(List<String> nameCode);

    List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByNameList(List<String> nameList);

    CrmEnterpriseRelationShipDTO getCrmEnterpriseRelationShipByGroupNameAndCustomerCode(List<String> groupNameList,String customerCode);

    Page<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipPage(QueryCrmEnterpriseRelationShipRequest request);
    /**
     * 删除三者关系
     *
     * @param request   删除条件
     * @return  成功/失败
     */
    Boolean remove(RemoveCrmEnterpriseRelationShipRequest request);

    /**
     * 备份表根据医疗机构id查询机构扩展信息
     *
     * @param idList  主键Id
     * @param tableSuffix   表名
     * @return  查询三者关系数据
     */
    List<CrmEnterpriseRelationShipDO> listSuffixByIdList(List<Long> idList,String tableSuffix);

    /**
     * 备份表根据机构id查询三者关系表id
     *
     * @param idList  映射表id
     * @param crmEnterpriseId  机构id
     * @param tableSuffix   表后缀 比如wash_202303
     * @return  三者关系表id
     */
    @Deprecated
    Long listSuffixByCrmEnterpriseIdList(List<Long> idList,Long crmEnterpriseId, String tableSuffix);

    /**
     * 通过商品code和机构id查询三者关系
     *
     * @param goodsCode  商品code
     * @param orgId  机构id
     * @param tableSuffix   表后缀 比如wash_202303
     * @return  三者关系表id
     */
    Long listSuffixByOrgIdAndGoodsCode(Long goodsCode,Long orgId, String tableSuffix);

    /**
     * 备份表根据医疗机构id查询机构扩展信息
     *
     * @param productGroupIds  产品组名称集合
     * @param id  机构id
     * @param tableSuffix   表名
     * @return  查询三者关系数据
     */
    List<CrmEnterpriseRelationShipDO> listSuffixByNameList(List<Long> productGroupIds,Long id,String tableSuffix);

    /**
     * 通过品种ID和机构id获取三者关系
     * @param categoryId
     * @param id
     * @param tableSuffix
     * @return
     */
    CrmEnterpriseRelationShipDO listSuffixByCategoryId(Long categoryId, Long id, String tableSuffix);

    /**
     * 更新三者关系表信息
     *
     * @param relationShipDOS  产品组名称集合
     * @return  查询三者关系数据
     */
    Boolean updateBackUpBatchById(List<CrmEnterpriseRelationShipDO> relationShipDOS, String backupTableName);

    /**
     * 查询所有的三者关系岗位信息
     *
     * @return  查询三者关系数据
     */
    List<Long> getCrmEnterprisePostCode();

    /**
     * 更新三者关系表信息
     *
     * @param relationShipRequest  产品组名称集合
     * @return  查询三者关系数据
     */
    boolean updateBackUpBatchByPostCode(UpdateCrmEnterpriseRelationShipRequest relationShipRequest, String tableSuffix);

    /**
     * 用岗位id查三者关系表crmEnterpriseId集合
     *
     * @param postCode  产品组名称集合
     * @return  查询三者关系数据
     */
    List<Long> getCrmEnterIdListByPostCode(String postCode);

    /**
     * 用岗位id查三者关系表crmEnterpriseId集合
     *
     * @param idList  业务部门与产品组对应关系表id
     * @param crmEnterpriseId  机构id
     * @return  查询三者关系表id
     */
    Long listByCrmEnterpriseIdList(List<Long> idList, Long crmEnterpriseId) ;

    /**
     * 根据产品组映射表id，查看是否被三者关系关联
     * @param departRelationIds
     * @return
     */
    Map<Long, Boolean> getUseByDepartRelationIds(List<Long> departRelationIds);

    /**
     * 用机构ids查询三者关系表集合
     *
     * @param ids  机构ids
     * @return  查询三者关系数据
     */
    List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByCrmenterpriseIdList(List<Long> ids);


     List<CrmEnterpriseRelationShipDO> listByProductGroupIdsList(List<Long> productGroupId, Long id);

    /**
     * 根据crmEnterpriseId判断是否存在三者关系
     * @param crmEnterpriseId
     * @return
     */
    Boolean getExistByCrmEnterpriseId(Long crmEnterpriseId) ;

    /**
     * 用岗位id查询三者关系集合
     *
     * @param postCode  岗位id
     * @param tableSuffix 备份表
     * @return  查询三者关系数据
     */
    List<CrmEnterpriseRelationShipDO> listByPostCodeList(List<Long> postCode, String tableSuffix);

    /**
     * 用岗位id查询三者关系集合
     *
     * @param request  岗位id
     * @param tableSuffix 备份表
     * @return  查询三者关系数据
     */
    Page<CrmEnterpriseRelationShipDO> pageByPostCodeList(QueryRelationShipByPoCodeRequest request, String tableSuffix);

    /**
     *  批量删除三者关系根据机构编码
     * @param cEnIdList opUserId,message
     * @return
     */
    int batchDeleteWithCrmEnterIds(List<Long> cEnIdList, Long opUserId,String message,Long manorId);

    List<CrmEnterpriseRelationShipDO> listByEidsAndRole(List<Long> crmEIds, int supplyChainRole);

    /**
     * 对于医疗机构，用month，和year暂存上级主管信息，更新的时候替换到上级主管字段里面
     *
     * @param tableSuffix 备份表
     * @return  查询三者关系数据
     */
    void updateBackUpBatchForHospital(String tableSuffix);

}
