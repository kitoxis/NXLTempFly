package lt.nxl.config.languages

import de.exlll.configlib.Configuration
import de.exlll.configlib.YamlConfigurationProperties
import de.exlll.configlib.YamlConfigurations
import lt.nxl.TempFly
import lt.nxl.config.languages.LM.Companion.instance
import java.io.File
import java.nio.charset.StandardCharsets


@Configuration
class Locale {
    var general: General = General()
    var commands: Commands = Commands()
    var flight: Flight = Flight()
    var time: Time = Time()
    @Configuration
    class General {
        var prefix: String = "<gray>[<aqua><b>TempFly</b><gray>]</gray>"
        var noPermission: String = "%prefix% <red>You do not have permission to run this command!"
    }
    @Configuration
    class Time {
        var days: String = "days"
        var day: String = "day"
        var hours: String = "hours"
        var hour: String = "hour"
        var minutes: String = "minutes"
        var minute: String = "minute"
        var seconds: String = "seconds"
        var second: String = "second"
    }

    @Configuration
    class Commands {
        var tempFly: TempFly = TempFly()
        var fly: Fly = Fly()
        var reload: Reload = Reload()
        @Configuration
        class TempFly {
            var invalidAction: String = "%prefix% <red>Invalid action! Actions: add, subtract, set"
            var invalidTime: String = "%prefix% <red>Invalid time!"
            var invalidTimeFormat: String = "%prefix% <red>Invalid time format!"
            var invalidTarget: String = "%prefix% <red>%target% is either invalid or offline!"
            var notEnoughFlightTime: String = "%prefix% <red>%target% does not have enough flight time!"
            var added: Added = Added()
            var subtracted: Subtracted = Subtracted()
            var set: Set = Set()
            @Configuration
            class Added {
                var player: String = "%prefix% <green>Added %time% to %target%'s flight time! <gray>(%target%'s current flight time: %new_time%)"
                var target: String = "%prefix% <green>%time% was added to your flight time! <gray>(Your current flight time: %new_time%)"
            }
            @Configuration
            class Subtracted {
                var player: String = "%prefix% <green>Subtracted %time% from %target%'s flight time! <gray>(%target%'s current flight time: %new_time%)"
                var target: String = "%prefix% <green>%time% was subtracted from your flight time! <gray>(Your current flight time: %new_time%)"
            }
            @Configuration
            class Set {
                var player: String = "%prefix% <green>Set %target%'s flight time to %time%!"
                var target: String = "%prefix% <green>Your flight time was set to %time%!"
            }
        }
        @Configuration
        class Fly {
            var canFly: String = "%prefix% <green>You can fly now!"
            var cannotFly: String = "%prefix% <red>You can no longer fly!"
            var targetCanFly: String = "%prefix% <green>%target% can fly now!"
            var targetCannotFly: String = "%prefix% <red>%target% can no longer fly!"
        }
        @Configuration
        class Reload {
            var reloading: String = "%prefix% <yellow>Reloading settings and messages..."
            var reloaded: String = "%prefix% <green>Reload completed in %duration% ms."
        }
    }
    @Configuration
    class Flight {
        var flightTime: String = "<aqua>Flight time: <b>%time%</b>"
        var noMoreFlightTime: String = "%prefix% <red><b>You have run out of flight time!</b>"
        var noFlightTime: String = "%prefix% <red>You do not have any flight time!"
        var targetNoFlightTime: String = "%prefix% <red>%target% does not have any flight time!"
    }

    companion object {
        private val CONFIG_HEADER = """
            NXL Temporary Flight Language File
            
            Language: English (US)
            YOU CAN USE MINIMESSAGE FORMATTING HERE. BUT ONLY MINIMESSAGE FORMATTING.
            """.trimIndent()

        private val PROPERTIES: YamlConfigurationProperties = YamlConfigurationProperties.newBuilder()
            .charset(StandardCharsets.UTF_8)
            .header(CONFIG_HEADER).build()

        fun get(localeCode: String): Locale {
            val file = File(LM.FOLDER, "$localeCode.yml").toPath()
            return if (file.toFile().exists()) {
                YamlConfigurations.load(file, Locale::class.java, PROPERTIES)
            } else {
                YamlConfigurations.update(file, Locale::class.java, PROPERTIES)
            }
        }
    }
}