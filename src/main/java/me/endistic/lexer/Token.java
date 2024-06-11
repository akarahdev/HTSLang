package me.endistic.lexer;

public sealed interface Token {
    record Keyword(String value) implements Token {}
    record Number(String value) implements Token {}
    record StringValue(String value) implements Token {}
    record Boolean(boolean value) implements Token {}
    record OpenParen() implements Token  {}
    record CloseParen() implements Token  {}
    record OpenBrace() implements Token  {}
    record CloseBrace() implements Token  {}
    record OpenBracket() implements Token  {}
    record CloseBracket() implements Token  {}

    record Plus() implements Token  {}
    record Minus() implements Token  {}
    record Star() implements Token  {}
    record Slash() implements Token  {}
    record Equals() implements Token  {}

}
