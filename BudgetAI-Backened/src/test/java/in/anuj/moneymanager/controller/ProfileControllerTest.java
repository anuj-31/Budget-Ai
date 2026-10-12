package in.anuj.moneymanager.controller;

import in.anuj.moneymanager.dto.ProfileDto;
import in.anuj.moneymanager.service.DuplicateEmailException;
import in.anuj.moneymanager.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfileControllerTest {
    private final ProfileService profileService = mock(ProfileService.class);
    private final ProfileController profileController = new ProfileController(profileService);

    @Test
    void duplicateEmailReturnsConflict() {
        when(profileService.registerProfile(new ProfileDto()))
                .thenThrow(new DuplicateEmailException("Email is already registered"));

        var response = profileController.registerProfile(new ProfileDto());

        assertEquals(409, response.getStatusCode().value());
        assertEquals(
                "Email is already registered",
                ((java.util.Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void unexpectedRegistrationFailureReturnsServerErrorInsteadOfConflict() {
        when(profileService.registerProfile(new ProfileDto()))
                .thenThrow(new DataAccessResourceFailureException("database unavailable"));

        var response = profileController.registerProfile(new ProfileDto());

        assertEquals(500, response.getStatusCode().value());
        assertEquals(
                "Registration failed. Please try again later.",
                ((java.util.Map<?, ?>) response.getBody()).get("message"));
    }
}
