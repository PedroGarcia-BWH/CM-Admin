package es.uca.cm.admin.webUser;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WebUserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebUserRepository userRepository;

    @InjectMocks
    private WebUserService userService;

    public WebUserServiceTest() {
        MockitoAnnotations.initMocks(this);
        userService = new WebUserService(userRepository, passwordEncoder);
    }

    @Test
    public void testCifrar() {
        String inputPassword = "password";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(inputPassword)).thenReturn(encodedPassword);

        String result = userService.Cifrar(inputPassword);

        assertEquals(encodedPassword, result);
        verify(passwordEncoder, times(1)).encode(inputPassword);
    }

    @Test
    public void testSave_NewUser() {
        WebUser user = new WebUser();
        user.setPass("password");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(user.getPass())).thenReturn(encodedPassword);

        WebUser savedUser = new WebUser();
        when(userRepository.save(user)).thenReturn(savedUser);

        WebUser result = userService.save(user);

        assertEquals(encodedPassword, user.getPass());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSave_ExistingUser() {
        WebUser user = new WebUser();
        UUID uuid = UUID.randomUUID();
        user.setPass("password");
        user.setId(uuid);

        WebUser savedUser = new WebUser();
        when(userRepository.save(user)).thenReturn(savedUser);

        WebUser result = userService.save(user);

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, never()).encode(user.getPass());
    }

    @Test
    public void testCount() {
        long expectedCount = 10L;
        when(userRepository.count()).thenReturn(expectedCount);

        long result = userService.count();

        verify(userRepository, times(1)).count();
    }

    @Test
    public void testChangePass() {
        WebUser user = new WebUser();
        user.setPass("oldPassword");

        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        WebUser savedUser = new WebUser();
        when(userRepository.save(user)).thenReturn(savedUser);

        WebUser result = userService.changePass(user, newPassword);

        assertEquals(encodedNewPassword, user.getPass());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(newPassword);
    }
}