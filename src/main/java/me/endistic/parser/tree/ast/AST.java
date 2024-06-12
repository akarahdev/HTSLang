package me.endistic.parser.tree.ast;

import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

import java.util.List;

/**
 * An abstract syntax tree representing a program in HTSLScript.
 */
public sealed interface AST permits AST.NamespacedGroup, Action, Expression, Header {
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
