

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class ApiError {

   private HttpStatus status;
   private String message;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
//   @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//   @JsonSerialize(using = LocalDateTimeSerializer.class)
   private LocalDateTime timestamp;
   

   public ApiError() {
	super();
	}

	ApiError(HttpStatus status) {
		   timestamp = LocalDateTime.now();
	       this.status = status;
	   }

   ApiError(HttpStatus status, Throwable ex) {
	   timestamp = LocalDateTime.now();
       this.status = status;
       this.message = ex.getMessage();
   }

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	

}