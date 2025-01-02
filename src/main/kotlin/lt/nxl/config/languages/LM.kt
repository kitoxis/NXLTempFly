package lt.nxl.config.languages

import de.exlll.configlib.YamlConfigurations
import lt.nxl.TempFly
import lt.nxl.config.Settings
import lt.nxl.config.Settings.Companion.PROPERTIES
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class LM {
    companion object {
        var instance: LM? = null

        val FOLDER: File = File(TempFly.instance.dataFolder, "languages")
        val DEFAULT: String = "en_US"

        fun i(): LM {
            if (instance == null) {
                instance = LM()
            }
            return instance as LM
        }
    }

    private val locales: MutableMap<String, Locale> = ConcurrentHashMap()
    private lateinit var enfile: File
    private lateinit var ltfile: File

    init {
        enfile = File(FOLDER, "en_US.yml")
        ltfile = File(FOLDER, "lt_LT.yml")
        if (!FOLDER.exists()) FOLDER.mkdir()
        loadDefault()
        loadLocales()
    }

    fun getDefault(): Locale {
        return i().getOverride(DEFAULT)
            .orElseThrow { NullPointerException("Who removed the english locale?") }
    }

    fun loadLocales() {
        val files: Array<String> = Objects.requireNonNull(FOLDER.list())
        for (fileNames in files) {
            val name = fileNames.replace(".yml", "")
            val locale = Locale.get(name)
            locales[name] = locale
        }
    }
    fun reloadLocales() {
        val files: Array<String> = Objects.requireNonNull(FOLDER.list())
        for (fileNames in files) {
            YamlConfigurations.load(
                File(FOLDER, fileNames).toPath(),
                Locale::class.java, PROPERTIES
            )
        }
    }

    private fun loadDefault() {
        if (!enfile.exists()) {
            TempFly.instance.saveResource("languages/en_US.yml", false);
        }
        if (!ltfile.exists()) {
            TempFly.instance.saveResource("languages/lt_LT.yml", false);
        }
        Locale.get(DEFAULT)
        Locale.get("lt_LT")
    }
    fun getLocale(): Locale {
        val locale = locales[fix(Settings.i().language)]
            ?: return getOverride(DEFAULT).orElseThrow { NullPointerException("Who removed the english locale?") }
        return locale
    }

    fun getOverride(localeName: String): Optional<Locale> {
        return Optional.ofNullable(locales[localeName])
    }

    fun getAll(): List<Locale> {
        return ArrayList(locales.values)
    }

    fun fix(localeName: String): String {
        val strings = localeName.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return strings[0] + "_" + strings[1].uppercase(java.util.Locale.getDefault())
    }
}