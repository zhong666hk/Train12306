package com.wbu.train.genrator_mybatisplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
/**
 * <p>created time:2023/4/25 19:34</p>
 * <p>des:
 *     代码生成器（新）
 * </p>
 *
 * @author Ya Shi
 */
public class MyBatis_plus_Generator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/train", "root", "zzb200166")
            .dbQuery(new MySqlQuery());

    /**
     * 输出路径
     */
    private static final String outputDir = System.getProperty("user.dir") + "/src/main/java";

    public static void main(String[] args) {

        FastAutoGenerator.create(DATA_SOURCE_CONFIG) //全局配置
                .globalConfig(builder -> {
                    builder.author("zzb") // 设置作者
                            .enableSpringdoc()
                            // .enableSwagger() // 开启 swagger 模式
                            .outputDir(outputDir)   // 指定输出目录
                            .disableOpenDir();   //禁止打开输出目录
                })
                .packageConfig(builder -> { //包配置---包名
                    builder.parent("com.wbu.train.passenger"); // 设置父包名
                })
                .strategyConfig(builder -> { // 策略配置
                    builder.addInclude("passenger") //包含的表
                            .controllerBuilder().enableFileOverride().enableRestStyle() //开启restful风格
                            .serviceBuilder().enableFileOverride() // 开启文件的覆盖策略
                            .entityBuilder().enableFileOverride()   //开启文件覆盖策略

                            .mapperBuilder().enableFileOverride(); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}


