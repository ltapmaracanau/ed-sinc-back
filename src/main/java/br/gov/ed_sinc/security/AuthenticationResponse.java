package br.gov.ed_sinc.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponse {

	private String token;
	private String nome;

    public AuthenticationResponse() {
        // no-arg constructor for frameworks
    }

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public static AuthenticationResponse withToken(String token) {
        return new AuthenticationResponse(token);
    }
}
