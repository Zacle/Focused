package com.zn.apps.domain.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun OffsetDateTime.formatToString(): String = this.format(formatter)