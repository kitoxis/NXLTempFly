package lt.nxl.cmds

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import lt.nxl.funs.Color
import lt.nxl.funs.CombinedTime
import lt.nxl.funs.DeCombinedTime
import lt.nxl.TempFly
import lt.nxl.config.Settings
import lt.nxl.config.languages.LM
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Fly {
    companion object {
        fun load() {
            val arguments: MutableList<Argument<*>> = ArrayList<Argument<*>>()
            arguments.add(
                StringArgument("action").replaceSuggestions(
                    ArgumentSuggestions.strings<CommandSender>(
                        "add", "subtract", "set"
                    )
                )
            )
            val prefix = LM.i().getLocale().general.prefix
            CommandAPICommand(Settings.i().commands.tempfly)
                .withArguments(arguments)
                .withArguments(PlayerArgument("player"))
                .withArguments(GreedyStringArgument("time"))
                .executesPlayer(
                    PlayerCommandExecutor { player, args ->
                        if (!player.hasPermission(Settings.i().permissions.tempfly_admin)) {
                            var message = LM.i().getLocale().general.noPermission
                            message = message.replace("%prefix%", prefix)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        if (!(args.get("action") == "set" || args.get("action") == "add" || args.get("action") == "subtract")) {
                            var message = LM.i().getLocale().commands.tempFly.invalidAction
                            message = message.replace("%prefix%", prefix)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        if (args.get("time") == "0") {
                            var message = LM.i().getLocale().commands.tempFly.invalidTime
                            message = message.replace("%prefix%", prefix)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        if (args.get("player") == null) {
                            var message = LM.i().getLocale().commands.tempFly.invalidTarget
                            message = message.replace("%prefix%", prefix)
                            message = message.replace("%target%", args.get("player").toString())
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        val time = args.get("time") as String
                        val target = args.get("player") as Player
                        val action = args.get("action") as String
                        val parsedTime = try {
                            CombinedTime.parseCombinedTime(time)
                        } catch (e: IllegalArgumentException) {
                            var message = LM.i().getLocale().commands.tempFly.invalidTimeFormat
                            message = message.replace("%prefix%", prefix)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        val deParsedTime = DeCombinedTime.deparseCombinedTime(parsedTime)
                        val oldFlightTime = TempFly.getFlightTime(target)
                        if (action == "subtract" && oldFlightTime - parsedTime < 0) {
                            var message = LM.i().getLocale().commands.tempFly.notEnoughFlightTime
                            message = message.replace("%prefix%", prefix)
                            message = message.replace("%target%", target.name)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }

                        when (action) {
                            "add" -> TempFly.setFlightTime(target, parsedTime + oldFlightTime)
                            "subtract" -> TempFly.setFlightTime(target, oldFlightTime - parsedTime)
                            "set" -> TempFly.setFlightTime(target, parsedTime)
                        }
                        when (action) {
                            "add" -> {
                                var messageAddPlayer = LM.i().getLocale().commands.tempFly.added.player
                                var messageAddTarget = LM.i().getLocale().commands.tempFly.added.target
                                messageAddPlayer = messageAddPlayer.replace("%prefix%", prefix).replace("%target%", target.name).replace("%time%", deParsedTime).replace("%new_time%", DeCombinedTime.deparseCombinedTime(TempFly.getFlightTime(target)))
                                messageAddTarget = messageAddTarget.replace("%prefix%", prefix).replace("%time%", deParsedTime).replace("%new_time%", DeCombinedTime.deparseCombinedTime(TempFly.getFlightTime(target)))
                                player.sendMessage(Color.format(messageAddPlayer))
                                target.sendMessage(Color.format(messageAddTarget))
                            }
                            "subtract" -> {
                                var messageSubtractPlayer = LM.i().getLocale().commands.tempFly.subtracted.player
                                var messageSubtractTarget = LM.i().getLocale().commands.tempFly.subtracted.target
                                messageSubtractPlayer = messageSubtractPlayer.replace("%prefix%", prefix).replace("%target%", target.name).replace("%time%", deParsedTime).replace("%new_time%", DeCombinedTime.deparseCombinedTime(TempFly.getFlightTime(target)))
                                messageSubtractTarget = messageSubtractTarget.replace("%prefix%", prefix).replace("%time%", deParsedTime).replace("%new_time%", DeCombinedTime.deparseCombinedTime(TempFly.getFlightTime(target)))
                                player.sendMessage(Color.format(messageSubtractPlayer))
                                target.sendMessage(Color.format(messageSubtractTarget))
                            }
                            "set" -> {
                                var messageSetPlayer = LM.i().getLocale().commands.tempFly.set.player
                                var messageSetTarget = LM.i().getLocale().commands.tempFly.set.target
                                messageSetPlayer = messageSetPlayer.replace("%prefix%", prefix).replace("%target%", target.name).replace("%time%", deParsedTime)
                                messageSetTarget = messageSetTarget.replace("%prefix%", prefix).replace("%time%", deParsedTime)
                                player.sendMessage(Color.format(messageSetPlayer))
                                target.sendMessage(Color.format(messageSetTarget))
                            }
                        }
                    }
                )
                .register()
            CommandAPICommand(Settings.i().commands.fly)
                .withOptionalArguments(PlayerArgument("target"))
                .executesPlayer(
                    PlayerCommandExecutor { player, args ->
                        if (args.get("target") == null) {
                            if (!player.hasPermission(Settings.i().permissions.toggle_flight)) {
                                var message = LM.i().getLocale().general.noPermission
                                message = message.replace("%prefix%", prefix)
                                player.sendMessage(Color.format(message))
                                return@PlayerCommandExecutor
                            }
                            if (TempFly.getFlightTime(player) <= 0) {
                                var message = LM.i().getLocale().flight.noFlightTime
                                message = message.replace("%prefix%", prefix)
                                player.sendMessage(Color.format(message))
                                player.isFlying = false
                                player.allowFlight = false
                                return@PlayerCommandExecutor
                            }
                            player.allowFlight = !player.allowFlight
                            if (player.allowFlight) {
                                var message = LM.i().getLocale().commands.fly.canFly
                                message = message.replace("%prefix%", prefix)
                                player.sendMessage(Color.format(message))
                            } else {
                                var message = LM.i().getLocale().commands.fly.cannotFly
                                message = message.replace("%prefix%", prefix)
                                player.sendMessage(Color.format(message))
                            }
                            return@PlayerCommandExecutor
                        }
                        val target = args.get("target") as Player
                        if (!player.hasPermission(Settings.i().permissions.toggle_flight_other)) {
                            var message = LM.i().getLocale().general.noPermission
                            message = message.replace("%prefix%", prefix)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        if (TempFly.getFlightTime(target) <= 0) {
                            var message = LM.i().getLocale().flight.targetNoFlightTime
                            message = message.replace("%prefix%", prefix)
                            message = message.replace("%target%", target.name)
                            player.sendMessage(Color.format(message))
                            target.isFlying = false
                            target.allowFlight = false
                            return@PlayerCommandExecutor
                        }
                        target.allowFlight = !target.allowFlight
                        if (target.allowFlight) {
                            var message = LM.i().getLocale().commands.fly.targetCanFly
                            message = message.replace("%prefix%", prefix)
                            message = message.replace("%target%", target.name)
                            player.sendMessage(Color.format(message))
                            var messageTarget = LM.i().getLocale().commands.fly.canFly
                            messageTarget = messageTarget.replace("%prefix%", prefix)
                            target.sendMessage(Color.format(messageTarget))
                        } else {
                            var message = LM.i().getLocale().commands.fly.targetCannotFly
                            message = message.replace("%prefix%", prefix)
                            message = message.replace("%target%", target.name)
                            player.sendMessage(Color.format(message))
                            var messageTarget = LM.i().getLocale().commands.fly.cannotFly
                            messageTarget = messageTarget.replace("%prefix%", prefix)
                            target.sendMessage(Color.format(messageTarget))
                        }
                    }
                )
                .register()
            CommandAPICommand(Settings.i().commands.reload)
                .executesPlayer(
                    PlayerCommandExecutor { player, _ ->
                        if (!player.hasPermission(Settings.i().permissions.tempfly_reload)) {
                            var message = LM.i().getLocale().general.noPermission
                            message = message.replace("%prefix%", prefix)
                            player.sendMessage(Color.format(message))
                            return@PlayerCommandExecutor
                        }
                        var message = LM.i().getLocale().commands.reload.reloading
                        message = message.replace("%prefix%", prefix)
                        player.sendMessage(Color.format(message))

                        val startTime = System.currentTimeMillis()

                        Settings.reload()
                        LM.i().getAll()

                        val endTime = System.currentTimeMillis()
                        val duration = endTime - startTime

                        var messageReload = LM.i().getLocale().commands.reload.reloaded
                        messageReload = messageReload.replace("%prefix%", prefix)
                        messageReload = messageReload.replace("%duration%", duration.toString())
                        player.sendMessage(Color.format(messageReload))
                    }
                )
                .register()
        }
    }
}