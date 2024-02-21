package io.ngocnhan_tran1996.code.example.https.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
public class WebClientResponse {

    private Long executionTimeMilis;
    private HttpStatusCode statusCode;
    private HttpHeaders headers;
    private MultiValueMap<String, ResponseCookie> cookies;
    private Object body;

}