package karmanchik.chtotib.restservice.parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Общий класс для файловых обработчиков
 */
public abstract class BaseParser {

    /**
     * Преобразует входной файл в коллекцию строк
     * @param file Массив байт файла
     * @return Коллекция строк
     * @throws IOException
     * @throws InvalidFormatException
     */
    public abstract List<List<String>> parse(byte[] file) throws IOException, InvalidFormatException;

    /**
     * Преобразует входной файл в коллекцию [ключ -> значение]
     * @param file Массив байт файла
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public abstract List<List<Map<String, Object>>> parseToListMap(byte[] file) throws IOException, InvalidFormatException;
}
