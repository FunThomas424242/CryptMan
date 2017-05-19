/* $Id:$ */
/******************************************************************************
 |OkButtonListener.java|  -  TODO description

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

import javax.swing.JOptionPane;

/**
 * @author tmichel
 *
 */
public class OkButtonListener implements ActionListener {

    /**
     * Dialog welcher den OK Button enthält.
     */
    private IDialog dialog;

    /**
     * Klasse welche auf Ereignisse für die Bestätigungsschaltfläche
     * des Passworteingabedialogs wartet.
     *
     * @param pDialog Implementierung des Eingabedialogs
     */
    public OkButtonListener(final IDialog pDialog) {
        super();
        this.dialog = pDialog;
    }

    /**
     * Bearbeitet auftretende Ereignis.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * @param event aufgetretenes Ereignis.
     */
    public final void actionPerformed(final ActionEvent event) {
        final char[] text = this.dialog.getPassword();
        this.dialog.setReadedPassword(text);

        // gültig?
        if (text != null && text.length > 0) {
            // Dialog wird nur bei validen Passwort geschlossen
            dialog.close();
        } else {
            JOptionPane.showMessageDialog(null, "Kein gültiges Passwort",
                    "Hinweis", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * ChangeLog
 *
 * $Revision:$
 *
 */
