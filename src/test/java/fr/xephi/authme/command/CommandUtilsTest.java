package fr.xephi.authme.command;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link CommandUtils}.
 */
public class CommandUtilsTest {

    @Test
    public void shouldPrintPartsForStringRepresentation() {
        // given
        Iterable<String> parts = Arrays.asList("some", "parts", "for", "test");

        // when
        String str = CommandUtils.labelsToString(parts);

        // then
        assertThat(str, equalTo("some parts for test"));
    }

    @Test
    public void shouldPrintEmptyStringForNoArguments() {
        // given
        List<String> parts = Collections.EMPTY_LIST;

        // when
        String str = CommandUtils.labelsToString(parts);

        // then
        assertThat(str, equalTo(""));
    }

    @Test
    public void shouldPrintLabels() {
        // given
        List<String> labels = Arrays.asList("authme", "help", "reload");

        // when
        String result = CommandUtils.labelsToString(labels);

        // then
        assertThat(result, equalTo("authme help reload"));
    }

    @Test
    public void shouldReturnCommandPath() {
        // given
        CommandDescription base = CommandDescription.builder()
            .labels("authme", "auth")
            .description("Base")
            .detailedDescription("Test base command.")
            .executableCommand(mock(ExecutableCommand.class))
            .build();
        CommandDescription command = CommandDescription.builder()
            .parent(base)
            .labels("help", "h", "?")
            .description("Child")
            .detailedDescription("Test child command.")
            .executableCommand(mock(ExecutableCommand.class))
            .build();

        // when
        String commandPath = CommandUtils.constructCommandPath(command);

        // then
        assertThat(commandPath, equalTo("/authme help"));
    }


    // ------
    // min / max arguments
    // ------
    @Test
    public void shouldComputeMinAndMaxOnEmptyCommand() {
        // given
        CommandDescription command = getBuilderForArgsTest().build();

        // when / then
        checkArgumentCount(command, 0, 0);
    }

    @Test
    public void shouldComputeMinAndMaxOnCommandWithMandatoryArgs() {
        // given
        CommandDescription command = getBuilderForArgsTest()
            .withArgument("Test", "Arg description", false)
            .withArgument("Test22", "Arg description 2", false)
            .build();

        // when / then
        checkArgumentCount(command, 2, 2);
    }

    @Test
    public void shouldComputeMinAndMaxOnCommandIncludingOptionalArgs() {
        // given
        CommandDescription command = getBuilderForArgsTest()
            .withArgument("arg1", "Arg description", false)
            .withArgument("arg2", "Arg description 2", true)
            .withArgument("arg3", "Arg description 3", true)
            .build();

        // when / then
        checkArgumentCount(command, 1, 3);
    }


    private static void checkArgumentCount(CommandDescription command, int expectedMin, int expectedMax) {
        assertThat(CommandUtils.getMinNumberOfArguments(command), equalTo(expectedMin));
        assertThat(CommandUtils.getMaxNumberOfArguments(command), equalTo(expectedMax));
    }

    private static CommandDescription.CommandBuilder getBuilderForArgsTest() {
        return CommandDescription.builder()
            .labels("authme", "auth")
            .description("Base")
            .detailedDescription("Test base command.")
            .executableCommand(mock(ExecutableCommand.class));
    }
}
