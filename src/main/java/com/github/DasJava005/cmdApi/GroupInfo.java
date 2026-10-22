package com.github.DasJava005.cmdApi;

import java.util.List;

public record GroupInfo(String label, List<String> aliases, String description) {}