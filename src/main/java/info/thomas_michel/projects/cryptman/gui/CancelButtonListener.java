/* $Id:$ */
/******************************************************************************
 |CancelButtonListener.java|  -  TODO description

 begin                : |07.01.2007|
 copyright            : (C) |2007| by |tmichel|
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
package info.thomas_michel.projects.cryptman.gui;

import info.thomas_michel.projects.cryptman.io.IDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener welcher auf Ereignisse der Abbruch Schaltfläche
 * des Passworteingabedialogs lauscht.
 * 
 * @author tmichel
 *
 */
public class CancelButtonListener implements ActionListener {

    protected IDialog dialog=null;
    
    /**
     * Wartet auf das Drücken der Abbruch Schaltfläche
     * @param impl Implementierung des Eingabedialogs
     * 
     */
    public CancelButtonListener(IDialog dialog) {
        super();
        this.dialog=dialog;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        dialog.setUserCanceled(true);
        dialog.close();
    }

}


/**
* ChangeLog
* 
* $Revision:$
* 
*/