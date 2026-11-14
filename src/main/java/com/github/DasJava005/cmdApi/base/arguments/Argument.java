package com.github.DasJava005.cmdApi.base.arguments;

public sealed interface Argument permits StaticArgument, SwallowArgument {

    String key();

}
