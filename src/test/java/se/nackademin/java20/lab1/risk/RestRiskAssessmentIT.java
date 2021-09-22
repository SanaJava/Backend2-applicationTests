package se.nackademin.java20.lab1.risk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

class RestRiskAssessmentIT {

    private WireMockServer mockRiskServer;

    @BeforeEach
    public void before() {
        mockRiskServer = new WireMockServer(9090);
        mockRiskServer.start();
    }

    @AfterEach
    public void after() {
        mockRiskServer.stop();
    }

    @Test
    void canMakeRiskAssessment() {
        mockRiskServer.stubFor(get(urlEqualTo("/risk/dan")).willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("content-type", APPLICATION_JSON.toString())
                .withBody("{\"pass\": true}")));
        final RestRiskAssessment restRiskAssessment = new RestRiskAssessment(new RestTemplate(), mockRiskServer.baseUrl());
        final boolean passingCreditCheck = restRiskAssessment.isPassingCreditCheck("dan");
        assertTrue(passingCreditCheck);
    }

    // Göra ett restanrop GET /risk/username
    // Få en json tillbaka
    // deserialisera json -> json objekt

    // inte gå mot riktiga tjänsten utan gå mot en mock istället

    // starta upp risk servicen
    // docker run -p"8081:8080" -t daneidmark/risk:0.0.1

    //postForEntity osv.
    // Entity = (statuskod tex: 200, 400, 500, Body text json-objekt)
    // Statuskoder 101
    // 2xx Tummen upp! allt gick bra
    // 200 OK och jag har data åt dig
    // 201 CREATED
    // 204 NO CONTENT (OK men jag har ingen data åt dig)
    // 3xx redirection
    // 4xx Client error, Klienten kan rädda situationen genom att tex skicka bättre data eller så
    // 400 BAD REQUEST
    // 401 NOT AUTHENTICATED
    // 403 NOT AUTHORIZED
    // 404 Not found

    static class Dto {
        boolean pass;

        @JsonCreator
        public Dto(@JsonProperty("pass") boolean pass) {
            this.pass = pass;
        }

        public boolean isPass() {
            return pass;
        }

        @Override
        public String toString() {
            return "Dto{" +
                    "pass=" + pass +
                    '}';
        }
    }

    @Test
    void testRiskAssessment() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Dto> response = restTemplate.getForEntity("http://localhost:8081/risk/sana", Dto.class);
        System.out.println(response.getStatusCodeValue());
        System.out.println(response.getBody());
    }

    static class SignupDto {
        @JsonProperty("username")
        private final String username;
        @JsonProperty("password")
        private final String password;
        @JsonProperty("roles")
        private final List<String> roles;

        public SignupDto(String username, String password, List<String> roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }
    }

    @Test
    void showAndTellPost() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:8082/user/signup",
                new SignupDto("user", "pass", List.of("ADMIN")),
                Void.class
        );

        System.out.println(response.getStatusCodeValue());
        System.out.println(response.getBody());
    }
}