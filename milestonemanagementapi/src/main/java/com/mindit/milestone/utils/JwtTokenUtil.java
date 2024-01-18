// package com.mindit.milestone.utils;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import java.io.Serializable;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.function.Function;
// import javax.crypto.SecretKey;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
//
// @Component
// public class JwtTokenUtil implements Serializable {
//  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60l;
//
//  @Value("${mindit.jwt.secret.key}")
//  private String key;
//
//  public String getIdFromToken(String token) {
//    return getClaimFromToken(token, Claims::getSubject);
//  }
//
//  public Date getExpirationDateFromToken(String token) {
//    return getClaimFromToken(token, Claims::getExpiration);
//  }
//
//  public String getRoleFromUsers(String token) {
//    Claims claims;
//    claims = getAllClaimsFromToken(token);
//    Claims claim = claims;
//    return getRoleFromUsers(String.valueOf(claim));
//  }
//
//  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//    final Claims claims = getAllClaimsFromToken(token);
//    return claimsResolver.apply(claims);
//  }
//
//  private Claims getAllClaimsFromToken(String token) {
//    return Jwts.parserBuilder()
//        .setSigningKey(getSecretKey())
//        .build()
//        .parseClaimsJws(token)
//        .getBody();
//  }
//
//  private SecretKey getSecretKey() {
//    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
//  }
//
//  private Boolean isTokenExpired(String token) {
//    final Date expiration = getExpirationDateFromToken(token);
//    return expiration.before(new Date());
//  }
//
//  public String generateToken(String userId, String role) {
//    Map<String, Object> claims = new HashMap<>();
//    claims.put("role", role);
//    return doGenerateToken(claims, userId);
//  }
//
//  private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//    return Jwts.builder()
//        .setClaims(claims)
//        .setSubject(subject)
//        .setIssuedAt(new Date(System.currentTimeMillis()))
//        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//        .signWith(getSecretKey(), SignatureAlgorithm.HS256)
//        .compact();
//  }
//
//  public Boolean validateToken(String token, String userId) {
//    String id = getIdFromToken(token);
//    return (id.equals(userId) && !isTokenExpired(token));
//  }
//
//  public Boolean validateRole(String token, String userRole) {
//    String role = getRoleFromUsers(token);
//    return (role.equals(userRole) && !isTokenExpired(token));
//  }
// }
