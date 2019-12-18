package com.angel.plugin;

import com.angel.plugin.generator.base.ClassConfig;
import com.angel.plugin.generator.base.Constants;
import com.google.common.base.CaseFormat;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author angel
 */
public class ParamDialogWrapper extends DialogWrapper {

    private JTextField packageTextField = new JTextField();
    private JTextField classTextField = new JTextField();
    private JTextField encodingTextField = new JTextField();
    private ComboBox<CaseFormat> tableComboBox = new ComboBox<>(CaseFormat.values());
    private ComboBox<CaseFormat> columnComboBox = new ComboBox<>(CaseFormat.values());
    private ComboBox<Boolean> lombokComboBox = new ComboBox<>(new Boolean[]{true, false});



    public ParamDialogWrapper() {
        super(true);
        init();
        setTitle(Constants.title);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new GridLayout(6,2,0,5));
        JLabel packageLabel = new JLabel("Bean generate Package :");
        JLabel classLabel = new JLabel("emodel class name :");
        JLabel encodingLabel = new JLabel("file encoding :");
        JLabel tableFormat = new JLabel("original table pattern :");
        JLabel columnFormat = new JLabel("original column pattern :");
        JLabel lombokLabel = new JLabel("use lombok :");
        dialogPanel.add(packageLabel);
        dialogPanel.add(packageTextField);
        dialogPanel.add(classLabel);
        dialogPanel.add(classTextField);
        dialogPanel.add(encodingLabel);
        dialogPanel.add(encodingTextField);
        dialogPanel.add(tableFormat);
        dialogPanel.add(tableComboBox);
        dialogPanel.add(columnFormat);
        dialogPanel.add(columnComboBox);
        dialogPanel.add(lombokLabel);
        dialogPanel.add(lombokComboBox);
        return dialogPanel;
    }

    public ClassConfig getClassConfig() {
        ClassConfig classConfig = new ClassConfig();
        String packageTextFieldText = packageTextField.getText();
        if (!packageTextFieldText.isEmpty()) {
            classConfig.setPackageName(packageTextField.getText());
        }
        String classTextFieldText = classTextField.getText();
        if (!classTextFieldText.isEmpty()) {
            classConfig.setClassName(classTextField.getText());
        }
        if (!encodingTextField.isEnabled()) {
            classConfig.setFileEncoding(encodingTextField.getText());
        }
        classConfig.setTableFormat(tableComboBox.getItemAt(tableComboBox.getSelectedIndex()));
        classConfig.setColumnFormat(columnComboBox.getItemAt(columnComboBox.getSelectedIndex()));
        classConfig.setUseLomBok(lombokComboBox.getItemAt(lombokComboBox.getSelectedIndex()));
        return classConfig;
    }

}