package Dev.springMVCrestart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//2. Spring MVC Config Bean, The Web Configuration is as follows - 
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "Dev.springMVCrestart" })
public class AppConfig implements WebMvcConfigurer {
	// Manually register view resolvers, message converters, etc.
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		//resolver.setPrefix("/WEB-INF/");  specifying the path of jsp files
		resolver.setPrefix("/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}

/**
//If not providing .jsp extension in mv.setViewName() then we need to do internalResourceViewResolver bit.
//Don't need the "dispatcher-servlet.xml" file now. This class is replacement for dispatcher-servelet.xml file.
//(Refer - LegacySpringXMLConfig Project)
//Dispatcher servlet (dispatcher-servlet.xml) replacement.  (in this we configure the dispatcher-Servlet)
//EnableWebMvc is to specify that we are going to use Annotations
//@Configuration is to specify that this is a configuration file
 */


/**
 * @Configuration: Marks the class as a source of bean definitions for the
 *                 Spring IoC container.
 */

/**
 * @EnableWebMvc: Activates default Spring MVC configurations
 *                (enables @Controller, @RequestMapping, parameter type
 *                conversion, JSON/XML support, etc.).
 */

/**
 * @ComponentScan("Dev.springMVCrestart"): Instructs Spring to scan that package
 * and its sub-packages for annotated classes
 * (like @Controller, @Service, @Component) and automatically instantiate them.
 */

/**
 * configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer):
 * Overrides the default fallback handler. Calling configurer.enable() instructs
 * the DispatcherServlet to forward unmapped or static requests (such as JSP
 * files, images, CSS) directly to Tomcat's default servlet instead of throwing
 * a 404.
 */

/**
 * InternalResourceViewResolver (commented out in your code): When enabled, this
 * bean translates logical view names like "display" into physical file paths
 * like /display.jsp.
 */