package com.epam.esm.auth.models;

import com.epam.esm.auth.tokenjwt.model.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private TokenDTO token;
}