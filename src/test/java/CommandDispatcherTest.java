import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.CommandBuilder;
import com.github.DasJava005.cmdApi.base.CommandDispatcher;
import com.github.DasJava005.cmdApi.base.MatchResult;
import com.github.DasJava005.cmdApi.base.arguments.InputArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class CommandDispatcherTest {

    private final CommandDispatcher dispatcher;
    private final Method methodToTest;

    public CommandDispatcherTest() throws NoSuchMethodException {
        this.dispatcher = new CommandDispatcher(null);
        this.methodToTest = dispatcher.getClass().getDeclaredMethod("match", Command.class, CommandSender.class, String[].class);
        this.methodToTest.setAccessible(true);
    }

    public MatchResult runMethod(Command command, CommandSender sender, String[] userProvidedArgs) {
        try {
            return (MatchResult) this.methodToTest.invoke(dispatcher, command, sender, userProvidedArgs);
        } catch (InvocationTargetException | IllegalAccessException ite){
            throw new RuntimeException(ite);
        }
    }

    public static Stream<Arguments> matchCases() {
        return Stream.of(
                Arguments.of("success", new String[]{"give", "playerToReceiveMoney", "10"}, MatchResult.Success.class),
                Arguments.of("missing argument", new String[]{"give", "playerToReceiveMoney"}, MatchResult.NotEnoughArguments.class),
                Arguments.of("no integer", new String[]{"give", "playerToReceiveMoney", "abc"}, MatchResult.ParseFailure.class),
                Arguments.of("missing sub command", new String[]{"delete", "playerToReceiveMoney", "10"}, MatchResult.LiteralMismatch.class)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchCases")
    public void isMatch(String description, String[] userArgs, Class<? extends  MatchResult> expectedResult) {
        Command cmd = CommandBuilder.of("bal")
                .literal("give")
                .sender(Player.class)
                .argument(InputArguments.STRING.createArgument("player"))
                .argument(InputArguments.INTEGER.createArgument("amount"))
                .create();

        Player sender = mock(Player.class);
        when(sender.hasPermission("")).thenReturn(true);

        MatchResult result = runMethod(cmd, sender, userArgs);

        assertEquals(expectedResult, result.getClass());
    }

    @Test
    public void noPermissionCase() {
        Player sender = mock(Player.class);
        when(sender.hasPermission("command.permission")).thenReturn(false);

        Command cmd = CommandBuilder.of("bal")
                .permission("command.permission")
                .literal("give")
                .sender(Player.class)
                .argument(InputArguments.STRING.createArgument("player"))
                .argument(InputArguments.INTEGER.createArgument("amount"))
                .create();

        MatchResult result = runMethod(cmd, sender, new String[]{"give", "playerB", "10000000"});

        assertEquals(MatchResult.NoPermission.class, result.getClass());
    }

}
