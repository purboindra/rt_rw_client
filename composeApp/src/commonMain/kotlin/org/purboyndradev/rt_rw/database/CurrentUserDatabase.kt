package org.purboyndradev.rt_rw.database

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource
import okio.use
import org.purboyndradev.rt_rw.core.data.model.UserDBModel

val json = Json { ignoreUnknownKeys = true }

internal object UserJsonSerializer : OkioSerializer<UserDBModel?> {
    
    override val defaultValue: UserDBModel? = null
    
    override suspend fun readFrom(source: BufferedSource): UserDBModel? {
        return try {
            json.decodeFromString<UserDBModel>(source.readUtf8())
        } catch (e: Exception) {
            defaultValue
        }
    }
    
    override suspend fun writeTo(
        t: UserDBModel?,
        sink: BufferedSink
    ) {
        sink.use {
            if (t != null) {
                it.writeUtf8(
                    json.encodeToString(UserDBModel.serializer(), t)
                )
            }
        }
    }
}