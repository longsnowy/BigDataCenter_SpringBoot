package snow;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("snow.mapper")
public class BigDataCenterSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigDataCenterSpringBootApplication.class, args);
    }

}
