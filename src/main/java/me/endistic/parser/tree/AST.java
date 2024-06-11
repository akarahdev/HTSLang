package me.endistic.parser.tree;

import me.endistic.compiler.ArgumentSet;
import me.endistic.parser.context.FunctionBehavior;
import me.endistic.parser.context.FunctionData;
import me.endistic.parser.context.ParseContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An abstract syntax tree representing a program in HTSLScript.
 */
public sealed interface AST {
    /**
     * Represents an action in HTSLScript to execute.
     */
    sealed interface Action extends AST {
        /**
         * Represents a standard Housing action. For example, setting a player's health.
         * This can also result in a `trigger` call if the function data is {@link FunctionBehavior.UserGenerated}.
         * @param name The name of the function.
         * @param arguments Arguments to pass to it.
         */
        record Command(Namespace name, List<AST.Expression> arguments) implements Action {
            @Override
            public Type getInferredType() {
                return new Type.None();
            }

            @Override
            public void buildFiles(ParseContext context) {
                context.findFunction(this.name())
                    .ifPresent((f) -> {
                        f.behavior().append(context, new ArgumentSet(this.arguments()));
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
                switch (stat.name().get(0)) {
                    case "globalstat", "gs" -> {
                        context
                            .builder()
                            .appendSpaced("globalstat")
                            .appendSpaced(this.stat().removePrefix().toString())
                            .appendSpaced(operation);
                        rhs.buildFiles(context);
                    }
                    case "playerstat", "ps" -> {
                        context
                            .builder()
                            .appendSpaced("playerstat")
                            .appendRaw(this.stat().removePrefix().toString())
                            .appendSpaced(" ")
                            .appendSpaced(operation);
                        rhs.buildFiles(context);
                    }
                    default -> {
                        context
                            .builder()
                            .appendSpaced("globalstat")
                            .appendSpaced(this
                                .stat()
                                .addPrefix(context.builder().getCurrentFile())
                                .addPrefix("tmp").toHTSLFormat())
                            .appendSpaced(operation);
                        rhs.buildFiles(context);
                    }
                }
                context.builder().appendRaw("\n");
            }

            @Override
            public void generateContext(ParseContext context) {

            }
        }
    }

    /**
     * Represents a value passed to an {@link Action}.
     */
    sealed interface Expression extends AST {
        /**
         * Represents a housing statistic
         * @param stat The namespace in the code for the statistic
         */
        record Statistic(Namespace stat) implements Expression {
            @Override
            public Type getInferredType() {
                return new Type.Integer();
            }

            @Override
            public void buildFiles(ParseContext context) {
                switch (stat.name().get(0)) {
                    case "globalstat" -> {
                        context.builder().appendRaw("%stats.global/").appendRaw(stat.toString()).appendRaw("%");
                    }
                    case "playerstat" -> {
                        context.builder().appendRaw("%stats.player/").appendRaw(stat.toString()).appendRaw("%");
                    }
                    default -> {
                        context.findPlaceholder(stat).ifPresent(st -> {
                            context.builder().appendRaw(st).appendRaw(" ");
                        });
                    }
                }
            }

            @Override
            public void generateContext(ParseContext context) {

            }
        }

        /**
         * Represents a string value
         * @param value The content of the string
         */
        record StringNode(String value) implements Expression {
            @Override
            public Type getInferredType() {
                return new Type.String();
            }

            @Override
            public void buildFiles(ParseContext context) {
                context.builder().appendRaw("\"").appendRaw(this.value).appendRaw("\"").appendRaw(" ");
            }

            @Override
            public void generateContext(ParseContext context) {

            }
        }

        /**
         * Represents a number value
         * @param value The content of the number
         */
        record NumberNode(String value) implements Expression {
            @Override
            public Type getInferredType() {
                return new Type.String();
            }

            @Override
            public void buildFiles(ParseContext context) {
                context.builder().appendSpaced(this.value);
            }

            @Override
            public void generateContext(ParseContext context) {

            }
        }
    }

    /**
     * Represents a header inside of a Namespace block.
     */
    sealed interface Header extends AST {
        /**
         * Defines a new function.
         * @param name The name of the function
         * @param returnType The type the function returns
         * @param actions Actions the function will run when invoked
         */
        record Function(Namespace name, Type returnType, List<Action> actions) implements Header {
            @Override
            public Type getInferredType() {
                return this.returnType();
            }

            @Override
            public void buildFiles(ParseContext context) {
                context.builder().setCurrentFile(name.toString());
                for (var a : actions) {
                    a.buildFiles(context);
                }
            }

            @Override
            public void generateContext(ParseContext context) {
                context.functions().add(new FunctionData(
                    this.name(),
                    List.of(),
                    List.of(),
                    new Type.None(),
                    new FunctionBehavior.UserGenerated(this.name())
                ));
            }
        }
    }

    /**
     * Represents a group of {@link Header} under a namespace.
     * @param name The name of this namespace.
     * @param headers The headers this namespace contains.
     */
    record NamespacedGroup(Namespace name, List<Header> headers) implements AST {
        @Override
        public Type getInferredType() {
            return new Type.None();
        }

        @Override
        public void buildFiles(ParseContext context) {
            for (var h : headers)
                h.buildFiles(context);
        }

        @Override
        public void generateContext(ParseContext context) {
            for (var h : headers)
                h.generateContext(context);
        }
    }

    /**
     * @return The inferred type of this expression.
     */
    Type getInferredType();

    /**
     * Builds an HTSLBuilder for this node.
     * @param context The context of the building.
     */

    void buildFiles(ParseContext context);

    /**
     * Generates the data for the context.
     * @param context The context to generate data for.
     */
    void generateContext(ParseContext context);
}
