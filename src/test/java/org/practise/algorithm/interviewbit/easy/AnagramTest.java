package org.practise.algorithm.interviewbit.easy;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class AnagramTest {
    private Anagram obj = new Anagram();

    @Test
    public void testAnagram() {
        final List<String> list = Arrays.asList("cat", "dog", "god", "tca");
        final ArrayList<ArrayList<Integer>> anagramsIndex = obj.anagrams(list);
    }

    @Test
    public void testAnagram2() {
        final List<String> list = Arrays.asList("b");
        final ArrayList<ArrayList<Integer>> anagramsIndex = obj.anagrams(list);
    }

}