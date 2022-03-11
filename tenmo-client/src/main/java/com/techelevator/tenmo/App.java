package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLOutput;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());

            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is: " + accountService.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		getAllUsers();

        long yourUserLong = currentUser.getUser().getId();
        long yourAccountId = accountService.findAccountIdByUserId(yourUserLong);
        int yourAccountIdAsInt = (int) yourAccountId;
        System.out.println("Your Account ID is " + yourAccountIdAsInt);

        int destinationAccount = consoleService.promptForInt("Enter account ID you would like to send to: ");


        if (yourAccountId == destinationAccount) {
            System.out.println("You aren't allowed to send money to yourself.");
            return;
        }

        String amountToSend = consoleService.promptForString("How much would you like to send? ");
        BigDecimal amount = new BigDecimal(amountToSend);
        if (amount.compareTo(accountService.getBalance()) > 0) {
            System.out.println("You can't send more money than you have.");
            return;
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("You can't send zero or less than zero.");
            return;
        }
        Transfer newTransfer = new Transfer();
        newTransfer.setTransfer_status_id(2);
        newTransfer.setTransfer_type_id(2);
        newTransfer.setAccount_from(yourAccountIdAsInt);
        newTransfer.setAccount_to(destinationAccount);
        newTransfer.setAmount(amount);

        transferService.sendBucks(newTransfer);
        System.out.println();
        System.out.println("Transfer has been completed: ");
        System.out.println("From: " + currentUser.getUser().getUsername());
        System.out.println("To: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(destinationAccount)));
        System.out.println("Amount: $" + amount);

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
    private void getAllUsers() {
        Account[] allAccounts = accountService.getAllAccounts();
        System.out.println("The following users are available to send money to: ");
        System.out.println();
        for (Account account : allAccounts) {
            System.out.println("Username: " + accountService.findUsernameByUserId(account.getUserId()));
            System.out.println("User ID: " + account.getUserId());
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println();
        }


    }

}
