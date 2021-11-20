package com.github.ai.fprovider.data.dao

import android.content.Context
import com.github.ai.fprovider.data.serialization.Deserializer
import com.github.ai.fprovider.data.serialization.Serializer
import com.github.ai.fprovider.entity.TokenAndPath

internal class TokenDaoImpl(
    context: Context,
    private val serializer: Serializer<TokenAndPath>,
    private val deserializer: Deserializer<TokenAndPath>
) : TokenDao {

    private val preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun add(token: TokenAndPath) {
        val newValues = readData()
            .toMutableSet()
            .apply {
                add(serializer.serialize(token))
            }

        saveData(newValues)
    }

    override fun getAll(): List<TokenAndPath> {
        return readData()
            .mapNotNull { deserializer.deserialize(it) }
    }

    override fun removeAll() {
        saveData(emptySet())
    }

    private fun readData(): Set<String> {
        return preferences.getStringSet(KEY_TOKENS_DATA, null) ?: emptySet()
    }

    private fun saveData(data: Set<String>) {
        preferences.edit().apply {
            putStringSet(KEY_TOKENS_DATA, data)
            apply()
        }
    }

    companion object {
        internal const val KEY_TOKENS_DATA = "tokensData"
        internal const val SHARED_PREFS_NAME = "internal-storage-file-provider"
    }
}