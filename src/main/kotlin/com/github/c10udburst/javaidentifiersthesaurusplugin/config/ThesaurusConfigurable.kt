package com.github.c10udburst.javaidentifiersthesaurusplugin.config

import com.github.c10udburst.javaidentifiersthesaurusplugin.Keywords
import com.github.c10udburst.javaidentifiersthesaurusplugin.ThesaurusBundle
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

class ThesaurusConfigurable: BoundConfigurable(ThesaurusBundle.message("configurable.name")) {
    override fun createPanel() = panel {
        for (i in Keywords.keywords) {
            row {
                label(ThesaurusBundle.message("configurable.$i"))
                textArea()
                    .bindText(
                        { ThesaurusConfig.INSTANCE.getSynonyms(i) },
                        { ThesaurusConfig.INSTANCE.setSynonyms(i, it) }
                    )
            }
        }
    }
}