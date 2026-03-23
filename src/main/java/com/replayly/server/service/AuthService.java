package com.replayly.server.service;

import com.replayly.server.dto.AuthLoginRequest;
import com.replayly.server.dto.AuthRegisterRequest;
import com.replayly.server.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthLoginRequest request);
    AuthResponse register(AuthRegisterRequest request);
}
