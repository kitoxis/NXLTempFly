package lt.nxl.funs

class CombinedTime {
    companion object {
        fun parseCombinedTime(input: String): Long {
            val regex = "(\\d+d(?:ay(?:s)?)?)?(\\d+h(?:our(?:s)?)?)?(\\d+m(?:in(?:ute(?:s)?)?)?)?(\\d+s(?:ec(?:ond(?:s)?)?)?)?".toRegex()
            val matchResult = regex.matchEntire(input) ?: throw IllegalArgumentException("Invalid time format")

            var totalSeconds = 0L
            matchResult.groupValues.drop(1).forEach { group ->
                if (group.isNotEmpty()) {
                    val value = group.takeWhile { it.isDigit() }.toLong()
                    when {
                        group.endsWith("d") || group.endsWith("day") || group.endsWith("days") -> totalSeconds += value * 86400
                        group.endsWith("h") || group.endsWith("hour") || group.endsWith("hours") -> totalSeconds += value * 3600
                        group.endsWith("m") || group.endsWith("min") || group.endsWith("minute") || group.endsWith("minutes") -> totalSeconds += value * 60
                        group.endsWith("s") || group.endsWith("sec") || group.endsWith("second") || group.endsWith("seconds") -> totalSeconds += value
                    }
                }
            }
            return totalSeconds
        }
    }
}