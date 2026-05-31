package com.example.demo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    // ====================== 这里改成你自己的 SQL Server 信息 ======================
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=wms;encrypt=false;trustServerCertificate=true";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456";
    private static final String PACKAGE_NAME = "com.example.demo";
    private static final String AUTHOR = "author";
    private static final String TABLE_NAMES = "record"; // 你要生成的表名
    private static final String TABLE_PREFIX = "";

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR)
                            .outputDir(System.getProperty("user.dir") + "/demo/src/main/java")
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd")
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_NAME)
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAMES)
                            .addTablePrefix(TABLE_PREFIX)
                            .entityBuilder()
                            .enableLombok()
                            .idType(IdType.AUTO)

                            .controllerBuilder()
                            .enableRestStyle()

                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")

                            .mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}