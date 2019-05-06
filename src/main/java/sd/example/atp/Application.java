package sd.example.atp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping(value = "/customers") 
//@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Customer getCustomer(@PathVariable("id") Integer id) throws Exception {

		Customer cust = new Customer(id, "Sibendu", "Kolkata"); 
			
		DataSourceSample ds = new DataSourceSample();
		cust = ds.getCustomer(id.toString());
		return cust;// new ResponseEntity<List<Customer>>(null, HttpStatus.OK);
	}

}
