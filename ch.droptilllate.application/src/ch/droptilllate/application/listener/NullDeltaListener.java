/**
 * Date: 03.11.2010
 * SteadyCrypt v2 Project by Joerg Harr and Marvin Hoffmann
 *
 */

package ch.droptilllate.application.listener;

public class NullDeltaListener implements IDeltaListener {
	protected static NullDeltaListener soleInstance = new NullDeltaListener();

	public static NullDeltaListener getSoleInstance() {
		return soleInstance;
	}

	public void add(DeltaEvent event) {
	}

	public void remove(DeltaEvent event) {
	}

}
