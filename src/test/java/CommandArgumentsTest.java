import com.github.DasJava005.cmdApi.base.CommandArguments;
import com.github.DasJava005.cmdApi.base.arguments.Argument;
import com.github.DasJava005.cmdApi.base.arguments.GreedyArgument;
import com.github.DasJava005.cmdApi.base.arguments.LiteralArgument;
import com.github.DasJava005.cmdApi.base.arguments.impl.IntegerArgument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandArgumentsTest {

    @Test
    void getArgumentAtIndexReturnsArgumentWhenIndexExists() {
        CommandArguments arguments = new CommandArguments(List.of(new LiteralArgument("string1")));

        Argument result = arguments.getArgumentAtIndex(0);

        assertEquals(arguments.getArguments().getFirst(), result);
    }

    @Test
    void getArgumentAtIndexReturnsGreedyArgument() {
        IntegerArgument intArg = new IntegerArgument("key0");
        GreedyArgument greedyArgument = new GreedyArgument("key1");

        CommandArguments arguments = new CommandArguments(List.of(intArg, greedyArgument));

        assertEquals(intArg, arguments.getArgumentAtIndex(0));

        for(int i = 1; i <= 10; i++){
            assertEquals(greedyArgument, arguments.getArgumentAtIndex(i));
        }
    }

    @Test
    void getArgumentAtIndexReturnsNullWhenIndexDoesNotExist() {
        CommandArguments arguments = new CommandArguments(List.of(new LiteralArgument("string1")));

        Argument result = arguments.getArgumentAtIndex(1);

        assertNull(result);
    }


}
