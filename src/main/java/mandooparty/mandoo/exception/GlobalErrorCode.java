package mandooparty.mandoo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    //  Member
    // 400 BAD_REQUEST - 잘못된 요청
    NOT_VALID_EMAIL(BAD_REQUEST, "유효하지 않은 이메일 입니다."),
    PASSWORD_MISMATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_PASSWORD(BAD_REQUEST, "비밀번호가 동일합니다."),
    NOT_VALID_PASSWORD(BAD_REQUEST, "영문, 숫자, 특수문자를 포함한 8~20 글자를 입력해 주세요."),
    WRONG_EMAIL_FORM(BAD_REQUEST, "잘못된 이메일 형식입니다."),
    NOT_VALID_KEYWORD(BAD_REQUEST, "유효하지 않은 검색어 입니다."),
    INVALID_MEMBER_STATUS(BAD_REQUEST,"유효하지 않은 사용자 상태입니다.(관리자 또는 사용자로 선택해주세요)"),
    ALREADY_LOGGED_OUT(BAD_REQUEST, "이미 로그아웃 된 상태입니다."),
    // 401 Unauthorized - 권한 없음
    LOGIN_REQUIRED(UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    MEMBER_NOT_AUTHORIZED(UNAUTHORIZED, "접근 권한이 있는 회원이 아닙니다."),
    USER_NOT_AUTHORIZED(UNAUTHORIZED, "사용자가 권한이 없습니다."),

    // 404 Not Found - 찾을 수 없음
    EMAIL_NOT_FOUND(NOT_FOUND, "존재하지 않는 이메일 입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "등록된 사용자가 없습니다."),
    MEMBER_INFO_NOT_FOUND(NOT_FOUND, "등록된 사용자 정보가 없습니다."),

    POST_NOT_FOUND(NOT_FOUND, "등록된 게시물이 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND,"등록된 댓글이 없습니다"),

    // 409 CONFLICT : Resource 를 찾을 수 없음
    DUPLICATE_EMAIL(CONFLICT, "중복된 이메일이 존재합니다."),
    DUPLICATE_NICKNAME(CONFLICT, "중복된 닉네임이 존재합니다."),
    DUPLICATE_LIKES(CONFLICT,"중복된 좋아요가 존재합니다."),
    CATEGORY_NOT_FOUND(CONFLICT, "카테고리를 찾을 수 없음"),
    FILE_SAVE_ERROR(CONFLICT, "파일을 저장할 수 없음");



    private final HttpStatus httpStatus;
    private final String message;
}