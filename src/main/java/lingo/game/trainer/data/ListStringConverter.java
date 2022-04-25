package lingo.game.trainer.data;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        List<String> cardStrings = new ArrayList<>(strings);
        return Strings.join(cardStrings, ';');
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        String[] stringsStrings = data.split(";");
        return new ArrayList<>(Arrays.asList(stringsStrings));
    }

}