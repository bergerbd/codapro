/*
 * Copyright 2021 CoDaPro project. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package de.codapro.tdms;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import de.codapro.tdms.io.TDMsReader;
import de.codapro.tdms.model.TDMsChannel;
import de.codapro.tdms.model.TDMsFile;
import de.codapro.tdms.model.TDMsGroup;
import de.codapro.tdms.model.TDMsObject;

@SuppressWarnings("squid:S106")
public class TDMsInfo {
	public static void main(String ... args) throws Exception {
		try(final TDMsReader reader = new TDMsReader(new File(args[0]))) {
			print(reader.read());
		}
	}

	private static void print(final TDMsFile file) {
		System.out.println("TDMsFile " + file.getName());
		printProperties(file);

		file.getGroups().forEach(TDMsInfo::print);
	}

	private static void print(final TDMsGroup group) {
		System.out.println("Start TDMsGroup " + group.getName());
		printProperties(group);

		group.getChannels().forEach(TDMsInfo::print);
		System.out.println("End TDMsGroup " + group.getName());
	}

	private static void print(final TDMsChannel channel) {
		System.out.println("Start TDMsChannel " + channel.getName());
		printProperties(channel);

		System.out.println("  Data type is " + channel.getType());

		int count = 0;
		final Iterator<Object> iterator = channel.iterator();
		while(iterator.hasNext()) {
			iterator.next();
			count++;
		}
		System.out.println("  Containing " + count + " data sets.");
		System.out.println("End TDMsChannel " + channel.getName());
	}

	private static void printProperties(final TDMsObject obj) {
		System.out.println("  properties are:");
		for(Map.Entry<String, Object> entry : obj.getProperties().entrySet()) {
			System.out.println("    " + entry.getKey() + " -> " + entry.getValue());
		}
	}
}
