package karmanchik.chtotib.restservice.parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class BaseParser {
    public abstract List<List<String>> parse(byte[] file) throws IOException, InvalidFormatException;

    public abstract List<List<Map<String, Object>>> parseToListMap(byte[] file) throws IOException, InvalidFormatException;
}
