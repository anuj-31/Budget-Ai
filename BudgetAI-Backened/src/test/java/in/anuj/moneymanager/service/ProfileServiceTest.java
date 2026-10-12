package in.anuj.moneymanager.service;

import in.anuj.moneymanager.dto.ProfileDto;
import in.anuj.moneymanager.entity.ProfileEntity;
import in.anuj.moneymanager.repository.ProfileRepository;
import in.anuj.moneymanager.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(
                profileRepository, emailService, passwordEncoder, authenticationManager, jwtUtil);
        ReflectionTestUtils.setField(profileService, "activationURL", "https://example.test");
        ReflectionTestUtils.setField(profileService, "contextPath", "/api/v1.0");
    }

    @Test
    void registrationNormalizesEmailAndKeepsPasswordHashed() {
        ProfileDto request = ProfileDto.builder()
                .fullName("New User")
                .email("  New.User@Example.com  ")
                .password("user-password")
                .build();
        when(profileRepository.existsByNormalizedEmail("new.user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("user-password")).thenReturn("hashed-password");
        when(profileRepository.save(any(ProfileEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProfileDto registered = profileService.registerProfile(request);

        assertEquals("new.user@example.com", registered.getEmail());
        verify(profileRepository).existsByNormalizedEmail("new.user@example.com");
        verify(profileRepository).save(org.mockito.ArgumentMatchers.argThat(profile ->
                "new.user@example.com".equals(profile.getEmail())
                        && "hashed-password".equals(profile.getPassword())));
        verify(emailService).sendEmail(
                org.mockito.ArgumentMatchers.eq("new.user@example.com"),
                any(String.class),
                any(String.class));
    }

    @Test
    void registrationRejectsAnExistingNormalizedEmail() {
        ProfileDto request = ProfileDto.builder()
                .fullName("Existing User")
                .email("  EXISTING@EXAMPLE.COM ")
                .password("user-password")
                .build();
        when(profileRepository.existsByNormalizedEmail("existing@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> profileService.registerProfile(request));

        verify(profileRepository, never()).save(any(ProfileEntity.class));
        verify(emailService, never()).sendEmail(any(String.class), any(String.class), any(String.class));
    }
}
