package com.yiling.user.agreement.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.bo.EntThirdAgreementInfoBO;
import com.yiling.user.agreement.bo.StatisticsAgreementDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementPageListItemDTO;
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
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
public interface AgreementApi {

	/**
	 * 保存协议
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
	 * 保存修改协议
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

//	/**
//	 * 获取条件信息
//	 *
//	 * @param conditionId
//	 * @return
//	 */
//	AgreementConditionDTO getAgreementConditionById(Long conditionId);

	/**
	 * 根据主键Id获取协议详情信息
	 *
	 * @param ids
	 * @return
	 */
	List<AgreementDTO> getAgreementDetailsInfoByIds(List<Long> ids);

	/**
	 * 根据主体，采购商，时间维度，协议类型 查询主协议个数
	 *
	 * @param request
	 * @return
	 */
	Integer countAgreementByTimeAndOther(QueryAgreementCountRequest request);

	/**
	 * 统计协议数量
	 *
	 * @return
	 */
	EntThirdAgreementInfoBO statisticsAgreement();

	/**
	 * 统计某一个供应商下面的协议
	 *
	 * @param eidList
	 * @param queryEid 如果该值为空，则统计eidList全部协议数，反之则统计eidList-queryEid两者之间的协议数
	 * @return
	 */
	List<StatisticsAgreementDTO> statisticsAgreementByEid(List<Long> eidList, Long queryEid);

	/**
	 * 根据企业id或父协议Id、协议状态查询协议列表
	 * parentAgreementIds 为空时查询年度协议列表 否则查询补充协议列表
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementPageListItemDTO> queryAgreementsPageList(QueryAgreementPageListRequest request);

	/**
	 * 根据queryEid和eid分页查询双方发生过的补充三方协议
	 *
	 * @param request
	 * @return
	 */
	Page<AgreementPageListItemDTO> querySupplementAgreementsPageListByEids(QueryThirdAgreementPageListRequest request);

	/**
	 * 根据queryEid和eid查询双方发生过的补充三方协议
	 *
	 * @param request
	 * @return
	 */
	List<AgreementPageListItemDTO> querySupplementAgreementsByEids(QueryThirdAgreementPageListRequest request);

//	/**
//	 * 根据协议id查询临时协议个数(包含停用)
//	 *
//	 * @param agreementIdList
//	 * @param modeEnum
//	 * @return
//	 */
//	Map<Long, Long> queryTempAgreementCount(List<Long> agreementIdList, AgreementModeEnum modeEnum);

	/**
	 * 根据协议id查询临时协议(包含停用)
	 *
	 * @param agreementIdList
	 * @param modeEnum
	 * @return
	 */
	Map<Long, List<AgreementDTO>> queryTempAgreement(List<Long> agreementIdList, AgreementModeEnum modeEnum);

	/**
	 * 根据企业id查询所有与该企业签订过三方协议的企业
	 *
	 * @param request
	 * @return
	 */
	Page<ThirdAgreementEntPageListItemDTO> queryThirdAgreementsEntPageList(QueryThirdAgreementEntListRequest request);

	/**
	 * 停用或删除协议
	 *
	 * @param request
	 * @return
	 */
	Boolean agreementClose(CloseAgreementRequest request);


	/**
	 * 查询年度协议详情
	 *
	 * @param id
	 * @return
	 */
	YearAgreementDetailDTO queryYearAgreementsDetail(Long id);

	/**
	 * 查询补充协议详情
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
	 * 根据采购企业id和model、agreementCategoryEnum查询所有协议
	 *
	 * @param eidList
	 * @param mode                  =null时查所有
	 * @param agreementCategoryEnum =null时查所有
	 * @return
	 */
	List<AgreementDTO> queryAgreementList(List<Long> eidList, AgreementModeEnum mode, AgreementCategoryEnum agreementCategoryEnum);


    /**
     * 根据采购企业id查询所有进返利池协议集合
     *
     * @param buyerEid
     * @return
     */
    List<AgreementDTO> queryAgreementListByBuyerEid(Long buyerEid);

	/**
	 * 查询已经开始的协议列表
	 *
	 * @return
	 */
	List<SupplementAgreementDetailDTO> queryStartSupplementAgreementsDetail();

	/**
	 * 更新协议
	 *
	 * @param request
	 * @return
	 */
	Boolean updateAgreement(UpdateAgreementRequest request);

    /**
     * 每天晚上判断协议是否到期
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

}
