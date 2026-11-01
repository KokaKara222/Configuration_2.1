package depgraph

/** Имена параметров в XML-файле конфигурации. */
object ConfigKeys {
    const val PACKAGE_NAME = "package_name"
    const val REPOSITORY = "repository"
    const val MODE = "mode"
}

/** Режим работы приложения с репозиторием пакетов. */
enum class RepoMode(val xmlValue: String) {
    REMOTE("remote"),
    TEST("test");

    companion object {
        /** Возвращает режим по значению из XML или `null`, если такого режима нет. */
        fun fromXml(value: String): RepoMode? = entries.firstOrNull { it.xmlValue == value }
    }
}

/**
 * Параметры, настраиваемые пользователем.
 *
 * @property packageName имя анализируемого пакета
 * @property repository URL репозитория или путь к файлу тестового репозитория
 * @property mode режим работы с репозиторием
 */
data class Config(
    val packageName: String,
    val repository: String,
    val mode: RepoMode,
) {
    /** Возвращает параметры в виде упорядоченных пар «ключ — значение». */
    fun toKeyValue(): Map<String, String> = linkedMapOf(
        ConfigKeys.PACKAGE_NAME to packageName,
        ConfigKeys.REPOSITORY to repository,
        ConfigKeys.MODE to mode.xmlValue,
    )
}

/** Ошибка чтения или проверки конфигурации; сообщение предназначено пользователю. */
class ConfigException(message: String, cause: Throwable? = null) : Exception(message, cause)