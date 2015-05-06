package jsondemo.service;

import com.google.common.io.ByteSource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class UpdateService {

    @Value("${tamr.url}")
    private String url;


    public void dosomething(final ByteSource map){

        final RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add("Content-type", "application/octet-stream");
                IOUtils.copy(map.openBufferedStream(), request.getBody());
            }
        };

        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(requestFactory);

        final HttpMessageConverterExtractor<String> responseExtractor =
                new HttpMessageConverterExtractor<String>(String.class, restTemplate.getMessageConverters());
        restTemplate.execute("http://localhost:8888", HttpMethod.POST, requestCallback, responseExtractor);


    }

}

