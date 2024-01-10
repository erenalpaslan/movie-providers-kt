package utils

import com.google.crypto.tink.subtle.AesGcmJce
import com.google.crypto.tink.subtle.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CryptographyUtils {

    fun decryptWithAES(key: String, strToDecrypt: String?): String? {
        return try {
            strToDecrypt?.decrypt(key)
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun String.decrypt(password: String): String {
        val ivParameterSpec =  IvParameterSpec(Base64.decode(password, Base64.DEFAULT))

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec =  PBEKeySpec(password.toCharArray(), this.toByteArray(), 10000, 256)
        val tmp = factory.generateSecret(spec);
        val secretKey =  SecretKeySpec(tmp.encoded, "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return  String(cipher.doFinal(Base64.decode(this, Base64.DEFAULT)))
    }

    private fun generateKey(password: String): SecretKeySpec {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray()
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

}