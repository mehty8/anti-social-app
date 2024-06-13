package antisocial.app.backend.security;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Scope("singleton")
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${secret.jwt.name}")
    private String jwtSecretName;

    @Value("${parameter.jwt.expirationTime}")
    private String jwtExpirationTimeParameter;

    private AWSSecretsManager awsSecretsManager;

    private AWSSimpleSystemsManagement awsSimpleSystemsManagement;

    private String jwtSecret;

    private int jwtExpirationTime;


    public JwtUtils(AWSSecretsManager awsSecretsManager, AWSSimpleSystemsManagement awsSimpleSystemsManagement) {
        this.awsSecretsManager = awsSecretsManager;
        this.awsSimpleSystemsManagement = awsSimpleSystemsManagement;
    }

    @PostConstruct
    private void initJwtSetters(){
        setJwtSecret();
        setJwtExpirationTime();
    }


    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private void setJwtSecret(){
        if(jwtSecret == null){
            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(jwtSecretName);
            GetSecretValueResult getSecretValueResult = awsSecretsManager.getSecretValue(getSecretValueRequest);
            jwtSecret = getSecretValueResult.getSecretString();
        }
    }

    private void setJwtExpirationTime(){
        if(jwtExpirationTime == 0){
            GetParameterRequest request = new GetParameterRequest().withName(jwtExpirationTimeParameter).withWithDecryption(false);
            GetParameterResult result = awsSimpleSystemsManagement.getParameter(request);
            jwtExpirationTime = Integer.parseInt(result.getParameter().getValue());
        }
    }
}
