package com.example.springBoot.jpa.h2;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.springBoot.jpa.h2.model.Product;
import com.example.springBoot.jpa.h2.repository.ProductRepository;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner{

	private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
	@Override
	public void run(String... args) throws Exception {
		if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                new Product("Keyboard", new BigDecimal("45.00")),
                new Product("Optical Mouse", new BigDecimal("19.99")),
                new Product("Mechanical Keyboard", new BigDecimal("110.00")),
                new Product("HD Monitor 24in", new BigDecimal("140.00")),
                new Product("4K Monitor 27in", new BigDecimal("320.00")),
                new Product("USB-C Hub", new BigDecimal("25.50")),
                new Product("Desk Mat", new BigDecimal("15.00")),
                new Product("Webcam 1080p", new BigDecimal("65.00")),
                new Product("Noise Canceling Headphones", new BigDecimal("180.00")),
                new Product("Ergonomic Chair", new BigDecimal("250.00")),

                // Keyboards & Input Devices
                new Product("Standard USB Keyboard", new BigDecimal("24.99")),
                new Product("Mechanical Keyboard (Blue Switches)", new BigDecimal("89.99")),
                new Product("Mechanical Keyboard (Red Switches)", new BigDecimal("94.50")),
                new Product("Wireless Ergonomic Split Keyboard", new BigDecimal("129.00")),
                new Product("Tenkeyless (TKL) RGB Keyboard", new BigDecimal("75.00")),
                new Product("Optical Gaming Mouse", new BigDecimal("39.99")),
                new Product("Wireless Productivity Mouse", new BigDecimal("29.50")),
                new Product("Ergonomic Vertical Mouse", new BigDecimal("49.99")),
                new Product("Precision Trackball Mouse", new BigDecimal("62.00")),
                new Product("Wireless Presentation Clicker", new BigDecimal("18.50")),

                // Monitors & Display Accessories
                new Product("Full HD Monitor 24in", new BigDecimal("139.99")),
                new Product("Ultra-Slim Monitor 27in", new BigDecimal("189.50")),
                new Product("QHD Gaming Monitor 27in 144Hz", new BigDecimal("279.00")),
                new Product("4K UHD Monitor 32in", new BigDecimal("399.99")),
                new Product("Ultrawide Curved Monitor 34in", new BigDecimal("480.00")),
                new Product("Portable USB-C Monitor 15.6in", new BigDecimal("159.00")),
                new Product("Dual Monitor Arm Desk Mount", new BigDecimal("65.00")),
                new Product("Single Gas-Spring Monitor Arm", new BigDecimal("42.00")),
                new Product("Monitor Light Bar with Touch Sensor", new BigDecimal("38.50")),
                new Product("Privacy Screen Filter 24in", new BigDecimal("27.00")),

                // Audio & Video
                new Product("Noise Canceling Bluetooth Headphones", new BigDecimal("179.99")),
                new Product("Studio Monitoring Headphones", new BigDecimal("149.00")),
                new Product("True Wireless ANC Earbuds", new BigDecimal("89.99")),
                new Product("Wired USB Call Center Headset", new BigDecimal("34.50")),
                new Product("Cardioid Condenser USB Microphone", new BigDecimal("69.99")),
                new Product("Desktop Microphone Boom Arm", new BigDecimal("29.00")),
                new Product("Foam Microphone Pop Filter", new BigDecimal("9.99")),
                new Product("Webcam 1080p 60FPS", new BigDecimal("59.99")),
                new Product("Ultra HD 4K Conference Webcam", new BigDecimal("129.00")),
                new Product("10-inch LED Ring Light with Tripod", new BigDecimal("24.50")),

                // Cables, Hubs & Power
                new Product("8-in-1 USB-C Hub (Dual HDMI)", new BigDecimal("45.00")),
                new Product("Thunderbolt 4 Docking Station", new BigDecimal("210.00")),
                new Product("Braided USB-C to USB-C Cable (2m)", new BigDecimal("12.99")),
                new Product("High-Speed HDMI 2.1 Cable (3m)", new BigDecimal("14.50")),
                new Product("DisplayPort 1.4 Cable (1.8m)", new BigDecimal("13.00")),
                new Product("Cat 6 Gigabit Ethernet Cable (5m)", new BigDecimal("8.99")),
                new Product("65W GaN Fast Wall Charger", new BigDecimal("32.00")),
                new Product("100W Multi-Port Desktop Charger", new BigDecimal("55.00")),
                new Product("Surge Protector Power Strip (8 Outlets)", new BigDecimal("26.50")),
                new Product("Qi-Certified Wireless Charging Pad", new BigDecimal("19.99")),

                // Desk Setup, Storage & Ergonomics
                new Product("Extended Anti-Fray Desk Mat (XXL)", new BigDecimal("19.99")),
                new Product("Faux Leather Dual-Sided Desk Pad", new BigDecimal("22.50")),
                new Product("Ergonomic Memory Foam Wrist Rest", new BigDecimal("14.00")),
                new Product("Adjustable Aluminum Laptop Stand", new BigDecimal("36.00")),
                new Product("Vertical Laptop Docking Stand", new BigDecimal("24.00")),
                new Product("High-Back Mesh Ergonomic Chair", new BigDecimal("249.99")),
                new Product("Adjustable Under-Desk Footrest", new BigDecimal("34.00")),
                new Product("Electric Standing Desk Frame", new BigDecimal("310.00")),
                new Product("Under-Desk Cable Management Tray", new BigDecimal("21.50")),
                new Product("External Solid State Drive 1TB", new BigDecimal("89.99")),
                new Product("Portable External Hard Drive 2TB", new BigDecimal("64.50")),
                new Product("High-Endurance MicroSD Card 128GB", new BigDecimal("18.00"))
            ));
        }		
	}
	
	/** To test pagination without typing 15 POST requests manually, let Spring insert sample data on startup.
	 * CommandLineRunner runs automatically once after the Spring application context starts up.*/

}
