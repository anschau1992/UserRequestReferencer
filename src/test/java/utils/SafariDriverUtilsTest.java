package utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for SafariDriverUtils class.
 */
public class SafariDriverUtilsTest {

    @Test
    public void testCleanupText() {
        assertEquals("blabla blabla", SafariDriverUtils.cleanupText("blabla blabla", "What's new", "Read more"));
        assertEquals("blabla blabla", SafariDriverUtils.cleanupText(" blabla blabla     ", "What's new",
                "Read more"));
        assertEquals("blabla blabla", SafariDriverUtils.cleanupText("    What's new blabla blabla     ", "What's new",
                "Read more"));
        assertEquals("blabla blabla", SafariDriverUtils.cleanupText("    What's new blabla blabla    Read more ",
                "WHAT'S NEW", "Read more"));
        assertEquals("blabla blabla", SafariDriverUtils.cleanupText("    Whats new blabla blabla    Read more ",
                new String[]{"WHAT'S NEW", "Whats new"}, "Read more"));
    }
}
