package Dev.springMVCrestart.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**1. Dispatcher Servlet & Context Registration (Dispatcher Initializer) */
public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { AppConfig.class };

	}

	@Override
	protected String[] getServletMappings() {
		// Map Spring to handle all requests
		return new String[] { "/" };
	}

}

/**
 * In Traditional Spring Framework To expose a single endpoint, you must
 * manually define the web configuration, register the dispatcher servlet, and
 * deploy it to a servlet container.
 */
