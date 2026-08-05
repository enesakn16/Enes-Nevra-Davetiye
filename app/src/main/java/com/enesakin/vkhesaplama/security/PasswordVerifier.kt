package com.enesakin.vkhesaplama.security

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordVerifier {
    private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val FORMAT = "pbkdf2-sha1"
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_LENGTH_BYTES = 16

    fun create(password: String): String {
        require(password.length >= 8) { "Password must contain at least 8 characters." }
        val salt = ByteArray(SALT_LENGTH_BYTES).also(SecureRandom()::nextBytes)
        return create(password, salt)
    }

    internal fun create(password: String, salt: ByteArray): String {
        require(password.length >= 8) { "Password must contain at least 8 characters." }
        require(salt.size >= SALT_LENGTH_BYTES) { "Salt must contain at least 16 bytes." }

        val hash = derive(password, salt, ITERATIONS)
        return listOf(FORMAT, ITERATIONS.toString(), salt.toHex(), hash.toHex()).joinToString("$")
    }

    fun verify(password: String, encodedVerifier: String): Boolean {
        val parts = encodedVerifier.split('$')
        if (parts.size != 4 || parts[0] != FORMAT) return false

        return runCatching {
            val iterations = parts[1].toInt()
            require(iterations >= ITERATIONS)
            val salt = parts[2].hexToBytes()
            val expected = parts[3].hexToBytes()
            val actual = derive(password, salt, iterations)
            MessageDigest.isEqual(expected, actual)
        }.getOrDefault(false)
    }

    fun isSecureEncoding(value: String): Boolean = value.startsWith("$FORMAT$")

    private fun derive(password: String, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private fun ByteArray.toHex(): String = joinToString(separator = "") { byte -> "%02x".format(byte) }

    private fun String.hexToBytes(): ByteArray {
        require(length % 2 == 0)
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}
