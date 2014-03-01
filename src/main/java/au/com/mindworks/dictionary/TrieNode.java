package main.java.au.com.mindworks.dictionary;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private boolean endOfWord = false;
    private Map<Character, TrieNode> children = new HashMap();

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public final boolean isEndOfWord() {
        return endOfWord;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public void setEndOfWord(final boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

    public boolean hasChild(final Character character) {
        return children.containsKey(character);
    }

    public TrieNode getChild(final Character character) {
        return children.get(character);
    }

    public void removeChild(final Character character) {
        children.remove(character);
    }

    public void addChild(final Character firstCharacter) {
        children.put(firstCharacter, new TrieNode());
    }
}
