package com.github.c10udburst.javaidentifiersthesaurusplugin.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.IntentionActionWithChoice
import com.intellij.codeInsight.intention.IntentionActionWithOptions
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInsight.intention.choice.DefaultIntentionActionWithChoice
import com.intellij.codeInsight.intention.preview.IntentionPreviewUtils
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.PopupChooserBuilder
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaToken
import com.intellij.psi.util.elementType
import com.intellij.ui.components.JBList
import com.intellij.ui.popup.list.ListPopupImpl


abstract class ThesaurusAction: PsiElementBaseIntentionAction() {

    protected abstract val identifier: String

    override fun getFamilyName(): String {
        return "$identifier thesaurus"
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        thisLogger().warn("type: ${element.elementType.toString()}")
        if (element !is PsiJavaToken)
            return false
        return element.elementType.toString() == identifier
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        if (IntentionPreviewUtils.isIntentionPreviewActive())
            return

        val jbList = JBList(getOptions())

        val builder = PopupChooserBuilder(jbList)
        builder.setTitle("Choose a synonym")
        builder.setItemChoosenCallback {
            WriteCommandAction.writeCommandAction(project).run<Throwable> {
                element.replace(JavaPsiFacade.getElementFactory(project).createIdentifier(getOptions()[jbList.selectedIndex]))
            }
        }

        builder.createPopup().showInBestPositionFor(editor!!);
    }

    fun getOptions(): List<String> {
        return listOf(
            "synonym1",
            "synonym2",
        )
    }
}