package com.yiling.admin.cms.question.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.goods.standard.api.StandardGoodsTagApi;
import com.yiling.goods.standard.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.question.form.QueryQuestionDelegatePageForm;
import com.yiling.admin.cms.question.form.QueryQuestionPageForm;
import com.yiling.admin.cms.question.form.SaveQuestionForm;
import com.yiling.admin.cms.question.form.SaveQuestionReplyForm;
import com.yiling.admin.cms.question.form.StandardGoodsInfoForm;
import com.yiling.admin.cms.question.vo.DocumentVO;
import com.yiling.admin.cms.question.vo.FileReplyVO;
import com.yiling.admin.cms.question.vo.QuestionDelegateDetailVO;
import com.yiling.admin.cms.question.vo.QuestionDelegatePageVO;
import com.yiling.admin.cms.question.vo.QuestionDetailVO;
import com.yiling.admin.cms.question.vo.QuestionPageListVO;
import com.yiling.admin.cms.question.vo.QuestionReplyDetailInfoVO;
import com.yiling.admin.cms.question.vo.QuestionStandardGoodsInfoVO;
import com.yiling.admin.cms.question.vo.StandardGoodVO;
import com.yiling.admin.cms.question.vo.StandardGoodsSpecificationVO;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.question.api.QuestionApi;
import com.yiling.cms.question.api.QuestionReplyApi;
import com.yiling.cms.question.api.QuestionResourceApi;
import com.yiling.cms.question.dto.QuestionDTO;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionReplyDTO;
import com.yiling.cms.question.dto.QuestionReplyDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionResourceDTO;
import com.yiling.cms.question.dto.ReplyFileDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.SaveQuestionReplyRequest;
import com.yiling.cms.question.dto.request.SaveQuestionRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 疑问处理
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Slf4j
@Api(tags = "疑问处理")
@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {

    @DubboReference
    QuestionApi questionApi;
    @DubboReference
    QuestionResourceApi questionResourceApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    QuestionReplyApi questionReplyApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    DocumentApi documentApi;
    @DubboReference
    StandardGoodsApi standardGoodsApi;
    @Autowired
    FileService fileService;

    @DubboReference
    StandardGoodsTagApi standardGoodsTagApi;

    @ApiOperation(value = "问题知识库列表")
    @PostMapping("/pageList")
    public Result<Page<QuestionPageListVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryQuestionPageForm form) {
        QueryQuestionPageRequest request = PojoUtils.map(form, QueryQuestionPageRequest.class);
        request.setType(1);
        Page<QuestionDTO> questionList = questionApi.listPage(request);
        Page<QuestionPageListVO> result = PojoUtils.map(questionList, QuestionPageListVO.class);
        if (CollectionUtil.isNotEmpty(questionList.getRecords())) {
            List<Long> userList = new ArrayList<>();
            questionList.getRecords().stream().forEach(s -> {
                userList.add(s.getCreateUser());
            });
            List<UserDTO> userDto = userApi.listByIds(userList);
            Map<Long, String> mapUser = userDto.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName, (k1, k2) -> k1));
            result.getRecords().forEach(s -> {
                s.setCreateUserName(mapUser.get(s.getCreateUser()));
            });
        }
        return Result.success(result);
    }

    @ApiOperation(value = "医代问题社区列表")
    @PostMapping("/delegate/list")
    public Result<Page<QuestionDelegatePageVO>> pageDelegateList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryQuestionDelegatePageForm form) {
        QueryQuestionPageRequest request = PojoUtils.map(form, QueryQuestionPageRequest.class);
        request.setType(2);
        Page<QuestionDTO> questionList = questionApi.listPage(request);
        Page<QuestionDelegatePageVO> result = PojoUtils.map(questionList, QuestionDelegatePageVO.class);
        if (CollectionUtil.isNotEmpty(result.getRecords())) {
            List<Long> ids = result.getRecords().stream().map(o -> o.getId()).collect(Collectors.toList());
            List<QuestionResourceDTO> questionResourceList = questionResourceApi.listByQuestionAndType(ids, 4);
            Map<Long, List<QuestionResourceDTO>> map = new HashMap<>();
            if (CollectionUtil.isNotEmpty(questionResourceList)) {
                map = questionResourceList.stream().collect(Collectors.groupingBy(o -> o.getQuestionId()));
            }

            List<QuestionResourceDTO> resourceTypeTwoList = questionResourceApi.listByQuestionAndType(ids, 2);
            Map<Long, List<QuestionResourceDTO>> typeTwoMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(resourceTypeTwoList)) {
                typeTwoMap = resourceTypeTwoList.stream().collect(Collectors.groupingBy(o -> o.getQuestionId()));
            }

            for (QuestionDelegatePageVO one : result.getRecords()) {
                List<QuestionResourceDTO> resourceTypeTwo = typeTwoMap.get(one.getId());
                if (CollectionUtil.isNotEmpty(resourceTypeTwo)) {
                    Long sellSpecificationsId = resourceTypeTwo.get(0).getSellSpecificationsId();

                    StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(sellSpecificationsId);
                    if (standardGoodsSpecification != null) {
                        one.setName(standardGoodsSpecification.getName());
                        one.setSellSpecifications(standardGoodsSpecification.getSellSpecifications());
                    }
                }

                //图片信息
                List<QuestionResourceDTO> resourceList = map.get(one.getId());
                if (CollectionUtil.isNotEmpty(resourceList)) {
                    List<FileInfoVO> fileList = new ArrayList<>();
                    for (QuestionResourceDTO resourceDTO : resourceList) {
                        FileInfoVO infoVO = new FileInfoVO();
                        infoVO.setFileKey(resourceDTO.getResourceKey());
                        infoVO.setFileUrl(fileService.getUrl(resourceDTO.getResourceKey(), FileTypeEnum.QUESTION_RESOURCE_PICTURE));
                        fileList.add(infoVO);
                    }
                    one.setFileList(fileList);
                }

                if (StringUtils.isBlank(one.getToUserName()) && one.getToUserId() == 0) {
                    if (one.getReplyFlag() == 1) {
                        one.setToUserName("无，请运营人员回复");
                    } else {
                        QuestionReplyDTO questionReplyDTO = questionReplyApi.selectLastReply(one.getId());
                        UserDTO user = userApi.getById(questionReplyDTO.getCreateUser());
                        if (user != null) {
                            one.setToUserName(user.getName());
                        }
                    }
                    one.setShowReplyButtonFlag(1);
                }
            }
        }
        return Result.success(result);
    }

    @ApiOperation(value = "问题知识库详情")
    @GetMapping("/detail")
    public Result<QuestionDetailVO> detail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "questionId") Long questionId) {
        QuestionDetailInfoDTO questionDetail = questionApi.getQuestionDetail(questionId);
        QuestionDetailVO result = PojoUtils.map(questionDetail, QuestionDetailVO.class);
        if (questionDetail != null) {

            // List<Long> specificationsIds = questionDetail.getStandardGoodsList().stream().map(o -> o.getSellSpecificationsId()).collect(Collectors.toList());
            // List<StandardGoodsSpecificationDTO> listStandardGoodsSpecification = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(specificationsIds);

            List<Long> standardIds = questionDetail.getStandardGoodsList().stream().map(o -> o.getStandardId()).collect(Collectors.toList());
            List<StandardGoodsDTO> standardGoodsByIds = standardGoodsApi.getStandardGoodsByIds(standardIds);


            if (CollectionUtil.isNotEmpty(standardGoodsByIds)) {
                List<QuestionStandardGoodsInfoVO> standardGoodsList = new ArrayList<>();
                standardGoodsByIds.stream().forEach(s -> {
                    QuestionStandardGoodsInfoVO standardGoods = new QuestionStandardGoodsInfoVO();
                    standardGoods.setName(s.getName());
                    standardGoods.setStandardId(s.getId());
                    standardGoodsList.add(standardGoods);
                });
                result.setStandardGoodsList(standardGoodsList);
            }

            // if (CollectionUtil.isNotEmpty(listStandardGoodsSpecification)) {
            //     List<QuestionStandardGoodsInfoVO> standardGoodsList = new ArrayList<>();
            //     listStandardGoodsSpecification.stream().forEach(s -> {
            //         QuestionStandardGoodsInfoVO standardGoods = new QuestionStandardGoodsInfoVO();
            //         standardGoods.setName(s.getName());
            //         standardGoods.setSellSpecifications(s.getSellSpecifications());
            //         standardGoods.setSellSpecificationsId(s.getId());
            //         standardGoods.setStandardId(s.getStandardId());
            //         standardGoodsList.add(standardGoods);
            //     });
            //     result.setStandardGoodsList(standardGoodsList);
            // }

            if (CollectionUtil.isNotEmpty(questionDetail.getDocumentIdList())) {
                List<DocumentDTO> documentDTOS = documentApi.getDocumentByIds(questionDetail.getDocumentIdList());
                if (CollectionUtil.isNotEmpty(documentDTOS)) {
                    List<DocumentVO> documentList = new ArrayList<>();

                    documentDTOS.stream().forEach(s -> {
                        DocumentVO documentOne = new DocumentVO();
                        documentOne.setDocumentId(s.getId());
                        documentOne.setDocumentTitle(s.getTitle());
                        documentList.add(documentOne);
                    });
                    result.setDocumentList(documentList);
                }
            }
        }
        return Result.success(result);
    }


    @ApiOperation(value = "医药代表社区详情")
    @GetMapping("/delegate/detail")
    public Result<QuestionDelegateDetailVO> delegateDetail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "questionId") Long questionId) {
        QuestionDetailInfoDTO questionDetail = questionApi.getQuestionDetail(questionId);

        QuestionDelegateDetailVO result = PojoUtils.map(questionDetail, QuestionDelegateDetailVO.class);

        if (result != null) {
            if (StringUtils.isBlank(questionDetail.getToUserName()) && questionDetail.getToUserId() == 0) {
                result.setShowReplyButtonFlag(1);
            }

            Long specificationId = questionDetail.getStandardGoodsList().stream().map(o -> o.getSellSpecificationsId()).collect(Collectors.toList()).get(0);
            StandardGoodsSpecificationDTO specification = standardGoodsSpecificationApi.getStandardGoodsSpecification(specificationId);
            // Long standardId = questionDetail.getStandardGoodsList().stream().map(o -> o.getStandardId()).collect(Collectors.toList()).get(0);
            // StandardGoodsAllInfoDTO standardGoodsById = standardGoodsApi.getStandardGoodsById(standardId);

            // if (standardGoodsById != null) {
            //     result.setName(standardGoodsById.getBaseInfo().getName());
            //     result.setStandardId(standardGoodsById.getId());
            // }
            if (specification != null) {
                result.setName(specification.getName());
                result.setSellSpecifications(specification.getSellSpecifications());
                result.setStandardId(specification.getStandardId());
                result.setSellSpecificationsId(specificationId);
            }

            List<String> keyList = questionDetail.getKeyList();
            if (CollectionUtil.isNotEmpty(keyList)) {
                List<FileInfoVO> pictureList = new ArrayList<>();
                for (String one : keyList) {
                    FileInfoVO file = new FileInfoVO();
                    file.setFileKey(one);
                    file.setFileUrl(fileService.getUrl(one, FileTypeEnum.QUESTION_RESOURCE_PICTURE));
                    pictureList.add(file);
                }
                result.setPictureList(pictureList);
            }

            List<QuestionReplyDetailInfoDTO> replyDetailList = questionDetail.getReplyDetailList();
            if (CollectionUtil.isNotEmpty(replyDetailList)) {
                List<QuestionReplyDetailInfoVO> questionReplyList = PojoUtils.map(replyDetailList, QuestionReplyDetailInfoVO.class);
                Map<Long, QuestionReplyDetailInfoVO> replyMap = questionReplyList.stream().collect(Collectors.toMap(QuestionReplyDetailInfoVO::getId, o -> o, (k1, k2) -> k1));
                List<QuestionReplyDetailInfoVO> replyList = new ArrayList<>();
                for (QuestionReplyDetailInfoDTO replyOne : replyDetailList) {
                    QuestionReplyDetailInfoVO questionReplyDetailOne = replyMap.get(replyOne.getId());
                    UserDTO user = userApi.getById(replyOne.getCreateUser());
                    if (user != null) {
                        questionReplyDetailOne.setCreateUserName(user.getName());
                    }

                    if (CollectionUtil.isNotEmpty(replyOne.getReplyDocumentIdList())) {
                        List<DocumentDTO> documentDTOS = documentApi.getDocumentByIds(replyOne.getReplyDocumentIdList());
                        if (CollectionUtil.isNotEmpty(documentDTOS)) {
                            List<DocumentVO> replyDocumentList = new ArrayList<>();
                            documentDTOS.stream().forEach(s -> {
                                DocumentVO documentOne = new DocumentVO();
                                documentOne.setDocumentId(s.getId());
                                documentOne.setDocumentTitle(s.getTitle());
                                replyDocumentList.add(documentOne);
                            });
                            questionReplyDetailOne.setReplyDocumentList(replyDocumentList);
                        }
                    }

                    if (CollectionUtil.isNotEmpty(replyOne.getReplyFileKeyList())) {
                        List<FileReplyVO> replyFileList = new ArrayList<>();
                        for (ReplyFileDTO one : replyOne.getReplyFileKeyList()) {
                            FileReplyVO file = new FileReplyVO();
                            file.setFileKey(one.getReplyFileKey());
                            file.setFileUrl(fileService.getUrl(one.getReplyFileKey(), FileTypeEnum.QUESTION_REPLY_RESOURCE_PICTURE));
                            file.setFileName(one.getFileName());
                            replyFileList.add(file);
                        }
                        questionReplyDetailOne.setReplyFileList(replyFileList);
                    }

                    if (CollectionUtil.isNotEmpty(replyOne.getReplyPictureKeyList())) {
                        List<FileInfoVO> replyPictureList = new ArrayList<>();
                        for (String one : replyOne.getReplyPictureKeyList()) {
                            FileInfoVO file = new FileInfoVO();
                            file.setFileKey(one);
                            file.setFileUrl(fileService.getUrl(one, FileTypeEnum.QUESTION_REPLY_RESOURCE_PICTURE));
                            replyPictureList.add(file);
                        }
                        questionReplyDetailOne.setReplyPictureList(replyPictureList);
                    }
                    replyList.add(questionReplyDetailOne);
                }
                result.setReplyList(replyList);
            }
        }
        return Result.success(result);
    }


    @ApiOperation(value = "问题知识库编辑添加")
    @PostMapping("/update")
    public Result<Boolean> saveOrUpdateQuestion(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveQuestionForm form) {

        SaveQuestionRequest request = PojoUtils.map(form, SaveQuestionRequest.class);
        request.setType(1);
        request.setReplyFlag(3);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = questionApi.saveOrUpdateQuestion(request);
        return Result.success(result);
    }

    @ApiOperation(value = "医药代表社区保存回复")
    @PostMapping("/reply/add")
    public Result<Boolean> saveQuestionReply(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveQuestionReplyForm form) {
        SaveQuestionReplyRequest request = PojoUtils.map(form, SaveQuestionReplyRequest.class);
        log.info("登录人信息：{}", JSON.toJSONString(staffInfo));
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = questionReplyApi.saveQuestionReply(request);
        return Result.success(result);
    }

    @ApiOperation(value = "删除问题知识库")
    @GetMapping("/delete")
    public Result<Boolean> deleteQuestion(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "questionId") Long questionId) {
        Boolean result = questionApi.deleteQuestion(questionId, staffInfo.getCurrentUserId());
        return Result.success(result);
    }


    @ApiOperation(value = "获取以岭商品信息")
    @PostMapping("/get/standardGoods")
    public Result<Page<StandardGoodVO>> getStandardGoods(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid StandardGoodsInfoForm form) {
        StandardGoodsInfoRequest request = PojoUtils.map(form, StandardGoodsInfoRequest.class);
        StandardGoodsTagDTO tagByTagsName = standardGoodsTagApi.getTagByTagsName("c-以岭品");
        request.setTagIds(Collections.singletonList(tagByTagsName.getId()));
        Page<StandardGoodsInfoDTO> standardGoodsPage = standardGoodsApi.getStandardGoodsInfo(request);
        Page<StandardGoodVO> result = PojoUtils.map(standardGoodsPage, StandardGoodVO.class);
        if (CollectionUtil.isNotEmpty(result.getRecords())) {
            result.getRecords().forEach(s -> {
                s.setPic(getGoodsPicUrl(s.getPic()));
            });
        }
        return Result.success(result);
    }

    @ApiOperation(value = "获取以岭商品规格信息")
    @GetMapping("/get/specification")
    public Result<List<StandardGoodsSpecificationVO>> getSpecification(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "standardGoodId") Long standardGoodId) {
        List<StandardGoodsSpecificationDTO> standardGoodsSpecificationList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(new ArrayList<Long>() {{
            add(standardGoodId);
        }});

        List<StandardGoodsSpecificationVO> result = PojoUtils.map(standardGoodsSpecificationList, StandardGoodsSpecificationVO.class);
        return Result.success(result);
    }

    private String getGoodsPicUrl(String pic) {
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
        }
    }

}
