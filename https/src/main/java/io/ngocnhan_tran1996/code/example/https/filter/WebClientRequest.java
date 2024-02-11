package io.ngocnhan_tran1996.code.example.https.filter;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebClientRequest {

  private HttpMethod method;
  private URI url;
  private HttpHeaders headers;
  private MultiValueMap<String, String> cookies;
  private Object body;

}