// package com.mindit.milestone.services.impl;
//
// import com.mindit.milestone.contants.enums.UserRole;
// import com.mindit.milestone.data.entity.AccessToken;
// import com.mindit.milestone.data.entity.User;
// import com.mindit.milestone.data.repository.AccessTokenRepository;
// import com.mindit.milestone.data.repository.UserRepository;
// import com.mindit.milestone.exception.CommonErrorCode;
// import com.mindit.milestone.exception.MMException;
// import com.mindit.milestone.models.request.LoginRequest;
// import com.mindit.milestone.models.response.TokenResponse;
// import com.mindit.milestone.services.AuthService;
// import com.mindit.milestone.utils.EncryptionService;
// import com.mindit.milestone.utils.JwtTokenUtil;
// import jakarta.transaction.Transactional;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
//
// import java.util.List;
// import java.util.Optional;
//
// @AllArgsConstructor
// @Slf4j
// @Service
// @Transactional
// public class AuthServiceImpl implements AuthService {
//
//    private final UserRepository userRepository;
//    private final AccessTokenRepository accessTokenRepository;
//
//    private final JwtTokenUtil jwtTokenUtil;
//
//    @Override
//    public TokenResponse login(LoginRequest loginRequest) {
//        try {
//            Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
//            if (user.isPresent()) {
//                String encryptedPassword = EncryptionService.encrypt(loginRequest.getPassword());
//                User userDetails = user.get();
//                if (userDetails.getPassword().equals(encryptedPassword)) {
//                    String userId = userDetails.getId();
//                    markPreviousAccessTokensInactive(userId);
//                    AccessToken accessToken = generateAccessToken(userDetails,
// userDetails.getRole());
//                    return new TokenResponse(accessToken.getToken());
//                }
//                throw new MMException(CommonErrorCode.PASSWORD_NOT_VALID);
//            }
//            throw new MMException(CommonErrorCode.EMAIL_NOT_EXISTS);
//        } catch (MMException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            log.error("Error occured during login : [{}]", ex.getMessage());
//            throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private void saveToken(AccessToken accessToken) {
//        accessTokenRepository.save(accessToken);
//    }
//
//    private AccessToken generateAccessToken(User user, UserRole role) {
//        AccessToken accessToken = new AccessToken();
//        String token = jwtTokenUtil.generateToken(user.getId(), role.name());
//        accessToken.setUser(user);
//        accessToken.setToken(token);
//        accessToken.setIsActive(true);
//        saveToken(accessToken);
//        return accessToken;
//    }
//
//    private void markPreviousAccessTokensInactive(String userId) {
//        List<AccessToken> existingTokens = accessTokenRepository.findByUserIdAndIsActive(userId,
// true);
//        for (AccessToken accessTokens : existingTokens) {
//            accessTokens.setIsActive(false);
//            accessTokenRepository.save(accessTokens);
//        }
//    }
//
//    @Override
//    public AccessToken getTokenDetails(String token) {
//        try {
//            return accessTokenRepository.findByToken(token);
//        } catch (MMException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            log.error("Error occured during getting token details : [{}]", ex.getMessage());
//            throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }
// }
