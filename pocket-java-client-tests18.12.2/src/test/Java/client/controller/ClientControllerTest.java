package client.controller;

import client.model.ServerResponse;
import client.model.formatMsgWithServer.AuthFromServer;
import client.model.formatMsgWithServer.AuthToServer;
import client.utils.HTTPSRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.entity.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientControllerTest  {
    private String requestString;
    private String token;

    private ClientController controller = ClientController.getInstance();

    @Test
    public void receiveMessage() throws Exception{
        String msg = "Message";
        controller.receiveMessage(msg);
    }

    @Test
    public void proceedLogIn(){
        String userLogin = "tester2";
        String userPassword = "123";
        boolean login = controller.proceedLogIn(userLogin, userPassword);
        Assert.assertTrue(login);
    }

    @Test
    public void addContact(){
        String log = "lala";
        ServerResponse response = null;
        controller.addContact(log);
        Assert.assertEquals(201, response.getResponseCode());
    }

    @Test
    public void proceedRestorePassword(){
        String email = "lala@lala.ru";
        String answer = controller.proceedRestorePassword(email);
        Assert.assertNotEquals("0", answer);
    }

    @Test
    public void proceedRegister(){

    }

    @After
    public void afterTests() {
        controller.disconnect();
    }
}
