package org.practise.algorithm.leetcode.string.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TextJustificationTest {
    private TextJustification obj = new TextJustification();

    @Test
    public void testTextJustification() {
        final String[] word = {"What", "must", "be", "acknowledgment", "shall", "be"};
        final List<String> list = obj.fullJustify(word, 16);
        //["What   must   be","acknowledgment  ","shall be        "]
        Assert.assertEquals(list.get(0), "What   must   be");
        Assert.assertEquals(list.get(1), "acknowledgment  ");
        Assert.assertEquals(list.get(2), "shall be        ");
    }

    @Test
    public void testTextJustification2() {
        final String[] word = {"Science","is","what","we","understand","well","enough","to","explain","to","a","computer.","Art","is","everything","else","we","do"};
        final List<String> list = obj.fullJustify(word, 20);
        Assert.assertEquals(list.get(0), "Science  is  what we");
        Assert.assertEquals(list.get(1), "understand      well");
        Assert.assertEquals(list.get(2), "enough to explain to");
        Assert.assertEquals(list.get(3), "a  computer.  Art is");
        Assert.assertEquals(list.get(4), "everything  else  we");
        Assert.assertEquals(list.get(5), "do                  ");
    }
}