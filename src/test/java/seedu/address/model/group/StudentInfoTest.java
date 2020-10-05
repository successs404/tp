package seedu.address.model.group;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StudentInfoTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudentInfo(null, null, null));
    }
}
