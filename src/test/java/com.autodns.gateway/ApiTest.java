package com.autodns.gateway;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiTest {
    @Test
    public void test() {
        Response response = given()
                .when()
                .get("https://gateway.autodns.com/")
                .then()
                .log().all()
                .extract().response();
        int a = (response.body().asString().length() - response.body().asString().replace("</", "").length())/2;
        System.out.println(a);
    }


}
