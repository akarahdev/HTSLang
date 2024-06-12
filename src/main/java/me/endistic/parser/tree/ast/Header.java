package me.endistic.parser.tree.ast;

import me.endistic.parser.context.FunctionBehavior;
import me.endistic.parser.context.FunctionData;
import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

import java.util.List;

/**
 * Represents a header inside of a Namespace block.
 */
sealed public interface Header extends AST {
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
            context.builder().setCurrentFile(name);
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
