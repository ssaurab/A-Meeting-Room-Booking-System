

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringServerApplication.class, args);
        openHomePage();
    }

    private static void openHomePage() {
        try {
            String url = "http://localhost:8080";
            String Command = "open " + url;
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().contains("windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (osName.toLowerCase().contains("mac")) {
                Process Child = Runtime.getRuntime().exec(Command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

