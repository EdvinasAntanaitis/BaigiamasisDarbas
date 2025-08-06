package lt.code.samples.maven.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageService {

    private final MessageSource messageSource;

    public String getTranslatedMessage(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            if (key != null && !key.equals("")) {
                log.atError().log("Translation key not found: {}", key);

                return String.format("?%s?", key);
            }
        }

        return "";
    }
}