package ru.kpfu.desktop.setup.model;

public record Dictionary(
        int size, SizeUnit sizeUnit
)
{
    public enum SizeUnit {
        MB, GB
    }
}
