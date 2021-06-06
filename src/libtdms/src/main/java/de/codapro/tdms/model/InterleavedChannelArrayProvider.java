package de.codapro.tdms.model;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InterleavedChannelArrayProvider<T> implements InterleavedChannelDataProvider {
	private Optional<BigInteger> size = Optional.empty();
	private boolean modified = true;

	private final Type[] types;
	private final T[][] values;
	private int index = -1;

	public InterleavedChannelArrayProvider(final Type[] types, final T[][] values) {
		this.types = types;
		this.values = values;
	}

	@Override
	public Optional<BigInteger> size() {
		if(size.isPresent()) {
			return size;
		}

		return Optional.of(BigInteger.valueOf(values.length));
	}

	@Override
	public boolean hasNext() {
		return index + 1 < values.length;

	}

	@Override
	public T[] next() {
		if(!hasNext()) {
			throw new NoSuchElementException("Iterator is already at its end.");
		}

		return values[++index];
	}

	@Override
	public Type getType(final int channelIndex) {
		return types[channelIndex];
	}

	@Override
	public int getChannelCount() {
		return types.length;
	}

	@Override
	public void setSize(long count) {
		size = Optional.of(BigInteger.valueOf(count));
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setClean() {
		modified = false;
	}
}
