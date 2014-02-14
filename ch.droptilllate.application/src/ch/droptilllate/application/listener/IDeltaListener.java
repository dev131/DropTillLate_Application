/**
 * Date: 03.11.2010
 * SteadyCrypt v2 Project by Joerg Harr and Marvin Hoffmann
 *
 */

package ch.droptilllate.application.listener;

public interface IDeltaListener {
	public void add(DeltaEvent event);

	public void remove(DeltaEvent event);
}
