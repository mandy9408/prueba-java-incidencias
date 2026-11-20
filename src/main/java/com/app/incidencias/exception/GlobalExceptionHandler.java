package com.app.incidencias.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.ErrorResponse;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> validarDatos(
                        MethodArgumentNotValidException exception) {

                String mensaje = exception.getBindingResult()
                                .getFieldErrors()
                                .get(0)
                                .getDefaultMessage();

                return ResponseEntity.badRequest()
                                .body(new ApiError(400, mensaje));
        }

        @ExceptionHandler(UsuarioNoExisteException.class)
        public ResponseEntity<ApiError> usuarioNoExiste(
                        UsuarioNoExisteException exception) {

                return ResponseEntity.badRequest()
                                .body(new ApiError(400, exception.getMessage()));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiError> jsonIncorrecto() {

                return ResponseEntity.badRequest()
                                .body(new ApiError(400,
                                                "El JSON está mal formado o contiene valores no válidos"));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiError> parametroIncorrecto() {

                return ResponseEntity.badRequest()
                                .body(new ApiError(400,
                                                "Un parámetro de la URL no es válido"));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> errorInesperado(Exception exception) {

                if (exception instanceof ErrorResponse error) {
                        int status = error.getStatusCode().value();

                        HttpStatus httpStatus = HttpStatus.resolve(status);

                        String mensaje = httpStatus != null
                                        ? httpStatus.getReasonPhrase()
                                        : "Error en la petición";

                        return ResponseEntity.status(status)
                                        .body(new ApiError(status, mensaje));
                }

                return ResponseEntity.internalServerError()
                                .body(new ApiError(500, "Error interno del servidor"));
        }

        @ExceptionHandler(IncidenciaNoExisteException.class)
        public ResponseEntity<ApiError> incidenciaNoExiste(
                        IncidenciaNoExisteException exception) {

                return ResponseEntity.status(404)
                                .body(new ApiError(404, exception.getMessage()));
        }

        @ExceptionHandler(IncidenciaCerradaException.class)
        public ResponseEntity<ApiError> incidenciaCerrada(
                        IncidenciaCerradaException exception) {

                return ResponseEntity.status(409)
                                .body(new ApiError(409, exception.getMessage()));
        }
}