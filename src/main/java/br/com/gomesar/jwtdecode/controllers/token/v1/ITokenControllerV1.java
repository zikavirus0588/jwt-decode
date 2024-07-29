package br.com.gomesar.jwtdecode.controllers.token.v1;

import br.com.gomesar.jwtdecode.commons.models.AbstractApiResponse;
import br.com.gomesar.jwtdecode.controllers.token.v1.models.DecodeTokenResponse;
import br.com.gomesar.jwtdecode.controllers.validators.AuthorizationHeaderValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ITokenControllerV1 {


    @Operation(
        summary = "Decode a JWT Token and verify if claim is valid",
        description = "This resource receives a JWT Token and verify claims authenticity based on some " +
            "business rules",
        tags = {"Tokens"}
    )
    @PostMapping(value = "/decode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Valid JWT Token")
    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
        @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
            schema = @Schema(implementation = ProblemDetail.class))
    }
    )
    @ApiResponse(responseCode = "401", description = "Invalid JWT", content = {
        @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
            schema = @Schema(implementation = ProblemDetail.class))}
    )
    @ApiResponse(responseCode = "422", description = "Invalid JWT Claims", content = {
        @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
            schema = @Schema(implementation = ProblemDetail.class))}
    )
    AbstractApiResponse<DecodeTokenResponse> decodeToken(
        @RequestHeader("Authorization")
        @AuthorizationHeaderValue final String headerValue
    );

}
