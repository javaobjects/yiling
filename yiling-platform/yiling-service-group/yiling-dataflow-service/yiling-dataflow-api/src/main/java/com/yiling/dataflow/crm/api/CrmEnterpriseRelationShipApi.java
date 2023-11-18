package com.yiling.dataflow.crm.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationShipRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/9/19/agency/getUnPermitCrmEnterprisePartInfoByName
 */
public interface CrmEnterpriseRelationShipApi {

    /**
     * 事业部! =事业一部(呼吸) 时，根据团购出库终端在 终爱三者关系里面进行查找，
     * 看终端三者关系里面是否存在 部门 事业三部(呼吸)且 产品组名称=呼吸事业部产品组 的数据，
     * 如果不存在，此节点跳过，如果存在，需要 张坚雄进行审批
     *
     * @param nameCode 客户编码
     * @return true=存在 false=不存在
     */
    boolean isBreathingDepartmentByNameCode(List<String> nameCode);

    List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByNameList(List<String> nameList);

    Page<CrmEnterpriseRelationShipDTO> getCrmRelationPage(QueryCrmEnterpriseRelationShipPageListRequest crmEnterpriseRelationShipPageListRequest);

    Boolean saveOrUpdate(SaveCrmEnterpriseRelationShipRequest request);

    /**
     * 删除三者关系
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    Boolean remove(RemoveCrmEnterpriseRelationShipRequest request);

    /**
     * 根据id查询三者关系信息
     *
     * @param id 主键id
     * @return 三者关系信息
     */
    CrmEnterpriseRelationShipDTO queryById(Long id);

    String getProvinceAreaByThreeParms(String yxDept, String yxProvince);

    List<CrmDepartmentAreaRelationDTO> getGoodsGroup(String ywbm, Integer supplyChainRole);

    /**
     * 根据档案基本信息ID查询关联的三者关系
     * @param enterpriseIds
     * @return
     */
    List<CrmEnterpriseRelationPostDTO> getEnterpriseRelationPostByProductGroup(List<Long> enterpriseIds);

    Boolean unlockRelation(List<Long> srcRelationShipIps);

    Boolean changeRelationShip(ChangeRelationShipRequest srcRelationShipIps);

    Boolean batchUpdateById(List<CrmEnterpriseRelationShipDTO> updateData,String backupTableName);

    Long listSuffixByNameList(List<Long> nameList, Long id, String tableSuffix);

    List<CrmDepartmentAreaRelationDTO>getAllData();

    List<Long> getCrmEnterprisePostCode();

    /**
     * 根据三者关系岗位编码返回机构编码列表
     * @param postCode
     * @return
     */
    List<Long> getCrmEnterIdListByPostCode(String postCode);

    /**
     * 根据产品组映射表id，查看是否被三者关系关联
     * @param departRelationIds
     * @return true被关联，false没有被关联
     */
    @Deprecated
    Map<Long,Boolean> getUseByDepartRelationIds(List<Long> departRelationIds);

    List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByCrmenterpriseIdList(List<Long> toList);

    /**
     * 根据crmEnterpriseId判断是否存在三者关系
     * @param crmEnterpriseId
     * @return
     */
    Boolean getExistByCrmEnterpriseId(Long crmEnterpriseId) ;

    /**
     * 根据主键id列表、表后缀名查询三者关系
     *
     * @param idList 主键Id列表
     * @param tableSuffix 表名后缀：空值查询实时表数据，传值不为空则查询备份表数据（例如“_wash_202303”）
     * @return  三者关系列表
     */
    List<CrmEnterpriseRelationShipDTO> listSuffixByIdList(List<Long> idList,String tableSuffix);

    /**
     * 备份表根据机构id查询三者关系表id
     *
     * @param idList  映射表id
     * @param crmEnterpriseId  机构id
     * @param tableSuffix   表后缀 比如wash_202303
     * @return  三者关系表id
     */
    Long listSuffixByCrmEnterpriseIdList(List<Long> idList,Long crmEnterpriseId, String tableSuffix);

}
