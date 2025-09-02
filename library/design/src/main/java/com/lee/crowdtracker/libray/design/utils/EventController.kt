package com.lee.crowdtracker.libray.design.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID

/**
 * @param throttledTimeMillis default 1000ms
 * @param id 프로세스 아이디
 * @param onProcessed
 *
 * 전달 받은 onProcessed를 throttle 하게 실행한다.
 * id를 기반으로 onProcessed를 저장하여 사용한다.
 *
 * **/
@Composable
fun throttledProcess(
    throttledTimeMillis: Long = DefaultThrottlingTime,
    id: String = remember { randomUUID().toString() },
    onProcessed: () -> Unit,
): () -> Unit {
    val multipleEventsCutter = remember { EventProcessor.getThrottlingProcessor(id) }
    val newOnClick: () -> Unit = {
        multipleEventsCutter.processEvent(throttledTimeMillis) { onProcessed() }
    }
    return newOnClick
}

@Composable
fun debouncedProcess(
    debouncedTimeMillis: Long = DefaultDebouncingTime,
    id: String = remember { randomUUID().toString() },
    onProcessed: () -> Unit,
): () -> Unit {
    val scope = rememberCoroutineScope()
    val multipleEventsCutter = remember { EventProcessor.getDebouncingProcessor(id, scope) }
    val newOnClick: () -> Unit = {
        multipleEventsCutter.processEvent(timeMillis = debouncedTimeMillis) { onProcessed() }
    }
    return newOnClick
}

internal interface EventProcessor {
    fun processEvent(
        timeMillis: Long,
        event: () -> Unit,
    )

    companion object {
        val eventProcessorMap = mutableMapOf<String, EventProcessor>()
    }
}

private class ThrottlingProcessorImpl : EventProcessor {
    private val now: Long
        get() = System.currentTimeMillis()
    private var lastEventTimeMs: Long = 0

    override fun processEvent(
        timeMillis: Long,
        event: () -> Unit,
    ) {
        if (now - lastEventTimeMs >= timeMillis) {
            event.invoke()
            lastEventTimeMs = now
        }
    }
}

private class DebouncingProcessorImpl(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : EventProcessor {
    private var debounceJob: Job? = null

    override fun processEvent(
        timeMillis: Long,
        event: () -> Unit
    ) {
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(timeMillis)
            event.invoke()
        }
    }
}

internal fun EventProcessor.Companion.getThrottlingProcessor(id: String): EventProcessor =
    eventProcessorMap.getOrPut(id) {
        ThrottlingProcessorImpl()
    }

internal fun EventProcessor.Companion.getDebouncingProcessor(
    id: String,
    scope: CoroutineScope,
): EventProcessor =
    eventProcessorMap.getOrPut(id) {
        DebouncingProcessorImpl(scope = scope)
    }

private const val DefaultThrottlingTime = 1_000L
private const val DefaultDebouncingTime = 1_000L