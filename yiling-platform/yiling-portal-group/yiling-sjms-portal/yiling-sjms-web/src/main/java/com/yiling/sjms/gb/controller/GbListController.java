package com.yiling.sjms.gb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.gb.api.GbAttachmentApi;
import com.yiling.sjms.gb.api.GbBaseInfoApi;
import com.yiling.sjms.gb.api.GbCompanyRelationApi;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.api.GbGoodsInfoApi;
import com.yiling.sjms.gb.api.GbMainInfoApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.BaseInfoDTO;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;
import com.yiling.sjms.gb.dto.GbFormInfoDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.dto.MainInfoDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.form.QueryGBFormListPageForm;
import com.yiling.sjms.gb.vo.FileNameInfoVO;
import com.yiling.sjms.gb.vo.GbBaseInfoVO;
import com.yiling.sjms.gb.vo.GbCompanyInfoVO;
import com.yiling.sjms.gb.vo.GbFormDetailVO;
import com.yiling.sjms.gb.vo.GbFormInfoListVO;
import com.yiling.sjms.gb.vo.GbGoodsInfoVO;
import com.yiling.sjms.gb.vo.GbMainInfoVO;
import com.yiling.sjms.gb.vo.GbProcessDetailVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.workflow.workflow.api.ProcessApi;
import com.yiling.workflow.workflow.dto.ProcessDetailDTO;
import com.yiling.workflow.workflow.dto.request.GetProcessDetailRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

;

/**
 * 团购提报列表
 * @author: wei.wang
 * @date: 2022/11/28
 */
@Slf4j
@RestController
@RequestMapping("/gb/all")
@Api(tags = "团购提报")
public class GbListController extends BaseController {

    @DubboReference
    GbFormApi gbFormApi;
    @DubboReference
    GbBaseInfoApi gbBaseInfoApi;
    @DubboReference
    GbMainInfoApi gbMainInfoApi;
    @DubboReference
    GbGoodsInfoApi gbGoodsInfoApi;
    @DubboReference
    GbAttachmentApi gbAttachmentApi;
    @DubboReference
    ProcessApi processApi;
    @DubboReference
    GbCompanyRelationApi gbCompanyRelationApi;


    @Autowired
    FileService fileService;
    @Autowired
    RedisService redisService;

    @ApiOperation(value = "团购列表")
    @PostMapping("/list")
    public Result<Page<GbFormInfoListVO>> getGBSubmitFormListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBFormListPageForm form){

        QueryGBFormListPageRequest request = PojoUtils.map(form, QueryGBFormListPageRequest.class);
        if(StringUtils.isNotBlank(form.getStartMonthTime())){
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse(form.getStartMonthTime(), "yyyy-MM")));
        }

        if(StringUtils.isNotBlank(form.getEndMonthTime())){
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse(form.getEndMonthTime(), "yyyy-MM")));
        }

        if(request.getStartApproveTime()!= null){
            request.setStartApproveTime(DateUtil.beginOfDay(request.getStartApproveTime()));
        }
        if(request.getEndApproveTime()!= null){
            request.setEndApproveTime(DateUtil.endOfDay(request.getEndApproveTime()));
        }

        if(request.getStartSubmitTime()!= null){
            request.setStartSubmitTime(DateUtil.beginOfDay(request.getStartSubmitTime()));
        }
        if(request.getEndSubmitTime()!= null){
            request.setEndSubmitTime(DateUtil.endOfDay(request.getEndSubmitTime()));
        }

        Page<GbFormInfoListDTO> gbFormListPage = gbFormApi.getGBFormListPage(request);


        return Result.success(PojoUtils.map(gbFormListPage, GbFormInfoListVO.class));
    }
    @ApiOperation(value = "团购明细")
    @GetMapping("/detail")
    public Result<GbFormDetailVO> getGBSubmitFormDetailPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id",required = true)Long id){
        GbFormInfoDTO formDTO = gbFormApi.getOneById(id);
        GbFormDetailVO result = PojoUtils.map(formDTO, GbFormDetailVO.class);
        if(result != null){
            BaseInfoDTO baseInfoOne = gbBaseInfoApi.getOneByGbId(formDTO.getId());
            GbBaseInfoVO baseInfo = PojoUtils.map(baseInfoOne, GbBaseInfoVO.class);
            result.setBaseInfo(baseInfo);

            MainInfoDTO mainInfoOne = gbMainInfoApi.getOneByGbId(formDTO.getId());
            GbMainInfoVO mainInfo = PojoUtils.map(mainInfoOne, GbMainInfoVO.class);
            result.setMainInfo(mainInfo);

            List<GbCompanyInfoVO> companyInfoList = new ArrayList<>();

            List<GoodsInfoDTO> goodsInfoDTOS = gbGoodsInfoApi.listByGbId(formDTO.getId());
            Map<Long, List<GoodsInfoDTO>> goodsMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(goodsInfoDTOS)){
                goodsMap = goodsInfoDTOS.stream().collect(Collectors.groupingBy(GoodsInfoDTO::getCompanyId));
            }
            List<GbCompanyRelationDTO> gbCompanyRelationList = gbCompanyRelationApi.listByFormId(formDTO.getId());
            if(CollectionUtil.isNotEmpty(gbCompanyRelationList)){
                for(GbCompanyRelationDTO companyRelation : gbCompanyRelationList){
                    GbCompanyInfoVO companyOne = PojoUtils.map(companyRelation, GbCompanyInfoVO.class);
                    List<GbGoodsInfoVO> goodsInfoList = PojoUtils.map(goodsMap.get(companyRelation.getId()), GbGoodsInfoVO.class);
                    companyOne.setGbGoodsInfoList(goodsInfoList);
                    companyInfoList.add(companyOne);
                }
            }

            result.setCompanyInfoList(companyInfoList);

            List<AttachmentDTO> attachmentList = gbAttachmentApi.listByGbId(formDTO.getId());

            if(CollectionUtil.isNotEmpty(attachmentList)){
                List<FileNameInfoVO> fileInfoList = new ArrayList<>();
                for(AttachmentDTO one : attachmentList){
                    FileNameInfoVO fileInfoVO = new FileNameInfoVO();
                    fileInfoVO.setFileKey(one.getFileKey());
                    fileInfoVO.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.GB_PROCESS_SUBMIT_SUPPORT_PICTURE));
                    fileInfoVO.setFileName(one.getFileName());
                    fileInfoVO.setFileMd5(one.getFileMd5());
                    fileInfoList.add(fileInfoVO);
                }
                result.setFileInfoList(fileInfoList);
            }
        }
        if(StringUtils.isNotBlank(formDTO.getGbNo()) && FormStatusEnum.getByCode(formDTO.getStatus()) != FormStatusEnum.UNSUBMIT){
            GetProcessDetailRequest request = new GetProcessDetailRequest();
            request.setBusinessKey(formDTO.getGbNo());
            List<ProcessDetailDTO> processDetail = processApi.getProcessDetail(request);
            List<GbProcessDetailVO> gbProcessDetailList = PojoUtils.map(processDetail, GbProcessDetailVO.class);
            result.setProcessDetailList(gbProcessDetailList);
        }
        return Result.success(result);
    }
    @ApiOperation(value = "开启团购/关闭团购")
    @GetMapping("/switch")
    public Result<Boolean> gBSubmitSwitch(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "flag",required = true)Boolean flag){

        redisService.set(RedisKey.generate("sjms", "gb","submit","button"), flag);
        return Result.success();
    }

    @ApiOperation(value = "获取团购开关信息")
    @GetMapping("/get/switch")
    public Result<Boolean> getGBSubmitFormSwitch(@CurrentUser CurrentSjmsUserInfo userInfo){

        Boolean flag = (Boolean)redisService.get(RedisKey.generate("sjms", "gb","submit", "button"));
        if(flag == null){
            flag = false;
        }
        return Result.success(flag);
    }



}
