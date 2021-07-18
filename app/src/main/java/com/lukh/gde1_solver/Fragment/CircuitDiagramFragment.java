package com.lukh.gde1_solver.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.lukh.gde1_solver.Listeners.OnCircuitComponentDragListener;
import com.lukh.gde1_solver.Listeners.OnCircuitComponentTouchedListener;
import com.lukh.gde1_solver.Listeners.OnCircuitDiagramScale;
import com.lukh.gde1_solver.R;
import com.lukh.gde1_solver.view.ZoomableMaterialCardView;

public class CircuitDiagramFragment extends Fragment {

    private LinearLayout circuitComponentLinearLayout;
    private ZoomableMaterialCardView ciruitDiagramCardView;
    private ImageView resistance;
    private ImageView voltageSource;
    private ImageView currentSource;
    private Context context;
    private OnCircuitComponentTouchedListener onCircuitComponentTouchedListener;
    private ScaleGestureDetector scaleGestureDetector;
    private ImageView tagForDraggedComponent;

    public CircuitDiagramFragment (Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.circuit_diagram,container,false);
        resistance = view.findViewById(R.id.resistance);
        voltageSource = view.findViewById(R.id.voltage_source);
        currentSource = view.findViewById(R.id.current_source);
        ciruitDiagramCardView = view.findViewById(R.id.circuit_diagram);
        circuitComponentLinearLayout =  view.findViewById(R.id.componentstackContainer);
        onCircuitComponentTouchedListener = new OnCircuitComponentTouchedListener();
        resistance.setOnTouchListener(onCircuitComponentTouchedListener);
        voltageSource.setOnTouchListener(onCircuitComponentTouchedListener);
        currentSource.setOnTouchListener(onCircuitComponentTouchedListener);
        currentSource.setTag("currentsource0");
        voltageSource.setTag("voltagesource0");
        resistance.setTag("resistance0");
        tagForDraggedComponent = new ImageView(getContext());
        ciruitDiagramCardView.setOnDragListener(new OnCircuitComponentDragListener(context,onCircuitComponentTouchedListener,tagForDraggedComponent));
        circuitComponentLinearLayout.setOnDragListener(new OnCircuitComponentDragListener(context,onCircuitComponentTouchedListener,tagForDraggedComponent));
        ciruitDiagramCardView.setOnDragListener(new OnCircuitComponentDragListener(context,onCircuitComponentTouchedListener,tagForDraggedComponent));
        ciruitDiagramCardView.setTagForDraggedComponent(tagForDraggedComponent);
        ciruitDiagramCardView.setOnCircuitComponentTouchedListener(onCircuitComponentTouchedListener);
        //ciruitDiagramCardView.initViewPort();





        return view;
    }





}
