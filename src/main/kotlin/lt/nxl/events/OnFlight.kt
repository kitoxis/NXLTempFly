package lt.nxl.events

import lt.nxl.funs.Color
import lt.nxl.funs.DeCombinedTime
import lt.nxl.TempFly
import lt.nxl.config.languages.LM
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.scheduler.BukkitRunnable

class OnFlight : Listener {
    @EventHandler
    fun onFlight(event: PlayerToggleFlightEvent) {
        val player = event.player

        if (!player.isFlying) {
            object : BukkitRunnable() {
                override fun run() {
                    if (!player.isFlying) {
                        cancel()
                        return
                    }
                    if (TempFly.getFlightTime(player) <= 0) {
                        var message = LM.i().getLocale().flight.noMoreFlightTime
                        message = message.replace("%prefix%", LM.i().getLocale().general.prefix)
                        player.sendActionBar(Color.format(message))
                        player.isFlying = false
                        player.allowFlight = false
                        cancel()
                        return
                    }
                    var message = LM.i().getLocale().flight.flightTime
                    message = message.replace("%time%", DeCombinedTime.deparseCombinedTime(TempFly.getFlightTime(player)))
                    player.sendActionBar(Color.format(message))
                    TempFly.setFlightTime(player, TempFly.getFlightTime(player) - 1)
                }
            }.runTaskTimer(TempFly.getPluginInstance(), 0L, 20L)
        } else {
            return
        }
    }
}