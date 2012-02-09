/* $Id: OSCPacketDispatcher.java,v 1.2 2008/07/01 15:29:46 modin Exp $
 * Created on 28.10.2003
 */
package com.illposed.osc.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

/**
 * @author cramakrishnan
 *
 * Copyright (C) 2003, C. Ramakrishnan / Auracle
 * All rights reserved.
 * 
 * See license.txt (or license.rtf) for license information.
 * 
 * Dispatches OSCMessages to registered listeners.
 * 
 */

public class OSCPacketDispatcher {
	private Hashtable<String,ArrayList<OSCListener>> addressToClassTable = new Hashtable<String,ArrayList<OSCListener>>();
	
	/**
	 * 
	 */
	public OSCPacketDispatcher() {
		super();
	}

	public void addListener(String address, OSCListener listener) {
		if (addressToClassTable.containsKey(address)) {
			addressToClassTable.get(address).add(listener);
		} else {
			ArrayList<OSCListener> arraylst = new ArrayList<OSCListener>();
			arraylst.add(listener);
			addressToClassTable.put(address, arraylst);
		}
	}
	
	public void dispatchPacket(OSCPacket packet) {
		if (packet instanceof OSCBundle)
			dispatchBundle((OSCBundle) packet);
		else
			dispatchMessage((OSCMessage) packet);
	}
	
	public void dispatchPacket(OSCPacket packet, Date timestamp) {
		if (packet instanceof OSCBundle)
			dispatchBundle((OSCBundle) packet);
		else
			dispatchMessage((OSCMessage) packet, timestamp);
	}
	
	private void dispatchBundle(OSCBundle bundle) {
		Date timestamp = bundle.getTimestamp();
		OSCPacket[] packets = bundle.getPackets();
		for (int i = 0; i < packets.length; i++) {
			dispatchPacket(packets[i], timestamp);
		}
	}
	
	private void dispatchMessage(OSCMessage message) {
		dispatchMessage(message, null);
	}
	
	private void dispatchMessage(OSCMessage message, Date time) {
		System.out.println(message.getAddress());
		Enumeration keys = addressToClassTable.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			// this supports the OSC regexp facility, but it
			// only works in JDK 1.4, so don't support it right now
			//if (key.matches(message.getAddress())) {
			if (key.equals(message.getAddress())) {
				System.out.println("Found listener: "+message.getAddress());
				ArrayList<OSCListener> listenerlist = addressToClassTable.get(key);
				for (OSCListener listener : listenerlist) {
					listener.acceptMessage(time, message);
				}
			}
		}
	}
}
