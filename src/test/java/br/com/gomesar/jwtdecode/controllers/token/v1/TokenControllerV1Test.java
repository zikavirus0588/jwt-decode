package br.com.gomesar.jwtdecode.controllers.token.v1;

import br.com.gomesar.jwtdecode.commons.models.AbstractApiResponse;
import br.com.gomesar.jwtdecode.controllers.token.v1.models.DecodeTokenResponse;
import br.com.gomesar.jwtdecode.services.token.ITokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TokenControllerV1.class)
class TokenControllerV1Test {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ITokenService tokenService;

    @Captor
    private ArgumentCaptor<String> jwtArgumentCaptor;

    @Test
    void testShouldReturn400BadRequestWhenAuthorizationHeaderIsNull() throws Exception {
        mockMvc.perform(post("/v1/tokens/decode")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testShouldReturn400BadRequestWhenAuthorizationHeaderValueIsInvalid() throws Exception {
        mockMvc.perform(post("/v1/tokens/decode")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testShouldReturn200OkWhenAuthorizationHeaderStartsWithBearerAndIsFollowedBySpaceAndSomeValue() throws Exception {
        final var validToken = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25p" +
                "bmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        final var expectedResponse = new AbstractApiResponse.Builder<DecodeTokenResponse>()
                .data(new DecodeTokenResponse.Builder().isValidToken(true).build())
                .build();

        doReturn(true).when(tokenService).decodeToken(any());

        final var result = mockMvc.perform(post("/v1/tokens/decode")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + validToken)
        ).andExpect(status().isOk()).andReturn();

        verify(tokenService, times(1)).decodeToken(jwtArgumentCaptor.capture());

        final var capturedToken = jwtArgumentCaptor.getValue();

        Assertions.assertThat(result.getResponse()).isNotNull();
        Assertions.assertThat(capturedToken.replace("Bearer ", "")).isEqualTo(validToken);
        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(expectedResponse));

    }

}