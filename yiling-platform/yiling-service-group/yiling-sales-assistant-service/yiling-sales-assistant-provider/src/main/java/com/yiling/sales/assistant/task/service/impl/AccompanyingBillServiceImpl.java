package com.yiling.sales.assistant.task.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.flow.api.FlowSaleMatchApi;
import com.yiling.dataflow.flow.dto.FlowSaleMatchResultDTO;
import com.yiling.dataflow.flow.dto.request.FlowSaleMatchRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.sales.assistant.task.dao.AccompanyingBillMapper;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.request.QueryAccompanyingBillPage;
import com.yiling.sales.assistant.task.dto.request.app.QueryAccompanyingBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.sales.assistant.task.entity.AccompanyingBillDO;
import com.yiling.sales.assistant.task.entity.AccompanyingBillMatchDO;
import com.yiling.sales.assistant.task.entity.MatchDetailDO;
import com.yiling.sales.assistant.task.enums.AccompanyBillAuditStatusEnum;
import com.yiling.sales.assistant.task.enums.AssistantErrorCode;
import com.yiling.sales.assistant.task.enums.FlowMatchResultEnum;
import com.yiling.sales.assistant.task.service.AccompanyingBillMatchService;
import com.yiling.sales.assistant.task.service.AccompanyingBillService;
import com.yiling.sales.assistant.task.service.MatchDetailService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.QueryStaffListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 随货同行单上传 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
@Service
public class AccompanyingBillServiceImpl extends BaseServiceImpl<AccompanyingBillMapper, AccompanyingBillDO> implements AccompanyingBillService {
    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private StaffApi staffApi;

    @DubboReference
    private FlowSaleMatchApi flowSaleMatchApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    private GoodsApi goodsApi;

    @Autowired
    private AccompanyingBillMatchService accompanyingBillMatchService;

    @Autowired
    private MatchDetailService matchDetailService;


    @Autowired
    FileService fileService;

    @Override
    public void save(SaveAccompanyingBillRequest request) {
        AccompanyingBillDO accompanyingBillDO = new AccompanyingBillDO();
        PojoUtils.map(request,accompanyingBillDO);
        if(ObjectUtil.isNull(request.getId())){
            LambdaQueryWrapper<AccompanyingBillDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(AccompanyingBillDO::getDistributorEid,request.getDistributorEid()).eq(AccompanyingBillDO::getDocCode,request.getDocCode());
            AccompanyingBillDO billDO = this.getOne(wrapper);
            if(Objects.nonNull(billDO)){
                throw new BusinessException(AssistantErrorCode.BILL_EXIST);
            }
            accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.TO_AUDIT.getStatus());
            accompanyingBillDO.setUploadTime(new Date());
        }else{
            AccompanyingBillDO oldAccompanyingBillDO = this.getById(request.getId());
            if(oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.ONE_PASS_AUDIT.getStatus()) || oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.TO_AUDIT.getStatus())
                    || oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.TWO_PASS_AUDIT.getStatus()) ){
                throw new BusinessException(AssistantErrorCode.BILL_STATUS_CHANGED);
            }
            accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.TO_AUDIT.getStatus()).setRejectionReason("");
            accompanyingBillDO.setLastUploadTime(new Date());
        }
        this.saveOrUpdate(accompanyingBillDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveV2(SaveAccompanyingBillRequest request) {
        AccompanyingBillDO accompanyingBillDO = new AccompanyingBillDO();
        PojoUtils.map(request,accompanyingBillDO);
        accompanyingBillDO.setAuditTime(new Date());
        DateTime matchDate = DateUtil.offset(DateUtil.beginOfMonth(new Date()), DateField.DAY_OF_MONTH, 14);
        DateTime endMatchDate = DateUtil.offset(DateUtil.beginOfMonth(new Date()), DateField.DAY_OF_MONTH, 27);
        DateTime offset = DateUtil.offset(new Date(), DateField.MONTH, -1);
        DateTime beginOfMonth = DateUtil.beginOfMonth(offset);
        DateTime endOfMonth = DateUtil.endOfMonth(offset);
        AccompanyingBillDO oldAccompanyingBillDO = this.getById(request.getId());
        if(ObjectUtil.isNull(request.getId())){
            LambdaQueryWrapper<AccompanyingBillDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(AccompanyingBillDO::getDistributorEid,request.getDistributorEid()).eq(AccompanyingBillDO::getDocCode,request.getDocCode());
            AccompanyingBillDO billDO = this.getOne(wrapper);
            if(Objects.nonNull(billDO)){
                throw new BusinessException(AssistantErrorCode.BILL_EXIST);
            }
            accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.ONE_PASS_AUDIT.getStatus());
            accompanyingBillDO.setUploadTime(new Date());

        }else{

            if(oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.ONE_PASS_AUDIT.getStatus()) || oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.TO_AUDIT.getStatus())
                    || oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.TWO_PASS_AUDIT.getStatus()) ){
                throw new BusinessException(AssistantErrorCode.BILL_STATUS_CHANGED);
            }
            accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.ONE_PASS_AUDIT.getStatus()).setRejectionReason("");
            accompanyingBillDO.setLastUploadTime(new Date());
        }

        LambdaQueryWrapper<AccompanyingBillMatchDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AccompanyingBillMatchDO::getAccompanyingBillId,request.getId());
        AccompanyingBillMatchDO accompanyingBillMatchDO = accompanyingBillMatchService.getOne(wrapper);
        if(Objects.isNull(accompanyingBillMatchDO)){
            accompanyingBillMatchDO = new AccompanyingBillMatchDO();
            accompanyingBillMatchDO.setCreateUser(request.getAuditUserId());
        }else {
            LambdaQueryWrapper<MatchDetailDO> goodswrapper = Wrappers.lambdaQuery();
            goodswrapper.eq(MatchDetailDO::getAccompanyingBillId,accompanyingBillDO.getId()).select(MatchDetailDO::getId);
            List<Object> matchDetailObjIds = matchDetailService.listObjs(goodswrapper);
            //匹配不一致后再次提交重置数据
            if(CollUtil.isNotEmpty(matchDetailObjIds)){
                List<Long> matchDetailIds = PojoUtils.map(matchDetailObjIds,Long.class);
                matchDetailService.removeByIds(matchDetailIds);
            }
            DateTime dateTime = DateUtil.parseDateTime("1970-01-01 00:00:00");
            accompanyingBillMatchDO.setUpdateUser(request.getAuditUserId()).setResult(FlowMatchResultEnum.TO_MATCH.getCode()).setErpMatchTime(dateTime).setCrmMatchTime(dateTime).setCrmResult(0).setErpResult(0)
            .setErpDeliveryTime(dateTime).setErpRecvName("").setErpDistributorName("").setErpDistributorEid(0L);
            //人工匹配流向DateUtil.now().compareTo()

            if(oldAccompanyingBillDO.getUploadTime().compareTo(beginOfMonth)>=0 && oldAccompanyingBillDO.getUploadTime().compareTo(endOfMonth)<=0 && new Date().compareTo(matchDate)>=0 && new Date().compareTo(endMatchDate)<0){
                if(oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.FAIL_TWO.getStatus()) || oldAccompanyingBillDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.FAIL_THREE.getStatus())){
                    accompanyingBillDO.setDistributorEid(oldAccompanyingBillDO.getDistributorEid()).setDocCode(request.getDocCode());
                    this.matchFlow(accompanyingBillDO,accompanyingBillMatchDO);
                }
            }
        }
        this.saveOrUpdate(accompanyingBillDO);
        accompanyingBillMatchDO.setAccompanyingBillId(accompanyingBillDO.getId()).setUploadTime(accompanyingBillDO.getUploadTime());
        accompanyingBillMatchService.saveOrUpdate(accompanyingBillMatchDO);
        if(ObjectUtil.isNotNull(request.getId()) && accompanyingBillMatchDO.getResult().equals(FlowMatchResultEnum.ERP_CRM_EQ.getCode())){
            MqMessageBO mqMessageTwoBO = new MqMessageBO(Constants.TOPIC_SA_BILL_MATCH, Constants.TAG_SA_BILL_MATCH, accompanyingBillDO.getId().toString());
            mqMessageTwoBO = mqMessageSendApi.prepare(mqMessageTwoBO);
            mqMessageSendApi.send(mqMessageTwoBO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditAccompanyingBill(SaveAccompanyingBillRequest request) {
        AccompanyingBillDO accompanyingBillDO = new AccompanyingBillDO();
        PojoUtils.map(request,accompanyingBillDO);
        accompanyingBillDO.setAuditTime(new Date()).setAuditUserId(request.getOpUserId());
        AccompanyingBillDO billDO = this.getById(request.getId());
        if(!billDO.getAuditStatus().equals(AccompanyBillAuditStatusEnum.TO_AUDIT.getStatus())){
            throw new BusinessException(AssistantErrorCode.BILL_STATUS_CHANGED);
        }
        //人工审核通过存入待匹配
        if(request.getAuditStatus().equals(AccompanyBillAuditStatusEnum.ONE_PASS_AUDIT.getStatus())){
            LambdaQueryWrapper<AccompanyingBillMatchDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(AccompanyingBillMatchDO::getAccompanyingBillId,request.getId());
            AccompanyingBillMatchDO accompanyingBillMatchDO = accompanyingBillMatchService.getOne(wrapper);
            if(Objects.isNull(accompanyingBillMatchDO)){
                accompanyingBillMatchDO = new AccompanyingBillMatchDO();
                accompanyingBillMatchDO.setCreateUser(request.getAuditUserId());
            }else {
                LambdaQueryWrapper<MatchDetailDO> goodswrapper = Wrappers.lambdaQuery();
                goodswrapper.eq(MatchDetailDO::getAccompanyingBillId,accompanyingBillDO.getId()).select(MatchDetailDO::getId);
                List<Object> matchDetailObjIds = matchDetailService.listObjs(goodswrapper);
                List<Long> matchDetailIds = PojoUtils.map(matchDetailObjIds,Long.class);
                matchDetailService.removeByIds(matchDetailIds);
                //人工匹配流向
                if(accompanyingBillMatchDO.getResult().equals(FlowMatchResultEnum.ERP_CRM_NQ.getCode()) || accompanyingBillMatchDO.getResult().equals(FlowMatchResultEnum.NONE.getCode())){
                    this.matchFlow(accompanyingBillDO,accompanyingBillMatchDO);
                }
                accompanyingBillMatchDO.setUpdateUser(request.getAuditUserId());
            }
            accompanyingBillMatchDO.setAccompanyingBillId(request.getId()).setCreateTime(new Date()).setUploadTime(billDO.getUploadTime());
            accompanyingBillMatchService.saveOrUpdate(accompanyingBillMatchDO);
        }
        this.updateById(accompanyingBillDO);
    }

    /**
     * 再次匹配流向
     * @param accompanyingBillDO
     * @param accompanyingBillMatchDO
     */
    private void matchFlow( AccompanyingBillDO accompanyingBillDO,AccompanyingBillMatchDO accompanyingBillMatchDO){
        FlowSaleMatchRequest request = new FlowSaleMatchRequest();
        request.setEid(accompanyingBillDO.getDistributorEid()).setSoNo(accompanyingBillDO.getDocCode());
        FlowSaleMatchResultDTO match = flowSaleMatchApi.match(request);
        accompanyingBillMatchDO.setCrmMatchTime(new Date()).setErpMatchTime(new Date());
        if(match.getErpList().size()==0){
            accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.FAIL_TWO.getStatus()).setRejectionReason("流向不存在，请确认上传随货同行单及单据是否正确");
            accompanyingBillMatchDO.setErpResult(0).setCrmResult(0);
        }
        List<MatchDetailDO> matchDetailDOList = Lists.newArrayList();
        if(match.getErpList().size()>0){
            FlowSaleMatchResultDTO.ErpGoodsInfo erpGoodsInfo = match.getErpList().get(0);
            accompanyingBillMatchDO.setErpResult(1).setErpDistributorEid(erpGoodsInfo.getEid()).setErpDistributorName(erpGoodsInfo.getEname()).setErpRecvName(erpGoodsInfo.getEnterpriseName()).setErpDeliveryTime(erpGoodsInfo.getSoTime());
            if(match.getCrmFlag()){
                accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.TWO_PASS_AUDIT.getStatus());
                accompanyingBillMatchDO.setCrmResult(1);
            }else{
                accompanyingBillDO.setAuditStatus(AccompanyBillAuditStatusEnum.FAIL_THREE.getStatus()).setRejectionReason("流向不存在，请确认上传随货同行单及单据是否正确");;
                accompanyingBillMatchDO.setCrmResult(0);
            }
            List<Long> specIdList = match.getErpList().stream().map(FlowSaleMatchResultDTO.ErpGoodsInfo::getSellSpecId).collect(Collectors.toList());
            List<GoodsDTO> goodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(specIdList, Collections.singletonList(accompanyingBillDO.getDistributorEid()));

            match.getErpList().forEach(goods->{
                MatchDetailDO matchDetailDO = new MatchDetailDO();
                matchDetailDOList.add(matchDetailDO);
                matchDetailDO.setAccompanyingBillId(accompanyingBillDO.getId()).setBatchNo(goods.getSoBatchNo()).setOutDate(goods.getSoTime()).setQuantity(goods.getSoQuantity().longValue()).setSellSpecificationsId(goods.getSellSpecId()).setYlGoodsId(goods.getYlGoodsId())
                        .setYlGoodsName(goods.getYlGoodsName()).setYlGoodsSpecifications(goods.getYlGoodsSpec());
                if(CollUtil.isEmpty(goodsDTOList)){
                    matchDetailDO.setGoodsId(0L);
                }else{
                    GoodsDTO goodsBase = goodsDTOList.stream().filter(goodsDTO -> goodsDTO.getSellSpecificationsId().equals(goods.getSellSpecId())).findFirst().orElse(null);
                    if(Objects.nonNull(goodsBase)){
                        matchDetailDO.setGoodsId(goodsBase.getId());
                    }else{
                        matchDetailDO.setGoodsId(0L);
                    }
                }
            });
        }
        if(accompanyingBillMatchDO.getCrmResult()==1 && accompanyingBillMatchDO.getErpResult()==1){
            accompanyingBillMatchDO.setResult(FlowMatchResultEnum.ERP_CRM_EQ.getCode());
        }
        if(accompanyingBillMatchDO.getCrmResult()==0 && accompanyingBillMatchDO.getErpResult()==1){
            accompanyingBillMatchDO.setResult(FlowMatchResultEnum.ERP_CRM_NQ.getCode());
        }
        if(accompanyingBillMatchDO.getErpResult()==0){
            accompanyingBillMatchDO.setResult(FlowMatchResultEnum.NONE.getCode());
        }
/*        LambdaQueryWrapper<MatchDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MatchDetailDO::getAccompanyingBillId,accompanyingBillDO.getId()).select(MatchDetailDO::getId);
        List<Object> matchDetailObjIds = matchDetailService.listObjs(wrapper);
        List<Long> matchDetailIds = PojoUtils.map(matchDetailObjIds,Long.class);
        matchDetailService.removeByIds(matchDetailIds);*/
        matchDetailService.saveBatch(matchDetailDOList);
    }

    @Override
    public AccompanyingBillDTO getDetailById(Long id) {
        AccompanyingBillDO byId = getById(id);
        AccompanyingBillDTO accompanyingBillDTO = new AccompanyingBillDTO();
        PojoUtils.map(byId,accompanyingBillDTO);
        accompanyingBillDTO.setAccompanyingBillPic(fileService.getUrl(byId.getAccompanyingBillPic(), FileTypeEnum.ACCOMPANYING_BILL_PIC));
        if(byId.getDistributorEid()>0){
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(byId.getDistributorEid());
            accompanyingBillDTO.setDistributorEname(enterpriseDTO.getName());
        }

        return accompanyingBillDTO;
    }

    @Override
    public Page<AccompanyingBillDTO> queryAppPage(QueryAccompanyingBillPageRequest request) {
        LambdaQueryWrapper<AccompanyingBillDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AccompanyingBillDO::getCreateUser,request.getUserId());
        //审核中
        if(request.getAuditStatus().equals(1)){
            wrapper.in(AccompanyingBillDO::getAuditStatus,AccompanyBillAuditStatusEnum.TO_AUDIT.getStatus(),AccompanyBillAuditStatusEnum.ONE_PASS_AUDIT.getStatus());
        }else if(request.getAuditStatus().equals(2)){
            //审核驳回
            wrapper.in(AccompanyingBillDO::getAuditStatus,AccompanyBillAuditStatusEnum.FAIL_ONE.getStatus(),AccompanyBillAuditStatusEnum.FAIL_TWO.getStatus(),AccompanyBillAuditStatusEnum.FAIL_THREE.getStatus());
        }else if(request.getAuditStatus().equals(3)){
            //审核通过
            wrapper.in(AccompanyingBillDO::getAuditStatus,AccompanyBillAuditStatusEnum.TWO_PASS_AUDIT.getStatus());
        }
        wrapper.like(StrUtil.isNotEmpty(request.getDocCode()),AccompanyingBillDO::getDocCode,request.getDocCode());
        wrapper.orderByDesc(AccompanyingBillDO::getId);
        Page<AccompanyingBillDO> billDOPage = this.page(request.getPage(), wrapper);
        if(billDOPage.getTotal()==0){
            return request.getPage();
        }
        Page<AccompanyingBillDTO> accompanyingBillDTOPage = PojoUtils.map(billDOPage,AccompanyingBillDTO.class);
        accompanyingBillDTOPage.getRecords().forEach(accompanyingBillDTO -> {
            accompanyingBillDTO.setAccompanyingBillPic(fileService.getUrl(accompanyingBillDTO.getAccompanyingBillPic(), FileTypeEnum.ACCOMPANYING_BILL_PIC));

        });
        return accompanyingBillDTOPage;
    }

    @Override
    public Page<AccompanyingBillDTO> queryPage(QueryAccompanyingBillPage request) {

        LambdaQueryWrapper<AccompanyingBillDO> wrapper = Wrappers.lambdaQuery();
        //根据提交人查询
        if(StrUtil.isNotEmpty(request.getCreateUserName())){
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameLike(request.getCreateUserName());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            if(CollUtil.isNotEmpty(staffList)){
                wrapper.in(AccompanyingBillDO::getCreateUser,staffList.stream().map(Staff::getId).collect(Collectors.toList()));
            }
        }
        // 按发货单位查询
        if(StrUtil.isNotEmpty(request.getDistributorEname())){
            QueryEnterpriseByNameRequest enterpriseByNameRequest = new QueryEnterpriseByNameRequest();
            enterpriseByNameRequest.setName(request.getDistributorEname());
            List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(enterpriseByNameRequest);
            if(CollUtil.isNotEmpty(enterpriseListByName)){
                wrapper.in(AccompanyingBillDO::getDistributorEid,enterpriseListByName.stream().map(EnterpriseDTO::getId).collect(Collectors.toList()));
            }
        }
        wrapper.ge(Objects.nonNull(request.getStartTime()),AccompanyingBillDO::getUploadTime,request.getStartTime());
        if(Objects.nonNull(request.getEndTime())){
            wrapper.le(AccompanyingBillDO::getUploadTime, DateUtil.endOfDay(request.getEndTime()));
        }
        wrapper.eq(StrUtil.isNotEmpty(request.getDocCode()),AccompanyingBillDO::getDocCode,request.getDocCode());
        if(Objects.nonNull(request.getAuditStatus()) && request.getAuditStatus()<4){
            wrapper.eq(AccompanyingBillDO::getAuditStatus,request.getAuditStatus());
        }
        if(Objects.nonNull(request.getAuditStatus()) && request.getAuditStatus()==4){
            wrapper.in(AccompanyingBillDO::getAuditStatus,AccompanyBillAuditStatusEnum.FAIL_TWO.getStatus(),AccompanyBillAuditStatusEnum.FAIL_THREE.getStatus());
        }
        wrapper.orderByDesc(AccompanyingBillDO::getId);
        Page<AccompanyingBillDO> billDOPage = this.page(request.getPage(), wrapper);
        Page<AccompanyingBillDTO> accompanyingBillDTOPage = PojoUtils.map(billDOPage,AccompanyingBillDTO.class);
        return accompanyingBillDTOPage;
    }
}
