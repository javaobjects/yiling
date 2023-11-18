package com.yiling.framework.generator.config;

import java.util.Properties;

/**
 * 代码生成工具配置文件
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
public class GeneratorConfig {

    private static GeneratorConfig generatorConfig;

    private String                 projectPath;
    private String                 parentPackage;
    private Boolean                override;
    private String                 tablePrefix;

    private String                 moduleName;
    private String[]               tableNames;
    private String                 developer;

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String schemaName;

    private GeneratorConfig() {
        Properties properties = new Properties();
        try {
            properties.load(GeneratorConfig.class.getResourceAsStream("/generator.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Global
        this.projectPath = properties.getProperty("global.project.path");
        this.parentPackage = properties.getProperty("global.parent.package");
        this.override = Boolean.valueOf(properties.getProperty("global.override"));
        this.tablePrefix = properties.getProperty("global.table.prefix");
        // Generate
        this.moduleName = properties.getProperty("generate.moduleName");
        this.tableNames = properties.getProperty("generate.tableNames").split(",");
        this.developer = properties.getProperty("generate.developer");
        // DataSource
        this.driverClassName = properties.getProperty("datasource.driver-class-name");
        this.url = properties.getProperty("datasource.url");
        this.username = properties.getProperty("datasource.username");
        this.password = properties.getProperty("datasource.password");
        this.schemaName = properties.getProperty("datasource.schemaName");

        print();
    }

    private void print() {
        System.out.println("代码生成工具配置文件加载完毕，配置信息如下：");
        System.out.println("--- Begin ---");
        System.out.println("# project");
        System.out.println("projectPath：" + this.projectPath);
        System.out.println("parentPackage：" + this.parentPackage);
        System.out.println("# generator");
        System.out.println("工程目录：");
        System.out.println("moduleName：" + this.moduleName);
        System.out.println("tableNames：" + this.tableNames);
        System.out.println("developer：" + this.developer);
        System.out.println("override：" + this.override);
        System.out.println("# generator.datasource");
        System.out.println("driverClassName：" + this.driverClassName);
        System.out.println("url：" + this.url);
        System.out.println("username：" + this.username);
        System.out.println("password：" + this.password);
        System.out.println("schemaName：" + this.schemaName);
        System.out.println("--- End ---");
    }

    public static GeneratorConfig getInstance() {
        if (null == generatorConfig) {
            synchronized (GeneratorConfig.class) {
                if (null == generatorConfig) {
                    generatorConfig = new GeneratorConfig();
                }
            }
        }
        return generatorConfig;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getParentPackage() {
        return parentPackage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public String getDeveloper() {
        return developer;
    }

    public Boolean getOverride() {
        return override;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchemaName() {
        return schemaName;
    }
}
