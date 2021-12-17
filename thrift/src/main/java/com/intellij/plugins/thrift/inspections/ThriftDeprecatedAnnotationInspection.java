package com.intellij.plugins.thrift.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.plugins.thrift.ThriftBundle;
import com.intellij.plugins.thrift.lang.psi.ThriftField;
import com.intellij.plugins.thrift.lang.psi.ThriftVisitor;
import com.intellij.plugins.thrift.util.ThriftPsiUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThriftDeprecatedAnnotationInspection extends LocalInspectionTool {

  @Override
  public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getGroupDisplayName() {
    return ThriftBundle.message("inspections.group.name");
  }

  @Override
  public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getDisplayName() {
    return ThriftBundle.message("thrift.inspection.deprecated");
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  public @NonNls
  @NotNull String getShortName() {
    return "ThriftDeprecatedAnnotation";
  }

  @Override
  public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
    final List<ProblemDescriptor> result = new ArrayList<>();
    new ThriftVisitor() {

      @Override
      public void visitField(@NotNull ThriftField o) {
        if (ThriftPsiUtil.isAnnotatedDeprecated(o)) {
          ProblemDescriptor problemDescriptor =
            manager.createProblemDescriptor(
              o,
              TextRange.from(0, o.getTextLength()),
              getDisplayName(),
              ProblemHighlightType.INFORMATION, // thrift deprecated field cannot be removed, use INFORMATION level to avoid noise
              isOnTheFly
            );
          problemDescriptor.setTextAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES);
          result.add(problemDescriptor);
        }
      }

      @Override
      public void visitElement(@NotNull PsiElement element) {
        super.visitElement(element);
        element.acceptChildren(this);
      }
    }.visitFile(file);
    return ArrayUtil.toObjectArray(result, ProblemDescriptor.class);
  }
}
