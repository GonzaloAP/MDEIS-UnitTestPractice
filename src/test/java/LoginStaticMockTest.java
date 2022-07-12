import authenticationStatic.Authentication;
import authenticationStatic.CredentialsStaticService;
import authenticationStatic.PermissionStaticService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class LoginStaticMockTest {

    // Step 2, create our mock obj
    MockedStatic<CredentialsStaticService> credentialsServiceMock;
    MockedStatic<PermissionStaticService> permissionServiceMock;

    @BeforeEach // It exec before the test
    public void setup() {
        credentialsServiceMock = Mockito.mockStatic(CredentialsStaticService.class);
        permissionServiceMock = Mockito.mockStatic(PermissionStaticService.class);
    }

    @AfterEach // It exec before the test
    public void cleanup() {
        credentialsServiceMock.close();
        permissionServiceMock.close();
    }

    @ParameterizedTest
    @CsvSource({
            "admin,4dm1n,CRUD,user authenticated successfully with permission: [CRUD]",
            "user1,123,CRU,user authenticated successfully with permission: [CRU]",
            "user2,us342,CR,user authenticated successfully with permission: [CR]"
    })
    public void verifyLoginWithValidCredentials(String user, String password, String permissions, String expectedResult) {

        // Step 3, Configure our mock obj
        credentialsServiceMock.when(() -> CredentialsStaticService.isValidCredential(user, password)).thenReturn(true);

        permissionServiceMock.when(() -> PermissionStaticService.getPermission(user)).thenReturn(permissions);

        // Step 4, use the mock
        Authentication authentication = new Authentication();

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
        credentialsServiceMock.when(() -> CredentialsStaticService.isValidCredential(user, password)).thenReturn(false);

        // Step 4, use the mock
        Authentication authentication = new Authentication();

        String actualResult = authentication.login(user, password);

        Assertions.assertEquals(expectedResult, actualResult);

    }

}
