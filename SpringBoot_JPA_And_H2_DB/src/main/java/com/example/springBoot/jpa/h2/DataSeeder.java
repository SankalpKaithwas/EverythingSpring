package com.example.springBoot.jpa.h2;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.springBoot.jpa.h2.model.Product;
import com.example.springBoot.jpa.h2.repository.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner{

	private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
	@Override
	public void run(String... args) throws Exception {
		if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                new Product("Keyboard", 45.00),
                new Product("Optical Mouse", 19.99),
                new Product("Mechanical Keyboard", 110.00),
                new Product("HD Monitor 24in", 140.00),
                new Product("4K Monitor 27in", 320.00),
                new Product("USB-C Hub", 25.50),
                new Product("Desk Mat", 15.00),
                new Product("Webcam 1080p", 65.00),
                new Product("Noise Canceling Headphones", 180.00),
                new Product("Ergonomic Chair", 250.00),
                
             // Keyboards & Input Devices
                new Product("Standard USB Keyboard", 24.99),
                new Product("Mechanical Keyboard (Blue Switches)", 89.99),
                new Product("Mechanical Keyboard (Red Switches)", 94.50),
                new Product("Wireless Ergonomic Split Keyboard", 129.00),
                new Product("Tenkeyless (TKL) RGB Keyboard", 75.00),
                new Product("Optical Gaming Mouse", 39.99),
                new Product("Wireless Productivity Mouse", 29.50),
                new Product("Ergonomic Vertical Mouse", 49.99),
                new Product("Precision Trackball Mouse", 62.00),
                new Product("Wireless Presentation Clicker", 18.50),

                // Monitors & Display Accessories
                new Product("Full HD Monitor 24in", 139.99),
                new Product("Ultra-Slim Monitor 27in", 189.50),
                new Product("QHD Gaming Monitor 27in 144Hz", 279.00),
                new Product("4K UHD Monitor 32in", 399.99),
                new Product("Ultrawide Curved Monitor 34in", 480.00),
                new Product("Portable USB-C Monitor 15.6in", 159.00),
                new Product("Dual Monitor Arm Desk Mount", 65.00),
                new Product("Single Gas-Spring Monitor Arm", 42.00),
                new Product("Monitor Light Bar with Touch Sensor", 38.50),
                new Product("Privacy Screen Filter 24in", 27.00),

                // Audio & Video
                new Product("Noise Canceling Bluetooth Headphones", 179.99),
                new Product("Studio Monitoring Headphones", 149.00),
                new Product("True Wireless ANC Earbuds", 89.99),
                new Product("Wired USB Call Center Headset", 34.50),
                new Product("Cardioid Condenser USB Microphone", 69.99),
                new Product("Desktop Microphone Boom Arm", 29.00),
                new Product("Foam Microphone Pop Filter", 9.99),
                new Product("Webcam 1080p 60FPS", 59.99),
                new Product("Ultra HD 4K Conference Webcam", 129.00),
                new Product("10-inch LED Ring Light with Tripod", 24.50),

                // Cables, Hubs & Power
                new Product("8-in-1 USB-C Hub (Dual HDMI)", 45.00),
                new Product("Thunderbolt 4 Docking Station", 210.00),
                new Product("Braided USB-C to USB-C Cable (2m)", 12.99),
                new Product("High-Speed HDMI 2.1 Cable (3m)", 14.50),
                new Product("DisplayPort 1.4 Cable (1.8m)", 13.00),
                new Product("Cat 6 Gigabit Ethernet Cable (5m)", 8.99),
                new Product("65W GaN Fast Wall Charger", 32.00),
                new Product("100W Multi-Port Desktop Charger", 55.00),
                new Product("Surge Protector Power Strip (8 Outlets)", 26.50),
                new Product("Qi-Certified Wireless Charging Pad", 19.99),

                // Desk Setup, Storage & Ergonomics
                new Product("Extended Anti-Fray Desk Mat (XXL)", 19.99),
                new Product("Faux Leather Dual-Sided Desk Pad", 22.50),
                new Product("Ergonomic Memory Foam Wrist Rest", 14.00),
                new Product("Adjustable Aluminum Laptop Stand", 36.00),
                new Product("Vertical Laptop Docking Stand", 24.00),
                new Product("High-Back Mesh Ergonomic Chair", 249.99),
                new Product("Adjustable Under-Desk Footrest", 34.00),
                new Product("Electric Standing Desk Frame", 310.00),
                new Product("Under-Desk Cable Management Tray", 21.50),
                new Product("External Solid State Drive 1TB", 89.99),
                new Product("Portable External Hard Drive 2TB", 64.50),
                new Product("High-Endurance MicroSD Card 128GB", 18.00)
            ));
        }
		
	}
	
	/** To test pagination without typing 15 POST requests manually, let Spring insert sample data on startup.
	 * CommandLineRunner runs automatically once after the Spring application context starts up.*/

}
