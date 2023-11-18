package com.yiling.framework.oss.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.bo.OssCallbackResult;
import com.yiling.framework.oss.bo.OssPolicyResult;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.form.GetPolicyForm;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.oss.vo.OssPolicyResultVO;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@RequestScope
@RestController
@RequestMapping("/file")
@Api(tags = "文件管理")
@Slf4j
public class FileController extends BaseController {

    @Value("${aliyun.oss.previewUrl:}")
    private String previewUrl;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传文件", required = true),
            @ApiImplicitParam(name = "type", value = "文件类型：" +
                    "商品图片（goodsPicture）" +
                    "，企业资质图片（enterpriseCertificate）" +
                    "，banner图片（bannerPicture）" +
                    "，订单收货回执单（orderReceiveOneReceipt）" +
                    "，订单购销合同附件（orderContract）" +
                    "，富文本编辑器中的图片及附件（richTextEditorFile）" +
                    "，移动端安装包文件（appInstallPackage）" +
                    "，会员权益图标（memberEquityPicture）" +
                    "，赠品库图片（marketingGoodsGiftPicture）" +
                    "，身份证正面照（idCardFrontPhoto）" +
                    "，身份证反面照（idCardBackPhoto）" +
                    "，组合包活动图片（combinationPackagePicture）" +
                    "，专场活动图片（specialActivityPicture）" +
                    "，店铺Logo（shopLogoPicture）" +
                    "，会员购买推广分享二维码（taskMemberShare）" +
                    "，协议附件（agreementAttachment）" +
                    "，内容封面（contentCover）" +
                    "，药加险广告图片（advertisementPic）" +
                    "，处方图片（prescriptionPic）" +
                    "，会议活动封面图（meetingBackgroundPic）" +
                    ",疑问知识库图片视频(questionResourcePicture)"+
                    ",疑问知识库回复图片视频(questionReplyResourcePicture)"+
                    "，手写签名（handSignaturePicture）" +
                    "，订单票据（orderReceipts）" +
                    "，活动码（activityDoctorQrcode）" +
                    "，抽奖活动海报（lotteryActivityFile）" +
                    "，会员背景图（memberBackgroundPicture）" +
                    "，会员点亮熄灭图（memberLightPicture）" +
                    "，团购提报流程证据文件（gbProcessSubmitSupportPicture）" +
                    "，随货同行单（accompanyingBillPic）" +
                    "", required = true)
    })
    @PostMapping("/upload")
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws Exception {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }
        FileInfo fileInfo = fileService.upload(file, fileType);
        return Result.success(fileInfo);
    }

    @ApiOperation("获取oss上传签名生成")
    @GetMapping("/policy/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "文件类型（由服务端开发人员提供）", required = true)
    })
    public Result<OssPolicyResultVO> policy(@PathVariable("type") String type, HttpServletRequest request) {
        return this.policy(type, new GetPolicyForm(), request);
    }

    @ApiOperation("获取oss上传签名生成（支持自定义回调地址，便于业务方判断文件是否重复等逻辑）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "文件类型（由服务端开发人员提供）", required = true)
    })
    @PostMapping("/policy/{type}")
    public Result<OssPolicyResultVO> policy(@PathVariable("type") String type, @RequestBody GetPolicyForm form, HttpServletRequest request) {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }

        OssPolicyResult ossPolicy = null;
        if (StrUtil.isBlank(form.getCallbackUrl())) {
            ossPolicy = fileService.policy(fileType);
        } else {
            ossPolicy = fileService.policy(fileType, form.getCallbackUrl());
        }
        OssPolicyResultVO resultVO = PojoUtils.map(ossPolicy, OssPolicyResultVO.class);
        resultVO.setDomain("https://" + URLUtil.getHost(URLUtil.url(request.getRequestURL().toString())).getHost());
        return Result.success(resultVO);
    }

    @ApiOperation(value = "oss上传成功回调")
    @PostMapping("/callback")
    public Result<OssCallbackResult> callback(HttpServletRequest request) {
        log.debug("ossCallbackResult -> {}", JSONUtil.toJsonStr(request.getParameterMap()));
        OssCallbackResult ossCallbackResult = fileService.callback(request);
        return Result.success(ossCallbackResult);
    }

    @ApiOperation(value = "获取文件预览链接")
    @PostMapping("/getPreviewUrl")
    public Result<String> getPreviewUrl(@ApiParam(value = "文件链接", required = true) @RequestParam String fileUrl) {
        String filePreviewUrl = StrUtil.format(previewUrl, URLEncoder.createAll().encode(Base64.encode(fileUrl, StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        return Result.success(filePreviewUrl);
    }

    @Deprecated
    @ApiOperation(value = "【已废弃】跳转到文件URL")
    @GetMapping("/redirect")
    public Result redirect(@RequestParam("type") String type, @RequestParam("key") String key, HttpServletResponse response) throws IOException {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }

        String url = fileService.getUrl(key, fileType);
        response.sendRedirect(url);
        return Result.success();
    }

    @Deprecated
    @ApiOperation(value = "【已废弃】获取上传文件URL")
    @GetMapping("/getUrl")
    public Result getUrl(@RequestParam("type") String type, @RequestParam("key") String key) throws IOException {
        FileTypeEnum fileType = FileTypeEnum.getByType(type);
        if (fileType == null) {
            return Result.validateFailed("type", "文件类型未定义");
        }

        String url = fileService.getUrl(key, fileType);
        return Result.success(url);
    }

}
