/*Tanisha Hossain and Tihami Islam
Find The Princess
Description: A princess version of the popular game Spot It! where the player must identify the matching princess in both cards given before the timer runs out.
Created: September 24, 2024
Last Edited: October 7, 2024
Ethics Declaration: "This code is our own work"
*/

import java.util.*;

public class Main extends Thread {

    static Scanner scanner = new Scanner(System.in); // scanner for player's guesses during the game
    static Scanner options = new Scanner(System.in); // scanner for player's options when not in game yet
    static Random random = new Random();
    static int cardOne;
    static int cardTwo;
    static String guess = "";
    static int score = 10;
    static boolean correctAnswer = false; // checks if player's guess is correct
    static boolean continueToNextRound = false;
    static int numberOfRounds = 0;
    // Coloured text
    static String RED = "\u001B[31m";
    static String YELLOW = "\u001B[33m";
    static String MAGENTA = "\u001B[35m";
    static String BLUE = "\u001B[34m";
    static String RESET = "\u001B[0m";

    // A simplified deck with cards
    static String[][] deck = {
            { "aurora", "belle", "cinderella", "diana" },
            { "aurora", "esmeralda", "fiona", "giselle" },
            { "aurora", "harumi", "iduna", "jasmine" },
            { "aurora", "kiara", "leia", "mulan" },

            { "belle", "esmeralda", "harumi", "kiara" },
            { "belle", "fiona", "iduna", "leia" },
            { "belle", "giselle", "jasmine", "mulan" },

            { "cinderella", "esmeralda", "iduna", "mulan" },
            { "cinderella", "fiona", "jasmine", "kiara" },
            { "cinderella", "giselle", "harumi", "leia" },

            { "diana", "esmeralda", "jasmine", "leia" },
            { "diana", "fiona", "harumi", "mulan" },
            { "diana", "giselle", "iduna", "kiara" },

    };

    static void intro() {
        // intro
        System.out.println("\n\033[0;1mFIND THE PRINCESS!\033[0;0m"); // bolds text
        System.out.println(
                "\nWelcome to the wonderfully peaceful kingdom of Teetaw. Recently there has been a strange case of princesses getting cloned!! The kingdom is in desperate need of help and requires an observant knight to catch all the duplicate princesses before it's too late! Will you be able to save the once peaceful kingdom from ruin...?");
        System.out.println("\n\033[0;1mBy: Tihami Islam and Tanisha Hossain\033[0;0m");
        System.out.println(YELLOW +"\nPlease type HELP for instructions. Type P to play! To exit game, type EXIT." + RESET);
    }

    static void menu() {

        String menu = options.nextLine();

        switch (menu) {
            case "HELP":
                showHelp();
                menu();
                break;

            case "P":
                System.out.println("Enter number of rounds: ");
                boolean valid = false;
                while (!valid) {
                    try {
                        numberOfRounds = options.nextInt();
                        if (numberOfRounds >= 1 && numberOfRounds <= 15) {
                            valid = true;
                        } else {
                            System.out.println("Please enter a number between 1 and 15.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid number of rounds");
                        options.next();
                    }
                }
                loadRound();
                break;

            case "EXIT":
                leaveGame();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                menu();
                break;

        }
    }

    static void showHelp() {
        System.out.println("\nEach round, you will be given two cards with four princesses each.");
        System.out.println(
                "Your job is to find which princess is the same on both cards and enter their name before the ten second timer runs out.");
        System.out.println(
                "\nYou will start with ten points...don't run out of them! For every correct answer you earn five points, but every wrong answer deducts five points. You may choose to play between 1 and 15 rounds.\n");
        System.out.println(YELLOW + "Type P to play! Type EXIT to exit game.\n" + RESET);
    }

    static void leaveGame() {
        System.out.println(RED + "\nThanks for playing! See you next time!" + RESET);
        System.exit(0);
    }

    static void loadRound() {

        for (int round = 0; round < numberOfRounds; round++) {
            correctAnswer = false;
            continueToNextRound = false;
            chooseCards();
            askAndCheck();
            System.out.println("Your score is " + score + "\n");
        }
    }

    static void chooseCards() {
        cardOne = (int) (Math.random() * deck.length);
        cardTwo = (int) (Math.random() * deck.length);
        // ensures the same cards are not chosen
        while (cardOne == cardTwo) {
            cardTwo = (int) (Math.random() * deck.length);
        }

        // prints first random card from deck
        for (String princess : deck[cardOne]) {
            System.out.print(MAGENTA + princess + " " + RESET);
        }

        System.out.println("");

        // prints second random card from deck
        for (String princess : deck[cardTwo]) {
            System.out.print(BLUE + princess + " " + RESET);
        }

    }

    static void askAndCheck() {

        Main myThread = new Main(); // creating a new class named myThread
        myThread.start(); // myThread jumps to run method below and starts running timer in background

        System.out.println("\n\033[0;1mWhich princess appears twice?\033[0;0m");
        // ensures player can ask for help and exit during game time
        do {
            if (guess.equals("HELP")) {
                showHelp();
            } else if (guess.equals("EXIT")) {
                leaveGame();
            }
            guess = scanner.nextLine();
        } while (guess.equals("HELP") || guess.equals("EXIT"));

        // checks if timer has run out
        if (continueToNextRound) { // continueToNextRound boolean was intialized to false
            score -= 5;
            return;
        }
        // checks if timer hasn't run out and the guess is correct
        else if (Arrays.asList(deck[cardOne]).contains(guess) && Arrays.asList(deck[cardTwo]).contains(guess)) {
            myThread.interrupt(); // show InterruptedException to interrupt execution of myThread timer
            System.out.println("\033[0;1mCorrect! Yahoo!\033[0;0m");
            score += 5;
            correctAnswer = true;
        }
        // checks if timer hasn't run out and the guess is not correct
        else {
            myThread.interrupt(); // interrupts timer
            System.out.println("\033[0;1mIncorrect...womp womp\033[0;0m");
            score -= 5;
            correctAnswer = false;
        }
    }


    public void run() {
        try {
            timer();
        } catch (InterruptedException e) {
            // System.out.println("Timer was interrupted");
        }
    }


    static void timer() throws InterruptedException {
        Thread.sleep(10000);
        if (!correctAnswer) {
            System.out.println(RED + "TIME HAS RUN OUT! You lose 5 points :( Press enter to move on." + RESET);
            continueToNextRound = true;
        }
    }


    static void finishGame(){
        System.out.println("Your final score is " + score + "!");
        System.out.println(YELLOW + "Want to play again? Type P to play again. Type EXIT to exit game." + RESET);
        String playAgain = scanner.nextLine();
        switch (playAgain){
            case "P":
                replay();
                break;
            case "EXIT":
                leaveGame();
                break;
            default:
                System.out.println(YELLOW + "Please type either P or EXIT." + RESET);
        }

    }


    static void replay(){
        score = 10;
        guess = "";
        correctAnswer = false;
        continueToNextRound = false;
        System.out.println("Starting new game...");
        menu();
        finishGame();
    }


    public static void main(String[] args) {
        intro();
        menu();
        finishGame();

    } // end main

} // end class

