package com.zn.apps.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class PomodoroStateManagerSerializer @Inject constructor(): Serializer<PomodoroStateManager> {

    override val defaultValue: PomodoroStateManager
        get() = PomodoroStateManager.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PomodoroStateManager =
        try {
            PomodoroStateManager.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: PomodoroStateManager, output: OutputStream) {
        t.writeTo(output)
    }
}