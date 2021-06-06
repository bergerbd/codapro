package de.codapro.components.core.math;

import de.codapro.api.annotations.ColumnId;
import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Value;

@Component(name = "BaselineCorrector", doc = "Component to correct data stream by a given offset.")
public class BaselineCorrector {
	@Input(doc = "The offset the datastream is corrected by.", name = "offset", type = Double.class)
	private Value<Double> offset;

	/**
	 * Field for the value of {@code offset}.
	 */
	private double offsetValue = 0.0;

	@ColumnId(stream = "input-stream", name = "column", doc = "Name of column to correct.")
	private int dataIndex;

	@OnInit
	public void init() {
		offsetValue = offset.get();
	}

	@OnProcess
	public void process(final DataVector vector) {
		vector.set(dataIndex, ((Number) vector.get(dataIndex)).doubleValue() - offsetValue);
	}

	@Override
	public String toString() {
		return "BaselineCorrection [offset=" + offset + "]";
	}
}
