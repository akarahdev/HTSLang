package me.endistic.parser.tree.ast;

import me.endistic.parser.context.FunctionBehavior;
import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

import java.util.List;

/**
 * Represents an action in HTSLScript to execute.
 */
public sealed interface Action extends AST {
    /**
     * Represents a standard Housing action. For example, setting a player's health.
     * This can also result in a `trigger` call if the function data is {@link FunctionBehavior.UserGenerated}.
     * @param name The name of the function.
     * @param arguments Arguments to pass to it.
     */
    record Command(Namespace name, List<Expression> arguments) implements Action {
        @Override
        public Type getInferredType() {
            return new Type.None();
        }

        @Override
        public void buildFiles(ParseContext context) {
            context.findFunction(this.name())
                .ifPresent((f) -> {
                    f.behavior().append(context, this.arguments());
                });
        }

        @Override
        public void generateContext(ParseContext context) {

        }
    }

    /**
     * Modifies a stat in the housing.
     * @param stat The namespace of the stat to modify.
     * @param rhs The expression to set the stat to.
     * @param operation The operation to perform (in HTSL). Using "^="
     *                  for this field will invoke an exponent function.
     */
    record ModifyStat(Namespace stat, Expression rhs, String operation) implements Action {
        @Override
        public Type getInferredType() {
            return new Type.None();
        }

        @Override
        public void buildFiles(ParseContext context) {
            context.builder().appendStatisticPlaceholder(stat, context);
            context.builder().appendRaw(" ");
            context.builder().appendSpaced(operation);
            rhs.buildFiles(context);
            context.builder().appendRaw("\n");
        }

        @Override
        public void generateContext(ParseContext context) {

        }
    }
}