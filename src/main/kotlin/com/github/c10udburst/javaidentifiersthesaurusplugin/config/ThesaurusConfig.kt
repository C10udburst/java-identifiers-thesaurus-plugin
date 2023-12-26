package com.github.c10udburst.javaidentifiersthesaurusplugin.config

import com.intellij.openapi.util.registry.RegistryManager

private const val REGISTRY_ROOT = "com.github.c10udburst.javaidentifiersthesaurus.config"
class ThesaurusConfig {
    companion object {
        val INSTANCE = ThesaurusConfig()
    }
    fun getSynonyms(keyword: String) =
        RegistryManager.getInstance().get("$REGISTRY_ROOT.$keyword").asString()

    fun setSynonyms(keyword: String, synonyms: String) =
        RegistryManager.getInstance().get("$REGISTRY_ROOT.$keyword").setValue(synonyms)
}