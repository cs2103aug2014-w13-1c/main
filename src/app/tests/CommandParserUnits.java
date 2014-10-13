package app.tests;

import static org.junit.Assert.*;
import app.controllers.CommandParser;

import org.junit.Test;

public class CommandParserUnits {
    
    @Test
    public void testNextSpacePosition() {
        int test1 = CommandParser.nextSpacePosition("a bc def g h", 0);
        int test2 = CommandParser.nextSpacePosition("a bc def g h", 1);
        int test3 = CommandParser.nextSpacePosition("a bc def g h", 2);
        int test4 = CommandParser.nextSpacePosition("a bc def g h", 5);
        int test5 = CommandParser.nextSpacePosition("a bc def g h", 9);
        
        assertEquals(1, test1);
        assertEquals(1, test2);
        assertEquals(4, test3);
        assertEquals(8, test4);
        assertEquals(10, test5);
    }
    
    @Test
    public void testGetCommandWord() {
        String test1 = CommandParser.getCommandWord("add task1");
        String test2 = CommandParser.getCommandWord("delete");
        String test3 = CommandParser.getCommandWord("update 1");
        
        assertEquals("add", test1);
        assertEquals("delete", test2);
        assertEquals("update", test3);
    }
}
