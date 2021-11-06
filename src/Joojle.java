import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Joojle {
    HashMap<String, HashSet<File>> keywords;
    HashSet<File> unprocessedDocuments;
    HashSet<File> processedDocuments;

    public Joojle() {
        unprocessedDocuments = new HashSet<>();
        processedDocuments = new HashSet<>();
        keywords = new HashMap<>();
    }

    public boolean addDocuments(File folder) {
        if (!folder.isDirectory() && !processedDocuments.contains(folder)) {
            unprocessedDocuments.add(folder);
            return true;
        }
        File[] listFiles = folder.listFiles();
        if (listFiles == null) {
            return false;
        }
        for (File fileEntry : listFiles) {
            if (fileEntry.isDirectory()) {
                addDocuments(fileEntry);
            } else if (!processedDocuments.contains(fileEntry)) {
                unprocessedDocuments.add(fileEntry);
            }
        }
        return true;
    }

    public void process() {
        String word;
        Scanner scanner;
        File document;
        Iterator<File> iterator = unprocessedDocuments.iterator();
        while (iterator.hasNext()) {
            document = iterator.next();
            try {
                scanner = new Scanner(document);
                while (scanner.hasNext()) {
                    word = scanner.next().toLowerCase();
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
            processedDocuments.add(document);
            iterator.remove();
        }
    }

    public String search(String keyword) {
        int index = 1;
        HashSet<File> documents = keywords.get(keyword.toLowerCase());
        if (documents == null) {
            return "0 result found\n";
        }
        String results = documents.size() + " result found\n";
        for (File document : documents) {
            results += index + ") " + document.getName() + "\n";
            index++;
        }
        return results;
    }

    public String status() {
        int index = 1;
        String status = "Unprocessed Documents:\n";
        for (File document : unprocessedDocuments) {
            status += index + ") " + document.getName() + "\n";
            index++;
        }
        status += "\n";
        index = 1;
        status += "Processed Documents:\n";
        for (File document : processedDocuments) {
            status += index + ") " + document.getName() + "\n";
            index++;
        }
        status += "\n";
        return status;
    }
}