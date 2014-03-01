package main.java.au.com.mindworks.dictionary;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isBlank;

public class Dictionary {
    private TrieNode root = new TrieNode();

    public void addAllWords(final Collection<String> wordsToAdd) {
        for (String wordToAdd : wordsToAdd) {
            addWord(wordToAdd);
        }
    }

    /**
     * Addition Algorithm
     * <p/>
     * 1 - if wordToAdd is blank, then return.
     * 2 - if currentRootNode does not contain child corresponding to first character of wordToAdd, then add first character as child in currentRootNode.
     * 3 - if length of wordToAdd is 1, then mark the first character element in currentRootNode as end of word.
     * 4 - else take the sub string of wordToAdd from index 1 (first character is removed as it has already bean added in the currentRootNode),
     * and get the child corresponding to first character, resulting in sub string of wordToAdd as remaining wordToAdd
     * and the child corresponding to first character as the new current currentRootNode
     * 5 - Now repeat the above steps recursively until the end of word is reached.
     *
     * @param wordToAdd
     */
    public void addWord(final String wordToAdd) {
        if (isBlank(wordToAdd)) {
            return;
        }

        final String trimmedLowerCasedWordToAdd = wordToAdd.trim().toLowerCase();
        addWord(trimmedLowerCasedWordToAdd, root);
    }

    private void addWord(final String trimmedLowerCasedWordToAdd, final TrieNode currentRootNode) {
//        if (isBlank(trimmedLowerCasedWordToAdd)) {
//            return;
//        }
        final Character firstCharacter = trimmedLowerCasedWordToAdd.charAt(0);
        if (!currentRootNode.hasChild(firstCharacter)) {
            currentRootNode.addChild(firstCharacter);
        }

        if (trimmedLowerCasedWordToAdd.length() == 1) {
            currentRootNode.getChild(firstCharacter).setEndOfWord(true);
        } else {
            addWord(trimmedLowerCasedWordToAdd.substring(1), currentRootNode.getChild(firstCharacter));
        }
    }

    /**
     * Removal Algorithm
     * <p/>
     * 1 - if wordToRemove is blank, then return false.
     * 2 - if currentRootNode does not contain child corresponding to first character of wordToRemove, then return false.
     * 3 - if length of wordToRemove is 1
     * 3.1 - if if the trieNode corresponding to first character in currentRootNode is marked as end of word
     * 3.1.1 - if the trieNode corresponding to first character in currentRootNode has children, then remove the "end of word" mark
     * from the trieNode corresponding to first character in currentRootNode, and return true.
     * 3.1.2 - else remove the child corresponding to first character in the currentRootNode.
     * 3.1.3 - return true.
     * 3.2 - else return false
     * 4 - else take the sub string of wordToRemove from index 1 (first character is removed as it has already bean found in the currentRootNode),
     * and get the child corresponding to first character, resulting in sub string of wordToRemove as remaining wordToRemove
     * and the child corresponding to first character as the new current currentRootNode
     * 5 - Now repeat the above steps recursively until the end of word is reached.
     *
     * @param wordToRemove
     * @return true if the word is removed from dictionary, otherwise false (when word is not found in dictionary)
     */
    public boolean removeWord(final String wordToRemove) {
        if (isBlank(wordToRemove)) {
            return false;
        }

        final String trimmedLowerCasedWordToRemove = wordToRemove.trim().toLowerCase();
        return removeWord(trimmedLowerCasedWordToRemove, root);
    }

    private boolean removeWord(final String trimmedLowerCasedWordToRemove, final TrieNode currentRootNode) {
//        if (isBlank(trimmedLowerCasedWordToRemove)) {
//            return false;
//        }

        final Character firstCharacter = trimmedLowerCasedWordToRemove.charAt(0);
        if (!currentRootNode.hasChild(firstCharacter)) {
            return false;
        }

        final TrieNode childCorrespondingToFirstCharacter = currentRootNode.getChild(firstCharacter);
        if (trimmedLowerCasedWordToRemove.length() == 1) {
            if (childCorrespondingToFirstCharacter.isEndOfWord()) {
                if (childCorrespondingToFirstCharacter.hasChildren()) {
                    childCorrespondingToFirstCharacter.setEndOfWord(false);
                } else {
                    currentRootNode.removeChild(firstCharacter);
                }
                return true;
            }
            return false;
        }

        return removeWord(trimmedLowerCasedWordToRemove.substring(1), childCorrespondingToFirstCharacter);
    }

    public int size() {
        return searchAllWords().size();
    }

    /**
     * Searching Algorithm
     * <p/>
     * 1 - if root is empty, then return an empty list (i.e. dictionary does not contain any word).
     * 2 - Loop through all the elements in the root.
     * 2.1 - if the element in root is marked as end of word, then add it to the list of found words.
     * 2.2 - if the root has no children, then return.
     * 2.3 - else keep concatenating the words
     * and repeat the above steps (from 2) recursively until the whole dictionary is traversed.
     *
     * @return list of all the words in dictionary
     */
    public List<String> searchAllWords() {
        final List<String> listOfFoundWords = newArrayList();

        final Set<Character> characters = root.getChildren().keySet();
        for (Character character : characters) {
            searchAllWords(character.toString(), root.getChild(character), listOfFoundWords);
        }

        return listOfFoundWords;
    }

    /**
     * Searching Algorithm
     * <p/>
     * 1 - if the prefix is blank, the return list of all the words in the dictionary.
     * 2 - if root does not contain first character of prefix, then return empty list.
     * 3 - else get the child corresponding to the first character of prefix from root
     * 4 - Loop though the characters in the prefix
     * 4.1 - if currentRootNode does not have child for the specified character, then return empty list
     * 4.2 - get the child from currentRootNode with specified character, which will become the new currentRootNode for the next iteration of loop
     * 5 - call "searchAllWords(...)" method to get all the words starting with the specified prefix
     *
     * @param prefix
     * @return list of all the words in dictionary if prefix is blank, otherwise all words starting with prefix
     */
    public List<String> searchAllWordsStartingWith(final String prefix) {
        if (isBlank(prefix)) {
            return searchAllWords();
        }

        final String trimmedLowerCasedPrefix = prefix.trim().toLowerCase();
        final List<String> listOfFoundWordsWithPrefix = newArrayList();

        final Character firstCharacter = trimmedLowerCasedPrefix.charAt(0);
        if (!root.hasChild(firstCharacter)) {
            return listOfFoundWordsWithPrefix;
        }

        TrieNode currentRootNode = root.getChild(firstCharacter);
        for (int i = 1; i < trimmedLowerCasedPrefix.length(); i++) {
            if (!currentRootNode.hasChild(trimmedLowerCasedPrefix.charAt(i))) {
                return listOfFoundWordsWithPrefix;
            }
            currentRootNode = currentRootNode.getChild(trimmedLowerCasedPrefix.charAt(i));
        }
        searchAllWords(trimmedLowerCasedPrefix, currentRootNode, listOfFoundWordsWithPrefix);

        return listOfFoundWordsWithPrefix;
    }

    private void searchAllWords(final String word, final TrieNode currentRootNode, final List<String> listOfFoundWords) {
        if (currentRootNode.isEndOfWord()) {
            listOfFoundWords.add(word);
        }

        if (!currentRootNode.hasChildren()) {
            return;
        } else {
            final Set<Character> characters = currentRootNode.getChildren().keySet();
            for (Character character : characters) {
                searchAllWords(word + character, currentRootNode.getChild(character), listOfFoundWords);
            }
        }
    }

    /**
     * Searching Algorithm
     * <p/>
     * 1 - if wordToSearch is blank, then return false.
     * 2 - if currentRootNode does not contain child corresponding to first character of wordToSearch, then return false.
     * 3 - if length of wordToSearch is 1,
     * 3.1 - if the corresponding child for first character in currentRootNode is marked as end of word, then return true.
     * 3.2 - else return false as the wordToSearch is not marked as end of word in dictionary
     * 4 - else take the sub string of wordToSearch from index 1 (first character is removed as it has already bean found in the currentRootNode),
     * and get the child corresponding to first character, resulting in sub string of wordToSearch as remaining wordToSearch
     * and the child corresponding to first character as the new current currentRootNode
     * 5 - Now repeat the above steps recursively until the end of word is reached.
     *
     * @param wordToSearch
     * @return true if the wordToSearch is found in dictionary, otherwise return false
     */
    public boolean searchWord(final String wordToSearch) {
        if (isBlank(wordToSearch)) {
            return false;
        }

        final String trimmedLowerCasedWordToSearch = wordToSearch.trim().toLowerCase();
        return searchWord(trimmedLowerCasedWordToSearch, root);
    }

    private boolean searchWord(final String trimmedLowerCasedWordToSearch, final TrieNode currentRootNode) {
//        if (isBlank(trimmedLowerCasedWordToSearch)) {
//            return false;
//        }

        final Character firstCharacter = trimmedLowerCasedWordToSearch.charAt(0);
        if (!currentRootNode.hasChild(firstCharacter)) {
            return false;
        }
        if (trimmedLowerCasedWordToSearch.length() == 1) {
            return currentRootNode.getChild(firstCharacter).isEndOfWord();
        } else {
            return searchWord(trimmedLowerCasedWordToSearch.substring(1), currentRootNode.getChild(firstCharacter));
        }
    }

    public void printDictionaryTrie() {
        //TODO: implement
    }
}
