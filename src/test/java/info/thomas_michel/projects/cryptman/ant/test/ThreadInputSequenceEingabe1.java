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

import info.thomas_michel.projects.cryptman.gui.GUIConstants;

import javax.swing.JDialog;

import org.fest.swing.core.Robot;
import org.fest.swing.core.RobotFixture;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.DialogFixture;

class ThreadInputSequenceEingabe1 implements Runnable {

    public void run() {
        Robot robot = RobotFixture.robotWithCurrentAwtHierarchy();
        DialogFixture fixture = WindowFinder.findDialog(JDialog.class)
                .withTimeout(10000).using(robot);
        fixture.textBox(GUIConstants.FIELD_PASSWORD).enterText("Bitte eingeben:Das gibt aber Ärger bei so einem Übel");
        fixture.button(GUIConstants.BUTTON_OK).click();
        robot.cleanUp();

    }

}

/**
* ChangeLog
* 
* $Revision:$
* 
*/