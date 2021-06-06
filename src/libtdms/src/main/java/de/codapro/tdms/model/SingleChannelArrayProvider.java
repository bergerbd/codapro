package de.codapro.tdms.model;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * An array-based data provider for a single channel.
 */
public class SingleChannelArrayProvider<T> implements SingleChannelDataProvider<T> {
	/**
	 * Data for the provider
	 */
	private T [] data;

	/**
	 * Current index within the data array
	 */
	private int index = -1;

	/**
	 * Type of the data
	 */
	private final Type type;

	public SingleChannelArrayProvider(final Type type) {
		this.type = type;
		this.data = (T[]) new Object [0];
	}

	public SingleChannelArrayProvider(final Type type, final T [] data) {
		this.type = type;
		this.data = data;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public boolean hasNext() {
		return index + 1 < data.length;
	}

	@Override
	public T next() {
		if(!hasNext()) {
			throw new NoSuchElementException("Iterator already at its end.");
		}

		return data[++index];
	}

	@Override
	public Optional<BigInteger> size() {
		return Optional.of(BigInteger.valueOf(data.length));
	}
}
