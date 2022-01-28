

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentNotFoundException.class)
	protected ResponseEntity<ApiError> handleIllegalArgumentNotFoundException(final Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
		return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }
	
    @ExceptionHandler(IllegalArgumentBadRequestException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentBadRequestException(final Exception ex) {
    	ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
    	return new ResponseEntity<ApiError>(apiError, apiError.getStatus());
    }
}
