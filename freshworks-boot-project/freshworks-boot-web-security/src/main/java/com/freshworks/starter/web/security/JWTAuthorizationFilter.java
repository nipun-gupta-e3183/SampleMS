package com.freshworks.starter.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

import static com.freshworks.starter.web.security.SecurityConstants.HEADER_STRING;
import static com.freshworks.starter.web.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    private final PermissionsDecoder permissionsDecoder;
    private Map<String, Algorithm> algorithms = new HashMap<>();

    @SuppressWarnings("WeakerAccess")
    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityConfig securityConfig, PermissionsDecoder permissionsDecoder) {
        super(authManager);
        this.permissionsDecoder = permissionsDecoder;
        try {
            for (Map.Entry<String, String> publicKeyEntry : securityConfig.getAuthzPublicKeys().entrySet()) {
                PublicKey publicKey = getPublicKey(publicKeyEntry.getValue());
                algorithms.put(publicKeyEntry.getKey(), Algorithm.RSA256((RSAPublicKey) publicKey, null));
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String clientId = request.getHeader("X-Client-ID");
        String token = request.getHeader(HEADER_STRING);
        if (clientId == null || token != null) {
            String jwtTokenStr = token.replace(TOKEN_PREFIX, "");
            DecodedJWT decodedJWT = JWT.decode(jwtTokenStr);
            Claim kid = decodedJWT.getHeaderClaim("kid");
            if (!algorithms.containsKey(kid.asString())) {
                log.debug("JWT token doesn't have a valid kid: {}", kid.asString());
                return null;
            }
            try {
                DecodedJWT decodedJwt = JWT.require(algorithms.get(kid.asString())).build().verify(jwtTokenStr);
                String userId = getClaim(decodedJwt, "UserId");
                String accountId = getClaim(decodedJwt, "AccId");
                String orgId = getClaim(decodedJwt, "OrgId");
                String permissionsStr = getClaim(decodedJwt, "Permissions");
                String[] permissions = permissionsDecoder.decode(permissionsStr);
                List<GrantedAuthority> grantedAuthorities = Arrays.stream(permissions).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                FWUserDetails FWUserDetails = new FWUserDetails(userId, accountId, orgId, grantedAuthorities);
                return new UsernamePasswordAuthenticationToken(FWUserDetails, null, grantedAuthorities);
            } catch (JWTVerificationException e) {
                log.debug("Exception occurred while verifying JWT token", e);
                return null;
            }
        }
        return null;
    }

    private String getClaim(DecodedJWT decodedJwt, String claimName) {
        Claim claim = decodedJwt.getClaim(claimName);
        if (claim == null) {
            return null;
        }
        return claim.asString();
    }

    private PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kFactory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        byte[] pubKeyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pubKeyBytes);
        return kFactory.generatePublic(spec);
    }
}
