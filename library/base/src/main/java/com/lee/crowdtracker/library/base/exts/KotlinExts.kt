package com.lee.crowdtracker.library.base.exts

import kotlin.coroutines.cancellation.CancellationException

inline fun <T, R> T.runSuspendCatching(block: T.() -> R): Result<R> = try {
    Result.success(block())
} catch (c: CancellationException) {
    throw c
} catch (e: Throwable) {
    Result.failure(e)
}
