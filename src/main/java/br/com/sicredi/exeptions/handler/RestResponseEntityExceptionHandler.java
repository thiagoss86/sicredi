package br.com.sicredi.exeptions.handler;

import br.com.sicredi.exeptions.MessageError;
import br.com.sicredi.exeptions.MessageError.ApiError;
import br.com.sicredi.exeptions.NotFoundException;
import br.com.sicredi.exeptions.UnprocessableEntityException;
import br.com.sicredi.interfaces.Messages;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String INVALID_TYPE_FIELD = "Tipo invalido para o campo '%s' - Tipo esperado '%s'";
    public static final String FIELD_MESSAGE_PATTERN = "[{0}]";
    private final MessageError messageError;

    private static final String DETAIL = "%s - Detail: %s";

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected List<ApiError> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        var apiError = messageError.create(ex.getErrorCode().getCode());
        log.error(apiError.description(), ex);
        return List.of(apiError);
    }

    @ExceptionHandler(value = UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected List<ApiError> handlerUnprocessableEntityException(HttpServletRequest request, UnprocessableEntityException ex) {
        var apiError = messageError.create(ex.getErrorCode().getCode());
        log.error(apiError.description(), ex);
        return List.of(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<ApiError> errors = new ArrayList<>();

        if (Objects.isNull(ex.getCause())) {
            errors.add(messageError.create(Messages.REQUIRED_REQUEST_BODY));
        } else if (ex.getCause() instanceof InvalidFormatException ife) {
            createInvalidFormatResponseBody(errors, ife);
        } else if (ex.getCause() instanceof MismatchedInputException mie) {
            errors.add(messageError.create(Messages.FIELD_VALIDATION,
                    format(INVALID_TYPE_FIELD,
                            mie.getPath().stream()
                                    .map(p -> nonNull(p.getFieldName()) ? p.getFieldName() : MessageFormat.format(FIELD_MESSAGE_PATTERN, p.getIndex())).collect(joining()),
                            mie.getTargetType().getSimpleName())));
        } else if(ex.getCause() instanceof JsonParseException jpe) {
            errors.add(messageError.create(Messages.JSON_INVALID_FORMAT, jpe.getOriginalMessage()));
        }

        return ResponseEntity.status(status).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        var errors = new ArrayList<>(ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> messageError.create(Messages.FIELD_VALIDATION,
                        format("O campo '%s' %s", fieldError.getField(), fieldError.getDefaultMessage())))
                .toList());

        if (errors.isEmpty()) {
            errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
        }

        log.error(format(DETAIL, errors, ExceptionUtils.getRootCause(ex)), ex);
        return ResponseEntity.status(status).body(errors);
    }

    private void createInvalidFormatResponseBody(List<ApiError> errors, InvalidFormatException ife) {
        if (Objects.isNull(ife.getTargetType())) {
            errors.add(messageError.create(Messages.CONTACT_SYSTEM_ADMIN));
            return;
        }

        if (Objects.nonNull(ife.getTargetType().getEnumConstants())) {
            List<String> avaliableValues = getEnumValues(ife.getTargetType());
            String path = ife.getPath().stream()
                    .map(p -> nonNull(p.getFieldName()) ? p.getFieldName() : MessageFormat.format(FIELD_MESSAGE_PATTERN, p.getIndex()))
                    .collect(joining());
            String message = format("Valor invalido para o campo '%s' - Valores disponiveis '%s.'", path, avaliableValues);
            errors.add(messageError.create(Messages.FIELD_VALIDATION, message));
        } else {
            Class<?> targetType = ife.getTargetType().getSuperclass().equals(Object.class)
                    ? ife.getTargetType()
                    : ife.getTargetType().getSuperclass();

            String path = ife.getPath().stream()
                    .map(p -> nonNull(p.getFieldName()) ? p.getFieldName() : MessageFormat.format(FIELD_MESSAGE_PATTERN, p.getIndex()))
                    .collect(joining());
            String message = format(INVALID_TYPE_FIELD, path, targetType.getSimpleName());
            errors.add(messageError.create(Messages.FIELD_VALIDATION, message));
        }

    }

    private List<String> getEnumValues(Class<?> enumClass) {
        var enumConstants = Stream.of(enumClass.getEnumConstants())
                .map(Object::toString)
                .toList();

        return Stream.of(enumClass.getDeclaredFields())
                .filter(field -> enumConstants.contains(field.getName()))
                .map(field -> nonNull(field.getAnnotation(JsonProperty.class))
                        ? field.getAnnotation(JsonProperty.class).value()
                        : field.getName()
                ).toList();
    }
}
