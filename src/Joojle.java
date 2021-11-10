import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Joojle {
    private final HashMap<String, HashSet<File>> keywords;
    private final HashSet<File> unprocessedDocuments;
    private final HashSet<File> processedDocuments;
    private final HashSet<String> stopWords;

    public Joojle() {
        keywords = new HashMap<>();
        unprocessedDocuments = new HashSet<>();
        processedDocuments = new HashSet<>();
        stopWords = new HashSet<>();
        initialStopWords();
    }

    private void initialStopWords() {
        // Using text file allow us to edit them outside the application
        File file = new File("data/stop-words.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                stopWords.add(scanner.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean addDocuments(File file) {
        if (file.isFile() && !processedDocuments.contains(file)) {
            unprocessedDocuments.add(file);
            return true;
        }
        File[] listFiles = file.listFiles();
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
                    word = scanner.next();
                    word = removeSymbols(word.toLowerCase());
                    if (stopWords.contains(word)) continue;
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

    // old search method
    public HashSet<File> getResults(String keyword) {
        keyword = removeSymbols(keyword.toLowerCase());
        return keywords.get(keyword);
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

    // All word's characters have been converted to lower case
    private String removeSymbols(String word) {
        char character;
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            character = word.charAt(i);
            if ('a' <= character && character <= 'z') {
                result += character;
            }
        }
        return result;
    }

    public HashMap<String, HashSet<File>> getKeywords() {
        return keywords;
    }

    public HashSet<String> getStopWords() {
        return stopWords;
    }

    public HashSet<File> search(String searchInput) {
        HashSet<File> results = new HashSet<>();
        String[] keywords = searchInput.split(" ");
        ArrayList<String> plusWords = new ArrayList<>();
        ArrayList<String> simpleWords = new ArrayList<>();
        ArrayList<String> hyphenWords = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword.startsWith("+")) {
                plusWords.add(keyword);
            } else if (keyword.startsWith("-")) {
                hyphenWords.add(keyword);
            } else {
                simpleWords.add(keyword);
            }
        }
        HashSet<File> result;
        result = getResults(simpleWords.get(0));
        if (result != null) results.addAll(result);
        for (int i = 1; i < simpleWords.size(); i++) {
            result = getResults(simpleWords.get(i));
            if (result != null) results.retainAll(result);
        }
        for (String word : hyphenWords) {
            result = getResults(word);
            if (result != null) results.removeAll(result);
        }
        for (String word : plusWords) {
            result = getResults(word);
            if (result != null) results.addAll(result);
        }
        return results;
    }
}