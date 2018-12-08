package com.stlcbc.backend.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "okta", url="${auth.base-url}")
@Component
public interface OktaClient {
    @PostMapping(value = "api/v1/users?activate=true", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> createUser(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> body);
}
