import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class UserInterface {
    private static final Joojle joojle = new Joojle();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("-------------------------");
            System.out.println("Joojle Search Engine Menu");
            System.out.println("1. Search");
            System.out.println("2. Add Documents");
            System.out.println("3. Process Documents");
            System.out.println("4. Documents Status");
            System.out.println("5. Exit");
            switch (scanner.nextInt()) {
                case 1:
                    searchMenu();
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.print("Path: ");
                    String path = scanner.nextLine();
                    File file = new File(path);
                    if (joojle.addDocuments(file)) {
                        System.out.println("Documents added successfully");
                    } else {
                        System.out.println("Invalid path");
                    }
                    break;
                case 3:
                    joojle.process();
                    System.out.println("Database completed successfully");
                    break;
                case 4:
                    System.out.print(joojle.status());
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input, Try again");
            }
        }
    }

    private static void searchMenu() {
        System.out.print("Keyword: ");
        String keyword = scanner.next();
        HashSet<File> documentsSet = joojle.search(keyword);
        if (documentsSet == null) {
            System.out.println("0 result found");
            return;
        }
        System.out.println("0) Back");
        File[] documentsArray = documentsSet.toArray(new File[documentsSet.size()]);
        for (int i = 0; i < documentsArray.length; i++) {
            System.out.println((i + 1) + ") " + documentsArray[i].getName());
        }
        int choice = scanner.nextInt();
        if (choice == 0) return;
        // printDocument(documentsArray[choice - 1]);
    }
}