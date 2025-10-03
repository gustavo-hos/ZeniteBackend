package com.softtek.zenite.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class DicewarePassphraseGenerator(
    @Value("\${passphrase.words:4}") private val defaultWords: Int,
    @Value("\${passphrase.separator:-}") private val separator: String,
    private val resourceLoader: ResourceLoader
) {
    private val rng = SecureRandom()
    private val words: List<String> = loadWords()

    private fun loadWords(): List<String> {
        val res = resourceLoader.getResource("classpath:wordlists/pt_br_animais_insetos_small.txt")
        require(res.exists()) {
            "Missing wordlist. Add wordlists/eff_large_wordlist.txt to resources"
        }
        return res.inputStream.bufferedReader().useLines { lines ->
            lines.map { line ->
                val trimmed = line.trim()
                if (trimmed.isEmpty() || trimmed.startsWith("#")) return@map null
                val parts = trimmed.split(Regex("\\s+"))
                parts.lastOrNull()
            }.filterNotNull().toList()
        }.also { list ->
            require(list.isNotEmpty()) { "Wordlist is empty" }
        }
    }

    fun generate(numWords: Int? = null): String {
        val n = numWords ?: defaultWords
        require(n >= 3) { "Use at least 3 words" }
        return (1..n).joinToString(separator) { words[rng.nextInt(words.size)] }
    }
}