package jsondemo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpdateService {

    @Value("${tamr.url}")
    private String url;


    public void dosomething(Object map){
        RestTemplate template = new RestTemplate();
        template.put(url,map);
    }

}

