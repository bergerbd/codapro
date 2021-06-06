package de.codapro.niusb.api;

import de.codapro.niusb.driver.DriverLibrary;

public enum ClockTiming {
	Falling(DriverLibrary.DAQmx_Val_Falling),
	Rising(DriverLibrary.DAQmx_Val_Rising);

	private final int apiValue;

	private ClockTiming(final int apiValue) {
		this.apiValue = apiValue;
	}

	public int getApiValue() {
		return apiValue;
	}
}
