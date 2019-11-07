package com.freshworks.starter.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.freshworks.starter.web.security.SecurityConstants.HEADER_STRING;
import static com.freshworks.starter.web.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private SecurityConfig securityConfig;
    private final PermissionsDecoder permissionsDecoder;

    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityConfig securityConfig, PermissionsDecoder permissionsDecoder) {
        super(authManager);
        this.securityConfig = securityConfig;
        this.permissionsDecoder = permissionsDecoder;
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
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String jwtTokenStr = token.replace(TOKEN_PREFIX, "");
            String[] jwtSecrets = securityConfig.getJwtSecrets(getClaim(JWT.decode(jwtTokenStr), "ClientId"));
            if (jwtSecrets == null) {
                return null;
            }
            JWTVerificationException exception = null;
            for (String secret : jwtSecrets) {
                try {
                    DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(secret)).build().verify(jwtTokenStr);
                    String clientId = getClaim(decodedJwt, "ClientId");
                    String userId = getClaim(decodedJwt, "UserId");
                    String accountId = getClaim(decodedJwt, "AccId");
                    String orgId = getClaim(decodedJwt, "OrgId");
                    String permissionsStr = getClaim(decodedJwt, "Permissions");
                    String[] permissions = permissionsDecoder.decode(permissionsStr);
                    List<GrantedAuthority> grantedAuthorities = Arrays.stream(permissions).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    FWUserDetails FWUserDetails = new FWUserDetails(userId, clientId, accountId, orgId, grantedAuthorities);
                    return new UsernamePasswordAuthenticationToken(FWUserDetails, null, grantedAuthorities);
                } catch (JWTVerificationException e) {
                    exception = e;
                }
            }
            if (exception != null) {
                throw exception;
            }
            return null;
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
}
