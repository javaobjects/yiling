package com.yiling.open.math;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.open.BaseTest;
import com.yiling.open.util.DataBaseConnection;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author: shuang.zhang
 * @date: 2022/8/11
 */
@Slf4j
public class MathTest extends BaseTest {

    /**
     * rm-8vb2u1kv462s9mo0o.mysql.zhangbei.rds.aliyuncs.com 3306
     * 账号: yiling_read_prd
     * 密码: yiling_read_prd@123
     *
     * @throws Exception
     */
    @Test
    public void Test1() {
        Connection connection = null;
        DataBaseConnection dataBaseConnection = DataBaseConnection.getInstance();
        try {
            connection = dataBaseConnection.openCon("yiling", "yiling_read_prd", "yiling_read_prd@123", "rm-8vb2u1kv462s9mo0o.mysql.zhangbei.rds.aliyuncs.com", "3306");
            InputStream inputStream = new FileInputStream(new File("D://以岭数据文件/aaaa.xls"));
            ImportParams params = new ImportParams();
            params.setNeedVerify(true);
            params.setHeadRows(1);
            params.setStartSheetIndex(0);
            params.setKeyIndex(0);
            List<ErpCustomerExcel> list = new ArrayList<>();
            Connection finalConnection = connection;
            ExcelImportUtil.importExcelBySax(inputStream, ErpCustomerExcel.class, params, new IReadHandler<ErpCustomerExcel>() {
                @Override
                public void handler(ErpCustomerExcel o) {
                    if (StrUtil.isNotEmpty(o.getName())) {
                        if(StrUtil.isNotEmpty(o.getLicenseNumber())) {
                            if (StrUtil.isNotEmpty(o.getType()) && o.getType().equals("批发")) {
                                //查询企业信息
                                EnterpriseDTO enterpriseDTO = dataBaseConnection.executeEnterpriseQuery(finalConnection, "select t.id,t.`name`,t.license_number from enterprise t where t.`name`='" + o.getName() + "'");
                                if (enterpriseDTO != null) {
                                    o.setLicenseNumber(enterpriseDTO.getLicenseNumber());
                                }
                            } else {
                                //查询天眼查信息
                                TycEnterpriseInfoDTO tycEnterpriseInfoDTO = dataBaseConnection.executeTycQuery(finalConnection, "select t.id,t.`name`,t.credit_code from tyc_enterprise_info t where t.`name`='" + o.getName() + "'");
                                if (tycEnterpriseInfoDTO != null) {
                                    o.setLicenseNumber(tycEnterpriseInfoDTO.getCreditCode());
                                }
                            }
                        }
                    }
                    list.add(o);
                }

                @Override
                public void doAfterAll() {
                    System.out.println(list.size());
                    System.out.println(JSON.toJSONString(list.get(0)));
                    System.out.println("全部执行完毕了--------------------------------");
                }
            });

            ExportParams exportParams = new ExportParams();
            exportParams.setSheetName("111");

            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ErpCustomerExcel.class, list);
            FileOutputStream out = new FileOutputStream("D://aaaa.xls");
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
