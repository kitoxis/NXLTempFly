package lt.nxl

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import lt.nxl.cmds.Fly
import lt.nxl.config.Settings
import lt.nxl.config.languages.LM
import lt.nxl.events.OnFlight
import lt.nxl.funs.Color
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin


class TempFly : JavaPlugin() {
    companion object {
        lateinit var instance: TempFly

        fun log(message: String) {
            Bukkit.getConsoleSender().sendMessage(Color.format(message))
        }

        fun getPluginInstance(): TempFly {
            return instance
        }

        fun setFlightTime(player: Player, time: Long) {
            val namespacedKey = NamespacedKey(instance, "tempfly.time.${player.uniqueId}")
            player.persistentDataContainer.set(namespacedKey, PersistentDataType.LONG, time)
        }
        fun getFlightTime(player: Player): Long {
            val namespacedKey = NamespacedKey(instance, "tempfly.time.${player.uniqueId}")
            return player.persistentDataContainer.get(namespacedKey, PersistentDataType.LONG) ?: 0
        }
    }

    override fun onEnable() {
        instance = this
        log("<gradient:blue:green>NXLTempFly</gradient> <gray>|</gray> <green>Enabling...")
        CommandAPI.onLoad(CommandAPIBukkitConfig(instance))
        CommandAPI.onEnable()
        this.server.pluginManager.registerEvents(OnFlight(), this)
        Fly.load()
        LM.i()
        log("<gradient:blue:green>NXLTempFly</gradient> <gray>|</gray> <green>Enabled!")
    }

    override fun onDisable() {
        log("<gradient:blue:green>NXLTempFly</gradient> <gray>|</gray> <red>Shutting down...")
    }
}