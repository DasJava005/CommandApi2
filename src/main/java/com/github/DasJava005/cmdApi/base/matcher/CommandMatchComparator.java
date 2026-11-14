package com.github.DasJava005.cmdApi.base.matcher;

import java.util.Comparator;
import java.util.Map;

/**
 * Compares {@link MatchResult} instances based on their priority
 * to determine the most useful error message to display to the user.
 *
 * <p>Results with a lower precedence value are considered more relevant
 * and are placed before results with a higher priority value.</p>
 *
 * <p>Match result types not explicitly defined in {@code VALUES}
 * are assigned the lowest priority.</p>
 */
public class CommandMatchComparator implements Comparator<MatchResult> {

    protected Map<Class<? extends MatchResult>, Integer> VALUES = Map.of(
            MatchResult.Match.class, 0,
            MatchResult.NoMatch.ParseMismatch.class, 1,
            MatchResult.NoMatch.LiteralMismatch.class, 2,
            MatchResult.NoMatch.NotEnoughArguments.class, 3,
            MatchResult.NoMatch.TooManyArguments.class, 4,
            MatchResult.NoMatch.SenderTypeMismatch.class, 5,
            MatchResult.NoMatch.Permission.class, 6,
            MatchResult.NoMatch.class, 100
    );

    @Override
    public int compare(MatchResult o1, MatchResult o2) {
        return Integer.compare(
                VALUES.getOrDefault(o1.getClass(), Integer.MAX_VALUE),
                VALUES.getOrDefault(o2.getClass(), Integer.MAX_VALUE)
        );
    }

}
