package com.angel.plugin.generator;

import com.angel.plugin.generator.base.ClassConfig;
import com.angel.plugin.generator.base.E2BeanFactory;
import com.angel.plugin.generator.base.E2BeanGenerator;
import com.tsieframework.core.base.exception.TsieExceptionUtils;
import com.angel.emodel.core.model.EModel;
import com.angel.emodel.core.parser.ParserConfig;
import com.angel.emodel.core.parser.SimpleFileInputStream;
import com.angel.emodel.core.parser.support.DefaultEModelParser;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author angel
 * @description 根据e文本生成带注解的java bean(Bmodel)
 * @date 2019/11/4 19:16
 */
public class BeanGenerator {

    public static void generate(String filePath, ClassConfig classConfig) {
        File eFile = new File(filePath);
        if (eFile.exists()&& !eFile.isDirectory()) {
            try (FileInputStream in = new FileInputStream(eFile)) {
                ParserConfig config = new ParserConfig();
                config.setEncoding(classConfig.getFileEncoding());
                EModel eModel = new DefaultEModelParser().parse(new SimpleFileInputStream(in));
                //获取到Emodel后转为java bean
                E2BeanFactory e2BeanFactory = new E2BeanGenerator();
                e2BeanFactory.generateBean(eModel,classConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw TsieExceptionUtils.newBusinessException("file not found !");
        }
    }
}
