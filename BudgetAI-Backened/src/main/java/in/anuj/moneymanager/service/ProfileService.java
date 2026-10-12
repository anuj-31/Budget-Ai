package in.anuj.moneymanager.service;

import in.anuj.moneymanager.dto.AuthDTO;
import in.anuj.moneymanager.dto.ProfileDto;
import in.anuj.moneymanager.entity.ProfileEntity;
import in.anuj.moneymanager.repository.ProfileRepository;
import in.anuj.moneymanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final  EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Value("${app.activation.url}")
    private   String activationURL;

    @Value("${server.servlet.context-path:/api/v1.0}")
    private String contextPath;

    @Transactional
    public ProfileDto registerProfile(ProfileDto profileDTO) {
        String normalizedEmail = normalizeEmail(profileDTO.getEmail());
        profileDTO.setEmail(normalizedEmail);

        if (profileRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setIsActive(false);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        String activationLink = activationURL.replaceAll("/+$", "")
                + contextPath.replaceAll("/+$", "") + "/activate?token="
                + URLEncoder.encode(newProfile.getActivationToken(), StandardCharsets.UTF_8);
        String subject = "Activate Your BudgetAi Account";
        String body = buildActivationEmail(newProfile.getFullName(), activationLink);
        emailService.sendEmail(newProfile.getEmail(), subject, body);
        return toDTO(newProfile);
    }

    private String buildActivationEmail(String fullName, String activationLink) {
        String safeName = escapeHtml(fullName == null || fullName.isBlank() ? "there" : fullName);
        String safeLink = escapeHtml(activationLink);
        return """
                <!DOCTYPE html>
                <html>
                  <body style="margin:0;padding:24px;background:#f8fafc;font-family:Arial,sans-serif;color:#1f2937;">
                    <table role="presentation" style="max-width:600px;margin:0 auto;background:#ffffff;border-radius:12px;padding:32px;">
                      <tr><td>
                        <h1 style="font-size:24px;margin:0 0 24px;color:#111827;">Activate your BudgetAi account</h1>
                        <p>Hello %s,</p>
                        <p>Welcome to BudgetAi!</p>
                        <p>Thank you for creating an account. To get started, please activate your account by clicking the button below.</p>
                        <p style="margin:28px 0;">
                          <a href="%s" style="display:inline-block;padding:12px 24px;background:#7c3aed;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:bold;">Activate My Account</a>
                        </p>
                        <p style="font-size:14px;color:#4b5563;">If the button does not work, copy and paste the activation link into your browser.</p>
                        <p style="font-size:14px;word-break:break-all;"><a href="%s" style="color:#6d28d9;">%s</a></p>
                        <p>If you did not create this account, please ignore this email.</p>
                        <p>Best regards,<br>BudgetAi Team</p>
                      </td></tr>
                    </table>
                  </body>
                </html>
                """.formatted(safeName, safeLink, safeLink, safeLink);
    }

    private String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public ProfileEntity toEntity(ProfileDto profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }
    public ProfileDto toDTO(ProfileEntity profileEntity) {
        return ProfileDto.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
//                .password(profileEntity.getPassword())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }
    public boolean activateProfile(String activationToken) {
        if (activationToken == null || activationToken.isBlank()) {
            return false;
        }
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profile.setActivationToken(null);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }
    //
    public boolean isAccountActive(String email) {
        return profileRepository.findFirstByEmailOrderByIdDesc(normalizeEmail(email))
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findFirstByEmailOrderByIdDesc(normalizeEmail(authentication.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + authentication.getName()));
    }
    //
    public ProfileDto getPublicProfile(String email) {
        ProfileEntity currentUser = null;
        if (email == null) {
            currentUser = getCurrentProfile();
        }else {
            currentUser = profileRepository.findFirstByEmailOrderByIdDesc(normalizeEmail(email))
                    .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
        }

        return ProfileDto.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .profileImageUrl(currentUser.getProfileImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    public ProfileDto updateProfileImage(String profileImageUrl) {
        ProfileEntity currentUser = getCurrentProfile();
        currentUser.setProfileImageUrl(profileImageUrl);
        return toDTO(profileRepository.save(currentUser));
    }



    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        String normalizedEmail = normalizeEmail(authDTO.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(normalizedEmail, authDTO.getPassword()));
        } catch (AuthenticationException exception) {
            throw new RuntimeException("Invalid email or password");
        }

        ProfileEntity profile = profileRepository.findFirstByEmailOrderByIdDesc(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!Boolean.TRUE.equals(profile.getIsActive())) {
            throw new RuntimeException("Please activate your account using the link sent to your email");
        }

        String token = jwtUtil.generateToken(normalizedEmail);
        return Map.of(
                "token", token,
                "user", getPublicProfile(normalizedEmail)
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
