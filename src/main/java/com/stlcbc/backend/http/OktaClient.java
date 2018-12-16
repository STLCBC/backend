package com.stlcbc.backend.http;

import com.stlcbc.backend.models.okta.OktaUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "okta", url="${auth.base-url}")
@Component
public interface OktaClient {
    @PostMapping(value = "api/v1/users?activate=true", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> createUser(@RequestHeader("Authorization") String token, @RequestBody OktaUser body);

    @PutMapping(value = "api/v1/groups/{groupId}/users/{userId}")
    ResponseEntity<?> addUserToGroup(@RequestHeader("Authorization") String token, @PathVariable("groupId") String group, @PathVariable("userId") String user);
}
