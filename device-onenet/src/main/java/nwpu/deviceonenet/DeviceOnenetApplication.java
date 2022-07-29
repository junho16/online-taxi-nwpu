package nwpu.deviceonenet;

import com.onenet.studio.acc.sdk.annotations.ThingsModelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ThingsModelConfiguration("src/main/resources/model.json")
public class DeviceOnenetApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceOnenetApplication.class, args);
    }

}
