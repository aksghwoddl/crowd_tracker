package com.lee.crowdtracker.libray.navermap

import android.content.Context
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NaverMapSdkController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun init() {
        NaverMapSdk.getInstance(context).client =
            NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_CLIENT_ID)
    }
}