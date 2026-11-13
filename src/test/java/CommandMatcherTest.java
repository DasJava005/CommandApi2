import com.github.DasJava005.cmdApi.base.Command;
import com.github.DasJava005.cmdApi.base.builder.CommandBuilder;
import com.github.DasJava005.cmdApi.base.registry.dispatcher.CommandMatcher;
import org.junit.jupiter.api.Test;

public class CommandMatcherTest {

    private final Command cmd0 = CommandBuilder.of("land").literal("members").literal("add").create();
    private final Command cmd1 = CommandBuilder.of("land").literal("members").literal("remove").create();
    private final Command cmd2 = CommandBuilder.of("land").literal("members").literal("list").create();

    @Test
    public void testCommandMatcher() {
        CommandMatcher commandMatcher = new CommandMatcher();



    }

}
