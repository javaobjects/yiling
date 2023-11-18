package com.yiling.dataflow.backup.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.dto.request.CheckAgencyBackupRequest;
import com.yiling.dataflow.backup.service.AgencyBackupService;
import com.yiling.dataflow.backup.util.DataBaseConnection;
import com.yiling.dataflow.config.DatasourceConfig;
import com.yiling.dataflow.crm.dao.CrmEnterpriseMapper;
import com.yiling.dataflow.crm.entity.CrmAgencyBackupRecordDO;
import com.yiling.dataflow.crm.service.CrmAgencyBackupRecordService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.user.esb.EsbBackUpApi;
import com.yiling.user.esb.dto.EsbBackUpRecordDTO;
import com.yiling.user.esb.dto.request.EsbBackUpRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgencyBackupServiceImpl implements AgencyBackupService {
    @Resource
    private DatasourceConfig datasourceConfig;
    @Resource
    private CrmEnterpriseMapper crmEnterpriseMapper;
    @DubboReference
    private EsbBackUpApi esbBackUpApi;
    public static final String FORMATE_YEAR_MONTH = "yyyyMM";
    public static final String FORMATE_YEAR = "yyyy";
    public static final String FORMATE_MONTH = "MM";

    /**
     * request中的表的前缀
     * \crm_enterprise
     * \crm_supplier
     * \crm_pharmacy\crm_hospital
     * \crm_enterprise_relation_ship
     * \crm_department_product_relation
     * \crm_department_area_relation
     */
    private final static List<String> backup_tables_prefix = Arrays.asList(
            "crm_enterprise",
            "crm_supplier",
            "crm_pharmacy",
            "crm_hospital",
            "crm_enterprise_relation_ship",
            "crm_department_area_relation",
            "crm_department_product_relation",
            "crm_goods_info",
            "crm_goods_group",
            "crm_goods_group_relation",
            "crm_goods_category",
            "crm_goods_tag",
            "crm_goods_tag_relation",
            "crm_enterprise_relation_pinch_runner");
    private final static List<String> esb_backup_tables_prefix = Arrays.asList(
            "esb_employee",
            "esb_organization");
    private final static String backup_tables_format = "%s_wash_%s";
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    private CrmAgencyBackupRecordService crmAgencyBackupRecordService;
    public void agencyInfoBackup(AgencyBackRequest request) {
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(), request.getOffsetMonth()==null?0:request.getOffsetMonth());
        // yyyy-MM
        String monthBackupStr = (Objects.isNull(request.getYearMonthFormat())||request.getYearMonthFormat().intValue()<=0) ?DateUtil.format(lastMonth, FORMATE_YEAR_MONTH):request.getYearMonthFormat()+"";
        log.info("备份基础档案月份:{}",monthBackupStr);
        //获取当前前缀的表
        List<String> crmTablesNames = DataBaseConnection.getInstance().getTableNames(datasourceConfig, "crm");
        //判断是否存在
        //需要备份的表明=backup_tables_prefix_yyyy-MM
        for (int i = 0; i < backup_tables_prefix.size(); i++) {
            String tableName = backup_tables_prefix.get(i);
            String backupTableName = String.format(backup_tables_format, tableName, monthBackupStr);
            //存在需要先删除表
            if (checkTablesNameExist(crmTablesNames, backupTableName)) {
                String newTablesName = String.format(backup_tables_format, tableName, new Date().getTime());
                crmEnterpriseMapper.renameBackupTable(newTablesName, backupTableName);

            }

            CrmAgencyBackupRecordDO recordDO=new CrmAgencyBackupRecordDO();
            crmEnterpriseMapper.createBackupTable(tableName, backupTableName);
            crmEnterpriseMapper.insertBackupTableData(tableName,backupTableName);
            Long backupTableCount= crmEnterpriseMapper.getBackupTableCount(backupTableName);
            recordDO.setYearMonths(Long.valueOf(monthBackupStr));
            recordDO.setInfo(backup_tables_prefix.get(i));
            recordDO.setName(backup_tables_prefix.get(i));
            recordDO.setStatus(1);
            recordDO.setCreateUser(0L);
            recordDO.setCreateTime(new Date());
            recordDO.setUpdateUser(0L);
            recordDO.setUpdateTime(new Date());
            recordDO.setCount(backupTableCount);
            crmAgencyBackupRecordService.save(recordDO);
            log.info("record:{}",recordDO);
        }

    }
    @Override
    public boolean agencyEsbInfoBackUp(AgencyBackRequest request) {

        agencyInfoBackup(request);
        //迁移定时任务新的定时任务
      //  esbInfoBackup(request);
        //SendResult sendResult = rocketMqProducerService.sendSync(TOPIC_UPDATE_CRM_RELATIONSHIP, TAG_UPDATE_CRM_RELATIONSHIP, DateUtil.formatDate(new Date()), JSON.toJSONString(request.getOffsetMonth()));
        return true;
    }

    @Override
    public boolean checkAgencyBackupByYearMonth(CheckAgencyBackupRequest request) {
        LambdaQueryWrapper<CrmAgencyBackupRecordDO> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(CrmAgencyBackupRecordDO::getYearMonths,request.getYearMonth());
        int count = crmAgencyBackupRecordService.count(wrapper);
        //是否有记录
        return count>0;
    }

    @Override
    public void esbInfoBackup(AgencyBackRequest request) {
        List<EsbBackUpRecordDTO> esbBackUpRecordDTOS = esbBackUpApi.esbInfoBackup(PojoUtils.map(request, EsbBackUpRequest.class));
        if(CollUtil.isNotEmpty(esbBackUpRecordDTOS)) {
            esbBackUpRecordDTOS.stream().forEach(m->{
                CrmAgencyBackupRecordDO recordDO=new CrmAgencyBackupRecordDO();
                PojoUtils.map(m,recordDO);
                crmAgencyBackupRecordService.save(recordDO);
                log.info("esbInfoBackup-record:{}",recordDO);
            });
        }
    }

    private boolean checkTablesNameExist(List<String> crmTablesNames, String crmBackupTable) {
        return crmTablesNames.contains(crmBackupTable);
    }

    public static void main(String[] args) {
        //备份月计算
        DateTime lastMonth = DateUtil.offsetMonth(new Date(),0);
        // yyyy-MM
        String month = DateUtil.format(lastMonth, FORMATE_MONTH);
        String year= DateUtil.format(lastMonth, FORMATE_YEAR);
        System.out.println(Integer.valueOf(year)+":"+month);
    }
}
