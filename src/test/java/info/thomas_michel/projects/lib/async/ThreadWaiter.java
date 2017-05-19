/* $Id:$ */
/******************************************************************************
 |ThreadWaiter.java|  -  TODO description

 begin                : |17.08.2008|
 copyright            : (C) |2008| by |SchubertT006|
 email                : |development@thomas-michel.info|
 ******************************************************************************/
/******************************************************************************
 *                                                                            *
 *   This program is free software; you can redistribute it and/or modify     *
 *   it under the terms of the Lesser GNU General Public License as published *
 *   by the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                      *
 *                                                                            *
 ******************************************************************************/
package info.thomas_michel.projects.lib.async;

/**
 * @author SchubertT006
 *
 */
public class ThreadWaiter {

    public static void waitForSubThread(final Thread subThread) {
        try {
            while (subThread.isAlive()) {
                subThread.join();
                Thread.currentThread().yield();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
* ChangeLog
* 
* $Revision:$
* 
*/
