package Dev.springMVCrestart.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

	public int add(int i, int j) {
		return i + j;
	}

	// Dependency Injection: Spring creates and manages the ServiceClass bean via
	// @Service and passes it directly to your @Controller constructor.
}
