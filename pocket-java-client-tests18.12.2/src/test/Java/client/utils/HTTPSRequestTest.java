package client.utils;

import client.model.ServerResponse;
import client.model.formatMsgWithServer.AuthFromServer;
import client.model.formatMsgWithServer.AuthToServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.*;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

public class HTTPSRequestTest {

    private String token;
    Gson gson;
    private String requestString;

    @Before
    public void beforeTests() throws Exception {
        // Для большинства операций с сервером требуется токен - получаем его до начала всех тестов
        // (для авторизации вводим логин и пароль любого реального юзера)

//        String newToken = "628adb4549f06656";
//        String login = "New_login";
//        String email = "emai@mail.ru";
//        String password = "321";
//        String requestJSON = "{" +
//                "\"account_name\": \"" + login + "\"," +
//                "\"email\": \"" + email + "\"," +
//                "\"password\": \"" + password + "\"" +
//                "}";
//        String userLogin = "tester2";
//        String userPassword = "123";

        //String requestString;
        String userLogin = "tester2";
        String userPassword = "123";
        AuthToServer ATS = new AuthToServer(userLogin, userPassword);
        requestString = new Gson().toJson(ATS);
        String response = HTTPSRequest.authorization(requestString);
        if (response.contains("token")) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            AuthFromServer AFS = gson.fromJson(response, AuthFromServer.class);
            token = AFS.token;
        }
        Assert.assertNotNull("Login or password incorrect!", token);
    }

//    @Test()
//    public void registration()throws Exception{
//        String login = "New_login";
//        String email = "emai@mail.ru";
//        String password = "321";
//        String requestJSON = "{" +
//                "\"account_name\": \"" + login + "\"," +
//                "\"email\": \"" + email + "\"," +
//                "\"password\": \"" + password + "\"" +
//                "}";
//        int response = HTTPSRequest.registration(requestJSON);
//        Assert.assertEquals(201,response);
//    }

    @Test()
    public void registrationConflict()throws Exception{
        String login = "tester2";
        String email = "emai@mail.ru";
        String password = "321";
        String requestJSON = "{" +
                "\"account_name\": \"" + login + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"" +
                "}";
        int response = HTTPSRequest.registration(requestJSON);
        Assert.assertEquals(409,response);
    }

    @Test()
    public void registrationBadRequest()throws Exception{
        String login = "New_login";
        String email = "123";
//        String userLogin = "tester2";
//        String userPassword = "123";
        String requestJSON = "{" +
                "\"account_name\": \"" + login + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" +
                "}";
        int response = HTTPSRequest.registration(requestJSON);
        Assert.assertEquals(400,response);
    }

    @Test()
    public void restorePassword() throws Exception{

        String email = "myemail1@mail.ru";
        //String email = "myemail1@mail.ru";
        String requestJSON = "{" +
                "\"email\": \"" + email + "\"" +
                "}";
        String response = HTTPSRequest.restorePassword(requestJSON);
        Assert.assertNotEquals("404", response);
    }

    @Test()
    public void changePassword() throws Exception{
        String codeRecovery = "12345";
        String email = "emai@mail.ru";
        String password = "567";
        String requestJSON = "{" +
                "\"email\": \"" + email + "\"," +
                "\"code\": \"" + codeRecovery + "\"," +
                "\"password\": \"" + password + "\"" +
                "}";
        String response = HTTPSRequest.changePassword(requestJSON);
        Assert.assertEquals("", response);
    }

    @Test()
    public void getUser() throws Exception {
        int userId = 51; // id тестируемого юзера
        ServerResponse response = HTTPSRequest.getUser(userId, token);
        Assert.assertNotEquals("Token not found!", 401, response.getResponseCode());
        Assert.assertNotEquals("Unauthorized!", 403, response.getResponseCode());
        Assert.assertNotEquals("User not found!", 404, response.getResponseCode());
    }

    @Test()
    public void getContacts() throws Exception {
        ServerResponse response = HTTPSRequest.getContacts(token);
        Assert.assertNotEquals("Token not found!", 401, response.getResponseCode());
        Assert.assertNotEquals("Unauthorized!", 403, response.getResponseCode());
    }

    @Test()
    public void getMyself() throws Exception{
        ServerResponse response = HTTPSRequest.getMySelf(token);
        Assert.assertNotEquals("Unauthorized", 403, response.getResponseCode());
        Assert.assertNotEquals("Token not found", 404, response.getResponseCode());
        Assert.assertNotEquals("Your token expired", 401, response.getResponseCode());
    }

    @Test()
    public void addContact() throws Exception{
        String email = "myemail1@mail.ru";
        String requestJSON = "{" +
                "\"email\": \"" + email + "\"," +
                "}";
        ServerResponse response = HTTPSRequest.addContact(requestJSON, token);
        Assert.assertNotEquals("User does not exists", 404, response.getResponseCode());
        Assert.assertNotEquals("Contact already in list", 409, response.getResponseCode());
    }

    @Test()
    public void addGroup() throws Exception{
        ServerResponse response = HTTPSRequest.addGroup(requestString, token);
        Assert.assertNotEquals("Bad JSON", 400, response.getResponseCode());
        Assert.assertNotEquals("Error token", 400, response.getResponseCode());
        Assert.assertNotEquals("Group not found", 404, response.getResponseCode());
        Assert.assertNotEquals("Internal Server Error", 500, response.getResponseCode());
    }

    @Test()
    public void addUserGroup() throws Exception{
        ServerResponse response = HTTPSRequest.addUserGroup(requestString, token);
        Assert.assertNotEquals("Bad JSON", 400, response.getResponseCode());
        Assert.assertNotEquals("Group not found", 404, response.getResponseCode());
        Assert.assertNotEquals("Internal Server Error", 500, response.getResponseCode());
    }
}