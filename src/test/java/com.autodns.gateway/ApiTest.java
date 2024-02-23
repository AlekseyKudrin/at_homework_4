package com.autodns.gateway;

import com.autodns.getway.dto.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static helper.Handling.buildMessage;
import static io.restassured.RestAssured.given;

public class ApiTest {
    @Test
    public void test1() {
        PageUsers pageUsersDto = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract().body().as(PageUsers.class);
        List<User> userList = pageUsersDto.getData();
        Map<User, List<User>> repeat = new HashMap<>();
        for (int i = 0; i < userList.size(); i++) {
            String avatar = userList.get(i).getAvatar();
            for (int j = 0; j < userList.size(); j++) {
                String avatarTemp = userList.get(j).getAvatar();
                if (i == j) continue;
                if (avatar.equals(avatarTemp)) {
                    List<User> users;
                    if (repeat.containsKey(userList.get(i))) {
                        users = repeat.get(userList.get(i));
                        users.add(userList.get(i));
                    } else {
                        users = new ArrayList<>();
                        users.add(userList.get(j));
                    }
                    repeat.put(userList.get(i), users);
                }
            }
        }
        Assert.assertEquals(repeat.size(), 0, buildMessage(repeat));
    }

    @Test
    public void test2() {
        Login login = new Login("eve.holt@reqres.in", "cityslicka");
        Response response = given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), 200, "Некорректный код ответа");
    }

    @Test
    public void test3() {
        Login login = new Login("eve.holt@reqres.in");
        Response response = given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), 400, "Некорректный код ответа");
    }

    @Test
    public void test4() {
        PageProfiles pageProfilesDto = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .extract().body().as(PageProfiles.class);
        List<Profile> profiles = pageProfilesDto.getData();

        List<Profile> profilesSort = profiles.stream().sorted(Comparator.comparingInt(Profile::getYear)).collect(Collectors.toList());
        for (int i = 0; i < profiles.size(); i++) {
            System.out.println(profilesSort.get(i));
            if (!profiles.get(i).getYear().equals(profilesSort.get(i).getYear())) {
                Assert.fail("Массив не отсортирован");
            }
        }
    }

    @Test
    public void test5() {
        Response response = given()
                .when()
                .get("https://gateway.autodns.com/")
                .then()
                .extract().response();
        int countTeg = (response.body().asString().length() - response.body().asString().replace("</", "").length()) / 2;
        Assert.assertEquals(countTeg, 15, "Количество тегов не равно 15");
    }
}
