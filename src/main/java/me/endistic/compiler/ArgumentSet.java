package me.endistic.compiler;

import me.endistic.parser.tree.AST;

import java.util.HashMap;
import java.util.List;

public record ArgumentSet(
    List<AST.Expression> values
) {
    public AST.Expression get(int index) {
        return this.values().get(index);
    }
}
