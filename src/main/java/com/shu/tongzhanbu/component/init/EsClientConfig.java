package com.shu.tongzhanbu.component.init;

import lombok.SneakyThrows;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import javax.net.ssl.SSLContext;
import java.util.List;

/**
 * @author tangyanqing
 * Description:
 * Date: 2020-06-28
 * Time: 17:38
 */
//@Configuration
public class EsClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${trust.store.path}")
    private String keyStore;
    @Value("${trust.store.password}")
    private String keyStorePassword;
    @Value("${elasticsearch.username}")
    private String esUser;
    @Value("${elasticsearch.password}")
    private String esPass;
    @Value("#{'${elasticsearch.hosts}'.split(',')}")
    private List<String> hosts;

    @SneakyThrows
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClassPathResource resource = new ClassPathResource(keyStore);
        String[] strings = {};
        String[] array = hosts.toArray(strings);
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(
                        resource.getURL(),
                        keyStorePassword.toCharArray()
                ).build();
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(array).usingSsl(sslContext).withBasicAuth(esUser, esPass)
                .withSocketTimeout(25 * 1000)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }


}
