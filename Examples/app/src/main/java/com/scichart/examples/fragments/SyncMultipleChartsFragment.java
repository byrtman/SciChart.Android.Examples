//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2017. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// SyncMultipleChartsFragment.java is part of the SCICHART® Examples. Permission is hereby granted
// to modify, create derivative works, distribute and publish any part of this source
// code whether for commercial, private or personal use.
//
// The SCICHART® examples are distributed in the hope that they will be useful, but
// without any warranty. It is provided "AS IS" without warranty of any kind, either
// expressed or implied.
//******************************************************************************

package com.scichart.examples.fragments;

import android.view.LayoutInflater;
import android.view.animation.DecelerateInterpolator;

import androidx.viewbinding.ViewBinding;

import com.scichart.charting.model.dataSeries.IDataSeries;
import com.scichart.charting.model.dataSeries.IXyDataSeries;
import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.axes.IAxis;
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.data.model.DoubleRange;
import com.scichart.data.model.IRange;
import com.scichart.drawing.utility.ColorUtil;
import com.scichart.examples.R;
import com.scichart.examples.databinding.ExampleSyncMultipleChartsFragmentBinding;
import com.scichart.examples.fragments.base.ExampleBaseFragment;

import java.util.Collections;

public class SyncMultipleChartsFragment extends ExampleBaseFragment<ExampleSyncMultipleChartsFragmentBinding> {
    private final static int POINTS_COUNT = 500;

    private IRange sharedXRange = new DoubleRange(0d, 1d);
    private IRange sharedYRange = new DoubleRange(0d, 1d);

    @Override
    public boolean showDefaultModifiersInToolbar() {
        return false;
    }

    @Override
    protected ExampleSyncMultipleChartsFragmentBinding inflateBinding(LayoutInflater inflater) {
        return ExampleSyncMultipleChartsFragmentBinding.inflate(inflater);
    }

    @Override
    protected void initExample(ExampleSyncMultipleChartsFragmentBinding binding) {
        initChart(binding.chart0);
        initChart(binding.chart1);
    }

    private void initChart(final SciChartSurface surface) {
        final IAxis xAxis = sciChartBuilder.newNumericAxis().withGrowBy(0.1d, 0.1d).withVisibleRange(sharedXRange).build();
        final IAxis yAxis = sciChartBuilder.newNumericAxis().withGrowBy(0.1d, 0.1d).withVisibleRange(sharedYRange).build();

        final FastLineRenderableSeries line = sciChartBuilder.newLineSeries().withDataSeries(createDataSeries()).withStrokeStyle(ColorUtil.Green, 1f, true).build();

        UpdateSuspender.using(surface, new Runnable() {
            @Override
            public void run() {
                Collections.addAll(surface.getXAxes(), xAxis);
                Collections.addAll(surface.getYAxes(), yAxis);
                Collections.addAll(surface.getRenderableSeries(), line);
                Collections.addAll(surface.getChartModifiers(), sciChartBuilder.newModifierGroup()
                        .withMotionEventsGroup("ModifiersSharedEventsGroup").withReceiveHandledEvents(true)
                        .withZoomExtentsModifier().build()
                        .withPinchZoomModifier().build()
                        .withRolloverModifier().withReceiveHandledEvents(true).build()
                        .withXAxisDragModifier().withReceiveHandledEvents(true).build()
                        .withYAxisDragModifier().withReceiveHandledEvents(true).build()
                        .build());

                surface.zoomExtents();

                sciChartBuilder.newAnimator(line).withSweepTransformation().withInterpolator(new DecelerateInterpolator()).withDuration(3000).withStartDelay(350).start();
            }
        });
    }

    private IDataSeries createDataSeries() {
        IXyDataSeries<Double, Double> dataSeries = new XyDataSeries<>(Double.class, Double.class);

        for (int i = 1; i < POINTS_COUNT; i++) {
            dataSeries.append((double) i, POINTS_COUNT * Math.sin(i * Math.PI * 0.1) / i);
        }

        return dataSeries;
    }
}