package br.com.sicredi.exeptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isAllBlank;

@Component
@RequiredArgsConstructor
public class MessageError {

    private final MessageSource messageSource;

    public ApiError create(String code, String... replacements) {
        return new ApiError(code, messageSource.getMessage(code, replacements,
                LocaleContextHolder.getLocale()));
    }


    public record ApiError(String code, String description) {
        @JsonIgnore
        public boolean isPropertiesBlank() {
            return isAllBlank(code, description);
        }
    }
}
