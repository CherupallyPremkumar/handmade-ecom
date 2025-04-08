package com.handmade.ecommerce;

import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.InterceptedBy;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@ChenileController(
        value = "handmadeService",
        serviceName = "searchService"
)
public class ReadQueryController extends ControllerSupport {
    public ReadQueryController(){
    }

    @PostMapping({"/q1/{queryName}"})
    @InterceptedBy("securityInterceptor")
    public ResponseEntity<GenericResponse<SearchResponse>> search(HttpServletRequest request, @PathVariable String queryName,
                                                                  @RequestBody SearchRequest<Map<String, Object>> searchRequest) {
        return this.process("search", request, new Object[]{queryName, searchRequest});
    }
}
