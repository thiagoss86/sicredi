package br.com.sicredi.exeptions.handler;

import br.com.sicredi.exeptions.MessageError;
import br.com.sicredi.exeptions.MessageError.ApiError;
import br.com.sicredi.exeptions.NotFoundException;
import br.com.sicredi.exeptions.UnprocessableEntityException;
import br.com.sicredi.interfaces.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation.ConstraintLocationKind;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNullElse;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageError messageError;

    private static final String DETAIL = "%s - Detail: %s";

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected List<ApiError> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {

        var errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    var constraintDescriptor = (ConstraintDescriptorImpl) violation.getConstraintDescriptor();
                    var propertyPath = (PathImpl) violation.getPropertyPath();
                    var message = violation.getMessage();
                    var value = requireNonNullElse(violation.getInvalidValue(), "").toString();

                    if (ConstraintLocationKind.PARAMETER.equals(
                            constraintDescriptor.getConstraintLocationKind())) {
                        return messageError.create(Messages.INVALID_PARAM, propertyPath.getLeafNode().getName(),
                                value, message);
                    }

                    return messageError.create(Messages.INVALID_FIELD, propertyPath.asString(), value, message);
                })
                .toList();

        log.error(format(DETAIL, errors, ex.getMessage()), ex);
        return errors;
    }

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
}
