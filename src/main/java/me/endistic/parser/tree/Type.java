package me.endistic.parser.tree;

import me.endistic.parser.context.FunctionData;

public sealed interface Type {
    record None() implements Type {}
    record Integer() implements Type {}
    record String() implements Type {}
    record Boolean() implements Type {}
    record Lambda() implements Type {}
}
