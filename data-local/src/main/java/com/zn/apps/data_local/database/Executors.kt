package com.zn.apps.data_local.database

import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(run : () -> Unit) {
    IO_EXECUTOR.execute(run)
}