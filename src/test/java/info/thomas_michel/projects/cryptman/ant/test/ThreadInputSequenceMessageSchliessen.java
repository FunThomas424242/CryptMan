/* $Id:$ */
/******************************************************************************
 |DialogTest.java|  -  TODO description

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
package info.thomas_michel.projects.cryptman.ant.test;

import java.awt.Dialog;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.core.matcher.JButtonByTextMatcher;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.DialogFixture;

class ThreadInputSequenceMessageSchliessen implements Runnable {

    private Robot robot = null;

    public ThreadInputSequenceMessageSchliessen(final Robot robot) {
        this.robot = robot;
    }

    public void run() {
        final DialogFixture fixture = WindowFinder.findDialog(
                new GenericTypeMatcher<Dialog>() {
                    protected boolean isMatching(Dialog dialog) {
                        return "Hinweis".equals(dialog.getTitle())
                                && dialog.isShowing();
                    }
                }).withTimeout(10000).using(robot);

        fixture.button(JButtonByTextMatcher.withText("OK")).click();

    }

}

/**
* ChangeLog
* 
* $Revision:$
* 
*/
