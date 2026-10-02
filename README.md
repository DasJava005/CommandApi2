## WIP

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
