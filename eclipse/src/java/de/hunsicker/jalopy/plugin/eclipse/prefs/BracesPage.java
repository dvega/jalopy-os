/*
 * Copyright (c) 2002, Marco Hunsicker. All rights reserved.
 *
 * The contents of this file are subject to the Common Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://jalopy.sf.net/license-cpl.html
 *
 * Copyright (c) 2001-2002 Marco Hunsicker
 */
package de.hunsicker.jalopy.plugin.eclipse.prefs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;


/**
 * The page for setting code formatter options
 */
public class BracesPage
    extends AbstractPage
{
    //~ Instance variables ---------------------------------------------------------------

    private Text _descriptionTextField;
    private Text _nameTextField;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new BracesPage object.
     */
    public BracesPage()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean performOk()
    {
        return true;
    }


    /**
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(final Composite parent)
    {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);

        TabFolder tabs = new TabFolder(container, SWT.NONE);
        tabs.setLayoutData(new GridData(GridData.FILL_BOTH));

        TabItem styleTab = new TabItem(tabs, SWT.NONE);
        styleTab.setText("Style");

        Composite styleContainer = new Composite(tabs, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 1;
        styleContainer.setLayout(layout);
        styleTab.setControl(styleContainer);
        layout = new GridLayout();
        layout.numColumns = 2;

        Group styleGroup = createGroup(styleContainer, layout, "Styles");
        Button cStyleCheckBox = new Button(styleGroup, SWT.CHECK);
        cStyleCheckBox.setText("C style");

        Button sunStyleCheckBox = new Button(styleGroup, SWT.CHECK);
        sunStyleCheckBox.setText("Sun style");

        Button gnuStyleCheckBox = new Button(styleGroup, SWT.CHECK);
        gnuStyleCheckBox.setText("GNU style");

        Button customStyleCheckBox = new Button(styleGroup, SWT.CHECK);
        customStyleCheckBox.setText("Custom style");

        // -----------
        layout = new GridLayout();
        layout.numColumns = 1;

        Group alignmentGroup = createGroup(styleContainer, layout, "Alignment");
        Button newlineBeforeCheckBox = new Button(alignmentGroup, SWT.CHECK);
        newlineBeforeCheckBox.setText("Newline before left brace");

        Button newlineAfterCheckBox = new Button(alignmentGroup, SWT.CHECK);
        newlineAfterCheckBox.setText("Newline after right brace");

        Button treatDifferentCheckBox = new Button(alignmentGroup, SWT.CHECK);
        treatDifferentCheckBox.setText("Treat class and method blocks different");

        // -------------------
        layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;

        Group whitespaceGroup = createGroup(styleContainer, layout, "Whitespace");
        String[] items = { "0", "1", "2", "3", "4", "5" };
        Label beforeLeftLabel = new Label(whitespaceGroup, SWT.NULL);
        beforeLeftLabel.setText("Before left brace");

        Combo beforeLeftCombo = new Combo(whitespaceGroup, SWT.DROP_DOWN);
        beforeLeftCombo.setItems(items);

        Label beforeRightLabel = new Label(whitespaceGroup, SWT.NULL);
        beforeRightLabel.setText("Before right brace");

        Combo beforeRightCombo = new Combo(whitespaceGroup, SWT.DROP_DOWN);
        beforeRightCombo.setItems(items);

        Label afterRightLabel = new Label(whitespaceGroup, SWT.NULL);
        afterRightLabel.setText("After right brace");

        Combo afterRightCombo = new Combo(whitespaceGroup, SWT.DROP_DOWN);
        afterRightCombo.setItems(items);

        // ---
        TabItem miscTab = new TabItem(tabs, SWT.NONE);
        miscTab.setText("Misc");

        return container;
    }


    /**
     * DOCUMENT ME!
     */
    protected void performConventionDefaults()
    {
    }


    /**
     * Create a group for encapsualting the buttons.
     *
     * @param composite Composite
     * @param layout DOCUMENT ME!
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Group createGroup(
        Composite composite,
        Layout    layout,
        String    text)
    {
        Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
        group.setText(text);

        //GridLayout
        group.setLayout(layout);

        //GridData
        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        group.setLayoutData(data);

        return group;
    }
}
