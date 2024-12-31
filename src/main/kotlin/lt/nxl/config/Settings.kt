package lt.nxl.config

import de.exlll.configlib.*
import lt.nxl.TempFly
import java.io.File
import java.nio.charset.StandardCharsets


@Configuration
class Settings {
    companion object {
        var instance: Settings? = null

        val CONFIG_HEADER: String = """
            # NXLTempFly configuration file
            # Settings file
            
            """.trimIndent()

        val PROPERTIES: YamlConfigurationProperties = YamlConfigurationProperties.newBuilder()
            .charset(StandardCharsets.UTF_8)
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .header(CONFIG_HEADER).build()

        fun reload() {
            instance = YamlConfigurations.update(
                File(TempFly.instance.dataFolder, "settings.yml").toPath(),
                Settings::class.java, PROPERTIES
            )
        }

        fun i(): Settings {
            if (instance == null)
                instance = YamlConfigurations.update(
                    File(TempFly.instance.dataFolder, "settings.yml").toPath(),
                    Settings::class.java, PROPERTIES
                )
            return instance ?: Settings()
        }
    }
    @Comment("Language of the plugin")
    var language: String = "en_US"

    var commands = Commands()

    @Configuration
    class Commands {
        @Comment("Command to toggle flight")
        var fly: String = "fly"
        @Comment("Command to give/remove/set flight time")
        var tempfly: String = "tf"
        @Comment("Command to reload the plugin")
        var reload: String = "tfreload"
    }

    var permissions = Permissions()
    @Configuration
    class Permissions{
        @Comment("Permission to use the command to add/remove/set flight time")
        var tempfly_admin: String = "tempfly.admin"
        @Comment("Permission to reload the plugin")
        var tempfly_reload: String = "tempfly.reload"
        @Comment("Permission to use the command to toggle flight")
        var toggle_flight: String = "tempfly.use"
        @Comment("Permission to use the command to toggle flight for other players")
        var toggle_flight_other: String = "tempfly.use.other"
    }
}