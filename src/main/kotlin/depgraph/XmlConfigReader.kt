package depgraph

import org.w3c.dom.Element
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException
import java.io.File
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

private const val ROOT_ELEMENT = "config"

/** Читает XML-файл конфигурации и возвращает «сырые» значения параметров. */
object XmlConfigReader {

    /**
     * Разбирает [file] и возвращает значения параметров по их именам.
     *
     * @throws ConfigException если XML некорректен или корневой элемент неверный
     */
    fun read(file: File): Map<String, String> {
        val root = parseRoot(file)
        if (root.tagName != ROOT_ELEMENT) {
            throw ConfigException(
                "Корневой элемент должен называться <$ROOT_ELEMENT>, найден <${root.tagName}>"
            )
        }
        return collectParameters(root)
    }

    private fun parseRoot(file: File): Element = try {
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).documentElement
    } catch (e: SAXParseException) {
        throw ConfigException(
            "Некорректный XML (строка ${e.lineNumber}, столбец ${e.columnNumber}): ${e.message}",
            e,
        )
    } catch (e: SAXException) {
        throw ConfigException("Ошибка разбора XML: ${e.message}", e)
    } catch (e: IOException) {
        throw ConfigException("Ошибка чтения файла конфигурации: ${e.message}", e)
    } catch (e: ParserConfigurationException) {
        throw ConfigException("Ошибка инициализации XML-парсера: ${e.message}", e)
    }

    private fun collectParameters(root: Element): Map<String, String> {
        val values = linkedMapOf<String, String>()
        val children = root.childNodes
        for (index in 0 until children.length) {
            val element = children.item(index) as? Element ?: continue
            values[element.tagName] = element.textContent.trim()
        }
        return values
    }
}