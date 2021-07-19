package me.gavin.calypso.settings;

public class EnumIndexer {

    public static Enum increaseEnum(Enum value) {
        int index = getEnumIndex(value);
        for (int i = 0; i < value.getClass().getEnumConstants().length; i++) {
            Enum e = value.getClass().getEnumConstants()[i];
            if (i != index + 1)
                continue;
            return e;
        }

        return value.getClass().getEnumConstants()[0];
    }

    public static Enum decreaseEnum(Enum value) {
        int index = getEnumIndex(value);
        for (int i = 0; i < value.getClass().getEnumConstants().length; i++) {
            Enum e = value.getClass().getEnumConstants()[i];
            if (i != index - 1)
                continue;
            return e;
        }

        return value.getClass().getEnumConstants()[value.getClass().getEnumConstants().length - 1];
    }

    public static int getEnumIndex(Enum value) {
        for (int i = 0; i < value.getClass().getEnumConstants().length; i++) {
            Enum e = value.getClass().getEnumConstants()[i];
            if (e.name().equalsIgnoreCase(value.name()))
                return i;
        }

        return -1;
    }

    public static Enum getEnumValue(int index, Enum clazz) {
        return clazz.getClass().getEnumConstants()[index];
    }
}
