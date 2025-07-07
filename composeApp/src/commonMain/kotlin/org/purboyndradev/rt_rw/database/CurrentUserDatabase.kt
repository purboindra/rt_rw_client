package org.purboyndradev.rt_rw.database

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource
import okio.use
import org.purboyndradev.rt_rw.domain.model.UserDBModel

val json = Json { ignoreUnknownKeys = true }

internal object UserJsonSerializer : OkioSerializer<UserDBModel?> {
    
    override val defaultValue: UserDBModel? = null
    
    override suspend fun readFrom(source: BufferedSource): UserDBModel? {
        return try {
            val raw = source.readUtf8()
            if (raw.isBlank() || raw == "null") {
                println("Datastore raw content is blank or 'null'")
                throw Exception("Datastore raw content is blank or 'null'")
            }
            println("Reading user from DataStore execute: $raw")
            json.decodeFromString(UserDBModel.serializer(), raw)
        } catch (e: Exception) {
            println("Error reading from DataStore: ${e.message}")
            null
        }
    }
    
    override suspend fun writeTo(t: UserDBModel?, sink: BufferedSink) {
        println("Writing user to DataStore: $t")
        sink.use {
            try {
                if (t != null) {
                    val encoded =
                        json.encodeToString(UserDBModel.serializer(), t)
                    sink.writeUtf8(encoded)
                }
            } catch (e: Exception) {
                println("Exception in writeTo: ${e.message}")
                throw e
            }
        }
    }
}