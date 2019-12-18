package com.angel.plugin;

import com.angel.plugin.generator.BeanGenerator;
import com.angel.plugin.generator.base.ClassConfig;
import com.angel.plugin.generator.base.Constants;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class GenerateAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //获取当前在操作的工程的上下文
        Project project = e.getData(PlatformDataKeys.PROJECT);

        //获取当前操作的类文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        assert psiFile != null;
        //获取当前文件所在路径的src目录
        String containingDirectory = psiFile.getContainingDirectory().getVirtualFile().getPresentableUrl();
        containingDirectory = containingDirectory.substring(0, containingDirectory.indexOf("src"));
        Constants.project = containingDirectory;

        //获取当前文件的路径
        String classPath = psiFile.getVirtualFile().getPath();
//        String suffix = classPath.substring(classPath.lastIndexOf("."));
//        if (!".CIME".equals(suffix.toUpperCase()) && !".DT".equals(suffix.toUpperCase())) {
//            throw TsieExceptionUtils.newBusinessException("只支持CIME或DT文件");
//        }
        ParamDialogWrapper paramDialogWrapper = new ParamDialogWrapper();
        paramDialogWrapper.show();
        if (paramDialogWrapper.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            ClassConfig config = paramDialogWrapper.getClassConfig();
            try {
                BeanGenerator.generate(classPath, config);
                Messages.showMessageDialog(project, "Generate successfully", Constants.title, Messages.getInformationIcon());
            } catch (Exception exception) {
                Messages.showMessageDialog(project, exception.getMessage(), Constants.title, Messages.getErrorIcon());
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
    }


}
