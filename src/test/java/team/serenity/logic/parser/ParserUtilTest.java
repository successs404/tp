package team.serenity.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static team.serenity.commons.util.ParserUtil.MESSAGE_INVALID_INDEX;
import static team.serenity.testutil.Assert.assertThrows;
import static team.serenity.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import team.serenity.commons.util.ParserUtil;
import team.serenity.logic.parser.exceptions.ParseException;
import team.serenity.model.group.Question;

public class ParserUtilTest {

    // Serenity
    private static final String VALID_QUESTION = "What is the deadline for the report?";

    private static final String INVALID_QUESTION = " ";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    // For Serenity

    @Test
    public void parseQuestion_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseQuestion((String) null));
    }

    @Test
    public void parseQuestion_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseQuestion(INVALID_QUESTION));
    }

    @Test
    public void parseQuestion_validValueWithoutWhitespace_returnsQuestion() throws Exception {
        Question expectedQuestion = new Question(VALID_QUESTION);
        assertEquals(expectedQuestion, ParserUtil.parseQuestion(VALID_QUESTION));
    }

    @Test
    public void parseQuestion_validValueWithWhitespace_returnsTrimmedQuestion() throws Exception {
        String questionWithWhitespace = WHITESPACE + VALID_QUESTION + WHITESPACE;
        Question expectedQuestion = new Question(VALID_QUESTION);
        assertEquals(expectedQuestion, ParserUtil.parseQuestion(questionWithWhitespace));
    }

}
