## CommandAPI
Status: Work in Progress

### Creating a command

```java
CommandRegistry registry = new CommandRegistry(this);

CommandBuilder builder = CommandBuilder.of("tablist")
        .literal("nick")
        .argument(InputArguments.STRING.createArgument("name"))
        .executor(ctx -> {
            Player p = ctx.getSender(Player.class);
            String name = ctx.get("name", String.class);

            p.playerListName(Component.text(name));
        });

registry.registerCommand(builder.create());
```

### Creating a command using annotations
Write the command logic:
Class name: TestTablistCommand
```java
@CommandGroup("tablist")
public class TestTablistCommand {

    @Cmd("nick #name")
    public void nickCommand(@Sender Player p, @Arg("name") String name){
        p.setPlayerListName(name);
    }

}
```

Register in main class:
```java
CommandRegistry registry = new CommandRegistry(this);

InputArgumentRegistry inputArgumentRegistry = InputArgumentRegistry.defaultRegistry();

var commandBuilder = new ReflectiveCommandBuilder(inputArgumentRegistry);

TestTablistCommand landCommand = new TestTablistCommand();

registry.registerCommands(commandBuilder.buildCommands(landCommand));
```

