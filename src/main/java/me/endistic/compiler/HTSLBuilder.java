package me.endistic.compiler;

import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.Namespace;

import java.util.HashMap;

/**
 * Builds up an HTSL project.
 */
public class HTSLBuilder {
    /**
     * The current file being modified.
     */
    Namespace currentFile = Namespace.of("unnamed");
    /**
     * Each file and their corresponding string builder to modify the file.
     */
    HashMap<Namespace, StringBuilder> files = new HashMap<>();

    public HashMap<Namespace, StringBuilder> getAllFiles() {
        return this.files;
    }

    public Namespace getCurrentFile() {
        return this.currentFile;
    }

    public HTSLBuilder setCurrentFile(Namespace newFile) {
        this.currentFile = newFile;
        return this;
    }

    public StringBuilder currentFileBuilder() {
        if (!files.containsKey(currentFile))
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

    public HTSLBuilder appendStatisticPlaceholder(Namespace namespace, ParseContext ctx) {
        switch (namespace.name().getFirst()) {
            case "playerstat" -> this.appendRaw("%")
                .appendRaw("stats.player/")
                .appendRaw(namespace.removePrefix().toHTSLFormat())
                .appendRaw("%");
            case "globalstat" -> this.appendRaw("%")
                .appendRaw("stats.global/")
                .appendRaw(namespace.removePrefix().toHTSLFormat())
                .appendRaw("%");
            default -> {
                if(namespace.isResult()) {
                    ctx.findPlaceholder(namespace).ifPresentOrElse(
                        this::appendRaw,
                        () -> {
                            this.appendRaw("%")
                                .appendRaw("stats.global/")
                                .appendRaw(namespace.toHTSLFormat())
                                .appendRaw("%");
                        });
                } else {
                    ctx.findPlaceholder(namespace).ifPresentOrElse(
                        this::appendRaw,
                        () -> {
                            this.appendRaw("%")
                                .appendRaw("stats.global/")
                                .appendRaw(this.getCurrentFile().toHTSLFormat())
                                .appendRaw("$")
                                .appendRaw(namespace.toHTSLFormat())
                                .appendRaw("")
                                .appendRaw("%");
                        });
                }

            }

        }
        return this;
    }


}
