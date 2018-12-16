package com.stlcbc.backend.models.okta;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @RequiredArgsConstructor
public class OktaProfile {

    private @NonNull String firstName;
    private @NonNull String lastName;
    private @NonNull String email;
    private @NonNull String login;
    private Boolean admin = false;
}
