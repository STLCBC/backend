package com.stlcbc.backend.models.transfer;

import com.stlcbc.backend.models.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EventCheckIn {

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid event code")
    private String code;
    private Rating rating;
}
