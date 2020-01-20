package com.angel.plugin.generator.base;

import com.angel.emodel.core.model.EModel;

import java.io.IOException;

/**
 * @author angel
 * @description
 * @date 2019/11/4 19:49
 */
public interface E2BeanFactory {

    /**
     * 根据emodel转为java bean
     * @param emodel
     * @param config
     */
    void generateBean(EModel emodel, ClassConfig config) throws IOException;
}
