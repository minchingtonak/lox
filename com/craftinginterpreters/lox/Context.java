package com.craftinginterpreters.lox;

import java.util.HashMap;

abstract class Context<T> {
    private HashMap<String, T> context = new HashMap<>();

    public T get(String key) {
        return context.get(key);
    }

    public T set(String key, T val) {
        return context.put(key, val);
    }
}

class PrintContext extends Context<String> {

    private String indent;

    PrintContext(int indent_amount) {
        set("indent", "");
        StringBuilder sb = new StringBuilder();
        for (; indent_amount > 0; --indent_amount) {
            sb.append(" ");
        }
        indent = sb.toString();
    }

    public String increaseIndent() {
        return set("indent", get("indent") + indent);
    }

    public String decreaseIndent() {
        String tmp = get("indent");
        return set("indent", tmp.substring(0, tmp.length() - indent.length()));
    }

    public String indent() {
        return get("indent");
    }
}