package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Tokenizer tokenizer = null;
        while (tokenizer == null) {
            System.out.print("Please enter the filename: ");
            String filename = input.nextLine();
            File file = new File(filename);
            try {
                tokenizer = new Tokenizer(file);
            } catch (IOException e) {
                System.out.println("Failed to read file: " + filename);
            }
        }

        for(Map.Entry<Token, Integer> count : tokenizer.getCount().entrySet()) {
            System.out.println(String.format("%20s : %d instances",count.getKey(), count.getValue()));
        }
    }
}
