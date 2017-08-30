package nz.co.trademe.mapme.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nz.co.trademe.mapme.annotations.MapAnnotation;
import nz.co.trademe.mapme.sample.activities.MapActivity;


public class MarkerBottomSheet extends BottomSheetDialogFragment {

    public static final String ARG_ANNOTATION = "annotation";
    private MapAnnotation mapAnnotation;

    public static MarkerBottomSheet newInstance(@NonNull MapAnnotation annotation) {
        MarkerBottomSheet fragment = new MarkerBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ANNOTATION, annotation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mapAnnotation = (MapAnnotation) getArguments().getSerializable(ARG_ANNOTATION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.marker_bottom_sheet, container, false);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MapActivity) getActivity()).unselectMarker(mapAnnotation);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.remove_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapActivity) getActivity()).removeAnnotation(mapAnnotation);
                dismiss();
            }
        });
    }
}
