package de.codapro.niusb.api;

import de.codapro.niusb.driver.DriverLibrary;

public enum LoggingMode {
	Off(DriverLibrary.DAQmx_Val_Off),
	Log(DriverLibrary.DAQmx_Val_Log),
	LogAndRead(DriverLibrary.DAQmx_Val_LogAndRead);

	private final int apiValue;

	private LoggingMode(final int apiValue) {
		this.apiValue = apiValue;
	}

	public int getApiValue() {
		return apiValue;
	}
}
