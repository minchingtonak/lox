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
    PrintContext() {
        set("indent", "");
    }

    public String increaseIndent() {
        return set("indent", get("indent") + "  ");
    }

    public String decreaseIndent() {
        String indent = get("indent");
        return set("indent", indent.substring(0, indent.length() - 2));
    }

    public String indent() {
        return get("indent");
    }
}