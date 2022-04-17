package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {

		Lombok lombok = new Lombok();
		lombok.setData("hi");
		String data = lombok.getData();
		System.out.println("data = " + data);


		SpringApplication.run(JpashopApplication.class, args);
	}

}
