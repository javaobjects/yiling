package com.yiling.open.cms.question.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.goods.standard.api.StandardGoodsTagApi;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.question.api.QuestionApi;
import com.yiling.cms.question.api.QuestionResourceApi;
import com.yiling.cms.question.dto.QuestionDTO;
import com.yiling.cms.question.dto.QuestionDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionReplyDetailInfoDTO;
import com.yiling.cms.question.dto.QuestionResourceDTO;
import com.yiling.cms.question.dto.ReplyFileDTO;
import com.yiling.cms.question.dto.request.QueryQuestionPageRequest;
import com.yiling.cms.question.dto.request.SaveQuestionRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.open.cms.question.form.QueryQuestionDelegatePageForm;
import com.yiling.open.cms.question.form.QueryQuestionPageForm;
import com.yiling.open.cms.question.form.QuestionStandardGoodsInfoForm;
import com.yiling.open.cms.question.form.SaveQuestionForm;
import com.yiling.open.cms.question.form.StandardGoodsInfoForm;
import com.yiling.open.cms.question.vo.DocumentVO;
import com.yiling.open.cms.question.vo.FileReplyVO;
import com.yiling.open.cms.question.vo.QuestionDelegateDetailVO;
import com.yiling.open.cms.question.vo.QuestionDetailVO;
import com.yiling.open.cms.question.vo.QuestionPageListVO;
import com.yiling.open.cms.question.vo.QuestionReplyDetailInfoVO;
import com.yiling.open.cms.question.vo.QuestionStandardGoodsInfoVO;
import com.yiling.open.cms.question.vo.StandardGoodVO;
import com.yiling.open.cms.question.vo.StandardGoodsSpecificationVO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.MrApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * <p>
 * 疑问处理
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Api(tags = "疑问处理")
@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {

    @DubboReference
    QuestionApi questionApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    StandardGoodsApi standardGoodsApi;
    @DubboReference
    DocumentApi documentApi;
    @DubboReference
    QuestionResourceApi questionResourceApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    MrApi mrApi;
    @DubboReference
    PopGoodsApi popGoodsApi;
    @DubboReference
    UserApi userApi;
    @Autowired
    FileService fileService;

    @DubboReference
    StandardGoodsTagApi standardGoodsTagApi;

    @ApiOperation(value = "用药助手列表")
    @PostMapping("/pageList")
    public Result<Page<QuestionPageListVO>> pageList(@RequestBody @Valid QueryQuestionPageForm form) {
        QueryQuestionPageRequest request = PojoUtils.map(form, QueryQuestionPageRequest.class);
        request.setType(1);
        Page<QuestionDTO> questionList = questionApi.listPage(request);
        Page<QuestionPageListVO> result = PojoUtils.map(questionList, QuestionPageListVO.class);
        if (CollectionUtil.isNotEmpty(result.getRecords())) {
            StandardGoodsDTO standardGoods = standardGoodsApi.getOneById(form.getStandardId());
            if (standardGoods != null) {
                result.getRecords().stream().forEach(s -> {
                    s.setName(standardGoods.getName());
                });
            }
        }
        return Result.success(result);
    }

    @ApiOperation(value = "我的提问列表")
    @PostMapping("/delegate/pageList")
    public Result<Page<QuestionPageListVO>> pageList(@RequestBody @Valid QueryQuestionDelegatePageForm form) {
        QueryQuestionPageRequest request = PojoUtils.map(form, QueryQuestionPageRequest.class);
        request.setType(2);
        Page<QuestionDTO> questionList = questionApi.listPage(request);
        Page<QuestionPageListVO> result = PojoUtils.map(questionList, QuestionPageListVO.class);
        if (CollectionUtil.isNotEmpty(result.getRecords())) {
            List<Long> ids = result.getRecords().stream().map(o -> o.getId()).collect(Collectors.toList());
            List<QuestionResourceDTO> questionResourceList = questionResourceApi.listByQuestionAndType(ids, 2);
            if (CollectionUtil.isNotEmpty(questionResourceList)) {
                Map<Long, Long> specificationMap = questionResourceList.stream().collect(Collectors.toMap(QuestionResourceDTO::getQuestionId, QuestionResourceDTO::getSellSpecificationsId, (K1, K2) -> K1));
                result.getRecords().stream().forEach(s -> {
                    // todo
                    Long sellSpecificationsId = specificationMap.getOrDefault(s.getId(), 0L);
                    StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(sellSpecificationsId);
                    s.setName(standardGoodsSpecification != null ? standardGoodsSpecification.getName() : "");
                    s.setSellSpecifications(standardGoodsSpecification != null ? standardGoodsSpecification.getSellSpecifications() : "");
                });
            }
        }
        return Result.success(result);
    }

    @ApiOperation(value = "用药助手详情")
    @GetMapping("/detail")
    public Result<QuestionDetailVO> detail(@RequestParam(value = "questionId") Long questionId) {
        QuestionDetailInfoDTO questionDetail = questionApi.getQuestionDetail(questionId);
        QuestionDetailVO result = PojoUtils.map(questionDetail, QuestionDetailVO.class);
        if (questionDetail != null) {
            // List<Long> specificationsIds = questionDetail.getStandardGoodsList().stream().map(o -> o.getSellSpecificationsId()).collect(Collectors.toList());
            // List<StandardGoodsSpecificationDTO> listStandardGoodsSpecification = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(specificationsIds);

            // 根据standardId查询品的信息
            List<Long> standardIds = questionDetail.getStandardGoodsList().stream().map(o -> o.getStandardId()).collect(Collectors.toList());
            List<StandardGoodsDTO> standardGoodsList = standardGoodsApi.getStandardGoodsByIds(standardIds);

            if (CollectionUtil.isNotEmpty(standardGoodsList)) {
                List<QuestionStandardGoodsInfoVO> standardGoodsVOList = new ArrayList<>();
                standardGoodsList.stream().forEach(s -> {
                    QuestionStandardGoodsInfoVO standardGoods = new QuestionStandardGoodsInfoVO();
                    standardGoods.setName(s.getName());
                    standardGoodsVOList.add(standardGoods);
                });
                result.setStandardGoodsList(standardGoodsVOList);
            }

            // if (CollectionUtil.isNotEmpty(listStandardGoodsSpecification)) {
            //     List<QuestionStandardGoodsInfoVO> standardGoodsList = new ArrayList<>();
            //     listStandardGoodsSpecification.stream().forEach(s -> {
            //         QuestionStandardGoodsInfoVO standardGoods = new QuestionStandardGoodsInfoVO();
            //         standardGoods.setName(s.getName());
            //         standardGoods.setSellSpecifications(s.getSellSpecifications());
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


    @ApiOperation(value = "我的提问详情")
    @GetMapping("/delegate/detail")
    public Result<QuestionDelegateDetailVO> delegateDetail(@RequestParam(value = "questionId") Long questionId) {
        QuestionDetailInfoDTO questionDetail = questionApi.getQuestionDetail(questionId);
        QuestionDelegateDetailVO result = PojoUtils.map(questionDetail, QuestionDelegateDetailVO.class);
        if (result != null) {
            Long specificationId = questionDetail.getStandardGoodsList().stream().map(o -> o.getSellSpecificationsId()).collect(Collectors.toList()).get(0);
            StandardGoodsSpecificationDTO specification = standardGoodsSpecificationApi.getStandardGoodsSpecification(specificationId);
            if (specification != null) {
                result.setName(specification.getName());
                result.setSellSpecifications(specification.getSellSpecifications());

            }

            if (questionDetail.getToUserId() != null && questionDetail.getToUserId() != 0) {
                MrBO mrBO = mrApi.getById(questionDetail.getToUserId());
                result.setMobile(mrBO != null ? mrBO.getMobile() : "");
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

    @ApiOperation(value = "保存提问信息")
    @PostMapping("/add")
    public Result<Boolean> saveOrUpdateQuestion(@RequestBody @Valid SaveQuestionForm form) {
        SaveQuestionRequest request = PojoUtils.map(form, SaveQuestionRequest.class);
        request.setType(2);
        request.setReplyFlag(1);
        request.setOpUserId(form.getFromUserId());
        QuestionStandardGoodsInfoForm questionStandardGoodsInfoForm = form.getStandardInfoList().get(0);

        List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);
        List<GoodsListItemBO> goodsList = popGoodsApi.findGoodsBySpecificationIdAndEids(questionStandardGoodsInfoForm.getSellSpecificationsId(), subEidLists);
        if (CollectionUtil.isNotEmpty(goodsList)) {
            List<Long> goodsIdList = goodsList.stream().map(o -> o.getId()).collect(Collectors.toList());
            List<Long> longs = mrApi.listEmoloyeeIdsByGoodsIds(goodsIdList);
            if (CollectionUtil.isNotEmpty(longs) && CollectionUtil.isNotEmpty(form.getUserIdList())) {
                for (Long id : longs) {
                    if (form.getUserIdList().contains(id)) {
                        request.setToUserId(id);
                        break;
                    }
                }
            }
        }
        if (request.getToUserId() != null && request.getToUserId() != 0) {
            MrBO mrBO = mrApi.getById(request.getToUserId());
            if (mrBO == null) {
                request.setToUserId(0L);
            } else {
                request.setToUserName(mrBO.getName());
                request.setToUserId(mrBO.getUserId());
            }


        }
        Boolean result = questionApi.saveOrUpdateQuestion(request);
        return Result.success(result);
    }

    @ApiOperation(value = "获取以岭商品信息")
    @PostMapping("/get/standardGoods")
    public Result<Page<StandardGoodVO>> getStandardGoods(@RequestBody @Valid StandardGoodsInfoForm form) {
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
    public Result<List<StandardGoodsSpecificationVO>> getSpecification(@RequestParam(value = "standardGoodId") Long standardGoodId) {
        List<StandardGoodsSpecificationDTO> standardGoodsSpecificationList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(new ArrayList<Long>() {{
            add(standardGoodId);
        }});

        List<StandardGoodsSpecificationVO> result = PojoUtils.map(standardGoodsSpecificationList, StandardGoodsSpecificationVO.class);
        return Result.success(result);
    }

    private String getGoodsPicUrl(String pic) {
        if (StringUtils.isNotBlank(pic)) {
            return fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE);
        } else {
            return fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
        }
    }
}
