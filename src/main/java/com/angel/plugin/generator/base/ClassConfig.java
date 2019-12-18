package com.angel.plugin.generator.base;

import com.google.common.base.CaseFormat;
import lombok.Data;

/**
 * @author angel
 * @description 生成的bean文件配置
 * @date 2019/11/4 19:37
 */
@Data
public class ClassConfig {

    /**
     * 包名
     */
    private String packageName = "com.angel.bean";

    /**
     * 最外层的bean名字
     */
    private String className = "EModel";

    /**
     * Etable标签的格式,默认"UPPER_UNDERSCORE"
     */
    private CaseFormat tableFormat= CaseFormat.UPPER_UNDERSCORE;

    /**
     * E文本的列名格式,默认"UPPER_UNDERSCORE"
     */
    private CaseFormat columnFormat = CaseFormat.UPPER_UNDERSCORE;

    /**
     * 文件格式
     */
    private String fileEncoding="utf-8";

    /**
     * 是否使用lombok
     */
    private boolean useLomBok = true;

}
