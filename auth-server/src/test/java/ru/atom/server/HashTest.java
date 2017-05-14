package ru.atom.server;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.atom.server.service.AuthService;

import java.io.IOException;

@Ignore
public class HashTest {
    @Test
    public void mainTest() throws IOException {
        String password = "123";
        Assert.assertTrue(AuthService.checkHash(AuthService.getHash(password), password));
        Assert.assertFalse(AuthService.checkHash(AuthService.getHash(password), password + "1"));

    }
}
