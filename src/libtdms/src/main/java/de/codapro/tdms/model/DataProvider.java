package de.codapro.tdms.model;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Optional;

/**
 * A data provider for writing data to a file.
 *
 * @param <T> Type of the data provided.
 */
public interface DataProvider<T> extends Iterator<T> {

	/**
	 * @return Number of data provided if known in advance. {@code Optional.empty()} otherwise.
	 */
	public Optional<BigInteger> size();
}
