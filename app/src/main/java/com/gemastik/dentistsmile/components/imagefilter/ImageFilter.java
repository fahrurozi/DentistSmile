package com.gemastik.dentistsmile.components.imagefilter;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ImageFilter {
    public static class ImageFilterResult{
        public double brightness, contrast;
        public boolean pass;
        public String message;

        public ImageFilterResult(double brightness, double contrast, boolean pass, String message) {
            this.brightness = brightness;
            this.contrast = contrast;
            this.pass = pass;
            this.message = message;
        }

        public ImageFilterResult(double brightness, double contrast) {
            this.brightness = brightness;
            this.contrast = contrast;
        }
    }
    public static double standardDeviation(List<Double> list) {
        double sum = 0.0, standard_deviation = 0.0;
        int array_length = list.size();
        for(double temp : list) {
            sum += temp;
        }
        double mean = sum/array_length;
        for(double temp: list) {
            standard_deviation += Math.pow(temp - mean, 2);
        }
        return Math.sqrt(standard_deviation/array_length);
    }
    public static ImageFilterResult calculateBrightnessContrastEstimate(android.graphics.Bitmap bitmap) {
        int pixelSpacing = 1;
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        List<Double> grayList = new ArrayList<>();
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            int r = Color.red(color), g = Color.green(color), b = Color.blue(color);
            R += r;
            G += g;
            B += b;
            n++;
            grayList.add(0.299*r + 0.587*g + 0.114*b);
        }
        double brightness = (R + B + G) / (n * 3);
        double contrast = standardDeviation(grayList);
        return new ImageFilterResult(brightness, contrast);
    }

    public static ImageFilterResult getImageStatus(android.graphics.Bitmap bitmap){
        double MIN_BRIGHTNESS = 70, MAX_BRIGHTNESS =190, MIN_CONTRAST =40, MAX_CONTRAST =95;
        ImageFilterResult result = calculateBrightnessContrastEstimate(bitmap);
        if (result.brightness < MIN_BRIGHTNESS){
            result.message = "Gambar terlalu gelap";
            result.pass = false;
            return result;
        }
        else if (result.brightness > MAX_BRIGHTNESS){
            result.message = "Gambar terlalu terang";
            result.pass = false;
            return result;
        } else if (result.contrast > MAX_CONTRAST){
            result.message = "Gambar terlalu tajam";
            result.pass = false;
            return result;
        } else if (result.contrast < MIN_CONTRAST){
            result.message = "Gambar terlalu monoton";
            result.pass = false;
            return result;
        }
        else{
            result.message = "OK!";
            result.pass = true;
            return result;
        }
//        return result;

    }






}
