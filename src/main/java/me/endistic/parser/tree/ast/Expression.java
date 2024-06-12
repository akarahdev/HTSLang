package me.endistic.parser.tree.ast;

import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

/**
 * Represents a value passed to an {@link Action}.
 */
public sealed interface Expression extends AST {
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
            context.builder().appendStatisticPlaceholder(stat, context);
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
