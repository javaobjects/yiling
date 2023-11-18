package com.yiling.export.export.service.impl;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetResolveDetailApi;
import com.yiling.dataflow.sale.api.SaleDepartmentTargetApi;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetResolveDetailBO;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.export.export.model.TargetResolveModel;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售指标分解模板生成
 * @author: gxl
 * @date: 2023/5/11
 */
@Slf4j
@Service
public class TargetResolveDetailExportImpl {
    @Autowired
    private FileService fileService;

    @DubboReference
    private SaleDepartmentSubTargetResolveDetailApi saleDepartmentSubTargetResolveDetailApi;

    @DubboReference
    private  SaleDepartmentTargetApi saleDepartmentTargetApi;

    @SneakyThrows
    public void genTemplate(QueryResolveDetailPageRequest request) {
        if (request == null) {
            return ;
        }

        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "saleTargetResolve";
        log.debug("销售指标模板位置tmpDirPath={}",tmpDirPath);
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "分解明细模板");

        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }

        String fileName ="分解明细模板";

        int fileIdx = 0;
        int current = 1;
        int size = 2000;
        outer: do {
            String fileFullName =  tmpExcelDir + File.separator + fileName + "-" + fileIdx + ".xlsx";
            ExcelWriter excelWriter = EasyExcel.write(fileFullName, TargetResolveModel.class).build();
            try {
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);
                    Page<SaleDepartmentSubTargetResolveDetailBO> result = saleDepartmentSubTargetResolveDetailApi.queryPage(request);
                    List<TargetResolveModel> list = Lists.newArrayListWithExpectedSize(result.getRecords().size());
                    for (SaleDepartmentSubTargetResolveDetailBO resolveDetailBO : result.getRecords()) {
                        TargetResolveModel targetResolveModel = PojoUtils.map(resolveDetailBO, TargetResolveModel.class);
                        targetResolveModel.setTargetApr(null).setTargetAug(null).setTargetDec(null).setTargetFeb(null).setTargetJan(null).setTargetJul(null).setTargetJun(null)
                                .setTargetMar(null).setTargetMay(null).setTargetNov(null).setTargetOct(null).setTargetSep(null);
                        // 状态和供应链角色字段处理
                        if(resolveDetailBO.getSupplyChainRole()>0){
                            targetResolveModel.setSupplyChainRoleStr(AgencySupplyChainRoleEnum.getByCode(resolveDetailBO.getSupplyChainRole()).getName());
                        }
                        list.add(targetResolveModel);
                    }
                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (CollUtil.isEmpty(list)) {
                        break outer;
                    }
                    if (list.size() < size) {
                        break outer;
                    }

                    if (current % 500 == 0) {   // 100w数据做文件切割
                        current++;
                        break;
                    }
                    current++;
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            } finally {
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
            fileIdx++;
        } while (true);
        String ossKey = "";
        try {
            File zipFile = ZipUtil.zip(tmpExcelDir);
            if (ObjectUtil.isNotNull(zipFile)) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                String orginalFileName = URLEncoder.encode("分解明细模板.zip" , "UTF-8");
                objectMetadata.setContentDisposition("inline;filename*=utf-8'zh_cn'" + orginalFileName );
                objectMetadata.setHeader("Content-disposition", "filename*=utf-8'zh_cn'" + orginalFileName );
                FileInfo upload = fileService.upload(FileUtil.getInputStream(zipFile),orginalFileName, FileTypeEnum.SALE_TARGET_RESOLVE_FILE,objectMetadata);
                ossKey = upload.getKey();
                log.info("生成销售指标分解模板osskey={}targetid={}deptid={}",request.getSaleTargetId(),request.getDepartId(),upload.getKey());
            }

        } catch (Exception e){
            log.error("上传分解模板出错msg={}",e.getMessage());
        } finally {
            FileUtil.del(tmpDirPath);
        }
        //变更配置状态
        UpdateConfigStatusRequest configReq = new UpdateConfigStatusRequest();
        configReq.setConfigStatus(CrmSaleDepartmentTargetConfigStatusEnum.COMPLETE_SPLIT).setDepartId(request.getDepartId()).setSaleTargetId(request.getSaleTargetId()).setTemplateUrl(ossKey);
        saleDepartmentTargetApi.updateConfigStatus(configReq);
    }
}