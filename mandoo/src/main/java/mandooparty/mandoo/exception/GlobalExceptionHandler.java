package mandooparty.mandoo.exception;
import lombok.extern.slf4j.Slf4j;
import mandooparty.mandoo.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {  @ExceptionHandler(value = { GlobalException.class})
protected ApiResponse handleCustomException(GlobalException e) {
    log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
    return ApiResponse.onFailure(e.getErrorCode(), "");

}

}
