import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class LoginMockTest {

    // Step 2, create our mock obj
    CredentialsService credentialsServiceMock;
    PermissionService permissionServiceMock;

    @BeforeEach // It exec before the test
    public void setup() {
        credentialsServiceMock = Mockito.mock(CredentialsService.class);
        permissionServiceMock = Mockito.mock(PermissionService.class);
    }

    @AfterEach // It exec before the test
    public void cleanup() {
    }

    @ParameterizedTest
    @CsvSource({
            "admin,4dm1n,CRUD,user authenticated successfully with permission: [CRUD]",
            "user1,123,CRU,user authenticated successfully with permission: [CRU]",
            "user2,us342,CR,user authenticated successfully with permission: [CR]"
    })
    public void verifyLoginWithValidCredentials(String user, String password, String permissions, String expectedResult) {

        // Step 3, Configure our mock obj
        Mockito.when(credentialsServiceMock.isValidCredential(user,password)).thenReturn(true);

        Mockito.when(permissionServiceMock.getPermission(user)).thenReturn(permissions);

        // Step 4, use the mock
        Authentication authentication = new Authentication();
        authentication.setCredentialsService(credentialsServiceMock);
        authentication.setPermissionService(permissionServiceMock);

        String actualResult = authentication.login(user, password);

        Assertions.assertEquals(expectedResult, actualResult);

    }

    @ParameterizedTest
    @CsvSource({
            "Admin,4dm1n,user or password incorrect",
            "User1,123,user or password incorrect",
            "User2,us342,user or password incorrect"
    })
    public void verifyLoginWithInvalidCredentials(String user, String password, String expectedResult) {

        // Step 3, Configure our mock obj
        Mockito.when(credentialsServiceMock.isValidCredential(user,password)).thenReturn(false);

        // Step 4, use the mock
        Authentication authentication = new Authentication();
        authentication.setCredentialsService(credentialsServiceMock);

        String actualResult = authentication.login(user, password);

        Assertions.assertEquals(expectedResult, actualResult);

    }


}
