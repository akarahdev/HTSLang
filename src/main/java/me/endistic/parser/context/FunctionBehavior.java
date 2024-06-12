package me.endistic.parser.context;

import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.ast.Expression;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents the behavior of a function defined in {@link FunctionData}.
 */
public sealed interface FunctionBehavior {
    /**
     * Marks a function created by the user. This will insert a call to it via `trigger`.
     * @param functionName The name of the usergenerated function.
     */
    record UserGenerated(Namespace functionName) implements FunctionBehavior {
        @Override
        public void append(ParseContext ctx, List<Expression> args) {
            ctx.builder()
                .appendSpaced("trigger")
                .appendRaw(" \"")
                .appendRaw(functionName.toHTSLFormat())
                .appendRaw("\" ")
                .appendSpaced("false")
                .appendRaw("\n");
        }
    }

    record StandardAction(String name) implements FunctionBehavior {
        @Override
        public void append(ParseContext ctx, List<Expression> args) {
            ctx.builder()
                    .appendSpaced(name);
            args.forEach(it -> it.buildFiles(ctx));
            ctx.builder().appendRaw("\n");
        }
    }

    /**
     * Marks a function as builtin.
     * @param function Code to execute to generate the function.
     */
    record BuiltIn(BiConsumer<ParseContext, List<Expression>> function) implements FunctionBehavior {
        @Override
        public void append(ParseContext ctx, List<Expression> args) {
            function.accept(ctx, args);
            ctx.builder().appendRaw("\n");
        }
    }

    void append(ParseContext ctx, List<Expression> args);
}
