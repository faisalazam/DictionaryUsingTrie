package au.com.mindworks.dictionary;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DictionaryTest {
    private static final List<String> LIST_OF_WORDS = asList(
            "a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate",
            "be",
            "dog", "dads", "dad", "digging",
            "ear", "earn",
            "i",
            "mom", "moms", "mommy",
            "you", "your"
    );

    private Dictionary dictionary = new Dictionary();

    @Test
    public void shouldNotAddAndSearchBlankWord() {
        final List<String> listOfWords = asList(
                "", " ", null
        );

        assertThat(dictionary.size(), is(0));
        for (String word : listOfWords) {
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(word));

            dictionary.addWord(word);

            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(word));
        }
        assertThat(dictionary.size(), is(0));
    }

    @Test
    public void shouldTrimSpacesOnSidesOfWordWhileAdditionAndSearch() {
        final List<String> listOfWords = asList(
                " a", "an ", " any ", "ant", "all", "allot", "alloy", "aloe", "are", "ate",
                "be",
                "dog", "dads      ", "dad", "digging",
                "ear", "earn",
                "    i      ",
                "mom", "     moms", "mommy",
                "you", "your"
        );

        for (String word : listOfWords) {
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(word));
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(" " + word));
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(word + " "));
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(" " + word + " "));

            dictionary.addWord(word);

            assertTrue("Expected to find word: '" + word + "'", dictionary.searchWord(word));
            assertTrue("Expected to find word: '" + word + "'", dictionary.searchWord(" " + word));
            assertTrue("Expected to find word: '" + word + "'", dictionary.searchWord(word + " "));
            assertTrue("Expected to find word: '" + word + "'", dictionary.searchWord(" " + word + " "));
        }
    }

    @Test
    public void shouldVerifyAdditionAndSearchIsCaseInsensitive() {
        final List<String> listOfWords = asList(
                "a", "aN", "Any", "aNt", "alL", "ALLOT", "alloy", "ALOE", "aRe", "Ate",
                "be",
                "Dog", "DadS", "dAd", "diGGing",
                "eaR", "EARN",
                "I",
                "mOm", "MOMS", "MoMmY",
                "yoU", "your"
        );

        for (String word : listOfWords) {
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(word));
            assertFalse("Expected to NOT find word: '" + word.toLowerCase() + "'", dictionary.searchWord(word.toLowerCase()));
            assertFalse("Expected to NOT find word: '" + word.toUpperCase() + "'", dictionary.searchWord(word.toUpperCase()));

            dictionary.addWord(word);

            assertTrue("Expected to find word: '" + word + "'", dictionary.searchWord(word));
            assertTrue("Expected to find word: '" + word.toLowerCase() + "'", dictionary.searchWord(word.toLowerCase()));
            assertTrue("Expected to find word: '" + word.toUpperCase() + "'", dictionary.searchWord(word.toUpperCase()));
        }
    }

    @Test
    public void shouldNotHaveDuplicatedWordsInDictionary() throws Exception {
        final List<String> listOfWords = asList(
                "a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate",
                "a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate",
                "a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate"
        );

        dictionary.addAllWords(listOfWords);

        assertThat(dictionary.size(), is(10));
        assertThat(dictionary.searchAllWords(), hasItems("a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate"));
    }

    @Test
    public void shouldAddAndSearchNonBlankWord() {
        for (String word : LIST_OF_WORDS) {
            assertFalse("Expected to NOT find word: '" + word + "'", dictionary.searchWord(word));

            dictionary.addWord(word);

            assertTrue("Expected to find word: '" + word + "'", dictionary.searchWord(word));
        }
    }

    @Test
    public void shouldVerifyThatDictionaryContainsAllTheAddedWords() {
        dictionary.addAllWords(LIST_OF_WORDS);

        assertTrue(dictionary.searchAllWords().containsAll(LIST_OF_WORDS));
    }

    @Test
    public void shouldVerifyThatDictionaryCanProvideAllWordsStartingWithSomePrefixCaseInsensitively() throws Exception {

        dictionary.addAllWords(LIST_OF_WORDS);

        assertThat(dictionary.searchAllWordsStartingWith("a"), hasItems("a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate"));
        assertThat(dictionary.searchAllWordsStartingWith("an"), hasItems("an", "any", "ant"));
        assertThat(dictionary.searchAllWordsStartingWith("ant"), hasItems("ant"));
        assertThat(dictionary.searchAllWordsStartingWith("any"), hasItems("any"));
        assertThat(dictionary.searchAllWordsStartingWith("aLl"), hasItems("all", "allot", "alloy"));
        assertThat(dictionary.searchAllWordsStartingWith("allO"), hasItems("allot", "alloy"));
        assertThat(dictionary.searchAllWordsStartingWith("I"), hasItems("i"));
        assertThat(dictionary.searchAllWordsStartingWith("dads"), hasItems("dads"));
        assertThat(dictionary.searchAllWordsStartingWith("diGGing"), hasItems("digging"));

        //Verify dictionary does not have any words starting with the following prefixes
        assertTrue(dictionary.searchAllWordsStartingWith("ad").isEmpty());
        assertTrue(dictionary.searchAllWordsStartingWith("arn").isEmpty());
        assertTrue(dictionary.searchAllWordsStartingWith("oms").isEmpty());
        assertTrue(dictionary.searchAllWordsStartingWith("sh").isEmpty());
        assertTrue(dictionary.searchAllWordsStartingWith("shad").isEmpty());
    }

    @Test
    public void shouldVerifyThatDictionaryCanProvideAllWordsInDictionaryWhenSearchedWithBlankPrefix() throws Exception {
        dictionary.addAllWords(LIST_OF_WORDS);

        final List<String> blankPrefixes = asList(
                "", " ", null
        );

        for (String blankPrefix : blankPrefixes) {
            final List<String> listOfFoundWordsWithBlankPrefix = dictionary.searchAllWordsStartingWith(blankPrefix);
            assertTrue(listOfFoundWordsWithBlankPrefix.containsAll(LIST_OF_WORDS));
        }
    }

    @Test
    public void shouldVerifyWordRemoval() {
        final List<String> listOfWords = new ArrayList<String>(
                asList(
                        "a", "an", "any", "ant", "all", "allot", "alloy", "aloe", "are", "ate",
                        "be",
                        "dog", "dads", "dad", "digging",
                        "ear", "earn",
                        "i",
                        "mom", "moms", "mommy",
                        "you", "your"
                )
        );
        final int totalNumberOfWords = listOfWords.size();

        dictionary.addAllWords(listOfWords);

        assertTrue(dictionary.size() > 0);
        assertThat(dictionary.size(), is(totalNumberOfWords));

        final Random random = new Random();

        for (int i = 0; i < totalNumberOfWords; i++) {
            int numberOfRemainingWords = listOfWords.size();
            final int randomWordIndex = random.nextInt(numberOfRemainingWords);
            final String wordToRemove = listOfWords.get(randomWordIndex);

            listOfWords.remove(wordToRemove);
            numberOfRemainingWords--;

            assertTrue("Expected to find word: '" + wordToRemove + "'", dictionary.searchWord(wordToRemove));

            final boolean isWordRemoved = dictionary.removeWord(wordToRemove);
            assertTrue("Expected to remove word: '" + wordToRemove + "'", isWordRemoved);

            assertFalse("Expected to NOT find word: '" + wordToRemove + "'", dictionary.searchWord(wordToRemove));
            assertThat(dictionary.size(), is(numberOfRemainingWords));
        }
        assertTrue(dictionary.size() == 0);
    }

    @Test
    public void shouldRemoveWordOnlyIfExistsInDictionary() throws Exception {
        final List<String> listOfWords = asList(
                "a", "an", "any", "ant", "all",
                "dolphin",
                "elephant", "ear",
                "fish",
                "ommy",
                "our", "ours",
                "yours"
        );

        dictionary.addAllWords(LIST_OF_WORDS);

        for (String wordToRemove : listOfWords) {
            final boolean doesWordExistInDictionary = dictionary.searchWord(wordToRemove);

            final boolean isWordRemoved = dictionary.removeWord(wordToRemove);

            assertThat(isWordRemoved, is(doesWordExistInDictionary));
        }
    }
}