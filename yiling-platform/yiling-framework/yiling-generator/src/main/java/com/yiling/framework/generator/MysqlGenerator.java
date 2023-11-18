package com.yiling.framework.generator;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.yiling.framework.generator.config.GeneratorConfig;
import com.yiling.framework.generator.config.converts.MyMySqlTypeConvert;

/**
 * <p>
 * mysql 代码生成器演示例子
 * </p>
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
public class MysqlGenerator {

    private static GeneratorConfig generatorConfig = GeneratorConfig.getInstance();

    private static GlobalConfig getGlobalConfig() {
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig()
                // 生成文件的输出目录【默认 D 盘根目录】
                .setOutputDir(generatorConfig.getProjectPath() + "/src/main/java")
                // 开发人员
                .setAuthor(generatorConfig.getDeveloper())
                // 是否覆盖已有文件
                .setFileOverride(generatorConfig.getOverride())
                // 是否开启 swagger2 模式(DO对象不会对外，生成swagger注解没用)
                .setSwagger2(false)
                // 是否开启 BaseResultMap
                .setBaseResultMap(true)
                // 是否开启 baseColumnList
                .setBaseColumnList(true)
                // 是否打开输出目录
                .setOpen(false)
                // 统一日期类型
                .setDateType(DateType.ONLY_DATE)
                // 让表对象名称以DO结尾
                .setEntityName("%sDO")
                // 让Service接口名称以Service结尾
                .setServiceName("%sService")
                // 让Mapper接口名称以Mapper结尾
                .setMapperName("%sMapper");
        return globalConfig;
    }

    private static DataSourceConfig getDataSourceConfig() {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                // com.mysql.jdbc.Driver
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl(generatorConfig.getUrl())
                .setUsername(generatorConfig.getUsername())
                .setPassword(generatorConfig.getPassword())
                .setSchemaName(generatorConfig.getSchemaName())
                // 增加自定义的类型转换器
                .setTypeConvert(new MyMySqlTypeConvert());
        return dataSourceConfig;
    }

    private static PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig
                .setParent(generatorConfig.getParentPackage())
                .setController("controller")
                .setEntity("entity")
                .setMapper("dao")
                .setService("service")
                .setServiceImpl("service.impl");

        String moduleName = generatorConfig.getModuleName();
        if (null != moduleName && !"".equals(moduleName.trim())) {
            packageConfig.setModuleName(moduleName);
        }

        return packageConfig;
    }

    private static InjectionConfig getInjectionConfig() {
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                StringBuilder sb = new StringBuilder()
                        .append(generatorConfig.getProjectPath())
                        .append("/src/main/resources/mapper/");

                String moduleName = generatorConfig.getModuleName();
                if (null != moduleName && !"".equals(moduleName.trim())) {
                    sb.append(moduleName).append("/");
                }
                sb.append(tableInfo.getEntityName().replace("DO", "")).append("Mapper").append(StringPool.DOT_XML);
                return sb.toString();
            }
        });

        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }

    private static StrategyConfig getStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setSuperEntityClass("com.yiling.framework.common.base.BaseDO")
                .setEntityLombokModel(true)
                .setSuperMapperClass("com.yiling.framework.common.base.BaseMapper")
                .setSuperServiceClass("com.yiling.framework.common.base.BaseService")
                .setSuperServiceImplClass("com.yiling.framework.common.base.BaseServiceImpl")
                .setSuperControllerClass("com.yiling.framework.common.base.BaseController")
                .setInclude(generatorConfig.getTableNames())
                .setSuperEntityColumns("id")
                .setLogicDeleteFieldName("del_flag")
                .setVersionFieldName("version")
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                .setTablePrefix(generatorConfig.getTablePrefix());

        // 自动填充配置
        TableFill createUser = new TableFill("create_user", FieldFill.INSERT);
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateUser = new TableFill("update_user",FieldFill.INSERT_UPDATE);
        TableFill updateTime = new TableFill("update_time",FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createUser);
        tableFills.add(createTime);
        tableFills.add(updateUser);
        tableFills.add(updateTime);
        strategyConfig.setTableFillList(tableFills);

        return strategyConfig;
    }

    private static TemplateConfig getTemplateConfig(){
        //配置 自定义模板
        TemplateConfig templateConfig = new TemplateConfig()
                .setEntity("templates/entity.java")
                .setMapper("templates/mapper.java")
                .setController("templates/mapper.xml")
                .setController("templates/service.java")
                .setController("templates/serviceImpl.java")
                .setController("templates/controller.java")
                .setXml(null);
        return templateConfig;
    }

    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
        // 获取全局配置
        autoGenerator.setGlobalConfig(getGlobalConfig());
        // 获取数据源
        autoGenerator.setDataSource(getDataSourceConfig());
        // 包配置
        autoGenerator.setPackageInfo(getPackageConfig());
        // 自定义配置
        autoGenerator.setCfg(getInjectionConfig());
        // 策略配置
        autoGenerator.setStrategy(getStrategyConfig());
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        // 自定义模板
        autoGenerator.setTemplate(getTemplateConfig());
        // 执行
        autoGenerator.execute();
    }

}
