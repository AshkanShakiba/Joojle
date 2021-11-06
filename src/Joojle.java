import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Joojle {
    HashSet<File> documents;
    HashMap<String, HashSet<File>> keywords;

    public Joojle() {
        documents = new HashSet<>();
        keywords = new HashMap<>();
    }

    public void addDocuments(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                addDocuments(fileEntry);
            } else {
                documents.add(fileEntry);
            }
        }
    }

    public void complete() {
        String word;
        Scanner scanner;
        File document;
        Iterator<File> iterator = documents.iterator();
        while (iterator.hasNext()) {
            document = iterator.next();
            try {
                scanner = new Scanner(document);
                while (scanner.hasNext()) {
                    word = scanner.next();
                    HashSet<File> documents = keywords.get(word);
                    if (documents == null) {
                        documents = new HashSet<>();
                    }
                    documents.add(document);
                    keywords.put(word, documents);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            iterator.remove();
        }
    }

    public void print() {
        for (File document : documents) {
            System.out.println(document.getName());
        }
    }
}