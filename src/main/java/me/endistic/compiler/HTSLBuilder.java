package me.endistic.compiler;

import java.util.HashMap;

/**
 * Builds up an HTSL project.
 */
public class HTSLBuilder {
    /**
     * The current file being modified.
     */
    String currentFile = "";
    /**
     * Each file and their corresponding string builder to modify the file.
     */
    HashMap<String, StringBuilder> files = new HashMap<>();

    public HashMap<String, StringBuilder> getAllFiles() {
        return this.files;
    }

    public String getCurrentFile() {
        return this.currentFile;
    }
    public HTSLBuilder setCurrentFile(String newFile) {
        this.currentFile = newFile;
        return this;
    }

    public StringBuilder currentFileBuilder() {
        if(!files.containsKey(currentFile))
            files.put(currentFile, new StringBuilder());
        return files.get(currentFile);
    }
    public HTSLBuilder appendSpaced(String value) {
        currentFileBuilder().append(value).append(" ");
        return this;
    }
    public HTSLBuilder appendRaw(String value) {
        currentFileBuilder().append(value);
        return this;
    }


}
