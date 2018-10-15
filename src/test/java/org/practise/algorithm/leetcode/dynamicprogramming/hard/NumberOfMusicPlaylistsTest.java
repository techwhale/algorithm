package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class NumberOfMusicPlaylistsTest {
    private NumberOfMusicPlaylists obj = new NumberOfMusicPlaylists();

    @Test
    public void testNumberOfMusicPlaylist() {
        Assert.assertEquals(obj.numMusicPlaylists(3, 3, 1), 6);
    }
}