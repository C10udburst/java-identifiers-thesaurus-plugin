package com.github.c10udburst.javaidentifiersthesaurusplugin.actions

import com.github.c10udburst.javaidentifiersthesaurusplugin.Keywords
import com.github.c10udburst.javaidentifiersthesaurusplugin.ThesaurusBundle
import com.github.c10udburst.javaidentifiersthesaurusplugin.config.ThesaurusConfig
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInsight.intention.preview.IntentionPreviewUtils
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.PopupChooserBuilder
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaToken
import com.intellij.psi.util.elementType
import com.intellij.ui.components.JBList


class ThesaurusAction: PsiElementBaseIntentionAction() {


    override fun getFamilyName(): String {
        return ThesaurusBundle.message("intention.family")
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (element !is PsiJavaToken)
            return false
        text = ThesaurusBundle.message("intention.text", element.text)
        for (i in Keywords.keywords) {
            if (element.elementType.toString() == i)
                return true
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        if (IntentionPreviewUtils.isIntentionPreviewActive())
            return

        val keyword = element.elementType.toString()

        val options = getOptions(keyword)
        val jbList = JBList(options)

        val builder = PopupChooserBuilder(jbList)
        builder.setTitle(ThesaurusBundle.message("intention.window.title"))
        builder.setItemChoosenCallback {
            WriteCommandAction.writeCommandAction(project).run<Throwable> {
                element.replace(JavaPsiFacade.getElementFactory(project).createIdentifier(options[jbList.selectedIndex]))
            }
        }

        builder.createPopup().showInBestPositionFor(editor!!)
    }

    private fun getOptions(keyword: String): List<String> {
        return ThesaurusConfig.INSTANCE.getSynonyms(keyword).split("\n")
    }
}