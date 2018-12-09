package com.stlcbc.backend.models.okta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OktaUser {

    private OktaProfile profile;
    private OktaCredentials credentials;
}
