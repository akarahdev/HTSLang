package me.endistic.parser.context;

import me.endistic.parser.tree.Namespace;
import me.endistic.parser.tree.Type;

import java.util.List;

/**
 * Represents the internal data of a Function.
 * @param name The name of the function
 * @param parameterNames Name of parameters
 * @param parameterTypes Names of parameters' types
 * @param returnType The return type of the function
 * @param behavior The behavior this function contains
 */
public record FunctionData(
    Namespace name,
    List<String> parameterNames,
    List<Type> parameterTypes,
    Type returnType,
    FunctionBehavior behavior
) {

}
