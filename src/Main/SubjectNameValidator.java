package Main;

import java.util.regex.Pattern;

public final class SubjectNameValidator implements Validator<String> {
    private static final Pattern BLANK = Pattern.compile("^\\s*$");

    @Override
    public void validate(String name) {
        if (name == null || BLANK.matcher(name).matches()) {
            throw new IllegalArgumentException("Subject name cannot be empty.");
        }
        if (name.length() > 64) {
            throw new IllegalArgumentException("Subject name must be ≤ 64 characters.");
        }
    }
}

