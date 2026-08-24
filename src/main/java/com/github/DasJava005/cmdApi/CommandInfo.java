package com.github.DasJava005.cmdApi;

import java.util.List;

public record CommandInfo(String label, List<String> aliases, String permission, String description) {}