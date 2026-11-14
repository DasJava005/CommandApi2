import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.arguments.*;
import com.github.DasJava005.cmdApi.base.builder.CommandBuilder;
import com.github.DasJava005.cmdApi.base.registry.dispatcher.CommandMatcher;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandMatcherTest {

    private final CommandMatcher matcher = new CommandMatcher();

    private final Command cmd1 = CommandBuilder.of("teleport").literal("structure").create();
    private final Command cmd2 = CommandBuilder.of("teleport").literal("location").argument(TypeArguments.VECTOR.createArgument("key")).create();

    private final Command cmd3 = CommandBuilder.of("provide").literal("someInteger").argument(TypeArguments.INT.createArgument("someKeyForIntValue")).create();

    private final Command cmd4 = CommandBuilder.of("admin")
            .argument(TypeArguments.STRING.createArgument("username"))
            .argument(TypeArguments.STRING.createArgument("password"))
            .permission("amdin.superSecretLogin")
            .create();

    @Test
    public void successfulLiteralCommand() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd1, sender, new String[]{"structure"});

        assertInstanceOf(CommandMatcher.MatchResult.Match.class, res);
    }

    @Test
    public void successfulCommandWithTypeArgument() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd3, sender, new String[]{"someInteger", "7"});

        assertInstanceOf(CommandMatcher.MatchResult.Match.class, res);
    }

    @Test
    public void tooManyArguments() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd3, sender, new String[]{"someInteger", "7", "8"});

        assertInstanceOf(CommandMatcher.MatchResult.NoMatch.TooManyArguments.class, res);
    }

    @Test
    public void parseMismatch() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd3, sender, new String[]{"someInteger", "noThisIsNoInteger",});

        assertInstanceOf(CommandMatcher.MatchResult.NoMatch.ParseMismatch.class, res);
    }

    @Test
    public void notEnoughArguments() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd3, sender, new String[]{"someInteger"});

        assertInstanceOf(CommandMatcher.MatchResult.NoMatch.NotEnoughArguments.class, res);
    }

    @Test
    public void literalMismatch() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd1, sender, new String[]{"thisIsNotStructure"});

        assertInstanceOf(CommandMatcher.MatchResult.NoMatch.LiteralMismatch.class, res);
    }

    @Test
    public void senderMismatch() {
        ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd1, sender, new String[]{"structure"});

        assertInstanceOf(CommandMatcher.MatchResult.NoMatch.SenderTypeMismatch.class, res);
    }

    @Test
    public void testComplexCommand() {
        Player sender = mock(Player.class);
        when(sender.hasPermission(anyString())).then(_ -> true);

        CommandMatcher.MatchResult res = matcher.match(cmd2, sender, new String[]{"location", "7", "8", "9"});

        assertInstanceOf(CommandMatcher.MatchResult.Match.class, res);
    }

    @Test
    public void permissionCheckWithSuccess() {
        Player sender = mock(Player.class);
        when(sender.hasPermission("amdin.superSecretLogin")).thenReturn(false);

        CommandMatcher.MatchResult res1 = matcher.match(cmd4, sender, new String[]{"admin", "passWord"});

        assertInstanceOf(CommandMatcher.MatchResult.NoMatch.Permission.class, res1);
    }

    @Test
    public void permissionCheckWithoutSuccess() {
        Player sender = mock(Player.class);
        when(sender.hasPermission("amdin.superSecretLogin")).thenReturn(true);

        CommandMatcher.MatchResult res1 = matcher.match(cmd4, sender, new String[]{"admin", "passWord"});

        assertInstanceOf(CommandMatcher.MatchResult.Match.class, res1);
    }

}
