import java.io.File;
import java.util.Scanner;

public class Main {
    static Joojle joojle = new Joojle();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("-------------------------");
            System.out.println("Joojle Search Engine Menu");
            System.out.println("1. Search");
            System.out.println("2. Add Documents");
            System.out.println("3. Completing Database");
            System.out.println("4. Exit");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.print("Keyword: ");
                    String keyword = scanner.next();
                    String result = joojle.search(keyword);
                    System.out.print(result);
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.print("Path: ");
                    String path = scanner.nextLine();
                    File folder = new File(path);
                    joojle.addDocuments(folder);
                    System.out.println("Documents added successfully");
                    break;
                case 3:
                    joojle.complete();
                    System.out.println("Database completed successfully");
                    break;
                case 4:
                    System.exit(0);
                    break;
                case 5:
                    joojle.print();
                    break;
                default:
                    System.out.println("Invalid input, Try again");
            }
        }
    }
}