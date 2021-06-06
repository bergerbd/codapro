package de.codapro.components.core.stream;

import javax.inject.Named;

import org.slf4j.Logger;

import de.codapro.api.ConversionException;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Log;
import de.codapro.api.annotations.OnFinish;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "GroupedStreamCreator", doc = "This component turns the whole input stream into a group.")
public class GroupedStreamCreator {
	@Log
	private Logger log;

	@OnInit
	public void copyHeaderInformation(@Named("output-stream") final Stream stream) {
		try {
			stream.append(DataVector.GROUP_START);
		} catch (ConversionException e) {
			log.error("Failed to add group start to output stream.");
		}
	}

	@OnFinish
	public void finish(@Named("output-stream") final Stream stream) {
		try {
			stream.append(DataVector.GROUP_END);
		} catch (ConversionException e) {
			log.error("Failed to add group start to output stream.");
		}
	}

	@OnProcess()
	public void process(final DataVector vector) {
		// nothing to do
	}

	@Override
	public String toString() {
		return "StreamGrouping []";
	}
}
