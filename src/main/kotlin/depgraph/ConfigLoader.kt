package depgraph

import java.io.File

/** Загружает и проверяет конфигурацию приложения из XML-файла. */
object ConfigLoader {

    /**
     * Читает конфигурацию из файла [path].
     *
     * @throws ConfigException если файл недоступен или значения параметров неверны
     */
    fun load(path: String): Config {
        ParameterValidator.readableFile(path, "конфигурации")
        val values = XmlConfigReader.read(File(path))
        val mode = ParameterValidator.mode(required(values, ConfigKeys.MODE))
        val packageName = ParameterValidator.packageName(
            required(values, ConfigKeys.PACKAGE_NAME),
            mode,
        )
        val repository = ParameterValidator.repository(
            required(values, ConfigKeys.REPOSITORY),
            mode,
        )
        return Config(packageName, repository, mode)
    }

    private fun required(values: Map<String, String>, key: String): String {
        val value = values[key] ?: throw ConfigException("Не задан обязательный параметр <$key>")
        if (value.isEmpty()) throw ConfigException("Параметр <$key> не может быть пустым")
        return value
    }
}