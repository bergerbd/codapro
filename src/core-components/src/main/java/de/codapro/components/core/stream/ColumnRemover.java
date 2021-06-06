package de.codapro.components.core.stream;

import java.util.Arrays;

import javax.inject.Named;

import de.codapro.api.annotations.Component;
import de.codapro.api.annotations.Input;
import de.codapro.api.annotations.OnInit;
import de.codapro.api.annotations.OnProcess;
import de.codapro.api.model.DataVector;
import de.codapro.api.model.Stream;

@Component(name = "ColumnRemover", doc = "Removes columns from a stream.")
public class ColumnRemover {
	@Input(doc="Name of the columns to remove.", name="columns")
	private String [] columns = {};

	private int [] columnIndices;

	@OnInit
	public void execute(final @Named("output-stream") Stream stream) {
		columnIndices = new int [columns.length];
		for(int i = 0; i < columns.length; ++i) {
			columnIndices[i] = stream.getHeader().indexOf(columns[i]);
		}

		Arrays.sort(columnIndices);

		for(int index = columnIndices.length - 1; index >= 0; --index) {
			stream.getHeader().remove(columnIndices[index]);
		}
	}

	@OnProcess
	public void removeColumns(final DataVector vector) {
		final int [] localColumns = columnIndices;

		for(int index = localColumns.length - 1; index >= 0; --index) {
			vector.remove(localColumns[index]);
		}
	}

	@Override
	public String toString() {
		return "ColumnRemover [columns=" + Arrays.toString(columns) + "]";
	}
}
