package me.endistic.parser;

import me.endistic.lexer.Token;
import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;
import me.endistic.parser.tree.ast.AST;
import me.endistic.parser.tree.ast.Action;
import me.endistic.parser.tree.ast.Header;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    List<Token> tokens;
    int index = -1;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token read() {
        while(tokens.get(index+1) instanceof Token.NewLine) { index++; }
        return tokens.get(++index);
    }

    public Token readWithWhitespace() {
        return tokens.get(++index);
    }

    public Token peek() {
        while(tokens.get(index+1) instanceof Token.NewLine) { index++; }
        return tokens.get(index+1);
    }

    public Token peekWithWhitespace() {
        return tokens.get(index+1);
    }

    public<T extends Token> T match(Class<T> clazz) {
        if(!clazz.isInstance(peek()))
            throw new RuntimeException("expected " + clazz + " found, " + tokens.get(index+1).getClass());
        return clazz.cast(this.read());
    }

    public AST.NamespacedGroup parseNamespace() {
        var kw = match(Token.Keyword.class);
        if(!kw.value().equals("namespace"))
            throw new RuntimeException("expected namespace");

        var name = match(Token.Keyword.class);

        match(Token.OpenBrace.class);

        var headers = new ArrayList<Header>();
        while(!(peek() instanceof Token.CloseBrace)) {
            headers.add(parseHeader());
        }
        match(Token.CloseBrace.class);

        return new AST.NamespacedGroup(
            Namespace.of(name.value()),
            headers
        );
    }

    public Header parseHeader() {
        var kw = match(Token.Keyword.class);
        switch (kw.value()) {
            case "function", "event" -> {
                return parseFunction();
            }
            default -> throw new IllegalStateException("invalid header keyword");
        }
    }

    public Header.Function parseFunction() {
        var name = match(Token.Keyword.class);
        match(Token.OpenParen.class);
        match(Token.CloseParen.class);
        match(Token.Colon.class);
        var returnedType = parseType();
        var block = parseBlock();

        return new Header.Function(
            Namespace.of(name.value()),
            returnedType,
            block
        );
    }

    public List<Action> parseBlock() {
        var a = new ArrayList<Action>();
        match(Token.OpenBrace.class);
        match(Token.CloseBrace.class);
        return a;
    }

    public Type parseType() {
        var kw = match(Token.Keyword.class);
        return switch (kw.value()) {
            case "none" -> new Type.None();
            case "integer" -> new Type.Integer();
            case "string" -> new Type.String();
            case "boolean" -> new Type.Boolean();
            case "lambda" -> new Type.Lambda();
            default -> throw new IllegalStateException("Unexpected value: " + kw.value());
        };
    }
}
