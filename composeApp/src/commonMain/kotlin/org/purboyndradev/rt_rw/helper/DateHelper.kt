package org.purboyndradev.rt_rw.helper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DateHelper {
    companion object {
        private val customDateTimeFormatAmPm = LocalDateTime.Format {
            day(padding = Padding.ZERO)
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
            char(' ')
            amPmHour(padding = Padding.ZERO)
            char(':')
            minute(padding = Padding.ZERO)
            char(' ')
            amPmMarker("AM", "PM")
        }
        
        
        @OptIn(ExperimentalTime::class)
        fun convertEpochToDate(epochTime: Int): String {
            try {
                val epochMillis =
                    if (epochTime.toString().length <= 10) {
                        epochTime * 1000L
                    } else {
                        epochTime.toLong()
                    }
                
                val instant = Instant.fromEpochMilliseconds(epochMillis)
                val localDateTime =
                    instant.toLocalDateTime(TimeZone.currentSystemDefault())
                
                val formatted = localDateTime.format(customDateTimeFormatAmPm)
                return formatted
            } catch (e: Exception) {
                println("Error convertEpochToDate: $e")
                return ""
            }
        }
    }
}