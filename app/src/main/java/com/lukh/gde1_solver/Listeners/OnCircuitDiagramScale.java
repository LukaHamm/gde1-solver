package com.lukh.gde1_solver.Listeners;


import android.view.ScaleGestureDetector;

import com.google.android.material.card.MaterialCardView;
import com.lukh.gde1_solver.view.ZoomableMaterialCardView;

public class OnCircuitDiagramScale extends ScaleGestureDetector.SimpleOnScaleGestureListener{

    private Double scaleFactor = 1.0;
    private ZoomableMaterialCardView zoomableMaterialCardView;


    public OnCircuitDiagramScale(ZoomableMaterialCardView zoomableMaterialCardView){
        this.zoomableMaterialCardView = zoomableMaterialCardView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();

        scaleFactor = Math.max(0.1,Math.min(scaleFactor,5.0));
        zoomableMaterialCardView.invalidate();
        return true;
    }

    public Double getScaleFactor() {
        return scaleFactor;
    }
}
