package com.example.demo.application.accessories;

import java.util.Random;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class GeneratorServiceImpl implements GeneratorService {


	@Override
	public void stopConsoleForCode() {
		System.out.println("Copy the code you obtained and then press enter.");
		  try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	}
	
	@Override
	public String generateValidationCode() {
		int codeLength = 4;
		
		StringBuilder buffer = new StringBuilder(codeLength);
		
		for (int i = 0; i < codeLength; i++) {
			buffer.append(getRandomCharacter());
		}
		
		return buffer.toString();
	}
	
	
	private char getRandomCharacter() {
		Random random = new Random();
		char currentChar = 0;
		int p = (int) (random.nextFloat() * 3);
		if(p == 3.) p = 2;
		
		switch (p) {
		case 0:
			currentChar = getLowLetter();
			break;
		case 1:
			currentChar = getCapitalLetter();
			break;
		case 2:
			currentChar = getDigit();
			break;
		}
		
		return currentChar;
	}

	private char getDigit() {
		return getLetterBetween('0', '9');
	}

	private char getCapitalLetter() {
		return getLetterBetween('A', 'Z');
	}

	private char getLowLetter() {
		return getLetterBetween('a', 'z');
	}
	
	private char getLetterBetween(char c1, char c2) {
		Random r = new Random();
		return (char) (c1 + (r.nextFloat() * (c2 - c1 + 1)));
	}	

	
	
	
	
	
	
//	@Override
//	public String generateValidationCode() {
//		int targetStringLength = 4; // code length
//		int leftWordLimit = 65; // letter 'A'
//		int rightWordLimit = 90; // letter 'Z'
//		int leftLowerLimit = 97; // letter 'a'
//		int rightLowerLimit = 122; // letter 'z'
//		int leftNumberLimit = 48; // number '0'
//		int rightNumberLimit = 57; // number '9'
//
//		Random random = new Random();
//		StringBuilder buffer = new StringBuilder(targetStringLength);
//		for (int i = 0; i < targetStringLength; i++) {
//			double p = random.nextDouble();
//			if (p < 0.33) {
//				int randomLimitedInt = leftWordLimit
//						+ (int) (random.nextFloat() * (rightWordLimit - leftWordLimit + 1));
//				buffer.append((char) randomLimitedInt);
//			} else if (p < 0.66 && p >= 0.33) {
//				int randomLimitedInt = leftNumberLimit
//						+ (int) (random.nextFloat() * (rightNumberLimit - leftNumberLimit + 1));
//				buffer.append((char) randomLimitedInt);
//			} else {
//				int randomLimitedInt = leftLowerLimit
//						+ (int) (random.nextFloat() * (rightLowerLimit - leftLowerLimit + 1));
//				buffer.append((char) randomLimitedInt);
//			}
//		}
//		return buffer.toString();
//	}
	
	
	
	
	
	
	
}
