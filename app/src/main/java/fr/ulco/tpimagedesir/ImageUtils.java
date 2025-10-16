package fr.ulco.tpimagedesir;


import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Objects;

public final class ImageUtils {
    private ImageUtils(){}
    private enum MirrorDirection { HORIZONTAL, VERTICAL }
    private enum RotationDirection { RIGHT, LEFT }
    private enum FilterType { INVERSION, GRAYSCALE }

    // Méthode pour créer une image miroir horizontalement
    public static Bitmap mirrorHorizontal(final Bitmap src) {
        return mirror(src, MirrorDirection.HORIZONTAL);
    }

    // Méthode pour créer une image miroir verticalement
    public static Bitmap mirrorVertical(final Bitmap src) {
        return mirror(src, MirrorDirection.VERTICAL);
    }

    public static Bitmap inversionColors(final Bitmap src) {
        return applyFilter(src, FilterType.INVERSION);
    }

    public static Bitmap grayLevel(final Bitmap src) {
        return applyFilter(src, FilterType.GRAYSCALE);
    }

    // Méthode pour faire une rotation de 90 degrés vers la droite
    public static Bitmap rotate90Right(final Bitmap src) {
        return rotate(src, RotationDirection.RIGHT);
    }

    // Méthode pour faire une rotation de 90 degrés vers la gauche
    public static Bitmap rotate90Left(final Bitmap src) {
       return rotate(src, RotationDirection.LEFT);
    }

    /* PRIVATE METHODS */

    // Méthode pour convertir un pixel en niveaux de gris
    private static int applyGrayScaled(final Bitmap bitmap, final int x, final int y) {
        int pixel = bitmap.getPixel(x, y);
        int a = Color.alpha(pixel);
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        int gray = (r + g + b) / 3;
        return Color.argb(a, gray, gray, gray);
    }


    // Méthode pour inverser les couleurs d'un pixel
    private static int applyInversion(final Bitmap bitmap, final int x, final int y) {
        final int pixel = bitmap.getPixel(x, y);
        final int a = Color.alpha(pixel);
        final int r = 255 - Color.red(pixel);
        final int g = 255 - Color.green(pixel);
        final int b = 255 - Color.blue(pixel);
        return Color.argb(a, r, g, b);
    }



    // Méthode privée pour créer une image miroir selon la direction spécifiée
    private static Bitmap mirror(final Bitmap src, final MirrorDirection direction) {
        if(src == null) return null;
        final int width = src.getWidth();
        final int height = src.getHeight();
        final Bitmap mirrored = Bitmap.createBitmap(width, height, Objects.requireNonNull(src.getConfig()));
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++){
                switch (direction){
                    case HORIZONTAL:
                        mirrored.setPixel(width - x - 1, y, src.getPixel(x, y));
                        break;
                    case VERTICAL:
                        mirrored.setPixel(x, height - y - 1, src.getPixel(x, y));
                        break;
                }
            }
        return mirrored;
    }


    // Méthode privée pour faire une rotation de l'image selon la direction spécifiée
    private static Bitmap rotate(final Bitmap src, final RotationDirection direction) {
        if(src == null) return null;
        final int width = src.getWidth();
        final int height = src.getHeight();
        final Bitmap rotated = Bitmap.createBitmap(height, width, Objects.requireNonNull(src.getConfig()));
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++){
                switch (direction){
                    case RIGHT:
                        rotated.setPixel(height - y - 1, x, src.getPixel(x, y));
                        break;
                    case LEFT:
                        rotated.setPixel(y, width - x - 1, src.getPixel(x, y)); // rotation 90° vers la gauche
                        break;
                }
            }
        return rotated;
    }

    // Méthode privée pour appliquer un filtre à une image selon le type de filtre spécifié
    private static Bitmap applyFilter(final Bitmap src, final FilterType filterType) {
        if(src == null) return null;
        final int width = src.getWidth();
        final int height = src.getHeight();
        final Bitmap filtered = Bitmap.createBitmap(width, height, Objects.requireNonNull(src.getConfig()));
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++){
                switch (filterType){
                    case INVERSION:
                        filtered.setPixel(x, y, applyInversion(src, x, y));
                        break;
                    case GRAYSCALE:
                        filtered.setPixel(x, y, applyGrayScaled(src, x, y));
                        break;
                }
            }
        return filtered;
    }
}