package org.feelfee.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("params")
@Configuration
public class Globals {
    public static final String APP_ID = "7505819";
    public static final String CLIENT_SECRET = "JDXQL29WH97pdkrWSx76";
    public static final String SOCIAL_VK = "VK";
    public static final String SOCIAL_FB = "FB";


    @Getter
    @Setter
    private String hostUrl;

    @Getter
    @Setter
    private String redirectUrl;

    @Getter
    @Setter
    private String bucketName;

}
