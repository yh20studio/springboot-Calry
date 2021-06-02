package com.yh20studio.springbootwebservice;

import io.restassured.RestAssured;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest
class SpringbootWebserviceApplicationTests {

    @Test
    void contextLoads() {
    }

}
