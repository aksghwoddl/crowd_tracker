package com.lee.crowdtracker.core.domain.beach.mapper

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.domain.beach.model.AreaModel

internal fun CsvDownloadEntity.toArea(): AreaModel = AreaModel(
    name = name,
    category = category,
    no = no
)