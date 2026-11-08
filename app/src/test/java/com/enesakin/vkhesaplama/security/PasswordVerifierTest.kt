package com.enesakin.vkhesaplama.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordVerifierTest {
    private val fixedSalt = ByteArray(16) { index -> index.toByte() }

    @Test
    fun `creates a verifier without storing the plaintext password`() {
        val verifier = PasswordVerifier.create("guvenli-şifre", fixedSalt)

        assertTrue(PasswordVerifier.isSecureEncoding(verifier))
        assertFalse(verifier.contains("guvenli-şifre"))
    }

    @Test
    fun `accepts the correct password and rejects an incorrect password`() {
        val verifier = PasswordVerifier.create("dogru-sifre", fixedSalt)

        assertTrue(PasswordVerifier.verify("dogru-sifre", verifier))
        assertFalse(PasswordVerifier.verify("yanlis-sifre", verifier))
    }

    @Test
    fun `uses a random salt for each stored verifier`() {
        val first = PasswordVerifier.create("ayni-sifre")
        val second = PasswordVerifier.create("ayni-sifre")

        assertNotEquals(first, second)
        assertTrue(PasswordVerifier.verify("ayni-sifre", first))
        assertTrue(PasswordVerifier.verify("ayni-sifre", second))
    }

    @Test
    fun `rejects plaintext legacy values and malformed encodings`() {
        assertFalse(PasswordVerifier.isSecureEncoding("eski-duz-metin"))
        assertFalse(PasswordVerifier.verify("eski-duz-metin", "eski-duz-metin"))
        assertFalse(
            PasswordVerifier.verify(
                "password",
                "pbkdf2-sha1\$not-a-number\$00\$00",
            ),
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `requires at least eight password characters`() {
        PasswordVerifier.create("kisa", fixedSalt)
    }
}
