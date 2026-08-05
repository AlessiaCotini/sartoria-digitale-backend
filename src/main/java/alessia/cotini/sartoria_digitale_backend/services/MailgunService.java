package alessia.cotini.sartoria_digitale_backend.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class MailgunService {

    @Value("${mailgun.apikey}")
    private String apiKey;

    @Value("${mailgun.domainname}")
    private String domainName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void invia(String destinatario, String oggetto, String testo) {
        if (destinatario == null || destinatario.isBlank()) return;

        String url = "https://api.mailgun.net/v3/" + domainName + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String credenziali = Base64.getEncoder()
                .encodeToString(("api:" + apiKey).getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + credenziali);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("from", "Bellariva <postmaster@" + domainName + ">");
        body.add("to", destinatario);
        body.add("subject", oggetto);
        body.add("text", testo);

        try {
            restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        } catch (Exception e) {
            // un'email non riuscita non deve mai bloccare l'operazione principale
            e.printStackTrace();
        }
    }
}