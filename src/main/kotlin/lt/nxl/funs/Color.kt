package lt.nxl.funs

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class Color {
    companion object {
        fun format (text: String): Component {
            return MiniMessage.miniMessage().deserialize(text)
        }
    }
}