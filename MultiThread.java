import javax.swing.plaf.synth.SynthLookAndFeel;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MultiThread extends Thread{
    private int threadNumber;
    MultiThread(int threadNumber){
        this.threadNumber = threadNumber;
    }
    private static int count = 0;

    @Override
    public void run() {
        long ms = System.nanoTime();
        while(TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - ms) <= 30){
            try {
                async2();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public CompletableFuture<String> async2() throws URISyntaxException {
        String json = "{\"record\":\"yes\"}";
        RestTemplate template = new RestTemplate();
        String url = "http://localhost:8080/";
        HttpEntity httpEntity = new HttpEntity(json, createHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(new URI(url), httpEntity, String.class);
        System.out.println(response);
        System.out.println("Thread -"+ threadNumber);
        System.out.println(++count);
        return CompletableFuture.completedFuture(response.getBody());
    }

    static HttpHeaders createheaders(){
        return new HttpHeaders(){
            String auth = "fbhdhb:"+"password";
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic" + new String(encodedAuth);
            set("Authorization", authHeader);
            set("Content-Type","application/vnd.kafka");
            set("Accept", "application/vnd.kafka.v2+json");
        };
    }
}
