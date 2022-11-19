package com.gemastik.dentistsmile.components.tflite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.tensorflow.lite.task.vision.segmenter.ColoredLabel;
import org.tensorflow.lite.task.vision.segmenter.Segmentation;
import static java.lang.Math.*;
public class ColorLabel {
    int id;
    String label;
    int rgbColor;
    public Boolean isExist = false;

    public ColorLabel(int id, String label, int rgbColor, Boolean isExist) {
        this.id = id;
        this.label = label;
        this.rgbColor = rgbColor;
        this.isExist = isExist;
    }

    public int getColor(){
        if (id==0){
            return Color.TRANSPARENT;
        }else{
            return Color.argb(128, 0, 148, 231);
        }
    }
}
