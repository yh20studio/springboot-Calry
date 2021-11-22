package com.yh20studio.springbootwebservice.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest
public class restAssuredTest {

    @BeforeEach
    public void setup() {
        RestAssured.port = 8080;
    }

    @Test
    public void test_접근하면_test_html_호출된다() throws Exception {
        given()
            .when()
            .get("/test")
            .then()
            .statusCode(200)
            .contentType("text/html")
            .body(containsString("권한 관리"));
    }
}
