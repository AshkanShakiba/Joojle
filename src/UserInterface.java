import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
            System.out.println("5. Dev Menu");
            System.out.println("6. Exit");
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
                    devMenu();
                    break;
                case 6:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input, Try again");
            }
        }
    }

    private static void searchMenu() {
        System.out.print("Search: ");
        // String keyword = scanner.next();
        // HashSet<File> documentsSet = joojle.simpleSearch(keyword);
        scanner.nextLine();
        String keywords = scanner.nextLine();
        HashSet<File> documentsSet = joojle.search(keywords);
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
        printDocument(documentsArray[choice - 1]);
    }

    private static void printDocument(File document) {
        System.out.println("Name:\n" + document.getName());
        System.out.println("Path:\n" + document.getPath());
        System.out.print("Context:\n");
        try {
            Scanner scanner = new Scanner(document);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void devMenu() {
        while (true) {
            System.out.println("* Dev Menu *");
            System.out.println("1. Keywords Map");
            System.out.println("2. Stop Words");
            System.out.println("3. Back");
            switch (scanner.nextInt()) {
                case 1:
                    int index = 1;
                    HashSet<File> documents;
                    Iterator<File> documentsIterator;
                    HashMap<String, HashSet<File>> keywords = joojle.getKeywords();
                    Iterator<String> keywordsIterator = keywords.keySet().iterator();
                    System.out.println("Keywords:");
                    while (keywordsIterator.hasNext()) {
                        String key = keywordsIterator.next();
                        System.out.print(index + ") " + key + " -> {");
                        documents = keywords.get(key);
                        documentsIterator = documents.iterator();
                        while (true) {
                            System.out.print(documentsIterator.next().getName());
                            if (documentsIterator.hasNext()) {
                                System.out.print(", ");
                            } else {
                                break;
                            }
                        }
                        System.out.println("}");
                        index++;
                    }
                    break;
                case 2:
                    HashSet<String> stopWords = joojle.getStopWords();
                    Iterator<String> stopWordsIterator = stopWords.iterator();
                    System.out.println("Stop Words:");
                    while (true) {
                        System.out.print(stopWordsIterator.next());
                        if (stopWordsIterator.hasNext()) {
                            System.out.print(", ");
                        } else {
                            System.out.println();
                            break;
                        }
                    }
                    break;
                case 3:
                    mainMenu();
                    return;
                default:
                    System.out.println("Invalid input, Try again");
            }
        }
    }
}