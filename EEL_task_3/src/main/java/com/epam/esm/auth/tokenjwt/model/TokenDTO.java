package com.epam.esm.auth.tokenjwt.model;

import lombok.Data;

@Data
public class TokenDTO {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}