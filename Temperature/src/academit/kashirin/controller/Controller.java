package academit.kashirin.controller;

import academit.kashirin.Scale;
import academit.kashirin.TemperatureView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private TemperatureView view;
    private Scale[] scales;

    public Controller(TemperatureView view, Scale[] scale) {
        this.view = view;
        this.scales = scale;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Double temperatureToCelsius = scales[view.getInputScale()].convertToCelsius(view.getInputTemperature());
        view.setOutputTemperature(scales[view.getOutputScale()].convertFromCelsius(temperatureToCelsius));
    }
}
