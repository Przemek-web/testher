package pl.lodz.p.it.gornik.pomocnikseniora.exceptions;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.it.gornik.pomocnikseniora.dtos.response.EtagResponse;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request){
        UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleActivationCode(ActivationCodeException ex, WebRequest request){
        ActivationCodeResponse exceptionResponse = new ActivationCodeResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleEtag(EtagException ex, WebRequest request){
        EtagResponse exceptionResponse = new EtagResponse(ex.getMessage());
        System.out.println("Exception etag message: " + ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleOptimisticLock(CustomOptimisticLockException ex, WebRequest request){
        CustomOptimisticLockException exceptionResponse = new CustomOptimisticLockException(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request){
        NotFoundException exceptionResponse = new NotFoundException(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public final ResponseEntity<Object> notEnoughPointsException(NotEnoughPointsException ex, WebRequest request){
        NotEnoughPointsResponse exceptionResponse = new NotEnoughPointsResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
