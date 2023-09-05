package com.vn.whatsapp.app.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String jwt;

    private boolean isAuth;


}
