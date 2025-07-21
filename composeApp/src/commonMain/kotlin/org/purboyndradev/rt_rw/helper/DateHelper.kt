package org.purboyndradev.rt_rw.helper

import kotlinx.datetime.*
import kotlin.time.ExperimentalTime
import kotlinx.datetime.format.*
import kotlin.time.Instant

class DateHelper {
    companion object {
        private val dateTimeFormat = LocalDateTime.Format {
            year()
            char('-')
            monthNumber(padding = Padding.ZERO)
            char('-')
            dayOfMonth(padding = Padding.ZERO)
            char(' ')
            hour(padding = Padding.ZERO)
            char(':')
            minute(padding = Padding.ZERO)
            char(':')
            second(padding = Padding.ZERO)
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
                
                val formatted = localDateTime.format(dateTimeFormat)
                return formatted
            } catch (e: Exception) {
                println("Error convertEpochToDate: $e")
                return ""
            }
        }
    }
}