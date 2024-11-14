package mandooparty.mandoo.apiPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import mandooparty.mandoo.exception.GlobalErrorCode;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {
    @JsonProperty("isSuccess") //해당 api가 성공인지 아닌지 알려주는 필드
    private final Boolean isSuccess;

   // @JsonProperty("code") //404, 200 같은 거
    private final String code;

    //@JsonProperty("message") //code에 추가적으로 메세지를 통해 상태를 알림
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) //실제로 프론트에게 필요한 데이터를 담음
    private T result;


    // 성공한 경우 응답 생성
    public static <T> ApiResponse<T> onSuccess(T data){
        return new ApiResponse<>(true, "200" , "요청에 성공하였습니다.", data);
    }

    // 실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(GlobalErrorCode code, T data){
        return new ApiResponse<>(false, String.valueOf(code.getHttpStatus().value()), code.getMessage(), data);
    }
}
