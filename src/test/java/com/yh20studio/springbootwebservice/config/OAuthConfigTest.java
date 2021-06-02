package com.yh20studio.springbootwebservice.config;

import io.restassured.RestAssured;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// 고정된 포트를 사용할 것이므로
@TestPropertySource(
        properties = "spring.config.location=classpath:/application-google.yml"
)
// google.yml을 Junit 테스트시에도 설정으로 적용할수 있도록
public class OAuthConfigTest {

    @Before("")
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void 구글로그인_시도하면_OAuth인증창_등장 () throws Exception{
        given()
                .when()
                    .redirects().follow(false)
                    .get("/oauth2/authorization/google")
                .then()
                    .statusCode(302)
                    .headers("Location", containsString("https://accounts.google.com/o/oauth2/v2"));
    }
}
