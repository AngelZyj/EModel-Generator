package com.angel.plugin.generator.base;

import com.google.common.base.CaseFormat;
import com.angel.emodel.core.model.EModel;
import com.angel.emodel.core.model.meta.Attribute;
import com.angel.emodel.core.model.meta.TableInfo;
import com.angel.emodel.core.model.meta.Template;
import com.angel.emodel.core.model.meta.TemplateProperty;
import com.angel.emodel.core.model.table.StructTable;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author angel
 * @description
 * @date 2019/11/4 19:53
 */
public class E2BeanGenerator implements E2BeanFactory {

    @Override
    public void generateBean(EModel emodel, ClassConfig config) throws IOException {
        List<ClassProperty> classProperties = parseWholeFile(emodel, config);
        ClassMarker marker = new ClassMarker();
        marker.make(classProperties,config.isUseLomBok());
    }

    private List<ClassProperty> parseWholeFile(EModel eModel, ClassConfig config) {
        List<ClassProperty> classProperties = new ArrayList<>();
        ClassProperty modelBean = new ClassProperty();
        modelBean.setPackageName(config.getPackageName());
        modelBean.setClassName(config.getClassName());
        modelBean.setParentPackageName("com.angel.emodel.easy.core");
        modelBean.setParentClassName("StaticEModel");
        List<FieldProperty> modelFields = new ArrayList<>();
        modelBean.setFiledProperties(modelFields);

        for (Map.Entry<String, StructTable> entry : eModel.getStructTableMap().entrySet()) {
            //最外层的model ------------- start
            TableInfo tableInfo = entry.getValue().getTableInfo();
            FieldProperty field = new FieldProperty();
            field.setFieldName(formatToLowerCamel(config.getTableFormat(), tableInfo.getTableName()+"s"));
            field.setFieldType("List<" + formatToUpperCamel(config.getTableFormat(),tableInfo.getTableName()) + ">");
            AnnotationProperty annotation = new AnnotationProperty();
            annotation.setPackageName("com.angel.emodel.easy.annotation");
            annotation.setName("ETable");
            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put("name", tableInfo.getTableName());
            if (StringUtils.isNotBlank(tableInfo.getInstanceName())) {
                String[] instant = tableInfo.getInstanceName().replace("\t", " ").split(" ");
                fieldMap.put("instance", instant[0]);
            }
            if (MapUtils.isNotEmpty(tableInfo.getArrtibutesMap())) {
                StringBuilder attrs = new StringBuilder("{");
                annotation.setAnnotationImportClass(new ArrayList<String>() {{
                    add("com.angel.emodel.easy.annotation.ETableAttr");
                }});
                for (Map.Entry<String, Attribute> attribute :tableInfo.getArrtibutesMap().entrySet()) {
                    AnnotationProperty annotationProperty = new AnnotationProperty();
                    annotationProperty.setName("ETableAttr");
                    annotationProperty.setPackageName("com.angel.emodel.easy.annotation");
                    Map<String, String> annotationFieldMap = new HashMap<>();
                    annotationFieldMap.put("name",attribute.getKey());
                    annotationFieldMap.put("value", attribute.getValue().getValue());
                    annotationProperty.setFieldMap(annotationFieldMap);

                    attrs.append(annotationProperty.toAnnotation());
                    attrs.append(",");
                }
                attrs.delete(attrs.lastIndexOf(","), attrs.lastIndexOf(",")+1);
                attrs.append("}");
                fieldMap.put("attrs", attrs.toString());
            }
            annotation.setFieldMap(fieldMap);
            field.setAnnotationProperties(new ArrayList<AnnotationProperty>() {{
                add(annotation);
            }});
            modelFields.add(field);
            //最外层的model ------------- end

            //逐个表的bean ------------- start
            classProperties.add(parseTable(entry.getValue(),config));
        }
        classProperties.add(modelBean);
        return classProperties;
    }

    private ClassProperty parseTable(StructTable structTable,ClassConfig config){
        ClassProperty classProperty = new ClassProperty();
        classProperty.setPackageName(config.getPackageName());
        if (structTable != null) {
            classProperty.setClassName(formatToUpperCamel(config.getTableFormat(), structTable.getTableInfo().getTableName()));
            Template template = structTable.getTemplate();
            if (template != null) {
                List<TemplateProperty> properties = template.getProperties();
                List<FieldProperty> fieldProperties = new ArrayList<>();
                for (TemplateProperty property : properties) {
                    FieldProperty fieldProperty = new FieldProperty();
                    fieldProperty.setFieldName(formatToLowerCamel(config.getColumnFormat(), property.getName()));
                    fieldProperty.setFieldType("String");
                    List<AnnotationProperty> annotationProperties = new ArrayList<>();
                    AnnotationProperty annotationProperty = new AnnotationProperty();
                    annotationProperty.setName("ETableField");
                    annotationProperty.setPackageName("com.angel.emodel.easy.annotation");
                    Map<String, String> annotationFieldMap = new HashMap<>();
                    annotationFieldMap.put("name", property.getName());
                    //todo:明明有备注这么一个属性，怎么别名就变成了备注
                    if (StringUtils.isNotBlank(property.getAlias())) {
                        annotationFieldMap.put("comment", property.getAlias());
                    }
                    annotationProperty.setFieldMap(annotationFieldMap);
                    annotationProperties.add(annotationProperty);
                    fieldProperty.setAnnotationProperties(annotationProperties);
                    fieldProperties.add(fieldProperty);
                }
                classProperty.setFiledProperties(fieldProperties);
            }
        }

        return classProperty;
    }


    //转小驼峰
    private String formatToLowerCamel(CaseFormat format, String str) {
        return format.to(CaseFormat.LOWER_CAMEL, str);
    }

    //转大驼峰
    private String formatToUpperCamel(CaseFormat format, String str) {
        return format.to(CaseFormat.UPPER_CAMEL, str);
    }

}
