package es.uca.cm.admin.username;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsernameServiceTest {

    @Mock
    private UsernameRepository usernameRepository;

    @InjectMocks
    private UsernameService usernameService;

    public UsernameServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByUsername() {
        String username = "johnDoe";
        Optional<Username> usernameOptional = Optional.of(new Username(username));
        when(usernameRepository.findByUsername(username)).thenReturn(usernameOptional);

        Optional<Username> result = usernameService.findByUsername(username);

        assertEquals(usernameOptional, result);
        verify(usernameRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testSave() {
        Username username = new Username("johnDoe");
        usernameService.save(username);

        verify(usernameRepository, times(1)).save(username);
    }

    @Test
    public void testDelete() {
        Username username = new Username("johnDoe");
        usernameService.delete(username);

        verify(usernameRepository, times(1)).delete(username);
    }

    @Test
    public void testFindByUsernameContainingIgnoreCase() {
        String username = "john";
        List<Username> usernameList = new ArrayList<>();
        when(usernameRepository.findByUsernameContainingIgnoreCase(username)).thenReturn(usernameList);

        List<Username> result = usernameService.finbByUsernameContainingIgnoreCase(username);

        assertEquals(usernameList, result);
        verify(usernameRepository, times(1)).findByUsernameContainingIgnoreCase(username);
    }

    @Test
    public void testFindByFirebaseId() {
        String firebaseId = "123456";
        Username username = new Username("johnDoe");
        when(usernameRepository.findByFirebaseId(firebaseId)).thenReturn(username);

        Username result = usernameService.findByFirebaseId(firebaseId);

        assertEquals(username, result);
        verify(usernameRepository, times(1)).findByFirebaseId(firebaseId);
    }
}