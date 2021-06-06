package de.codapro.niusb.api;

import de.codapro.niusb.driver.DriverLibrary;

public enum FileMode {
	Open(DriverLibrary.DAQmx_Val_Open),
	OpenOrCreate(DriverLibrary.DAQmx_Val_OpenOrCreate),
	CreateOrReplace(DriverLibrary.DAQmx_Val_CreateOrReplace),
	Create(DriverLibrary.DAQmx_Val_Create);

	private final int apiValue;

	private FileMode(final int apiValue) {
		this.apiValue = apiValue;
	}

	public int getApiValue() {
		return apiValue;
	}
}
