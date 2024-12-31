package lt.nxl.funs

import lt.nxl.config.languages.LM


class DeCombinedTime {
    companion object {
        fun deparseCombinedTime(seconds: Long): String {
            var remainingSeconds = seconds
            val days = remainingSeconds / 86400
            remainingSeconds %= 86400
            val hours = remainingSeconds / 3600
            remainingSeconds %= 3600
            val minutes = remainingSeconds / 60
            remainingSeconds %= 60

            val result = StringBuilder()
            if (days > 0) result.append("$days ${if (days > 1) LM.i().getLocale().time.days else LM.i().getLocale().time.day} ")
            if (hours > 0) result.append("$hours ${if (hours > 1) LM.i().getLocale().time.hours else LM.i().getLocale().time.hour} ")
            if (minutes > 0) result.append("$minutes ${if (minutes > 1) LM.i().getLocale().time.minutes else LM.i().getLocale().time.minute} ")
            if (remainingSeconds > 0) result.append("$remainingSeconds ${if (remainingSeconds > 1) LM.i().getLocale().time.seconds else LM.i().getLocale().time.second}")

            return result.toString().trim()
        }
    }
}