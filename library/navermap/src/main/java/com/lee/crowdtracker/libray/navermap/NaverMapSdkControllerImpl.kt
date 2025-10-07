package com.lee.crowdtracker.libray.navermap

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.lee.crowdtracker.library.base.exts.runSuspendCatching
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

private const val TAG = "NaverMapSdkController"

class NaverMapSdkControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NaverMapSdkController {
    override fun init() {
        NaverMapSdk.getInstance(context).client =
            NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_CLIENT_ID)
    }

    override suspend fun getLatLngByName(
        name: String,
        onCancellation: (Throwable) -> Unit,
    ): LatLng = withContext(Dispatchers.IO) {
        if (Geocoder.isPresent().not()) {
            return@withContext LatLng.INVALID
        }

        val geocoder = Geocoder(context, Locale.KOREA)

        suspendCancellableCoroutine { continuation ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(name, 1) {
                    val latLng = it.firstOrNull()?.let { address ->
                        LatLng(address.latitude, address.longitude)
                    } ?: LatLng.INVALID

                    if (continuation.isActive) {
                        continuation.resume(latLng) { cause, _, _ ->
                            onCancellation(cause)
                        }
                    }
                }
            } else {
                runSuspendCatching {
                    geocoder.getFromLocationName(name, 1)
                }.fold(
                    onSuccess = {
                        val latLng = it?.firstOrNull()?.let {
                            LatLng(it.latitude, it.longitude)
                        } ?: LatLng.INVALID

                        continuation.resume(latLng) { cause, _, _ ->
                            onCancellation(cause)
                        }
                    },
                    onFailure = {
                        Log.e(TAG, "getLatLngByName: fail get LatLng", it)
                        if (continuation.isActive) {
                            continuation.resume(LatLng.INVALID) { cause, _, _ ->
                                onCancellation(cause)
                            }
                        }
                    }
                )
            }
        }
    }
}
