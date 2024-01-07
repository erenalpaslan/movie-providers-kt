package utils

import java.security.Key
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptographyUtils {

    private fun generateKey(): Key {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }


    fun decryptWithAES(key: String, strToDecrypt: String?): String? {
        return try {
            strToDecrypt?.let {
                val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                cipher.init(Cipher.DECRYPT_MODE, generateKey(key), IvParameterSpec(ByteArray(16)))
                cipher.doFinal(strToDecrypt.toByteArray(Charsets.UTF_8)).decodeToString()
            }
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun generateKey(password: String): SecretKeySpec {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray()
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

}