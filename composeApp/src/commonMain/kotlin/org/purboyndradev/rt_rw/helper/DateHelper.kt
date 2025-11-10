package org.purboyndradev.rt_rw.helper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
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
        fun formatIsoToDate(
            iso: String,
            tz: TimeZone = TimeZone.currentSystemDefault()
        ): String {
            val instant = Instant.parse(iso)
            val localDateTime = instant.toLocalDateTime(tz)
            val formatted = localDateTime.format(customDateTimeFormatAmPm)
            return formatted
        }

        @OptIn(ExperimentalTime::class)
        fun formatIsoToTimeAgo(iso: String): String {
            try {
                val pastInstant = Instant.parse(iso)
                val now = Clock.System.now()
                val duration = now - pastInstant

                val date = formatIsoToDate(iso)

                return when {
                    duration.inWholeSeconds < 60 -> "just now"
                    duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes} minutes ago"
                    duration.inWholeHours < 24 -> "${duration.inWholeHours} hours ago"
                    duration.inWholeDays < 7 -> "${duration.inWholeDays} days ago"
                    else -> date
//                    duration.inWholeDays < 365 -> "${duration.inWholeDays / 7} weeks ago"
//                    duration.inWholeDays < 365 * 2 -> "${duration.inWholeDays / 30} months ago"
//                    else -> "${duration.inWholeDays / 365} years ago"
                }

            } catch (e: Exception) {
                println("Error formatIsoToTimeAgo: $e")
                return "-"
            }
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