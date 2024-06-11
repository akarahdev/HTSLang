package me.endistic.parser.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Namespace(List<String> name) {
    public static Namespace of(String namespace) {
        return new Namespace(List.of(namespace.split("[:./]")));
    }

    public Namespace removePrefix() {
        var rm = new ArrayList<>(this.name);
        rm.removeFirst();
        return new Namespace(rm);
    }

    public Namespace addPrefix(String prefix) {
        var rm = new ArrayList<>(this.name);
        rm.addFirst(prefix);
        return new Namespace(rm);
    }


    public String toString() {
        return String.join(".", name);
    }

    public String toHTSLFormat() {
        return String.join("__", name);
    }
}
