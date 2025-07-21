package com.lee.crowdtracker.core.domain.beach.mapper

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.domain.beach.model.Area

internal fun CsvDownloadEntity.toArea(): Area = Area(
    name = name,
    category = category,
    no = no
)