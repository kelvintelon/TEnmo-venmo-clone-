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
                System.exit(0);
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
        long yourUserLong = currentUser.getUser().getId();
        int yourUserAsInt = (int)yourUserLong;
		Transfer[] allTransfers = transferService.getAllTransfersByUserId(yourUserAsInt);
        System.out.println();
        System.out.println("This is your transfer history:");
        for (Transfer transfer : allTransfers) {
            System.out.println();
            System.out.println("Transfer ID: " + transfer.getTransfer_id());
            System.out.println("From: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(transfer.getAccount_from())));
            System.out.println("To: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(transfer.getAccount_to())));
            System.out.println("Amount: $" + transfer.getAmount());

        }
        System.out.println();
        int transferID = consoleService.promptForInt("Enter transfer ID to see more details:");
        Transfer returnedTransfer = transferService.getTransferByTransferId(transferID);
        System.out.println();
        System.out.println("Transfer details");
        System.out.println("----------------");
        System.out.println("Transfer ID: " + returnedTransfer.getTransfer_id());
        System.out.println("From: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(returnedTransfer.getAccount_from())));
        System.out.println("To: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(returnedTransfer.getAccount_to())));
        if (returnedTransfer.getTransfer_type_id() == 2) {
            System.out.println("Type: Send");
        }
        if (returnedTransfer.getTransfer_type_id() == 1) {
            System.out.println("Type: Request");
        }
        if (returnedTransfer.getTransfer_status_id() == 2) {
            System.out.println("Status: Approved");
        }
        if (returnedTransfer.getTransfer_status_id() == 1) {
            System.out.println("Status: Pending");
        }
        if (returnedTransfer.getTransfer_status_id() == 3) {
            System.out.println("Status: Rejected");
        }
        System.out.println("Amount: $" + returnedTransfer.getAmount());
	}

	private void viewPendingRequests() {
        long youUserLong = currentUser.getUser().getId();
        int yourUserInt = (int) youUserLong;
		Transfer[] pendingTransfers = transferService.getAllPendingTransfersByUserId(yourUserInt);
        System.out.println("These are your pending transfers: ");
        for (Transfer p : pendingTransfers) {
            System.out.println("Transfer ID: " + p.getTransfer_id());
            System.out.println("Account to: " + p.getAccount_to());
            System.out.println("Account from: " + p.getAccount_from());
            System.out.println("Transfer Amount: " + p.getAmount());
            System.out.println("Transfer Status: " + p.getTransfer_status_id());
            System.out.println("Transfer Type: " + p.getTransfer_type_id());
            System.out.println();
        }
        int transferId = consoleService.promptForInt("Please enter transfer ID to approve or reject: ");
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("0. Don't approve or reject");
        int choice = consoleService.promptForInt("Please choose an option: ");
        if (choice == 1) {
            Transfer approvedTransfer = transferService.getTransferByTransferId(transferId);
            if (approvedTransfer.getAccount_from() == accountService.findAccountIdByUserId(currentUser.getUser().getId())) {

                approvedTransfer.setTransfer_type_id(2);
                approvedTransfer.setTransfer_status_id(2);

                if (approvedTransfer.getAmount().compareTo(accountService.getBalance()) > 0) {
                    System.out.println("You can't send more money than you have.");
                    return;
                }
                if (approvedTransfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("You can't send zero or less than zero.");
                    return;
                }
                transferService.sendBucks(approvedTransfer);
                System.out.println();
                System.out.println("Transfer has been completed: ");
                System.out.println("From: " + currentUser.getUser().getUsername());
                System.out.println("To: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(approvedTransfer.getAccount_to())));
                System.out.println("Amount: $" + approvedTransfer.getAmount());
            } else {
                System.out.println("You are not allowed to approve a request for an account you do not own.");
            }

        }
        if (choice == 2) {
            Transfer rejectedTransfer = transferService.getTransferByTransferId(transferId);
            rejectedTransfer.setTransfer_status_id(3);
            System.out.println("Transfer has been rejected");

        }
        if (choice == 3) {
            return;
        }

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
        getAllUsers();

        long yourUserLong = currentUser.getUser().getId();
        long yourAccountId = accountService.findAccountIdByUserId(yourUserLong);
        int yourAccountIdAsInt = (int) yourAccountId;
        System.out.println("Your Account ID is " + yourAccountIdAsInt);

        int destinationAccount = consoleService.promptForInt("Enter account ID you would like to request from: ");


        if (yourAccountId == destinationAccount) {
            System.out.println("You aren't allowed to request money from yourself.");
            return;
        }

        String amountToRequest = consoleService.promptForString("How much would you like to request? ");
        BigDecimal amount = new BigDecimal(amountToRequest);

//        if (amount.compareTo(accountService.getBalance()) > 0) {
//            System.out.println("You can't send more money than you have.");
//            return;
//        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("You can't request zero or less than zero.");
            return;
        }
        Transfer newTransfer = new Transfer();
        newTransfer.setTransfer_status_id(1);
        newTransfer.setTransfer_type_id(1);
        newTransfer.setAccount_to(yourAccountIdAsInt);
        newTransfer.setAccount_from(destinationAccount);
        newTransfer.setAmount(amount);

        transferService.sendBucks(newTransfer);
        System.out.println();
        System.out.println("Request has been made: ");
        System.out.println("To: " + currentUser.getUser().getUsername());
        System.out.println("From: " + accountService.findUsernameByUserId(accountService.findUserIdByAccountId(destinationAccount)));
        System.out.println("Amount: $" + amount);

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
