package indicatorservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(info = @Info(title = "Indicator Service Movies ", version = "1.0.0"))
@SpringBootApplication
public class IndicatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndicatorServiceApplication.class, args);
	}

}
