package com.example.demo.services.otherservices;

import java.util.Random;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class GeneratorService implements IGeneratorService {

	private Scanner wait = new Scanner(System.in);

	@Override
	public void stopConsoleForCode() {
		System.out.println("Copy the code you provided and then press any key.");
		wait.next();
	}

	@Override
	public String generateValidationCode() {
		int targetStringLength = 4; // code length
		int leftWordLimit = 65; // letter 'A'
		int rightWordLimit = 90; // letter 'Z'
		int leftLowerLimit = 97; // letter 'a'
		int rightLowerLimit = 122; // letter 'z'
		int leftNumberLimit = 48; // number '0'
		int rightNumberLimit = 57; // number '9'

		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			double p = random.nextDouble();
			if (p < 0.33) {
				int randomLimitedInt = leftWordLimit
						+ (int) (random.nextFloat() * (rightWordLimit - leftWordLimit + 1));
				buffer.append((char) randomLimitedInt);
			} else if (p < 0.66 && p >= 0.33) {
				int randomLimitedInt = leftNumberLimit
						+ (int) (random.nextFloat() * (rightNumberLimit - leftNumberLimit + 1));
				buffer.append((char) randomLimitedInt);
			} else {
				int randomLimitedInt = leftLowerLimit
						+ (int) (random.nextFloat() * (rightLowerLimit - leftLowerLimit + 1));
				buffer.append((char) randomLimitedInt);
			}
		}
		return buffer.toString();
	}
}
