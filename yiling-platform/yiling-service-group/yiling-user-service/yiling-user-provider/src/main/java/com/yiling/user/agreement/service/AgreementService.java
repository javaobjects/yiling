package com.yiling.user.agreement.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.bo.AgreementPageListItemBO;
import com.yiling.user.agreement.bo.EntThirdAgreementInfoBO;
import com.yiling.user.agreement.bo.StatisticsAgreementDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.EntPageListItemDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.ThirdAgreementEntPageListItemDTO;
import com.yiling.user.agreement.dto.YearAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.CloseAgreementRequest;
import com.yiling.user.agreement.dto.request.EditAgreementRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementCountRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.QueryEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementEntListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementRequest;
import com.yiling.user.agreement.dto.request.UpdateAgreementRequest;
import com.yiling.user.agreement.entity.AgreementDO;
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;

/**
 * <p>
 * 协议表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
public interface AgreementService extends BaseService<AgreementDO> {

	/**
	 * 新增协议接口
	 *
	 * @param request
	 * @return
	 */
	Boolean save(SaveAgreementRequest request);

	/**
	 * 新增协议接口-用于导入
	 *
	 * @param request
	 * @return
	 */
	Long saveForImport(SaveAgreementRequest request);

	/**
	 * 修改协议接口
	 *
	 * @param request
	 * @return
	 */
	Boolean update(EditAgreementRequest request);

	/**
	 * 根据主键Id获取协议详情信息
	 *
	 * @param id
	 * @return
	 */
	AgreementDTO getAgreementDetailsInfo(Long id);

	/**
	 * 根据主键Id获取协议详情信息(批量)
	 *
	 * @param idList
	 * @return
	 */
	List<AgreementDTO> getAgreementList(List<Long> idList);

//	/**
//	 * 获取条件信息
//	 *
//	 * @param conditionId
//	 * @return
//	 */
//	AgreementConditionDTO getAgreementConditionById(Long conditionId);

	/**
	 * 获取补充协议详情
	 *
	 * @param id
	 * @return
	 */
	SupplementAgreementDetailDTO querySupplementAgreementsDetail(Long id);

	/**
	 * 批量查询补充协议详情
	 *
	 * @param ids
	 * @return
	 */
	List<SupplementAgreementDetailDTO> querySupplementAgreementsDetailList(List<Long> ids);

	/**
	 * 同时间维度是否存在相同的协议
	 *
	 * @param request
	 * @return
	 */
	Integer countAgreementByTimeAndOther(QueryAgreementCountRequest request);

	/**
	 * 根据协议id查询临时协议个数(包含停用)
	 *
	 * @param agreementIdList
	 * @param modeEnum
	 * @return
	 */
	Map<Long, Long> queryTempAgreementCount(List<Long> agreementIdList, AgreementModeEnum modeEnum);

	/**
	 * 根据协议id查询临时协议(包含停用)
	 *
	 * @param agreementIdList
	 * @param modeEnum
	 * @return
	 */
	Map<Long, List<AgreementDTO>> queryTempAgreement(List<Long> agreementIdList, AgreementModeEnum modeEnum);


	/**
	 * 根据企业id或父协议Id、协议状态查询协议列表
	 * parentAgreementIds 为空时查询年度协议列表 否则查询补充协议列表
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementPageListItemBO> queryAgreementsPageList(QueryAgreementPageListRequest request);

	/**
	 * 根据queryEid和eid分页查询双方发生过的补充三方协议
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementPageListItemBO> querySupplementAgreementsPageListByEids(QueryThirdAgreementPageListRequest request);

	/**
	 * 根据queryEid和eid查询双方发生过的补充三方协议
	 *
	 * @param request
	 * @return
	 */
	List<AgreementPageListItemBO> querySupplementAgreementsByEids(QueryThirdAgreementPageListRequest request);

	/**
	 * 根据企业id查询所有与该企业签订过三方协议的企业（查出的可以是二级商和ka）并过滤甲方
	 *
	 * @param request
	 * @return
	 */
	Page<ThirdAgreementEntPageListItemDTO> queryThirdAgreementsEntPageList(QueryThirdAgreementEntListRequest request);

	/**
	 * 统计协议总数量
	 *
	 * @return
	 */
	EntThirdAgreementInfoBO statisticsAgreement();

	/**
	 * 统计某一个供应商的协议数量
	 *
	 * @param eidList
	 * @param queryEid 如果该值为空，则统计eidList全部协议数，反之则统计eidList-queryEid两者之间的协议数
	 * @return
	 */
	List<StatisticsAgreementDTO> statisticsAgreementByEid(List<Long> eidList, Long queryEid);

	/**
	 * 删除或停用协议
	 *
	 * @param request
	 * @return
	 */
	Boolean agreementClose(CloseAgreementRequest request);

	/**
	 * 通过协议ID获取详情信息
	 *
	 * @param id
	 * @return
	 */
	YearAgreementDetailDTO queryYearAgreementsDetail(Long id);


	/**
	 * 查询已经开始的协议列表
	 *
	 * @return
	 */
	List<SupplementAgreementDetailDTO> queryStartSupplementAgreementsDetail();

	/**
	 * 根据采购企业id和model查询所有协议
	 *
	 * @param eidList
	 * @param mode                  =null时查所有
	 * @param agreementCategoryEnum =null时查所有
	 * @return
	 */
	List<AgreementDTO> queryAgreementList(List<Long> eidList, AgreementModeEnum mode, AgreementCategoryEnum agreementCategoryEnum);

	List<AgreementDTO> queryAgreementListByBuyerEid(Long buyerEid);
	/**
	 * 更新协议
	 *
	 * @param request
	 * @return
	 */
	Boolean updateAgreement(UpdateAgreementRequest request);

    /**
     * 每天判断协议是否过期，去维护协议关系
     * @return
     */
    Boolean syncAgreementRelation();

	/**
	 * 查询的企业列表按年度协议数量倒叙排序
	 *
	 * @param request
	 * @return
	 */
	Page<EntPageListItemDTO> queryEntPageList(QueryEntPageListRequest request);

	/**
	 * 查询所有生效的补充协议的丙方eid
	 *
	 * @return
	 */
	List<Long> queryEidByMigrate();

    /**
     * 根据三方eid查询生效的协议
     *
     * @param thirdEid
     * @return
     */
    List<AgreementDTO> queryAgreementListByThirdEid(Long thirdEid);

    /**
     * 批量更新迁移状态
     *
     * @param agreementDOS
     * @return
     */
    Boolean updateMigrateStatus(List<AgreementDO> agreementDOS);

}
