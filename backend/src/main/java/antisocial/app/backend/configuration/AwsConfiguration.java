package antisocial.app.backend.configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {

    @Value("${region}")
    private String region;

    @Bean
    public AmazonS3 getS3Client(){
        return  AmazonS3ClientBuilder.standard().withRegion(region).build();
    }

    @Bean
    public AWSSecretsManager getSecretManager(){
        return AWSSecretsManagerClientBuilder.standard().withRegion(region).build();
    }

    @Bean
    public AWSSimpleSystemsManagement getSystemManager(){
        return AWSSimpleSystemsManagementClientBuilder.standard().withRegion(region).build();
    }
}
