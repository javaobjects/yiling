package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetDetailApi;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetDetailBO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportSaleTargetDetailModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;

/**
 * 销售指标配置详情导出
 * @author: gxl
 * @date: 2023/4/18
 */
@Service("saleTargetDetailExportService")
public class SaleTargetDetailExportServiceImpl implements BaseExportQueryDataService<QuerySaleDeptSubTargetDetailRequest> {


    @DubboReference
    SaleDepartmentSubTargetDetailApi saleDepartmentSubTargetDetailApi;

    @Override
    public QueryExportDataDTO queryData(QuerySaleDeptSubTargetDetailRequest request) {
        return null;
    }

/*    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        if (!(queryParams instanceof QuerySaleDeptSubTargetDetailRequest)) {
            return Collections.emptyList();
        }
        QuerySaleDeptSubTargetDetailRequest request = (QuerySaleDeptSubTargetDetailRequest) queryParams;
        request.setCurrent(page);
        request.setSize(2000);
        Page<SaleDepartmentSubTargetDetailBO> saleDepartmentSubTargetDetailBOPage = saleDepartmentSubTargetDetailApi.queryPage(request);
        if (saleDepartmentSubTargetDetailBOPage == null || CollectionUtil.isEmpty(saleDepartmentSubTargetDetailBOPage.getRecords())) {
            return Collections.emptyList();
        }
       List result = saleDepartmentSubTargetDetailBOPage.getRecords().stream().map(s->{
            ExportSaleTargetDetailBO exportSaleTargetDetailBO = PojoUtils.map(s,ExportSaleTargetDetailBO.class);
            return exportSaleTargetDetailBO;
        }).collect(Collectors.toList());
        return result;
    }*/

    @Override
    @SneakyThrows
    public byte[] getExportByte(QuerySaleDeptSubTargetDetailRequest request, String fileName) {
        if (request == null) {
            return null;
        }

        fileName = fileName.substring(0, fileName.lastIndexOf(".zip"));

        String tmpDir = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
        String tmpExcelDir = tmpDir + File.separator + fileName;
        File tmpExcelFileDir = new File(tmpExcelDir);
        if (!tmpExcelFileDir.exists()) {
            tmpExcelFileDir.mkdirs();
        }

        int fileIdx = 0;
        int current = 1;
        int size = 2000;
        outer: do {
            String fileFullName = tmpExcelDir + File.separator + fileName + "-" + fileIdx + ".xlsx";
            ExcelWriter excelWriter = EasyExcel.write(fileFullName, ExportSaleTargetDetailModel.class).build();
            try {
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);
                    Page<SaleDepartmentSubTargetDetailBO> saleDepartmentSubTargetDetailBOPage = saleDepartmentSubTargetDetailApi.queryPage(request);
                    List<ExportSaleTargetDetailModel> list = PojoUtils.map(saleDepartmentSubTargetDetailBOPage.getRecords(), ExportSaleTargetDetailModel.class);
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
            } finally {
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
            fileIdx++;
        } while (true);

        //  压缩文件
        try {
            File zipFile = ZipUtil.zip(tmpExcelDir);
            if (null != zipFile) {
                return FileUtil.readBytes(zipFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(tmpDir);
        }

        return null;
    }

    @Override
    public QuerySaleDeptSubTargetDetailRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map,QuerySaleDeptSubTargetDetailRequest.class);
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public String getFileSuffix() {
        return "zip";
    }
}