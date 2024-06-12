package me.endistic;

import me.endistic.compiler.HTSLBuilder;
import me.endistic.lexer.Lexer;
import me.endistic.parser.Parser;
import me.endistic.parser.context.ParseContext;
import me.endistic.parser.tree.Type;
import me.endistic.parser.tree.ast.AST;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.ast.Action;
import me.endistic.parser.tree.ast.Expression;
import me.endistic.parser.tree.ast.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        var tokens = Lexer.lex("""
            namespace main {
                function main.hello(): string {}
                function main.other(): integer {}
            }
            """);

        System.out.println();



//        var expr = new AST.NamespacedGroup(Namespace.of("myhouse"), List.of(
//            new Header.Function(
//                Namespace.of("myhouse.hello"),
//                new Type.None(),
//                List.of(
//                    new Action.Command(
//                        Namespace.of("player.send_message"),
//                        List.of(
//                            new Expression.StringNode("Hi")
//                        )
//                    ),
//                    new Action.ModifyStat(
//                        Namespace.of("globalstat.x"),
//                        new Expression.Statistic(Namespace.of("y")),
//                        "+="
//                    ),
//                    new Action.ModifyStat(
//                        Namespace.of("y"),
//                        new Expression.Statistic(Namespace.of("myhouse.math$result")),
//                        "*="
//                    ),
//                    new Action.Command(
//                        Namespace.of("return"),
//                        List.of(
//                            new Expression.NumberNode("12")
//                        )
//                    )
//                )
//            )
//        ));




        var p = new Parser(tokens);
        var expr = p.parseNamespace();

        var ctx = new ParseContext(
            new ArrayList<>(),
            new HashMap<>(),
            new HTSLBuilder()
        );

        // Represents the compiler orders.

        // Add the housing builtins.
        ctx.withHousingBuiltins();

        // Generate the context.
        expr.generateContext(ctx);

        // Generate the files.
        expr.buildFiles(ctx);

        // Debug print the expressions and the builders.
        System.out.println(tokens);
        System.out.println(expr);
        System.out.println(ctx.builder().getAllFiles());
    }
}