package com.lee.crowdtracker.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDataResponseDto(
    @SerialName("SeoulRtd.citydata_ppltn")
    val cityDataList: List<CityDataDto>,

    @SerialName("RESULT")
    val result: ResultInfoDto
)

/**
 * 실시간 인구 데이터
 * **/
@Serializable
data class CityDataDto(
    @SerialName("AREA_NM") val areaName: String,
    @SerialName("AREA_CD") val areaCode: String,
    @SerialName("AREA_CONGEST_LVL") val congestionLevel: String,
    @SerialName("AREA_CONGEST_MSG") val congestionMessage: String,
    @SerialName("AREA_PPLTN_MIN") val populationMin: String,
    @SerialName("AREA_PPLTN_MAX") val populationMax: String,
    @SerialName("MALE_PPLTN_RATE") val malePopulationRate: String,
    @SerialName("FEMALE_PPLTN_RATE") val femalePopulationRate: String,
    @SerialName("PPLTN_RATE_0") val populationRate0: String,
    @SerialName("PPLTN_RATE_10") val populationRate10: String,
    @SerialName("PPLTN_RATE_20") val populationRate20: String,
    @SerialName("PPLTN_RATE_30") val populationRate30: String,
    @SerialName("PPLTN_RATE_40") val populationRate40: String,
    @SerialName("PPLTN_RATE_50") val populationRate50: String,
    @SerialName("PPLTN_RATE_60") val populationRate60: String,
    @SerialName("PPLTN_RATE_70") val populationRate70: String,
    @SerialName("RESNT_PPLTN_RATE") val residentPopulationRate: String,
    @SerialName("NON_RESNT_PPLTN_RATE") val nonResidentPopulationRate: String,
    @SerialName("REPLACE_YN") val replaceYn: String,
    @SerialName("PPLTN_TIME") val populationTime: String,
    @SerialName("FCST_YN") val forecastYn: String,
    @SerialName("FCST_PPLTN") val forecastPopulation: List<ForecastPopulationDto>
)

/**
 * 인구 예측 데이터
 * **/
@Serializable
data class ForecastPopulationDto(
    @SerialName("FCST_TIME") val forecastTime: String,
    @SerialName("FCST_CONGEST_LVL") val forecastCongestionLevel: String,
    @SerialName("FCST_PPLTN_MIN") val forecastPopulationMin: String,
    @SerialName("FCST_PPLTN_MAX") val forecastPopulationMax: String
)

@Serializable
data class ResultInfoDto(
    @SerialName("RESULT.CODE") val code: String,
    @SerialName("RESULT.MESSAGE") val message: String
)
