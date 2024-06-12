package me.endistic.lexer;

public record SpanData(
    int column,
    int row,
    String sourceLine,
    String sourceFile
) {
}
