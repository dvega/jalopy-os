/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the Jalopy project nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * $Id$
 */
package de.hunsicker.jalopy.ui;

import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


/**
 * A component that can be used to display/edit the Jalopy code inspection
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class TipsPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private JCheckBox _tip15CheckBox;
    private JCheckBox _tipAvoidThreadGroupsCheckBox;
    private JCheckBox _tipDeclareListCommentCheckBox;
    private JCheckBox _tipDontIgnoreExceptionCheckBox;
    private JCheckBox _tipDontSubstituteCheckBox;
    private JCheckBox _tipEmptyFinallyCheckBox;
    private JCheckBox _tipInterfaceTypeCheckBox;
    private JCheckBox _tipNeverExceptionCheckBox;
    private JCheckBox _tipNeverThrowableCheckBox;
    private JCheckBox _tipObeyEqualsCheckBox;
    private JCheckBox _tipOverrideEqualsCheckBox;
    private JCheckBox _tipOverrideHashCodeCheckBox;
    private JCheckBox _tipOverrideStringCheckBox;
    private JCheckBox _tipReferByInterfaceCheckBox;
    private JCheckBox _tipReplaceStructCheckBox;
    private JCheckBox _tipWaitOutsideLoopCheckBox;
    private JCheckBox _tipWrongListCommentCheckBox;
    private JCheckBox _tipZeroArrayCheckBox;

    //~ Constructors ··························································

    /**
     * Creates a new TipsPanel object.
     */
    public TipsPanel()
    {
        initialize();
    }


    /**
     * Creates a new TipsPanel.
     *
     * @param container the parent container.
     */
    TipsPanel(SettingsContainer container)
    {
        super(container);
        initialize();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.settings.putBoolean(Keys.TIP_NEVER_THROW_EXCEPTION,
                              _tipNeverExceptionCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_NEVER_THROW_THROWABLE,
                              _tipNeverThrowableCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_DONT_IGNORE_EXCEPTION,
                              _tipDontIgnoreExceptionCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_NEVER_WAIT_OUTSIDE_LOOP,
                              _tipWaitOutsideLoopCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_AVOID_THREAD_GROUPS,
                              _tipAvoidThreadGroupsCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_DECLARE_COLLECTION_COMMENT,
                              _tipDeclareListCommentCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_WRONG_COLLECTION_COMMENT,
                              _tipWrongListCommentCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_EMPTY_FINALLY,
                              _tipEmptyFinallyCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_DONT_SUBSTITUTE_OBJECT_EQUALS,
                              _tipDontSubstituteCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_OBEY_CONTRACT_EQUALS,
                              _tipObeyEqualsCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_OVERRIDE_HASHCODE,
                              _tipOverrideHashCodeCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_OVERRIDE_EQUALS,
                              _tipOverrideEqualsCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_OVERRIDE_TO_STRING,
                              _tipOverrideStringCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_INTERFACE_ONLY_FOR_TYPE,
                              _tipInterfaceTypeCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_REPLACE_STRUCTURE_WITH_CLASS,
                              _tipReplaceStructCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_RETURN_ZERO_ARRAY,
                              _tipZeroArrayCheckBox.isSelected());
        this.settings.putBoolean(Keys.TIP_REFER_BY_INTERFACE,
                              _tipReferByInterfaceCheckBox.isSelected());
    }


    private void initialize()
    {
        JPanel miscPanel = new JPanel();
        GridLayout miscPanelLayout = new GridLayout(2, 10);
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(BorderFactory.createCompoundBorder(
                                                               BorderFactory.createTitledBorder("Tips"),
                                                               BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _tipDontSubstituteCheckBox = new JCheckBox("1",
                                                   this.settings.getBoolean(
                                                                         Keys.TIP_DONT_SUBSTITUTE_OBJECT_EQUALS,
                                                                         false));
        _tipDontSubstituteCheckBox.setToolTipText("Don't substitute another type for Object in the equals declaration");
        miscPanel.add(_tipDontSubstituteCheckBox);
        _tipObeyEqualsCheckBox = new JCheckBox("2",
                                               this.settings.getBoolean(
                                                                     Keys.TIP_OBEY_CONTRACT_EQUALS,
                                                                     false));
        _tipObeyEqualsCheckBox.setToolTipText("Obey the general contract when overriding equals");
        miscPanel.add(_tipObeyEqualsCheckBox);
        _tipOverrideHashCodeCheckBox = new JCheckBox("3",
                                                     this.settings.getBoolean(
                                                                           Keys.TIP_OVERRIDE_HASHCODE,
                                                                           false));
        _tipOverrideHashCodeCheckBox.setToolTipText("Always override hashCode when you override equals");
        miscPanel.add(_tipOverrideHashCodeCheckBox);
        _tipOverrideEqualsCheckBox = new JCheckBox("4",
                                                   this.settings.getBoolean(
                                                                         Keys.TIP_OVERRIDE_EQUALS,
                                                                         false));
        _tipOverrideEqualsCheckBox.setToolTipText("Always override equals when you override hashCode");
        miscPanel.add(_tipOverrideEqualsCheckBox);
        _tipOverrideStringCheckBox = new JCheckBox("5",
                                                   this.settings.getBoolean(
                                                                         Keys.TIP_OVERRIDE_TO_STRING,
                                                                         false));
        _tipOverrideStringCheckBox.setToolTipText("Always override toString");
        miscPanel.add(_tipOverrideStringCheckBox);
        _tipInterfaceTypeCheckBox = new JCheckBox("6",
                                                  this.settings.getBoolean(
                                                                        Keys.TIP_INTERFACE_ONLY_FOR_TYPE,
                                                                        false));
        _tipInterfaceTypeCheckBox.setToolTipText("Use interfaces only to define types");
        miscPanel.add(_tipInterfaceTypeCheckBox);
        _tipReplaceStructCheckBox = new JCheckBox("7",
                                                  this.settings.getBoolean(
                                                                        Keys.TIP_REPLACE_STRUCTURE_WITH_CLASS,
                                                                        false));
        _tipReplaceStructCheckBox.setToolTipText("Replace structures with classes");
        miscPanel.add(_tipReplaceStructCheckBox);
        _tipZeroArrayCheckBox = new JCheckBox("8",
                                              this.settings.getBoolean(
                                                                    Keys.TIP_RETURN_ZERO_ARRAY,
                                                                    false));
        _tipZeroArrayCheckBox.setToolTipText("Return zero-length arrays, not nulls");
        miscPanel.add(_tipZeroArrayCheckBox);
        _tipReferByInterfaceCheckBox = new JCheckBox("9",
                                                     this.settings.getBoolean(
                                                                           Keys.TIP_REFER_BY_INTERFACE,
                                                                           false));
        _tipReferByInterfaceCheckBox.setToolTipText("Refer to objects by their interfaces");
        miscPanel.add(_tipReferByInterfaceCheckBox);
        _tipNeverExceptionCheckBox = new JCheckBox("10",
                                                   this.settings.getBoolean(
                                                                         Keys.TIP_NEVER_THROW_EXCEPTION,
                                                                         false));
        _tipNeverExceptionCheckBox.setToolTipText("Never declare that a method \"throws Exception\"");
        miscPanel.add(_tipNeverExceptionCheckBox);
        _tipNeverThrowableCheckBox = new JCheckBox("11",
                                                   this.settings.getBoolean(
                                                                         Keys.TIP_NEVER_THROW_THROWABLE,
                                                                         false));
        _tipNeverThrowableCheckBox.setToolTipText("Never declare that a method \"throws Throwable\"");
        miscPanel.add(_tipNeverThrowableCheckBox);
        _tipDontIgnoreExceptionCheckBox = new JCheckBox("12",
                                                        this.settings.getBoolean(
                                                                              Keys.TIP_DONT_IGNORE_EXCEPTION,
                                                                              false));
        _tipDontIgnoreExceptionCheckBox.setToolTipText("Don't ignore exceptions");
        miscPanel.add(_tipDontIgnoreExceptionCheckBox);
        _tipWaitOutsideLoopCheckBox = new JCheckBox("13",
                                                    this.settings.getBoolean(
                                                                          Keys.TIP_NEVER_WAIT_OUTSIDE_LOOP,
                                                                          false));
        _tipWaitOutsideLoopCheckBox.setToolTipText("Never invoke wait outside a loop");
        miscPanel.add(_tipWaitOutsideLoopCheckBox);
        _tipAvoidThreadGroupsCheckBox = new JCheckBox("14",
                                                      this.settings.getBoolean(
                                                                            Keys.TIP_AVOID_THREAD_GROUPS,
                                                                            false));
        _tipAvoidThreadGroupsCheckBox.setToolTipText("Avoid thread groups");
        miscPanel.add(_tipAvoidThreadGroupsCheckBox);
        _tipDeclareListCommentCheckBox = new JCheckBox("15",
                                                       this.settings.getBoolean(
                                                                             Keys.TIP_DECLARE_COLLECTION_COMMENT,
                                                                             false));
        _tipDeclareListCommentCheckBox.setToolTipText("Add comment for declaration of collection variables");
        miscPanel.add(_tipDeclareListCommentCheckBox);
        _tipWrongListCommentCheckBox = new JCheckBox("16",
                                                     this.settings.getBoolean(
                                                                           Keys.TIP_WRONG_COLLECTION_COMMENT,
                                                                           false));
        _tipWrongListCommentCheckBox.setToolTipText("Wrong comment for declaration of collection variables");
        miscPanel.add(_tipWrongListCommentCheckBox);
        _tipEmptyFinallyCheckBox = new JCheckBox("17",
                                                 this.settings.getBoolean(
                                                                       Keys.TIP_EMPTY_FINALLY,
                                                                       false));
        _tipEmptyFinallyCheckBox.setToolTipText("Empty finally block found");
        miscPanel.add(_tipEmptyFinallyCheckBox);
        _tip15CheckBox = new JCheckBox("18", false);
        _tip15CheckBox.setToolTipText("Avoid variable shadowing");
        miscPanel.add(_tip15CheckBox);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.bottom = 10;
        c.insets.top = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(miscPanel, c);
        add(miscPanel);
    }
}
