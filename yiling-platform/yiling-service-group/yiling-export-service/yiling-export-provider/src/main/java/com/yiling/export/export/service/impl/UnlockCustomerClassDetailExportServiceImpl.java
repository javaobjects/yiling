package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockCustomerClassDetailApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.enums.UnlockClassGroundEnum;
import com.yiling.dataflow.wash.enums.UnlockCustomerClassificationEnum;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.FlowSaleExcelModel;
import com.yiling.export.export.model.UnlockCustomerClassDetailExcelModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

/**
 * @author fucheng.bai
 * @date 2023/5/8
 */
@Service("unlockCustomerClassDetailExportService")
public class UnlockCustomerClassDetailExportServiceImpl implements BaseExportQueryDataService<QueryUnlockCustomerClassDetailPageRequest> {

    @DubboReference
    private UnlockCustomerClassDetailApi unlockCustomerClassDetailApi;

    @DubboReference
    private UserApi userApi;

    @Override
    public QueryExportDataDTO queryData(QueryUnlockCustomerClassDetailPageRequest queryUnlockCustomerClassDetailPageRequest) {
        return null;
    }

    @Override
    public QueryUnlockCustomerClassDetailPageRequest getParam(Map<String, Object> map) {
        //  数据权限暂未确定
        return PojoUtils.map(map, QueryUnlockCustomerClassDetailPageRequest.class);
    }

    @Override
    public String getFileSuffix() {
        return "xlsx";
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(QueryUnlockCustomerClassDetailPageRequest request, String fileName) {
        if (request == null) {
            return null;
        }

        String tmpDir = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
        String excelFileName = tmpDir + File.separator + fileName;
        File dirFile = FileUtil.newFile(tmpDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        ExcelWriter excelWriter = EasyExcel.write(excelFileName, UnlockCustomerClassDetailExcelModel.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

        int current = 1;
        int size = 2000;

        try {
            Map<Long, String> userNameMap = new HashMap<>();

            while (true) {
                request.setCurrent(current);
                request.setSize(size);
                Page<UnlockCustomerClassDetailDTO> pageResult = unlockCustomerClassDetailApi.listPage(request);

                List<UnlockCustomerClassDetailExcelModel> list = new ArrayList<>();
                for (UnlockCustomerClassDetailDTO unlockCustomerClassDetailDTO : pageResult.getRecords()) {
                    UnlockCustomerClassDetailExcelModel model = PojoUtils.map(unlockCustomerClassDetailDTO, UnlockCustomerClassDetailExcelModel.class);
                    //  枚举转换
                    if (model.getClassFlag() != null) {
                        switch (model.getClassFlag()) {
                            case 0:
                                model.setClassFlagDesc("未分类");
                                break;
                            case 1:
                                model.setClassFlagDesc("已分类");
                                break;
                            default:
                                model.setClassFlagDesc("");
                        }
                    }

                    if (model.getCustomerClassification() != null) {
                        model.setCustomerClassificationDesc(UnlockCustomerClassificationEnum.getDescByCode(model.getCustomerClassification()));
                    }

                    if (model.getClassGround() != null) {
                        model.setClassGroundDesc(UnlockClassGroundEnum.getDescByCode(model.getClassGround()));
                    }

                    if (DateUtil.format(model.getLastOpTime(), "yyyy-MM-dd HH:mm:ss").equals("1970-01-01 00:00:00")) {
                        model.setLastOpTime(null);
                    }

                    //  获取操作人姓名
                    if (model.getLastOpUser() != null) {
                        if (!userNameMap.containsKey(model.getLastOpUser())) {
                            UserDTO userDTO = userApi.getById(model.getLastOpUser());
                            if (userDTO != null) {
                                userNameMap.put(model.getLastOpUser(), userDTO.getName());
                            }
                        }
                        model.setLastOpUserName(userNameMap.get(model.getLastOpUser()));
                    }

                    list.add(model);
                }

                if (CollUtil.isEmpty(list)) {
                    break;
                }

                // 写文件
                excelWriter.write(list, writeSheet);
                if (list.size() < size) {
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

        try {
            File file = new File(excelFileName);
            if (file.exists()) {
                return  FileUtil.readBytes(file);
            }
        } finally {
            FileUtil.del(tmpDir);
        }
        return null;
    }
}
