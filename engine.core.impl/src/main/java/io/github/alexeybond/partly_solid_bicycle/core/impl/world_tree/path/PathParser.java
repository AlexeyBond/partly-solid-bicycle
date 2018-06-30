package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public enum PathParser {
    ;
    private static final Pattern SPLIT_PATTERN = Pattern.compile("/");

    @SuppressWarnings("StatementWithEmptyBody")
    @NotNull
    public static LogicNodePath parseString(@NotNull String string) {
        if ("".equals(string)) return RelativeBasePath.INSTANCE;
        boolean isAbsolute = string.startsWith("/");
        LogicNodePath result = isAbsolute ? RootPath.INSTANCE : RelativeBasePath.INSTANCE;
        String[] stepTexts = SPLIT_PATTERN.split(string, 0);

        for (int i = isAbsolute ? 1 : 0; i < stepTexts.length; ++i) {
            String stepText = stepTexts[i];

            if ("".equals(stepText)) {
                result = RootPath.INSTANCE;
            } else if (".".equals(stepText)) {
            } else if ("..".equals(stepText)) {
                result = new ParentPath(result);
            } else {
                result = new StepPath(
                        result,
                        PathUtils.normalizeStep(stepText)
                );
            }
        }

        return result;
    }
}
