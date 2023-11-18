package com.yiling.f2b.admin.agreement.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.agreement.form.AgreementCloseForm;
import com.yiling.f2b.admin.agreement.form.EditAgreementForm;
import com.yiling.f2b.admin.agreement.form.GetAgreementStatusCountForm;
import com.yiling.f2b.admin.agreement.form.QueryAgreementPortalPageListForm;
import com.yiling.f2b.admin.agreement.form.QuerySupplementAgreementPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryThirdAgreementCountForm;
import com.yiling.f2b.admin.agreement.form.QueryThirdAgreementEntPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryThirdAgreementPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryYearAgreementPageListForm;
import com.yiling.f2b.admin.agreement.form.SaveAgreementGoodsForm;
import com.yiling.f2b.admin.agreement.form.SaveTempAgreementForm;
import com.yiling.f2b.admin.agreement.form.SaveYearAgreementForm;
import com.yiling.f2b.admin.agreement.form.UpdateAgreementForm;
import com.yiling.f2b.admin.agreement.vo.AgreementConditionVO;
import com.yiling.f2b.admin.agreement.vo.AgreementCustomerListItemVO;
import com.yiling.f2b.admin.agreement.vo.AgreementPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.AgreementStatusCountVO;
import com.yiling.f2b.admin.agreement.vo.AgreementsCategoryVO;
import com.yiling.f2b.admin.agreement.vo.AgreementsSnapshotDetailVO;
import com.yiling.f2b.admin.agreement.vo.EntThirdAgreementInfoVO;
import com.yiling.f2b.admin.agreement.vo.SupplementAgreementDetailVO;
import com.yiling.f2b.admin.agreement.vo.SupplementAgreementPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.ThirdAgreementEntListVO;
import com.yiling.f2b.admin.agreement.vo.YearAgreementDetailVO;
import com.yiling.f2b.admin.enterprise.form.QueryThirdEnterpriseListForm;
import com.yiling.f2b.admin.enterprise.vo.EnterpriseVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsPatentEnum;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.api.AgreementSnapshotApi;
import com.yiling.user.agreement.bo.EntThirdAgreementInfoBO;
import com.yiling.user.agreement.bo.StatisticsAgreementDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.AgreementPageListItemDTO;
import com.yiling.user.agreement.dto.AgreementSnapshotDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.ThirdAgreementEntPageListItemDTO;
import com.yiling.user.agreement.dto.YearAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.CloseAgreementRequest;
import com.yiling.user.agreement.dto.request.EditAgreementRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementCountRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementEntListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementGoodsRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementRequest;
import com.yiling.user.agreement.dto.request.UpdateAgreementRequest;
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.enums.AgreementStatusEnum;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.ChannelCustomerApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议表 前端控制器
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@RestController
@RequestMapping("/agreement")
@Api(tags = "协议模块")
@Slf4j
public class AgreementController extends BaseController {

    @DubboReference
    AgreementApi                  agreementApi;
    @DubboReference
    AgreementGoodsApi             agreementGoodsApi;
    @DubboReference
    ChannelCustomerApi            channelCustomerApi;
    @DubboReference
    CustomerApi                   customerApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;
    @DubboReference
    UserApi                       userApi;
    @DubboReference
    AgreementSnapshotApi          agreementSnapshotApi;

    @ApiOperation(value = "pop后台主协议新增", httpMethod = "POST")
    @PostMapping("/saveYear")
    public Result<BoolObject> saveYear(@RequestBody @Valid SaveYearAgreementForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        if (form.getStartTime().getTime() > form.getEndTime().getTime()) {
            return Result.failed("时间设置不正确");
        }
        //判断时间履约结束时间不能晚于当前时间
        if (form.getEndTime().getTime() < System.currentTimeMillis()) {
            return Result.failed("结束时间不能晚于当前时间");
        }

        if (CollectionUtils.isEmpty(form.getAgreementGoodsList())) {
            return Result.failed("没有设置协议商品");
        }

        //判断商品是否在主体里面
        List<Long> goodsIds = goodsApi.getGoodsIdsByEid(form.getEid());
        for (SaveAgreementGoodsForm saveAgreementGoodsForm : form.getAgreementGoodsList()) {
            if (!goodsIds.contains(saveAgreementGoodsForm.getGoodsId())) {
                return Result.failed("配置商品信息不在主体商品范围内");
            }
        }

        //同一销售分公司（北京以岭、石家庄以岭、以岭健康城）、同一采购企业、同一时间维度、只能存在一份
        QueryAgreementCountRequest request = new QueryAgreementCountRequest();
        request.setEid(form.getEid());
        request.setSecondEid(form.getSecondEid());
        request.setStartTime(form.getStartTime());
        request.setEndTime(form.getEndTime());
        Integer count = agreementApi.countAgreementByTimeAndOther(request);
        if (count >= 1) {
            return Result.failed("同一维度的协议只能存在一份");
        }

        //添加商品标准库信息
        SaveAgreementRequest agreementRequest = PojoUtils.map(form, SaveAgreementRequest.class);
        List<SaveAgreementGoodsRequest> goodsList = agreementRequest.getAgreementGoodsList();
        if (CollUtil.isNotEmpty(goodsList)) {
            List<Long> goodsIdList = goodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList= goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
            Map<Long,StandardGoodsBasicDTO> map=standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            goodsList.forEach(e->{
                StandardGoodsBasicDTO standardGoodsBasicDTO=map.get(e.getGoodsId());
                e.setStandardGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
                e.setStandardLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
            });
        }

        agreementRequest.setCategory(AgreementCategoryEnum.YEAR_AGREEMENT.getCode());
        agreementRequest.setMode(AgreementModeEnum.SECOND_AGREEMENTS.getCode());
        agreementRequest.setStatus(AgreementStatusEnum.OPEN.getCode());
        agreementRequest.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(agreementApi.save(agreementRequest)));
    }

    @ApiOperation(value = "pop后台补充协议新增", httpMethod = "POST")
    @PostMapping("/saveTemp")
    public Result<BoolObject> saveTemp(@RequestBody @Valid SaveTempAgreementForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        if (form.getStartTime().getTime() > form.getEndTime().getTime()) {
            return Result.failed("时间设置不正确");
        }
        //判断时间履约结束时间不能晚于当前时间
        if (form.getEndTime().getTime() < System.currentTimeMillis()) {
            return Result.failed("结束时间不能晚于当前时间");
        }

        //获取主体协议
        AgreementDTO agreementDetailsInfo = agreementApi.getAgreementDetailsInfo(form.getParentId());

        if (agreementDetailsInfo.getStartTime().getTime() > form.getStartTime().getTime()
                || agreementDetailsInfo.getEndTime().getTime() < form.getStartTime().getTime()) {
            return Result.failed("临时协议的履约开始时间不在年度协议范围内");
        }
        if (agreementDetailsInfo.getStartTime().getTime() > form.getEndTime().getTime()
                || agreementDetailsInfo.getEndTime().getTime() < form.getEndTime().getTime()) {
            return Result.failed("临时协议的履约结束时间不在年度协议范围内");
        }

        if (CollectionUtils.isEmpty(form.getAgreementGoodsList())) {
            return Result.failed("没有设置协议商品");
        }

        //验证商品是否在主协议下
        List<Long> goodsIdList = form.getAgreementGoodsList().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getAgreementGoodsAgreementIdByList(form.getParentId());
        List<Long> agreementGoodsIdList = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        for (Long goodsId : goodsIdList) {
            if (!agreementGoodsIdList.contains(goodsId)) {
                return Result.failed("补充协议商品不再主协议内");
            }
        }

        //如果新增补充协议是专利和非专利需要
        if (form.getIsPatent().equals(GoodsPatentEnum.UN_PATENT.getCode()) || form.getIsPatent().equals(GoodsPatentEnum.PATENT.getCode())) {
            List<Long> goodsIds = agreementGoodsDTOList.stream().filter(e -> e.getIsPatent().equals(form.getIsPatent())).map(m -> m.getGoodsId())
                    .collect(Collectors.toList());
            for (Long goodsId : goodsIdList) {
                if (!goodsIds.contains(goodsId)) {
                    return Result.failed("补充协议商品类型不匹配");
                }
            }
        }

        //如果有第三方企业，就把第三方赋值到乙方
        SaveAgreementRequest agreementRequest = PojoUtils.map(form, SaveAgreementRequest.class);

        //添加商品标准库信息
        List<SaveAgreementGoodsRequest> goodsList = agreementRequest.getAgreementGoodsList();
        if (CollUtil.isNotEmpty(goodsList)) {
            List<Long> goodsIds = goodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList= goodsApi.batchQueryStandardGoodsBasic(goodsIds);
            Map<Long,StandardGoodsBasicDTO> map=standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            goodsList.forEach(e->{
                StandardGoodsBasicDTO standardGoodsBasicDTO=map.get(e.getGoodsId());
                e.setStandardGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
                e.setStandardLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
            });
        }

        //判断传入的值为直连
        if (form.getType() != null && form.getType() == 2) {
            agreementRequest.setConditionRule(0);
        }

        agreementRequest.setEid(agreementDetailsInfo.getEid());
        agreementRequest.setEname(agreementDetailsInfo.getEname());

        agreementRequest.setCategory(AgreementCategoryEnum.TEMP_AGREEMENT.getCode());
        //三方协议
        if (form.getThirdEid() != null && form.getThirdEid() != 0) {
            agreementRequest.setMode(AgreementModeEnum.THIRD_AGREEMENTS.getCode());
            agreementRequest.setSecondEid(form.getThirdEid());
            agreementRequest.setSecondName(form.getThirdName());
            agreementRequest.setSecondChannelName(form.getThirdChannelName());
            agreementRequest.setThirdEid(agreementDetailsInfo.getSecondEid());
            agreementRequest.setThirdName(agreementDetailsInfo.getSecondName());
            agreementRequest.setThirdChannelName(agreementDetailsInfo.getSecondChannelName());
        } else {
            //双方协议
            agreementRequest.setMode(AgreementModeEnum.SECOND_AGREEMENTS.getCode());
            agreementRequest.setSecondEid(agreementDetailsInfo.getEid());
            agreementRequest.setSecondName(agreementDetailsInfo.getEname());
            agreementRequest.setSecondChannelName(EnterpriseChannelEnum.INDUSTRY.getName());
            agreementRequest.setThirdEid(agreementDetailsInfo.getSecondEid());
            agreementRequest.setThirdName(agreementDetailsInfo.getSecondName());
            agreementRequest.setThirdChannelName(agreementDetailsInfo.getSecondChannelName());
        }
        agreementRequest.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(agreementApi.save(agreementRequest)));
    }

    /**
     * 协议首页统计
     *
     * @return
     */
    @ApiOperation(value = "协议首页统计", httpMethod = "POST")
    @PostMapping("/statistics")
    public Result<EntThirdAgreementInfoVO> statistics() {
        EntThirdAgreementInfoBO entThirdAgreementInfoBO = agreementApi.statisticsAgreement();
        return Result.success(PojoUtils.map(entThirdAgreementInfoBO, EntThirdAgreementInfoVO.class));
    }

    /**
     * 协议首页列表
     *
     * @param staffInfo
     * @return
     */
    @ApiOperation(value = "协议首页列表", httpMethod = "POST")
    @PostMapping("/list")
    public Result<Page<AgreementCustomerListItemVO>> list(@CurrentUser CurrentStaffInfo staffInfo,
                                                          @RequestBody @Valid QueryAgreementPortalPageListForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        //过滤掉工业
        if (form.getChannelId().equals(0L)) {
            request.setChannelIds(ListUtil.toList(EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode(), EnterpriseChannelEnum.LEVEL_1.getCode(),
                    EnterpriseChannelEnum.LEVEL_2.getCode(), EnterpriseChannelEnum.KA.getCode(), EnterpriseChannelEnum.Z2P1.getCode()));
        } else {
            request.setChannelIds(ListUtil.toList(form.getChannelId()));
        }
        request.setEids(Collections.singletonList(Constants.YILING_EID));
        request.setUseLine(EnterpriseCustomerLineEnum.POP.getCode());
        Page<EnterpriseCustomerDTO> pageDTO = customerApi.pageList(request);

        Page<AgreementCustomerListItemVO> page = PojoUtils.map(pageDTO, AgreementCustomerListItemVO.class);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(page);
        }

        //客户ID列表
        List<Long> eidList = page.getRecords().stream().map(AgreementCustomerListItemVO::getCustomerEid).collect(Collectors.toList());
        //统计协议数量
        Map<Long, StatisticsAgreementDTO> agreementBOMap = agreementApi.statisticsAgreementByEid(eidList, null).stream()
                .collect(Collectors.toMap(StatisticsAgreementDTO::getEid, e -> e));
        //查询企业信息
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        //查询商务联系人
        Map<Long, Long> customerContactsCountMap = customerApi.countCustomerContacts(staffInfo.getCurrentEid(), eidList);
        //补全相关信息
        page.getRecords().forEach(e -> {
            StatisticsAgreementDTO bo = agreementBOMap.get(e.getCustomerEid());
            e.setYearAgreementCount(bo.getYearAgreementList().size());
            e.setTempAgreementCount(bo.getTempAgreementList().size());
            e.setThirdAgreementCount(bo.getThirdAgreementsList().size());
            e.setThirdAgreementsOtherCount(bo.getThirdAgreementsCustomerList().size());
            e.setCustomerContactNum(customerContactsCountMap.getOrDefault(e.getCustomerEid(), 0L));
            e.setCustomerName(enterpriseDTOMap.get(e.getCustomerEid()).getName());
            //补全状态及渠道类型
            PojoUtils.map(enterpriseDTOMap.get(e.getCustomerEid()), e);
        });

        return Result.success(page);
    }

    /**
     * 第三方渠道商弹框
     *
     * @param staffInfo
     * @return
     */
    @ApiOperation(value = "pop后台第三方渠道商弹框", httpMethod = "POST")
    @PostMapping("/thirdEnterpriseList")
    public Result<Page<EnterpriseVO>> thirdEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo,
                                                          @RequestBody @Valid QueryThirdEnterpriseListForm form) {
        QueryPurchaseRelationPageListRequest request = PojoUtils.map(form, QueryPurchaseRelationPageListRequest.class);
        Page<EnterpriseDTO> pageList = enterprisePurchaseRelationApi.getSellerEnterprisePageList(request);
        Page<EnterpriseVO> result = PojoUtils.map(pageList, EnterpriseVO.class);
        result.getRecords().forEach(e -> {
            e.setChannelName(EnterpriseChannelEnum.getByCode(Long.valueOf(e.getChannelId())).getName());
        });
        return Result.success(result);
    }

    @ApiOperation(value = "查询三方协议企业列表")
    @PostMapping("/getThirdAgreementEntList")
    public Result<Page<ThirdAgreementEntListVO>> getThirdAgreementEntList(@Valid @RequestBody QueryThirdAgreementEntPageListForm form) {
        Page<ThirdAgreementEntListVO> result;
        QueryThirdAgreementEntListRequest request = PojoUtils.map(form, QueryThirdAgreementEntListRequest.class);
        //分页查询企业
        Page<ThirdAgreementEntPageListItemDTO> pageList = agreementApi.queryThirdAgreementsEntPageList(request);
        result = PojoUtils.map(pageList, ThirdAgreementEntListVO.class);

        if (CollUtil.isEmpty(result.getRecords())) {
            return Result.success(result);
        }

        List<Long> eidList = result.getRecords().stream().map(ThirdAgreementEntListVO::getEid).collect(Collectors.toList());
        //查询企业信息
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
        //统计企业下协议数
        Map<Long, StatisticsAgreementDTO> agreementBOMap = agreementApi.statisticsAgreementByEid(eidList, form.getEid()).stream()
                .collect(Collectors.toMap(StatisticsAgreementDTO::getEid, e -> e));

        //补全相关信息
        result.getRecords().forEach(e -> {
            //设置企业信息
            EnterpriseDTO enterpriseDTO = enterpriseMap.get(e.getEid());
            if (ObjectUtil.isNotNull(enterpriseDTO)) {
                PojoUtils.map(enterpriseMap.get(e.getEid()), e);
            }
            StatisticsAgreementDTO statisticsAgreementDTO = agreementBOMap.get(e.getEid());
            if (ObjectUtil.isNotNull(statisticsAgreementDTO)) {
                e.setSupplementAgreementCount(statisticsAgreementDTO.getThirdAgreementsList().size());
            }
        });
        return Result.success(result);
    }

    @ApiOperation(value = "分页查询补充协议---三方协议列表")
    @PostMapping("/getEntSupplementAgreementPageList")
    public Result<Page<SupplementAgreementPageListItemVO>> getEntSupplementAgreementPageList(@Valid @RequestBody QueryThirdAgreementPageListForm form) {
        Page<SupplementAgreementPageListItemVO> result;
        QueryThirdAgreementPageListRequest request = PojoUtils.map(form, QueryThirdAgreementPageListRequest.class);
        Page<AgreementPageListItemDTO> pageList = agreementApi.querySupplementAgreementsPageListByEids(request);
        result = PojoUtils.map(pageList, SupplementAgreementPageListItemVO.class);
        List userIds = result.getRecords().stream().map(SupplementAgreementPageListItemVO::getCreateUser).collect(Collectors.toList());
        if (CollUtil.isEmpty(result.getRecords())) {
            return Result.success(result);
        }
        List<UserDTO> list = userApi.listByIds(new ArrayList<>(userIds));
        Map<Long, String> userMap = list.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        //返回参数设置状态
        result.getRecords().forEach(e -> {
            e.setAgreementStatus(form.getAgreementStatus());
            e.setUpdateUserName(userMap.getOrDefault(e.getCreateUser(), ""));
        });
        return Result.success(result);
    }

    @ApiOperation(value = "分页查询年度协议列表")
    @PostMapping("/getAgreementPageList")
    public Result<Page<AgreementPageListItemVO>> getAgreementPageList(@Valid @RequestBody QueryYearAgreementPageListForm form) {
        Page<AgreementPageListItemVO> result;
        //操作人id列表
        Set<Long> userIds = new HashSet<>();
        //主协议id
        Set<Long> agreementIds = new HashSet<>();
        //临时协议map
        Map<Long, List<AgreementDTO>> tempCountMap;

        QueryAgreementPageListRequest request = PojoUtils.map(form, QueryAgreementPageListRequest.class);
        Page<AgreementPageListItemDTO> pageList = agreementApi.queryAgreementsPageList(request);
        result = PojoUtils.map(pageList, AgreementPageListItemVO.class);
        //返回参数设置状态
        if (CollUtil.isNotEmpty(result.getRecords())) {
            //查询用户信息
            result.getRecords().forEach(e -> {
                agreementIds.add(e.getId());
                userIds.add(e.getCreateUser());
                userIds.add(e.getUpdateUser());
            });
            Map<Long, UserDTO> userDOMap = userApi.listByIds(new ArrayList<>(userIds)).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
            //如果查询年度协议则查出补充协议个数
            if (CollUtil.isEmpty(request.getParentAgreementIds())) {
                //批量查询子协议数
                tempCountMap = agreementApi.queryTempAgreement(new ArrayList<>(agreementIds), AgreementModeEnum.SECOND_AGREEMENTS);

            } else {
                tempCountMap = MapUtil.newHashMap();
            }
            //补全操作人及子协议数
            result.getRecords().forEach(e -> {
                List<AgreementDTO> agreementTempList = tempCountMap.get(e.getId());
                if (CollUtil.isEmpty(agreementTempList)) {
                    e.setTempCount(0);
                } else {
                    e.setTempCount(agreementTempList.size());
                }
                e.setCreateUserName(userDOMap.get(e.getCreateUser()).getName());
                UserDTO userDTO = userDOMap.get(e.getUpdateUser());
                if (ObjectUtil.isNotNull(userDTO)) {
                    e.setUpdateUserName(userDTO.getName());
                }
                e.setAgreementStatus(form.getAgreementStatus());
            });
        }
        return Result.success(result);
    }

    @ApiOperation(value = "查询年度协议详情")
    @GetMapping("/getYearAgreementsDetail")
    public Result<YearAgreementDetailVO> getYearAgreementsDetail(@CurrentUser CurrentStaffInfo staffInfo,
                                                                 @RequestParam("agreementId") Long agreementId) {
        YearAgreementDetailVO result;
        YearAgreementDetailDTO detailDTO = agreementApi.queryYearAgreementsDetail(agreementId);
        result = PojoUtils.map(detailDTO, YearAgreementDetailVO.class);
        if (result != null) {
            Map<Long, Long> customerContactsCountMap = customerApi.countCustomerContacts(staffInfo.getCurrentEid(),
                    ListUtil.toList(detailDTO.getSecondEid()));
            result.setSecondSalesmanNum(customerContactsCountMap.getOrDefault(detailDTO.getSecondEid(), 0L));
        }
        return Result.success(result);
    }

    @ApiOperation(value = "分页查询补充协议列表")
    @PostMapping("/getSupplementAgreementPageList")
    public Result<Page<SupplementAgreementPageListItemVO>> getSupplementAgreementPageList(@Valid @RequestBody QuerySupplementAgreementPageListForm form) {
        Page<SupplementAgreementPageListItemVO> result;
        //操作人id列表
        Set<Long> userIds = new HashSet<>();
        //主协议id
        Set<Long> agreementIds = new HashSet<>();
        //临时协议map
        Map<Long, Long> tempCountMap;

        QueryAgreementPageListRequest request = PojoUtils.map(form, QueryAgreementPageListRequest.class);
        //设置父协议id
        request.setParentAgreementIds(ListUtil.toList(form.getParentAgreementId()));
        Page<AgreementPageListItemDTO> pageList = agreementApi.queryAgreementsPageList(request);
        result = PojoUtils.map(pageList, SupplementAgreementPageListItemVO.class);
        //返回参数设置状态
        if (CollUtil.isNotEmpty(result.getRecords())) {
            //查询用户信息
            result.getRecords().forEach(e -> {
                agreementIds.add(e.getId());
                userIds.add(e.getCreateUser());
                userIds.add(e.getUpdateUser());
            });
            Map<Long, UserDTO> userDOMap = userApi.listByIds(new ArrayList<>(userIds)).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
            //补全操作人及子协议数
            result.getRecords().forEach(e -> {
                e.setCreateUserName(userDOMap.get(e.getCreateUser()).getName());
                UserDTO updateUser = userDOMap.get(e.getUpdateUser());
                if (ObjectUtil.isNotNull(updateUser)) {
                    e.setUpdateUserName(userDOMap.get(e.getUpdateUser()).getName());
                }
                e.setAgreementStatus(form.getAgreementStatus());
            });
        }
        return Result.success(result);
    }

    @ApiOperation(value = "根据补充协议id查询协议详情")
    @GetMapping("/getSupplementAgreementsDetail")
    public Result<SupplementAgreementDetailVO> getSupplementAgreementsDetail(@CurrentUser CurrentStaffInfo staffInfo,
                                                                             @RequestParam("agreementId") Long agreementId) {
        SupplementAgreementDetailVO result;

        SupplementAgreementDetailDTO detailDTO = agreementApi.querySupplementAgreementsDetail(agreementId);
        if (detailDTO == null) {
            return Result.success();
        }

        result = PojoUtils.map(detailDTO, SupplementAgreementDetailVO.class);
        return buildSupplementAgreementDetailResult(staffInfo, agreementId, result, detailDTO);
    }

    private Result<SupplementAgreementDetailVO> buildSupplementAgreementDetailResult(CurrentStaffInfo staffInfo, Long agreementId,
                                                                                     SupplementAgreementDetailVO result,
                                                                                     SupplementAgreementDetailDTO detailDTO) {
        //客户ID列表
        ArrayList<Long> customerEids;
        //设置年度协议名称
        YearAgreementDetailDTO yearAgreementDetailDTO = agreementApi.queryYearAgreementsDetail(detailDTO.getParentId());
        if (yearAgreementDetailDTO == null) {
            return Result.failed("主协议不存在");
        }
        result.setParentName(yearAgreementDetailDTO.getName());
        result.setParentId(yearAgreementDetailDTO.getId());

        if (CollUtil.isEmpty(detailDTO.getAgreementsConditionList())) {
            log.error("该协议没有条件{" + agreementId + "}");
            return Result.failed("数据不完整");
        }
        //封装协议条件listVO
        AgreementConditionVO agreementConditionVO = PojoUtils.map(detailDTO.getAgreementsConditionList().get(0), AgreementConditionVO.class);
        agreementConditionVO.setIsPatent(detailDTO.getIsPatent());
        agreementConditionVO.setConditionRule(detailDTO.getConditionRule());
        List<AgreementConditionVO.ConditionDetailVO> conditionDetailVOS = PojoUtils.map(detailDTO.getAgreementsConditionList(),
                AgreementConditionVO.ConditionDetailVO.class);
        agreementConditionVO.setConditions(conditionDetailVOS);
        result.setAgreementsCondition(agreementConditionVO);
        //查询相关供应商的商务人员个数
        customerEids = ListUtil.toList(detailDTO.getSecondEid(), detailDTO.getThirdEid());
        //设置商务联系人
        Map<Long, Long> customerContactsCountMap = customerApi.countCustomerContacts(staffInfo.getCurrentEid(), customerEids);
        result.setSecondSalesmanNum(customerContactsCountMap.getOrDefault(detailDTO.getSecondEid(), 0L));
        result.setThirdSalesmanNum(customerContactsCountMap.getOrDefault(detailDTO.getThirdEid(), 0L));
        return Result.success(result);
    }

    @ApiOperation(value = "根据协议id进行停用、删除")
    @PostMapping("/agreementClose")
    public Result<BoolObject> agreementClose(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody AgreementCloseForm form) {
        CloseAgreementRequest request = PojoUtils.map(form, CloseAgreementRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(agreementApi.agreementClose(request)));
    }

    @ApiOperation(value = "pop后台协议添加商品", httpMethod = "POST")
    @PostMapping("/edit")
    public Result<BoolObject> edit(@RequestBody @Valid EditAgreementForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        AgreementDTO agreementDetailsInfo = agreementApi.getAgreementDetailsInfo(form.getId());
        if (agreementDetailsInfo.getEndTime().getTime() < System.currentTimeMillis()) {
            return Result.failed("协议已经结束不能添加商品");
        }

        if (CollectionUtils.isEmpty(form.getAgreementGoodsList())) {
            return Result.failed("没有设置协议商品");
        }

        if (agreementDetailsInfo.getCategory().equals(AgreementCategoryEnum.YEAR_AGREEMENT.getCode())) {
            //判断商品是否在主体里面
            List<Long> goodsIds = goodsApi.getGoodsIdsByEid(agreementDetailsInfo.getEid());
            for (SaveAgreementGoodsForm saveAgreementGoodsForm : form.getAgreementGoodsList()) {
                if (!goodsIds.contains(saveAgreementGoodsForm.getGoodsId())) {
                    return Result.failed("配置商品信息不在主体商品范围内");
                }
            }
        } else {
            //验证商品是否在主协议下
            List<Long> goodsIdList = form.getAgreementGoodsList().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getAgreementGoodsAgreementIdByList(agreementDetailsInfo.getParentId());
            List<Long> agreementGoodsIdList = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            for (Long goodsId : goodsIdList) {
                if (!agreementGoodsIdList.contains(goodsId)) {
                    return Result.failed("补充协议商品不再主协议内");
                }
            }

            //如果新增补充协议是专利和非专利需要
            if (agreementDetailsInfo.getIsPatent().equals(GoodsPatentEnum.UN_PATENT.getCode())
                    || agreementDetailsInfo.getIsPatent().equals(GoodsPatentEnum.PATENT.getCode())) {
                List<Long> goodsIds = agreementGoodsDTOList.stream().filter(e -> e.getIsPatent().equals(agreementDetailsInfo.getIsPatent()))
                        .map(m -> m.getGoodsId()).collect(Collectors.toList());
                for (Long goodsId : goodsIdList) {
                    if (!goodsIds.contains(goodsId)) {
                        return Result.failed("补充协议商品类型不匹配");
                    }
                }
            }
        }

        EditAgreementRequest request = PojoUtils.map(form, EditAgreementRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        //添加商品标准库信息
        List<SaveAgreementGoodsRequest> goodsList = request.getAgreementGoodsList();
        if (CollUtil.isNotEmpty(goodsList)) {
            List<Long> goodsIds = goodsList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList= goodsApi.batchQueryStandardGoodsBasic(goodsIds);
            Map<Long,StandardGoodsBasicDTO> map=standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            goodsList.forEach(e->{
                StandardGoodsBasicDTO standardGoodsBasicDTO=map.get(e.getGoodsId());
                e.setStandardGoodsName(standardGoodsBasicDTO.getStandardGoods().getName());
                e.setStandardLicenseNo(standardGoodsBasicDTO.getStandardGoods().getLicenseNo());
            });
        }

        return Result.success(new BoolObject(agreementApi.update(request)));
    }

    /**
     * 根据企业id查询年度协议状态数量
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据企业id查询年度协议状态数量")
    @PostMapping("/getAgreementStatusCount")
    public Result<AgreementStatusCountVO> getAgreementStatusCount(@RequestBody @Valid GetAgreementStatusCountForm statusCountForm) {
        AgreementStatusCountVO countVo = new AgreementStatusCountVO(0, 0, 0, 0);
        List<AgreementDTO> agreementList = agreementApi.queryAgreementList(ListUtil.toList(statusCountForm.getEid()),
                AgreementModeEnum.getByCode(statusCountForm.getAgreementMode()), AgreementCategoryEnum.getByCode(statusCountForm.getAgreementCategory()));

        if (CollUtil.isEmpty(agreementList)) {
            return Result.success(countVo);
        }
        agreementList.forEach(e -> {

            if (AgreementStatusEnum.CLOSE.getCode().equals(e.getStatus())) {
                countVo.setStop(countVo.getStop() + 1);
            } else {
                Date curentDate = new Date();
                //未开始
                if (curentDate.compareTo(e.getStartTime()) == -1) {
                    countVo.setUnStart(countVo.getUnStart() + 1);
                }
                //进行中
                if (curentDate.compareTo(e.getStartTime()) == 1 && curentDate.compareTo(e.getEndTime()) == -1) {
                    countVo.setStart(countVo.getStart() + 1);
                }
                //已过期
                if (curentDate.compareTo(e.getEndTime()) == 1) {
                    countVo.setExpire(countVo.getExpire() + 1);
                }
            }
        });

        return Result.success(countVo);
    }

    /**
     * 根据年度协议id查询状态数量
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据年度协议id查询议状态数量")
    @GetMapping("/getTempAgreementStatusCount")
    public Result<AgreementStatusCountVO> getTempAgreementStatusCount(@RequestParam Long parentId) {
        AgreementStatusCountVO countVo = new AgreementStatusCountVO(0, 0, 0, 0);
        List<AgreementDTO> agreementList = agreementApi.queryTempAgreement(ListUtil.toList(parentId), AgreementModeEnum.SECOND_AGREEMENTS)
                .getOrDefault(parentId, ListUtil.toList());

        if (CollUtil.isEmpty(agreementList)) {
            return Result.success(countVo);
        }
        agreementList.forEach(e -> {

            if (AgreementStatusEnum.CLOSE.getCode().equals(e.getStatus())) {
                countVo.setStop(countVo.getStop() + 1);
            } else {
                Date curentDate = new Date();
                //未开始
                if (curentDate.compareTo(e.getStartTime()) == -1) {
                    countVo.setUnStart(countVo.getUnStart() + 1);
                }
                //进行中
                if (curentDate.compareTo(e.getStartTime()) == 1 && curentDate.compareTo(e.getEndTime()) == -1) {
                    countVo.setStart(countVo.getStart() + 1);
                }
                //已过期
                if (curentDate.compareTo(e.getEndTime()) == 1) {
                    countVo.setExpire(countVo.getExpire() + 1);
                }
            }
        });

        return Result.success(countVo);
    }

    /**
     * 根据年度协议id查询状态数量
     *
     * @param
     * @return
     */
    @ApiOperation(value = "查询三方协议列表状态数量")
    @PostMapping("/getEntSupplementAgreementCount")
    public Result<AgreementStatusCountVO> getEntSupplementAgreementCount(@Valid @RequestBody QueryThirdAgreementCountForm form) {

        QueryThirdAgreementPageListRequest request = PojoUtils.map(form, QueryThirdAgreementPageListRequest.class);
        List<AgreementPageListItemDTO> agreementList = agreementApi.querySupplementAgreementsByEids(request);

        AgreementStatusCountVO countVo = new AgreementStatusCountVO(0, 0, 0, 0);

        if (CollUtil.isEmpty(agreementList)) {
            return Result.success(countVo);
        }
        agreementList.forEach(e -> {

            if (AgreementStatusEnum.CLOSE.getCode().equals(e.getStatus())) {
                countVo.setStop(countVo.getStop() + 1);
            } else {
                Date curentDate = new Date();
                //未开始
                if (curentDate.compareTo(e.getStartTime()) == -1) {
                    countVo.setUnStart(countVo.getUnStart() + 1);
                }
                //进行中
                if (curentDate.compareTo(e.getStartTime()) == 1 && curentDate.compareTo(e.getEndTime()) == -1) {
                    countVo.setStart(countVo.getStart() + 1);
                }
                //已过期
                if (curentDate.compareTo(e.getEndTime()) == 1) {
                    countVo.setExpire(countVo.getExpire() + 1);
                }
            }
        });

        return Result.success(countVo);
    }

    @ApiOperation(value = "更新协议接口", httpMethod = "POST")
    @PostMapping("/updateAgreement")
    public Result<BoolObject> updateAgreement(@RequestBody @Valid UpdateAgreementForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        //获取协议
        AgreementDTO agreementDetailsInfo = agreementApi.getAgreementDetailsInfo(form.getId());
        //条件校验
        Assert.notNull(agreementDetailsInfo, "协议不存在");

        UpdateAgreementRequest request = PojoUtils.map(form, UpdateAgreementRequest.class);
        request.setCategory(agreementDetailsInfo.getCategory());
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = agreementApi.updateAgreement(request);
        return Result.success(new BoolObject(result));
    }

    //	@ApiOperation(value = "新增协议商品", httpMethod = "POST")
    //	@PostMapping("/addGoods")
    //	public Result<BoolObject> addGoods(@RequestBody @Valid AddAgreementGoodsForm form, @CurrentUser CurrentStaffInfo staffInfo) {
    //		//判断商品是否在主体里面
    //		List<Long> goodsIds = goodsApi.getGoodsIdsByEid(form.getEid());
    //		if (!goodsIds.contains(form.getGoodsId())) {
    //			return Result.failed("配置商品信息不在主体商品范围内");
    //		}
    //		AddAgreementGoodsRequest request = PojoUtils.map(form, AddAgreementGoodsRequest.class);
    //		request.setOpUserId(staffInfo.getCurrentUserId());
    //		Boolean result = agreementGoodsApi.addAgreementGoods(request);
    //		if (result) {
    //			return Result.success(new BoolObject(result));
    //		} else {
    //			return Result.failed("商品新增失败");
    //		}
    //	}

    @ApiOperation(value = "根据补充协议id查询协议快照")
    @GetMapping("/getAgreementAndSnapshotDetail")
    public Result<AgreementsCategoryVO> getAgreementAndSnapshotDetail(@CurrentUser CurrentStaffInfo staffInfo,
                                                                      @RequestParam("agreementId") Long agreementId) {
        AgreementsCategoryVO result;

        // 查询补充协议信息
        AgreementDTO agreementDto = agreementApi.getAgreementDetailsInfo(agreementId);
        if (ObjectUtil.isNull(agreementDto)) {
            return Result.success();
        }
        result = PojoUtils.map(agreementDto, AgreementsCategoryVO.class);

        // 设置年度协议名称
        AgreementDTO yearAgreementDto = agreementApi.getAgreementDetailsInfo(agreementDto.getParentId());
        if (ObjectUtil.isNull(yearAgreementDto)) {
            return Result.failed("主协议不存在");
        }
        result.setParentName(yearAgreementDto.getName());
        result.setParentId(yearAgreementDto.getId());

        // 查询协议快照
        List<AgreementSnapshotDTO> agreementSnapshotList = agreementSnapshotApi.queryAgreementSnapshotByAgreementId(agreementId);
        // 取补充协议快照
        agreementSnapshotList = agreementSnapshotList.stream()
                .filter(o -> ObjectUtil.equal(AgreementCategoryEnum.TEMP_AGREEMENT.getCode(), o.getCategory())).collect(Collectors.toList());
        List<AgreementsSnapshotDetailVO> agreementsSnapshotList = PojoUtils.map(agreementSnapshotList, AgreementsSnapshotDetailVO.class);

        result.setAgreementsSnapshotList(agreementsSnapshotList);
        return Result.success(result);
    }

    @ApiOperation(value = "根据补充协议id和快照id查询历史协议详情")
    @GetMapping("/getSupplementAgreementsDetailBySnapshotId")
    public Result<SupplementAgreementDetailVO> getSupplementAgreementsDetailBySnapshotId(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                         @RequestParam("agreementSnapshotId") Long agreementSnapshotId,
                                                                                         @RequestParam("agreementId") Long agreementId) {
        SupplementAgreementDetailVO result;

        SupplementAgreementDetailDTO detailDTO = agreementSnapshotApi.querySupplementAgreementsDetailById(agreementSnapshotId);
        if (ObjectUtil.isNull(detailDTO)) {
            return Result.success();
        }

        result = PojoUtils.map(detailDTO, SupplementAgreementDetailVO.class);
        return buildSupplementAgreementDetailResult(staffInfo, agreementId, result, detailDTO);
    }

}
