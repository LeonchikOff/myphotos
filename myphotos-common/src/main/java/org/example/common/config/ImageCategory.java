package org.example.common.config;

public enum ImageCategory {

    PROFILE_AVATAR("media/avatar/", 128, 128, true, 0.8),
    SMALL_PHOTO("media/photo/", 400, 250, true, 0.7),
    LARGE_PHOTO("media/photo/", 1600, 900, false, 1.0);


    public static boolean isImageCategoryUrl(String imageUrl) {
        if (imageUrl != null)
            for (ImageCategory imageCategory : ImageCategory.values())
                if (imageUrl.contains(imageCategory.relativeRoot)) return true;
        return false;
    }


    private final String relativeRoot;
    private final int width;
    private final int height;
    private final boolean crop;
    private final double quality;
    private final String outputExtensionFormat;

    ImageCategory(String relativeRoot, int width, int height, boolean crop, double quality) {
        this.relativeRoot = relativeRoot;
        this.width = width;
        this.height = height;
        this.crop = crop;
        this.quality = quality;
        this.outputExtensionFormat = "jpg";
    }

    public String getRelativeRoot() {
        return relativeRoot;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isCrop() {
        return crop;
    }

    public double getQuality() {
        return quality;
    }

    public String getOutputExtensionFormat() {
        return outputExtensionFormat;
    }
}
