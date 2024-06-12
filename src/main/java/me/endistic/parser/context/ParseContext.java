package me.endistic.parser.context;

import me.endistic.compiler.HTSLBuilder;
import me.endistic.parser.tree.ast.AST;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Holds context for the {@link me.endistic.parser.Parser}. This is built up using 
 * {@link AST#generateContext(ParseContext)} and is compiled using {@link AST#buildFiles(ParseContext)}.
 * @param functions Represents all the functions in this parsing context.
 * @param placeholders Represents all the placeholders in this parsing context.
 * @param builder Represents the current HTSL project builder.
 */
public record ParseContext(
    List<FunctionData> functions,
    HashMap<Namespace, String> placeholders,
    HTSLBuilder builder
) {
    /**
     * Finds a function data in the context
     * @param namespace The function name to look for
     * @return The function data found (or not)
     */
    public Optional<FunctionData> findFunction(Namespace namespace) {
        return this.functions()
            .stream()
            .filter(it -> it.name().equals(namespace))
            .findFirst();
    }

    /**
     * Finds a housing placeholder in the context
     * @param namespace The placeholder name to look for
     * @return The housing internal placeholder found (or not)
     */

    public Optional<String> findPlaceholder(Namespace namespace) {
        if(!this.placeholders().containsKey(namespace))
            return Optional.empty();
        return Optional.of(this.placeholders().get(namespace));
    }

    /**
     * Applies housing builtins to this instance
     */
    public void withHousingBuiltins() {
        functions.add(new FunctionData(
            Namespace.of("player:send_message"),
            List.of("message"),
            List.of(new Type.String()),
            new Type.None(),
            new FunctionBehavior.StandardAction("chat")
        ));
        functions.add(new FunctionData(
            Namespace.of("player:send_action_bar"),
            List.of("message"),
            List.of(new Type.String()),
            new Type.None(),
            new FunctionBehavior.StandardAction("actionBar")
        ));
        functions.add(new FunctionData(
            Namespace.of("return"),
            List.of("value"),
            List.of(new Type.String()),
            new Type.None(),
            new FunctionBehavior.BuiltIn((ctx, args) -> {
                ctx.builder()
                    .appendSpaced("globalstat")
                    .appendSpaced(ctx.builder().getCurrentFile().toHTSLFormat() + "$result")
                    .appendSpaced("=");
                args.getFirst().buildFiles(ctx);
                ctx.builder.appendRaw("\n");
                ctx.builder.appendSpaced("exit");
            })
        ));

        placeholders.put(Namespace.of("player:max_health"), "%player.maxhealth%");
        placeholders.put(Namespace.of("player:health"), "%player.health%");
    }
}
