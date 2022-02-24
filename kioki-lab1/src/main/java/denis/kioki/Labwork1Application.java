package denis.kioki;

import denis.kioki.algorithm.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Labwork1Application {

	public static void main(String[] args) {
		SpringApplication.run(Labwork1Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			final String m = "CRYPTOGRAPHYANDDATASECURITY";
			final String codeWord = "SECRET";

			//RailFence
			String railFenceEncrypt = RailFence.encrypt(m, 4);
			String railFenceDecrypt = RailFence.decrypt(railFenceEncrypt, 4);
			System.out.println("Encrypted string by " + RailFence.class.getSimpleName() + ": " + railFenceEncrypt);
			System.out.println("Decrypted string by " + RailFence.class.getSimpleName() + ": " + railFenceDecrypt);
			System.out.println();

			//CodeWord
			String codeWordEncrypt = CodeWord.encrypt(m, codeWord);
			String codeWordDecrypt = CodeWord.decrypt(codeWordEncrypt, codeWord);
			System.out.println("Encrypted string by " + CodeWord.class.getSimpleName() + ": " + codeWordEncrypt);
			System.out.println("Decrypted string by " + CodeWord.class.getSimpleName() + ": " + codeWordDecrypt);
			System.out.println();

			//RotatingGrid
			List<Map.Entry<Integer, Integer>> indexes = List.of(
					Map.entry(0, 0),
					Map.entry(1, 3),
					Map.entry(2, 2),
					Map.entry(3, 1)
			);
			List<Map.Entry<Integer, Integer>> fakeIndexes = List.of(
					Map.entry(0, 3),
					Map.entry(2, 0),
					Map.entry(3, 1),
					Map.entry(1, 2)
			);
			String rotatingGridEncrypt = RotatingGrid.encrypt(m, indexes);
			String rotatingGridDecrypt = RotatingGrid.decrypt(rotatingGridEncrypt, fakeIndexes);
			System.out.println("Encrypted string by " + RotatingGrid.class.getSimpleName() + ": " + rotatingGridEncrypt);
			System.out.println("Decrypted string by " + RotatingGrid.class.getSimpleName() + ": " + rotatingGridDecrypt);
			System.out.println();

			//Cesar
			String cesarEncrypt = Cesar.encrypt("ýþÿ", 100);
			String cesarDecrypt = Cesar.decrypt(cesarEncrypt, 100);
			System.out.println("Encrypted string by " + Cesar.class.getSimpleName() + ": " + cesarEncrypt);
			System.out.println("Decrypted string by " + Cesar.class.getSimpleName() + ": " + cesarDecrypt);
			System.out.println();


			//Advances Cesar
			Map.Entry<Integer, Integer> keys = AdvancedCesar.getKeys();
			String advancedCesarEncrypt = AdvancedCesar.encrypt(m, keys.getKey());
			String advancedCesarDecrypt = AdvancedCesar.decrypt(advancedCesarEncrypt, keys.getValue());
			System.out.println("Encrypted string by " + AdvancedCesar.class.getSimpleName() + ": " + advancedCesarEncrypt);
			System.out.println("Decrypted string by " + AdvancedCesar.class.getSimpleName() + ": " + advancedCesarDecrypt);
			System.out.println();
		};
	}

}
