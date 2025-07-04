package org.purboyndradev.rt_rw.helper

import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.io.encoding.Base64

object JWTObject {
    
    fun decodeJwtPayload(jwtToken: String): JsonObject? {
        try {
            val parts = jwtToken.split(".")
            if (parts.size != 3) {
                println("Invalid JWT Format: Token does not have 3 parts")
                return null
            }
            
            val payloadBase64Url = parts[1]
            
            var payloadBase64 =
                payloadBase64Url.replace("-", "+").replace("_", "/")
            when (payloadBase64.length % 4) {
                2 -> payloadBase64 += "=="
                3 -> payloadBase64 += "="
            }
            
            val payloadJsonBytes = Base64.decode(payloadBase64)
            
            val charArray = CharArray(payloadJsonBytes.size)
            for (i in 0 until payloadJsonBytes.size) {
                charArray[i] = (payloadJsonBytes[i].toInt() and 0xff).toChar()
            }
            
            val payloadJsonString = charArray.concatToString()
            
            return Json.parseToJsonElement(payloadJsonString).jsonObject
            
        } catch (e: Exception) {
            return null
        }
    }
    
}