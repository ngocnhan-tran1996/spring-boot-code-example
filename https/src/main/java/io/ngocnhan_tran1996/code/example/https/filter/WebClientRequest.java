package io.ngocnhan_tran1996.code.example.https.filter;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
public class WebClientRequest {

    private HttpMethod method;
    private URI url;
    private HttpHeaders headers;
    private MultiValueMap<String, String> cookies;
    private Object body;

}