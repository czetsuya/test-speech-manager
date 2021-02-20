package com.czetsuyatech.web.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * @since
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development-h2")
@Slf4j
public abstract class AbstractControllerTest {

    protected static final ObjectMapper om = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        log.debug("Running tests on baseURI={}, port={}", RestAssured.baseURI, port);
    }

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
}
