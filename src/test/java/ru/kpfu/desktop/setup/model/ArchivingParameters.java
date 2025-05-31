package ru.kpfu.desktop.setup.model;

import javax.annotation.Nullable;

public record ArchivingParameters(
        String name,
        @Nullable CompressionLevel compressionLevel,
        @Nullable Dictionary dictionary,
        boolean deleteFilesAfterArchiving,
        @Nullable String password
) {
    public static ArchivingParameters withDefaults(String name) {
        return new ArchivingParameters(name, null, null, false, null);
    }

    public static ArchivingParametersBuilder builder() {
        return new ArchivingParametersBuilder();
    }

    public static class ArchivingParametersBuilder {
        private String name;
        private CompressionLevel compressionLevel;
        private Dictionary dictionary;
        private boolean deleteFilesAfterArchiving;
        private String password;

        public ArchivingParametersBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArchivingParametersBuilder compressionLevel(CompressionLevel compressionLevel) {
            this.compressionLevel = compressionLevel;
            return this;
        }

        public ArchivingParametersBuilder dictionary(Dictionary dictionary) {
            this.dictionary = dictionary;
            return this;
        }

        public ArchivingParametersBuilder deleteFilesAfterArchiving(boolean deleteFilesAfterArchiving) {
            this.deleteFilesAfterArchiving = deleteFilesAfterArchiving;
            return this;
        }

        public ArchivingParametersBuilder password(String password) {
            this.password = password;
            return this;
        }

        public ArchivingParameters build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Имя архива обязательно");
            }
            return new ArchivingParameters(name, compressionLevel, dictionary, deleteFilesAfterArchiving, password);
        }
    }
}
