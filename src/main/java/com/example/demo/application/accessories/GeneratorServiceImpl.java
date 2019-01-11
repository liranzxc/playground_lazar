package com.example.demo.application.accessories;

import java.util.Random;

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
	
	
}
