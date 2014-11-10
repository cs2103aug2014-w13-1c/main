package tests.units;
//@author A0114914L

import java.util.ArrayList;
import java.util.Calendar;

import app.controllers.CommandParser;
import app.helpers.CommandObject;
import app.helpers.Keyword;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommandParserUnits {
    CommandParser parserTest = new CommandParser();
    
    
    @Test
    public void canParseProperly() {
        CommandObject commandObjectTest = new CommandObject();
        
        // String parsing test
        commandObjectTest = parserTest.parseCommand("add foo");
        assertEquals("add", commandObjectTest.getCommandWord());
        assertEquals("foo", commandObjectTest.getCommandString());
        commandObjectTest = parserTest.parseCommand("delete 1");
        assertEquals("delete", commandObjectTest.getCommandWord());
        assertEquals("1", commandObjectTest.getCommandString());
        commandObjectTest = parserTest.parseCommand("clear");
        assertEquals("clear", commandObjectTest.getCommandWord());
        
        // Test for failures
        commandObjectTest = parserTest.parseCommand("blah this and that");
        assertEquals("blah", commandObjectTest.getCommandWord());
        assertEquals(null, commandObjectTest.getCommandString());
        commandObjectTest = parserTest.parseCommand("delete boo");
        assertEquals("delete", commandObjectTest.getCommandWord());
        assertEquals("boo", commandObjectTest.getCommandString());
        
        // Date parsing test
        Calendar expectedStartDate = Calendar.getInstance();
        expectedStartDate.set(2000, 5, 6);
        Calendar expectedEndDate = Calendar.getInstance();
        expectedEndDate.set(2015, 9, 13);
        
        commandObjectTest = parserTest.parseCommand("add dummy start 6 june 2000 end 13 october 2015");
        Calendar outputStartDate = Calendar.getInstance();
        outputStartDate.setTime(commandObjectTest.getStartDate());
        Calendar outputEndDate = Calendar.getInstance();
        outputEndDate.setTime(commandObjectTest.getEndDate());
        
        assertEquals(expectedStartDate.get(Calendar.DAY_OF_MONTH), outputStartDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedStartDate.get(Calendar.MONTH), outputStartDate.get(Calendar.MONTH));
        assertEquals(expectedStartDate.get(Calendar.YEAR), outputStartDate.get(Calendar.YEAR));
        
        assertEquals(expectedEndDate.get(Calendar.DAY_OF_MONTH), outputEndDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(expectedEndDate.get(Calendar.MONTH), outputEndDate.get(Calendar.MONTH));
        assertEquals(expectedEndDate.get(Calendar.YEAR), outputEndDate.get(Calendar.YEAR));
        
        // Priority parsing test
        commandObjectTest = parserTest.parseCommand("add foo priority high");
        assertEquals("1. High", commandObjectTest.getPriority());
        commandObjectTest = parserTest.parseCommand("add foo priority medium");
        assertEquals("2. Medium", commandObjectTest.getPriority());
        commandObjectTest = parserTest.parseCommand("add foo priority low");
        assertEquals("3. Low", commandObjectTest.getPriority());
    }
    
    @Test
    public void canDetectKeywordsProperly() {
        ArrayList<Keyword> testList = CommandParser.getKeywords("add foo start foo end dummy priority blah");
        assertEquals(0, testList.get(0).getStartIndex());
        assertEquals(2, testList.get(0).getEndIndex());
        assertEquals(8, testList.get(1).getStartIndex());
        assertEquals(12, testList.get(1).getEndIndex());
        assertEquals(18, testList.get(2).getStartIndex());
        assertEquals(20, testList.get(2).getEndIndex());
        assertEquals(28, testList.get(3).getStartIndex());
        assertEquals(35, testList.get(3).getEndIndex());
    }

}
