package me.endistic;

import me.endistic.compiler.HTSLBuilder;
import me.endistic.lexer.Lexer;
import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.AST;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println(Lexer.lex("""
            namespace endistic {
                function hello(): none {
                    player.send_message "Hi!"
                }
            }
            """));

        var ctx = new ParseContext(
            new ArrayList<>(),
            new HashMap<>(),
            new HTSLBuilder()
        );
        var expr = new AST.NamespacedGroup(Namespace.of("mygroup"), List.of(
            new AST.Header.Function(
                Namespace.of("mygroup:def"),
                new Type.None(),
                List.of()
            ),
            new AST.Header.Function(
                Namespace.of("mygroup:abc"),
                new Type.None(),
                List.of(
                    new AST.Action.Command(
                        Namespace.of("player.send_message"),
                        List.of(
                            new AST.Expression.StringNode("Hi!")
                        )
                    ),
                    new AST.Action.ModifyStat(
                        Namespace.of("x"),
                        new AST.Expression.NumberNode("5"),
                        "="
                    ),
                    new AST.Action.ModifyStat(
                        Namespace.of("y"),
                        new AST.Expression.Statistic(Namespace.of("player.max_health")),
                        "+="
                    )
                )
            )
        ));
        ctx.withHousingBuiltins();
        expr.generateContext(ctx);
        expr.buildFiles(ctx);
        System.out.println(expr);
        System.out.println(ctx.builder().getAllFiles());
    }
}